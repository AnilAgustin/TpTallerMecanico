package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.servicio.ITecnicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GestorTecnico {

    @Autowired
    private ITecnicoService tecnicoService;

    @GetMapping("/tecnicos")
    public String listarTecnicos(Model model) {
        var tecnico = tecnicoService.listarTecnicos();
        model.addAttribute("tecnico", tecnico);
        return "tecnicos";
    }

    @GetMapping("/agregarTecnico")
    public String agregarTecnico(Tecnico tecnico){
        return "agregarModificarTecnico";
    }

    @PostMapping("/guardarTecnico")
    public String guardarTecnico(@Valid Tecnico tecnico, BindingResult error){
        if(error.hasErrors()){
            return "agregarModificarTecnico";
        }
        
        tecnicoService.guardar(tecnico);
        return "redirect:/tecnicos";
    }

    @GetMapping("/modificarTecnico/{idTecnico}")
    public String modificarTecnico(Tecnico tecnico, Model model){
        tecnico = tecnicoService.buscarTecnico(tecnico);
        model.addAttribute("tecnico", tecnico);
        return "agregarModificarTecnico";
    }
    @GetMapping("/eliminarTecnico/{idTecnico}")
    public String eliminarTecnico(Tecnico tecnico){
        tecnicoService.eliminar(tecnico);
        return "redirect:/tecnicos";
    }
}