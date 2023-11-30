package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.*;
import com.TP.TallerMecanico.interfaz.IOrdenDao;
import com.TP.TallerMecanico.servicio.*;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

@Controller
public class GestorOrden {

    //El Autowired sirve para la inyeccion de dependencias 
    @Autowired
    private IOrdenService ordenService;

    @Autowired
    private IImagenService imagenService;

    @Autowired
    private IVehiculoService vehiculoService;

    @Autowired
    private IDetalleOrdenService detallesService;

    @Autowired
    private ITecnicoService tecnicoService;

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IMarcaService marcaService;

    @Autowired
    private IModeloService modeloService;

    @Autowired
    private IOrdenDao ordenDao;
    
    //Listar todos los ordenes cuando la URL sea /ordenes
    @GetMapping("/ordenes")
    public String listarOrdenes( @RequestParam(name = "marca", required = false) Long marcaId, 
                                 @RequestParam(name = "modelo", required = false) Long modeloId,
                                 @RequestParam(name="numero", required = false) Long numero,
                                 @RequestParam(name="fechaDesdeDocumento", required = false) LocalDate fechaDesdeDocumento,
                                 @RequestParam(name="fechaHastaDocumento", required = false) LocalDate fechaHastaDocumento, 
                                 Model model) {

        List<Orden> ordenes;
        //var orden = ordenService.listarOrdenes();
        
        if ((marcaId != null) || (modeloId != null || numero!=null || fechaDesdeDocumento !=null || fechaHastaDocumento != null)) {
            OrdenFiltrador ordenFiltrador = new OrdenFiltrador(ordenDao, ordenService);
            ordenes = ordenFiltrador.filtrarOrdenes(marcaId, modeloId, numero, fechaDesdeDocumento, fechaHastaDocumento);
        }else {
            // Si no se enviaron parámetros de búsqueda, lista todos los vehículos
            ordenes= ordenService.listarOrdenes();
        }

        //Lógica para mostrar todos 
        List<Marca> marcas = marcaService.listarMarcas();
        List<Modelo> modelos = modeloService.listarModelos();

        model.addAttribute("orden", ordenes);
        model.addAttribute("marcas", marcas);
        model.addAttribute("modelos", modelos);

        //Lógica para mostrar los filtros seleccionados
        model.addAttribute("idModelo", modeloId);
        model.addAttribute("idMarca", marcaId);
        model.addAttribute("numero", numero);
        model.addAttribute("fechaDesdeDocumento", fechaDesdeDocumento);
        model.addAttribute("fechaHastaDocumento", fechaHastaDocumento);
        
        return "ordenes";
    }

    //Permitir agregar un orden cuando la URL sea /agregarOrden
    @GetMapping("/agregarOrden")
    public String agregarOrden(Model model){
        var orden = new Orden();
        List<Vehiculo> vehiculos = vehiculoService.listarVehiculos(); // Obtener vehiculops activas
        model.addAttribute("vehiculos", vehiculos); // Agregar la lista de vehiculos a la orden
        List<Tecnico> tecnicos = tecnicoService.listarTecnicos(); // Obtener tecnicos
        model.addAttribute("tecnicos", tecnicos); // Agregar la lista de tecnicos a la orden
        model.addAttribute("orden", orden);
        model.addAttribute("modo", "nuevo");
        return "agregarModificarOrden";
    }

    @GetMapping("/ordenesEliminadas")
    public String ordenesEliminadas(Model model, @RequestParam(name="numero", required = false) Long numero){
        List<Orden> ordenes;

        if (numero!=null) {
            ordenes = ordenDao.filtrarOrdenEliminadaPorNumero(numero);
        }else{
            ordenes = ordenDao.findByEstadoFalse();
        }
        

        //Lógica para mostrar todos 
        List<Marca> marcas = marcaService.listarMarcas();
        List<Modelo> modelos = modeloService.listarModelos();

        model.addAttribute("orden", ordenes);
        model.addAttribute("marcas", marcas);
        model.addAttribute("modelos", modelos);
        model.addAttribute("numero", numero);

        return "ordenesEliminadas";
    }
    
    //Permite guardar un orden cuando la solicitud POST sea guardarOrden 
    @PostMapping("/guardarOrden")
    public String guardarOrden(@Valid Orden orden, BindingResult error, Model model, @RequestParam("agregarDetalles") String agregarDetalles) {
        if (error.hasErrors()) {
            model.addAttribute("modo", "nuevo");
            List<Vehiculo> vehiculos = vehiculoService.listarVehiculos(); // Obtener vehiculops activas
            model.addAttribute("vehiculos", vehiculos); // Agregar la lista de vehiculos a la orden

            List<Tecnico> tecnicos = tecnicoService.listarTecnicos(); // Obtener tecnicos
            model.addAttribute("tecnicos", tecnicos); // Agregar la lista de tecnicos a la orden
            return "agregarModificarOrden";
        }

        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaDocumento = orden.getFechaDocumento();

        //Sentencia de control para verificar si la fechaDocumento ingresada por el cliente es posterior a la fecha actual
        if (fechaDocumento != null && fechaDocumento.isAfter(fechaActual)) {
            model.addAttribute("errorFecha", "La fecha del documento no puede ser mayor que la fecha actual.");

            model.addAttribute("modo", "nuevo");
            List<Vehiculo> vehiculos = vehiculoService.listarVehiculos(); // Obtener vehiculops activas
            model.addAttribute("vehiculos", vehiculos); // Agregar la lista de vehiculos a la orden

            List<Tecnico> tecnicos = tecnicoService.listarTecnicos(); // Obtener tecnicos
            model.addAttribute("tecnicos", tecnicos); // Agregar la lista de tecnicos a la orden
            return "agregarModificarOrden";
        }
        
        // Continúa con el proceso de guardar la orden si la fecha es válida.
        // Se llama a la lógica para guardar la orden
        ordenService.guardar(orden);

        //Verificacion para saber a donde redirigir
        if ("si".equals(agregarDetalles)) {
            // Si el usuario eligió "Sí" para agregar detalles, redirige a la página de detalles de la orden
            Long nuevaOrdenId = orden.getIdOrden();
            return "redirect:/ordenes/detallesOrden/" + nuevaOrdenId;
        } else {
            // Si el usuario eligió "No" para agregar detalles, redirige a la página de todas las ordenes
            return "redirect:/ordenes";
        }
    }

    //Permite actualizar un cliente cuando la solicitud POST sea actualizarOrden
    @PostMapping("/actualizarOrden")
    public String actualizarVehiculo(@Valid @ModelAttribute Orden orden, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","editar");

            var fechaDocumento = orden.getFechaDocumento();
            model.addAttribute("fechaDocumento", fechaDocumento);

            List<Vehiculo> vehiculos = vehiculoService.listarVehiculos(); // Obtener vehiculops activas
            model.addAttribute("vehiculos", vehiculos); // Agregar la lista de vehiculos a la orden

            List<Tecnico> tecnicos = tecnicoService.listarTecnicos(); // Obtener tecnicos
            model.addAttribute("tecnicos", tecnicos); // Agregar la lista de tecnicos a la orden
            return "agregarModificarOrden";
        }

        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaDocumento = orden.getFechaDocumento();

        //Sentencia de control para verificar si la fechaDocumento ingresada por el cliente es posterior a la fecha actual
        if (fechaDocumento != null && fechaDocumento.isAfter(fechaActual)) {
            model.addAttribute("errorFecha", "La Fecha del Documento no puede ser mayor que la fecha actual.");

            var fechaDocumentoGuardada = orden.getFechaDocumento();                        //Buscar la fechaDocumento de la base de datos
            model.addAttribute("fechaDocumento", fechaDocumentoGuardada);    // Setear esta fecha en el model para que cuando salte el error no se pierda la fecha que estaba

            model.addAttribute("modo", "editar");             //Setear de nuevo el form en modo editar

            List<Vehiculo> vehiculos = vehiculoService.listarVehiculos();                  // Obtener vehiculops activas
            model.addAttribute("vehiculos", vehiculos);                      // Agregar la lista de vehiculos a la orden
            List<Tecnico> tecnicos = tecnicoService.listarTecnicos();                      // Obtener tecnicos
            model.addAttribute("tecnicos", tecnicos);                        // Agregar la lista de tecnicos a la orden
            return "agregarModificarOrden";
        }


        ordenService.actualizar(orden);
        return "redirect:/ordenes";
    }

    //Cuando se presiona el boton editar se retorna el html con todos los datos del orden seleccionado para modificar 
    @GetMapping("/modificarOrden/{idOrden}")
    public String modificarOrden(@PathVariable Long idOrden, Model model){
        var orden = ordenService.buscarOrden(idOrden);

        var fechaDocumento = orden.getFechaDocumento();
        model.addAttribute("fechaDocumento", fechaDocumento);

        List<Vehiculo> vehiculos = vehiculoService.listarVehiculos(); // Obtener vehiculops activas
        model.addAttribute("vehiculos", vehiculos); // Agregar la lista de vehiculos a la orden

        List<Tecnico> tecnicos = tecnicoService.listarTecnicos(); // Obtener tecnicos
        model.addAttribute("tecnicos", tecnicos); // Agregar la lista de tecnicos a la orden

        model.addAttribute("orden", orden);
        model.addAttribute("modo", "editar");
        return "agregarModificarOrden";
    }

    @GetMapping("/ordenes/detallesOrden/{idOrden}")
    public String detallesOrden(@PathVariable ("idOrden") Long id, Model model) {
        Orden orden = ordenService.buscarOrden(id);
        List<DetalleOrden> detalles = detallesService.listarDetallesPorOrden(orden);
        model.addAttribute("orden", orden);
        model.addAttribute("detalleOrden", detalles);

        if (orden.getEstado()==false) {
            model.addAttribute("eliminada", true);
        }

        return "detallesOrden";
    }

    @GetMapping("/ordenes/imagenesOrden/{idOrden}")
    public String imagenesOrden(@PathVariable ("idOrden") Long id, Model model){
        Orden orden = ordenService.buscarOrden(id);
        List<Imagen> imagenes = imagenService.listarImagenes(orden);
        model.addAttribute("orden", orden);
        model.addAttribute("imagen", imagenes);
        return "imagenesOrden";
    }

    //Cuando se presiona el boton eliminar se pasa el ID del orden y este se elimina (Soft Delete)
    @GetMapping("/eliminarOrden/{idOrden}")
    public String eliminarOrden(Orden orden){
        ordenService.eliminar(orden);
        return "redirect:/ordenes";
    }


    //Cuando se presiona el boton editar en "vehiculoPorTecnico" se retorna el html con todos los datos del kilometraje de la orden seleccionada para modificar 
    @GetMapping("/tecnico/modificarKilometrosPorTecnico/{idVehiculo}/{idTecnico}/{idOrden}")
    public String mostrarFormularioEditarTecnico(@PathVariable ("idVehiculo") Long idVehiculo, @PathVariable ("idTecnico") Long idTecnico, @PathVariable ("idOrden") Long idOrden, Model model) {
        var vehiculo = vehiculoService.buscarVehiculo(idVehiculo);
        Tecnico tecnico = tecnicoService.buscarTecnico(idTecnico);
        Orden orden = ordenService.buscarOrden(idOrden);
        Orden ordenDb = ordenService.buscarOrden(idOrden);

        model.addAttribute("tecnico", tecnico);
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("orden", orden);
        model.addAttribute("ordenDb", ordenDb);

        model.addAttribute("modo", "editar");
        
        return "modificarKilometrosPorTecnico";
    }


    //Permite actualizar los kilometros de la orden, cuando la solicitud POST venga desde el formulario del modificarKilometrosPorTecnico
    @PostMapping("/tecnico/actualizarKilometrosPorTecnico/{idOrden}")
    public String actualizarVehiculoPorTecnico(@Valid @ModelAttribute Orden orden, BindingResult error ,@PathVariable ("idOrden") Long idOrden, Model model){
        Orden ordenDb = ordenService.buscarOrden(idOrden);
        if (error.hasErrors()) {
            model.addAttribute("modo","editar");
            model.addAttribute("orden", orden);
            model.addAttribute("ordenDb", ordenDb);
            
            return "modificarKilometrosPorTecnico";
        }
        
        ordenService.actualizarKilometraje(orden);
        
        String redirectUrl = "redirect:/tecnico/vehiculos/" + ordenDb.getTecnico().getIdTecnico();

        // Redirige a la URL construida
        return redirectUrl;
    }
}
