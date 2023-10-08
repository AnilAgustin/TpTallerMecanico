package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.*;
<<<<<<< HEAD
import com.TP.TallerMecanico.servicio.IDetalleOrdenService;
import com.TP.TallerMecanico.servicio.IMarcaService;
import com.TP.TallerMecanico.servicio.IModeloService;
import com.TP.TallerMecanico.servicio.IOrdenService;
import com.TP.TallerMecanico.servicio.ITecnicoService;
import com.TP.TallerMecanico.servicio.IVehiculoService;
=======
import com.TP.TallerMecanico.servicio.*;
>>>>>>> origin/Peluk
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private IMarcaService marcaService;

    @Autowired
    private IModeloService modeloService;
    
    //Listar todos los ordenes cuando la URL sea /ordenes
    @GetMapping("/ordenes")
    public String listarOrdenes( @RequestParam(name = "marca", required = false) Long marcaId, 
                                 @RequestParam(name = "modelo", required = false) Long modeloId, 
                                 Model model) {

        List<Orden> ordenes;
        //var orden = ordenService.listarOrdenes();
        
        if ((marcaId != null) || (modeloId != null)) {
            ordenes = ordenService.filtrarOrdenes(marcaId,modeloId);
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

    //Permite guardar un orden cuando la solicitud POST sea guardarOrden 
    @PostMapping("/guardarOrden")
    public String guardarOrden(@Valid Orden orden, BindingResult error, Model model){
        if(error.hasErrors()){
            model.addAttribute("modo","nuevo");
            return "agregarModificarOrden";
        }

        //Se llama a la logica guardar definida en IOrdenService, pero en realidad es OrdenImplementacion
        ordenService.guardar(orden);
        return "redirect:/ordenes";
    }

    //Permite actualizar un cliente cuando la solicitud POST sea actualizarOrden
    @PostMapping("/actualizarOrden")
    public String actualizarVehiculo(@Valid @ModelAttribute Orden orden, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","editar");
            return "agregarModificarOrden";
        }
        ordenService.actualizar(orden);
        return "redirect:/ordenes";
    }

    //Cuando se presiona el boton editar se retorna el html con todos los datos del orden seleccionado para modificar 
    @GetMapping("/modificarOrden/{idOrden}")
    public String modificarOrden(@PathVariable Long idOrden, Model model){
        var orden = ordenService.buscarOrden(idOrden);
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
}
