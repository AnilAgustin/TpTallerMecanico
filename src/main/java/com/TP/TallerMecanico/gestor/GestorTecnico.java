package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.DetalleOrden;
import com.TP.TallerMecanico.entidad.Orden;
import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.entidad.Vehiculo;
import com.TP.TallerMecanico.servicio.ITecnicoService;
import com.TP.TallerMecanico.servicio.IVehiculoService;

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
public class GestorTecnico {

    //El Autowired sirve para la inyeccion de dependencias 
    @Autowired
    private ITecnicoService tecnicoService;

    @Autowired
    private IVehiculoService vehiculoService;

    //Listar todos los tecnicos cuando la URL sea /tecnicos
    @GetMapping("/tecnicos")
    public String listarTecnicos(@RequestParam(name = "nombre", required = false) String nombre, @RequestParam(name = "apellido", required = false) String apellido,@RequestParam(name="legajo", required = false) Long legajoCliente, Model model) {
        //var tecnico = tecnicoService.listarTecnicos();
        List<Tecnico> tecnicos;

        if (nombre!= null) {
            nombre = nombre.toUpperCase();
        }

        if (apellido != null) {
            apellido = apellido.toUpperCase();
        }
        
        String legajo = null;
        if (legajoCliente!=null) {
            legajo = Long.toString(legajoCliente);
        }


        if (nombre !=null || apellido != null || legajo != null) {
            tecnicos = tecnicoService.filtrarTecnicos(nombre, apellido, legajo);
        }else{
            tecnicos = tecnicoService.listarTecnicos();
        }

        model.addAttribute("tecnico", tecnicos);
        model.addAttribute("nombre", nombre);
        model.addAttribute("apellido", apellido);
        model.addAttribute("legajo", legajoCliente);
        return "tecnicos";
    }

    //Permitir agregar un tecnico cuando la URL sea /agregarTecnico
    @GetMapping("/agregarTecnico")
    public String agregarTecnico(Model model){
        var tecnico = new Tecnico();
        model.addAttribute("tecnico", tecnico);
        model.addAttribute("modo", "nuevo");
        return "agregarModificarTecnico";
    }

    //Permite guardar un tecnico cuando la solicitud POST sea guardarTecnico 
    @PostMapping("/guardarTecnico")
    public String guardarTecnico(@Valid Tecnico tecnico, BindingResult error, Model model){
        if(error.hasErrors()){
            model.addAttribute("modo","nuevo");
            return "agregarModificarTecnico";
        }

        //Se llama a la logica guardar definida en ITecnicoService, pero en realidad es TecnicoImplementacion
        tecnicoService.guardar(tecnico);
        return "redirect:/tecnicos";
    }

    //Permite actualizar un cliente cuando la solicitud POST sea actualizarTecnico
    @PostMapping("/actualizarTecnico")
    public String actualizarVehiculo(@Valid @ModelAttribute Tecnico tecnico, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","editar");
            return "agregarModificarTecnico";
        }
        tecnicoService.actualizar(tecnico);
        return "redirect:/tecnicos";
    }

    //Cuando se presiona el boton editar se retorna el html con todos los datos del tecnico seleccionado para modificar 
    @GetMapping("/modificarTecnico/{idTecnico}")
    public String modificarTecnico(@PathVariable Long idTecnico, Model model){
        var tecnico = tecnicoService.buscarTecnico(idTecnico);
        model.addAttribute("tecnico", tecnico);
        model.addAttribute("modo", "editar");
        return "agregarModificarTecnico";
    }

    //Cuando se presiona el boton eliminar se pasa el ID del tecnico y este se elimina (Soft Delete)
    @GetMapping("/eliminarTecnico/{idTecnico}")
    public String eliminarTecnico(Tecnico tecnico){
        tecnicoService.eliminar(tecnico);
        return "redirect:/tecnicos";
    }

    @GetMapping("/tecnico/vehiculos/{idTecnico}")
    public String detallesOrden(@PathVariable ("idTecnico") Long id, Model model) {
        Tecnico tecnico = tecnicoService.buscarTecnico(id);
        List<Vehiculo> vehiculos = vehiculoService.listarVehiculosPorTecnico(tecnico);

        //List<DetalleOrden> detalles = detallesService.listarDetallesPorOrden(orden);
        model.addAttribute("tecnico", tecnico );
        model.addAttribute("vehiculos", vehiculos);

        return "vehiculoPorTecnico";
    }
}