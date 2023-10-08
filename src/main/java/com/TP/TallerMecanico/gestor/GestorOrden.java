package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.*;
import com.TP.TallerMecanico.servicio.IDetalleOrdenService;
import com.TP.TallerMecanico.servicio.IOrdenService;
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

import java.util.List;

@Controller
public class GestorOrden {

    //El Autowired sirve para la inyeccion de dependencias 
    @Autowired
    private IOrdenService ordenService;

    @Autowired
    private IVehiculoService vehiculoService;

    @Autowired
    private IDetalleOrdenService detallesService;

    @Autowired
    private ITecnicoService tecnicoService;

    //Listar todos los ordenes cuando la URL sea /ordenes
    @GetMapping("/ordenes")
    public String listarOrdenes(Model model) {
        var orden = ordenService.listarOrdenes();
        model.addAttribute("orden", orden);
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

    //Cuando se presiona el boton eliminar se pasa el ID del orden y este se elimina (Soft Delete)
    @GetMapping("/eliminarOrden/{idOrden}")
    public String eliminarOrden(Orden orden){
        ordenService.eliminar(orden);
        return "redirect:/ordenes";
    }
}
