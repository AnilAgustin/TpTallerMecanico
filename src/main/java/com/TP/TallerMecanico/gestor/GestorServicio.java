package com.TP.TallerMecanico.gestor;


import com.TP.TallerMecanico.entidad.Servicio;
import com.TP.TallerMecanico.interfaz.IServicioDao;
import com.TP.TallerMecanico.servicio.IServicioService;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GestorServicio {

    //El Autowired sirve para la inyeccion de dependencias 
    @Autowired
    private IServicioService servicioService;

    @Autowired
    private IServicioDao servicioDao;

    //Listar todos los servicios cuando la URL sea /servicios
    @GetMapping("/servicios")
    public String listarServicios(@RequestParam(name="nombre", required=false) String nombre,  Model model) {

        //var servicio = servicioService.listarServicios();
        List<Servicio> servicios;

        if (nombre != null) {
            nombre = nombre.toUpperCase();
        }
        
        //Busqueda
        if (nombre != null) {
            servicios = servicioService.filtrarServicios(nombre);
        }else{
            servicios = servicioService.listarServicios();
        }

        
        model.addAttribute("servicio", servicios);
        return "servicios";
    }

    @GetMapping("/serviciosEliminados")
    public String serviciosEliminados(Model model, @RequestParam(name="nombre", required=false) String nombre){
        List<Servicio> servicios;

        if (nombre != null) {
            nombre = nombre.toUpperCase();
        }
        
        //Busqueda
        if (nombre != null) {
            servicios=servicioDao.filtrarPorNombreYEstadoFalse(nombre);
        }else{
            servicios = servicioDao.findByEstadoFalse();
        }
        
        
        model.addAttribute("servicio", servicios);
        
        return "serviciosEliminados";
    }


    @GetMapping("/recuperarServicio/{idServicio}")
    public String recuperarServicio(@PathVariable Long idServicio, Model model) {
        Servicio servicio= servicioDao.findByIdServicioAndEstadoFalse(idServicio);
        servicioService.activarServicio(servicio);
        return "redirect:/servicios";
    }


    //Permitir agregar un servicio cuando la URL sea /agregarServicio
    @GetMapping("/agregarServicio")
    public String agregarServicio(Model model){
        var servicio = new Servicio();
        model.addAttribute("servicio", servicio);
        model.addAttribute("modo", "nuevo");
        return "agregarModificarServicio";
    }

    //Permite guardar un servicio cuando la solicitud POST sea guardarServicio 
    @PostMapping("/guardarServicio")
    public String guardarServicio(@Valid Servicio servicio, BindingResult error, Model model){
        if(error.hasErrors()){
            model.addAttribute("modo","nuevo");
            return "agregarModificarServicio";
        }

        //Se llama a la logica guardar definida en IServicioService, pero en realidad es ServicioImplementacion
        servicioService.guardar(servicio);
        return "redirect:/servicios";
    }

    //Permite actualizar un cliente cuando la solicitud POST sea actualizarServicio
    @PostMapping("/actualizarServicio")
    public String actualizarVehiculo(@Valid @ModelAttribute Servicio servicio, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","editar");
            return "agregarModificarServicio";
        }
        servicioService.actualizar(servicio);
        return "redirect:/servicios";
    }

    //Cuando se presiona el boton editar se retorna el html con todos los datos del servicio seleccionado para modificar 
    @GetMapping("/modificarServicio/{idServicio}")
    public String modificarServicio(Servicio servicio, Model model){
        servicio = servicioService.buscarServicio(servicio);
        model.addAttribute("servicio", servicio);
        model.addAttribute("modo", "editar");
        return "agregarModificarServicio";
    }

    //Cuando se presiona el boton eliminar se pasa el ID del servicio y este se elimina (Soft Delete)
    @GetMapping("/eliminarServicio/{idServicio}")
    public String eliminarServicio(Servicio servicio){
        servicioService.eliminar(servicio);
        return "redirect:/servicios";
    }
}