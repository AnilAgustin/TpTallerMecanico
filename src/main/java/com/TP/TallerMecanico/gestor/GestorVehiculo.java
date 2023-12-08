package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.*;
import com.TP.TallerMecanico.interfaz.IClienteDao;
import com.TP.TallerMecanico.interfaz.IMarcaDao;
import com.TP.TallerMecanico.interfaz.IModeloDao;
import com.TP.TallerMecanico.interfaz.IVehiculoDao;
import com.TP.TallerMecanico.servicio.IClienteService;
import com.TP.TallerMecanico.servicio.IMarcaService;
import com.TP.TallerMecanico.servicio.IModeloService;
import com.TP.TallerMecanico.servicio.ITecnicoService;
import com.TP.TallerMecanico.servicio.IVehiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GestorVehiculo {
    //El Autowired sirve para la inyeccion de dependencias 
    @Autowired
    private IVehiculoService vehiculoService;

    @Autowired
    private IModeloService modeloService;

    @Autowired
    private IVehiculoDao vehiculoDao;

    @Autowired
    private IClienteDao clienteDao;

    @Autowired
    private IMarcaDao marcaDao;

    @Autowired
    private IModeloDao modeloDao;

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private ITecnicoService tecnicoService;

    @Autowired
    private IMarcaService marcaService;

    @GetMapping("/vehiculos")
    public String listarVehiculos(@RequestParam(name = "patente", required = false) String patente, 
                                  @RequestParam(name = "marca", required = false) Long marcaId, 
                                  @RequestParam(name = "modelo", required = false) Long modeloId,  Model model) {
        List<Vehiculo> vehiculos;

        //Lógica para filtrar
        if (patente!=null) {
            patente = patente.toUpperCase();
        }else{
            patente=null;
        }

        if ((patente != null) || (marcaId != null) || (modeloId != null)) {
            // Si se envió algún parámetro de búsqueda (patente, marca o modelo), realiza la búsqueda
            vehiculos = vehiculoService.filtrarVehiculos(patente, marcaId, modeloId);
        } else {
            // Si no se enviaron parámetros de búsqueda, lista todos los vehículos
            vehiculos = vehiculoService.listarVehiculos();
        }

        //Lógica para mostrar todos 
        List<Marca> marcas = marcaService.listarMarcas();
        List<Modelo> modelos = modeloService.listarModelos();

        model.addAttribute("patente", patente);
        model.addAttribute("idModelo", modeloId);
        model.addAttribute("idMarca", marcaId);

        model.addAttribute("vehiculo", vehiculos);
        model.addAttribute("marcas", marcas);
        model.addAttribute("modelos", modelos);

        return "vehiculos";
    }

    @GetMapping("/vehiculosEliminados")
    public String vehiculosEliminados(Model model, @RequestParam(name = "patente", required = false) String patente){
        List<Vehiculo> vehiculos;

        if (patente!=null && patente!="") {
            patente = patente.toUpperCase();

            vehiculos = vehiculoDao.findByPatenteAndEstadoFalse(patente);

        }else{
            vehiculos = vehiculoDao.findByEstadoFalse();
        }

        
        model.addAttribute("vehiculo", vehiculos);
        return "vehiculosEliminados";
    }


    @GetMapping("/recuperarVehiculo/{idVehiculo}")
    public String recuperarVehiculo(Model model, @PathVariable Long idVehiculo) {
        Vehiculo vehiculo = vehiculoDao.findByIdVehiculoAndEstadoFalse(idVehiculo);
    
        // Verificar la existencia del vehículo y de los objetos Cliente, Modelo y Marca asociados
        if (vehiculo != null && vehiculo.getCliente() != null && vehiculo.getModelo() != null && vehiculo.getModelo().getMarca() != null) {
            Cliente clienteAsociado = vehiculo.getCliente();
            Modelo modeloAsociado = vehiculo.getModelo();
            Marca marcaAsociada = modeloAsociado.getMarca();
    
            // Verificar si los objetos Cliente, Modelo y Marca asociados existen en la base de datos
            if (clienteDao.existsByIdClienteAndEstadoTrue(clienteAsociado.getIdCliente()) &&
                modeloDao.existsByIdModeloAndEstadoTrue(modeloAsociado.getIdModelo()) &&
                marcaDao.existsByIdMarcaAndEstadoTrue(marcaAsociada.getIdMarca())) {
    
                // Todos los objetos asociados existen, proceder con la recuperación del vehículo
                vehiculoService.activarVehiculo(vehiculo);
                return "redirect:/vehiculos";
            } else {
                // Alguno de los objetos asociados no existe, manejar el caso apropiadamente
                model.addAttribute("error", "No se puede recuperar el vehículo porque uno o más objetos asociados no existen.");
                vehiculosEliminados(model,"");
                return "vehiculosEliminados";
            }
        } else {
            // Manejar el caso cuando el vehículo no se encuentra o ya ha sido recuperado
            model.addAttribute("error", "El vehículo no se encuentra o ya ha sido recuperado.");
            vehiculosEliminados(model,"");
            return "vehiculosEliminados";
        }
    }

    //Permitir agregar un vehiculo cuando la URL sea /agregarVehiculo
    @GetMapping("/agregarVehiculo")
    public String agregarVehiculos(Model model) {
        var vehiculo = new Vehiculo();
        List<Modelo> modelos = modeloService.listarModelos();
        List<Cliente> clientes = clienteService.listarClientes();
        //List<Tecnico> tecnicos = tecnicoService.listarTecnicos();
        model.addAttribute("modelos", modelos);
        model.addAttribute("clientes", clientes);
        //model.addAttribute("tecnicos", tecnicos);
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("modo", "nuevo");
        return "agregarModificarVehiculo";
    }


    //Permite guardar un cliente cuando la solicitud POST sea guardarVehiculo 
    @PostMapping("/guardarVehiculo")
    public String guardarVehiculo(@Valid Vehiculo vehiculo, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","nuevo");
            List<Modelo> modelos = modeloService.listarModelos();
            List<Cliente> clientes = clienteService.listarClientes();
            List<Tecnico> tecnicos = tecnicoService.listarTecnicos();
            model.addAttribute("modelos", modelos);
            model.addAttribute("clientes", clientes);
            model.addAttribute("tecnicos", tecnicos);
            return "agregarModificarVehiculo";
        }

        //Se llama a la logica guardar definida en IVehiculoService, pero en realidad es VehiculoImplementacion
        vehiculoService.guardar(vehiculo);
        return "redirect:/vehiculos";
    }

    //Permite actualizar un vehiculo cuando la solicitud POST sea actualizarVehiculo
    @PostMapping("/actualizarVehiculo")
    public String actualizarVehiculo(@Valid @ModelAttribute Vehiculo vehiculo, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","editar");
            List<Modelo> modelos = modeloService.listarModelos();
            List<Cliente> clientes = clienteService.listarClientes();
            List<Tecnico> tecnicos = tecnicoService.listarTecnicos();
            model.addAttribute("modelos", modelos);
            model.addAttribute("clientes", clientes);
            model.addAttribute("tecnicos", tecnicos);
            return "agregarModificarVehiculo";
        }
        vehiculoService.actualizar(vehiculo);
        return "redirect:/vehiculos";
    }



    //Cuando se presiona el boton editar se retorna el html con todos los datos del vehiculo seleccionado para modificar 
    @GetMapping("/modificarVehiculo/{idVehiculo}")
    public String mostrarFormularioEditar(@PathVariable Long idVehiculo, Model model) {
        var vehiculo = vehiculoService.buscarVehiculo(idVehiculo);
        List<Modelo> modelos = modeloService.listarModelos();
        List<Cliente> clientes = clienteService.listarClientes();
        List<Tecnico> tecnicos = tecnicoService.listarTecnicos();
        model.addAttribute("modelos", modelos);
        model.addAttribute("clientes", clientes);
        model.addAttribute("tecnicos", tecnicos);
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("modo", "editar");
        return "agregarModificarVehiculo";
    }



    //Cuando se presiona el boton eliminar se pasa el ID del vehiculo y este se elimina (Soft Delete)
    @GetMapping("/eliminarVehiculo/{idVehiculo}")
    public String eliminarVehiculo(Vehiculo vehiculo) {
        vehiculoService.eliminar(vehiculo);
        return "redirect:/vehiculos";
    }
    
}