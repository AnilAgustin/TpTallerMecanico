package com.TP.TallerMecanico.gestor;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.TP.TallerMecanico.entidad.Orden;
import com.TP.TallerMecanico.entidad.Servicio;
import com.TP.TallerMecanico.entidad.DetalleOrden;
import com.TP.TallerMecanico.servicio.IOrdenService;
import com.TP.TallerMecanico.servicio.IServicioService;
import com.TP.TallerMecanico.servicio.IDetalleOrdenService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GestorDetalleOrden {

    //El Autowired sirve para la inyeccion de dependencias

    @Autowired
    private IOrdenService ordenService;
    @Autowired
    private IDetalleOrdenService detalleOrdenService; // Inyeccion de la clase (detalleOrdenImplementacion) a la interfaz detalleOrdenService

    @Autowired
    private IServicioService servicioService;

    //Permitir agregar un detalleOrden cuando la URL sea /agregarDetalleOrden
    @GetMapping("/ordenes/agregarDetalleOrden/{idOrden}")
    public String agregarDetalleOrden(Model model, @PathVariable ("idOrden") Long id) {
        var detalleOrden = new DetalleOrden(); // Crear una nueva instancia de DetalleOrden
        List<Servicio> servicios = servicioService.listarServicios(); // Obtener servicios activas
        Orden orden = ordenService.buscarOrden(id);
        model.addAttribute("orden", orden);
        model.addAttribute("servicios", servicios);// Agregar la lista de marcas al detalleOrden
        model.addAttribute("detalleOrden", detalleOrden);
        model.addAttribute("modo", "nuevo");
        return "agregarModificarDetallesOrden";
    }

    //Permite guardar un detalleOrden cuando la solicitud POST sea guardarDetalleOrden
    @PostMapping("/ordenes/guardarDetalleOrden/{idOrden}")
    public String guardarDetalleOrden(@PathVariable ("idOrden") Long id, @Valid DetalleOrden detalleOrden, BindingResult error, Model model) {
        if (error.hasErrors()) {
            List<Servicio> servicios = servicioService.listarServicios(); // Obtener marcas activas
            model.addAttribute("servicios", servicios);
            model.addAttribute("modo", "nuevo");
            return "agregarModificarDetallesOrden";
        }
        // Obtén el id de la orden después de guardar el detalle, asumiendo que puedes obtenerlo desde detalleOrden
        var orden = ordenService.buscarOrden(id);
        detalleOrden.setOrden(orden);
        //Se llama a la logica guardar definida en IDetalleOrdenService, pero en realidad es DetalleOrdenImplementacion
        detalleOrdenService.guardar(detalleOrden);

        // Construye la URL de redireccionamiento con el id de la orden
        String redirectUrl = "redirect:/ordenes/detallesOrden/" + id;

        // Redirige a la URL construida
        return redirectUrl;
    }

    //Permite actualizar un detalleOrden cuando la solicitud POST sea actualizarDetalleOrden
    @PostMapping("/ordenes/actualizarDetalleOrden/{idOrden}")
    public String actualizarDetalleOrden(@PathVariable ("idOrden") Long id,@Valid @ModelAttribute DetalleOrden detalleOrden, BindingResult error, Model model) {
        if (error.hasErrors()) {
            List<Servicio> servicios = servicioService.listarServicios(); // Obtener marcas activas
            model.addAttribute("servicios", servicios);
            model.addAttribute("modo", "editar");
            return "agregarModificarDetallesOrden";
        }
        detalleOrdenService.actualizar(detalleOrden,id);
        // Obtén el id de la orden después de guardar el detalle, asumiendo que puedes obtenerlo desde detalleOrden
        var orden = ordenService.buscarOrden(id);
        detalleOrden.setOrden(orden); //ESTO NO SE SI ESTA BIEN
        //SE SUPONE QUE SI ESTO ESTA ENCIMA DEL ACUTALIZAR NO HABRIA RAZON PARA TENER QUE PASAR UN IDORDEN POR PARAMETRO AL ACTUALIZAR
        //CHQUEAR EN ALGUN MOMENTO, NI IDEA
        // Construye la URL de redireccionamiento con el id de la orden
        String redirectUrl = "redirect:/ordenes/detallesOrden/" + id;

        // Redirige a la URL construida
        return redirectUrl;
    }

    //Cuando se presiona el boton editar se retorna el html con todos los datos del detalleOrden seleccionado para modificar
    @GetMapping("/ordenes/modificarDetalleOrden/{idDetalleOrden}/{idOrden}")
    public String modificarDetalleOrden(@PathVariable ("idDetalleOrden") Long idDetalleOrden,@PathVariable ("idOrden") Long idOrden, Model model) {
        var detalleOrden = detalleOrdenService.buscarDetalleOrden(idDetalleOrden);
        List<Servicio> servicios = servicioService.listarServicios(); // Obtener marcas activas
        Orden orden = ordenService.buscarOrden(idOrden);
        model.addAttribute("orden", orden);
        model.addAttribute("servicios", servicios);
        model.addAttribute("detalleOrden", detalleOrden);
        model.addAttribute("modo", "editar");
        return "agregarModificarDetallesOrden";
    }

    //Cuando se presiona el boton eliminar se pasa el ID del detalleOrden y este se elimina (Soft Delete)
    @GetMapping("/ordenes/eliminarDetalleOrden/{idDetalleOrden}/{idOrden}")
    public String eliminarDetalleOrden(@PathVariable ("idOrden") Long idOrden, DetalleOrden detalleOrden) {
        detalleOrdenService.eliminar(detalleOrden);
        var orden = ordenService.buscarOrden(idOrden);
        String redirectUrl = "redirect:/ordenes/detallesOrden/" + idOrden;

        // Redirige a la URL construida
        return redirectUrl;
    }
}