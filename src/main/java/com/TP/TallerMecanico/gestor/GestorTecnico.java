package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.servicio.ITecnicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GestorTecnico {

    //El Autowired sirve para la inyeccion de dependencias 
    @Autowired
    private ITecnicoService tecnicoService;

    //Listar todos los tecnicos cuando la URL sea /tecnicos
    @GetMapping("/tecnicos")
    public String listarTecnicos(Model model) {
        var tecnico = tecnicoService.listarTecnicos();
        model.addAttribute("tecnico", tecnico);
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
}