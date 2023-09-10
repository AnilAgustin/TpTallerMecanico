package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.Marca;
import com.TP.TallerMecanico.entidad.Modelo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.TP.TallerMecanico.servicio.IMarcaService;

@Controller
public class GestorMarca {

    @Autowired
    private IMarcaService marcaService; //Inyeccion de la clase (MarcaImplementacion) a la interfaz MarcaService

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/marcas")
    public String listarMarcas(Model model) {
        var marca = marcaService.listarMarcas();
        model.addAttribute("marca", marca);
        return "marcas";
    }

    @GetMapping("/agregarMarca")
    public String agregarMarca(Model model) {
        var marca = new Marca();
        model.addAttribute("marca", marca);
        model.addAttribute("modo", "nuevo");
        return "agregarModificarMarca";
    }

    @PostMapping("/guardarMarca")
    public String guardarMarca(@Valid Marca marca, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","nuevo");
            return "agregarModificarMarca";
        }
        marcaService.guardar(marca);
        return "redirect:/marcas";
    }

    @PostMapping("/actualizarMarca")
    public String actualizarModelo(@Valid @ModelAttribute Marca marca, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","editar");
            return "agregarModificarMarca";
        }
        marcaService.actualizar(marca);
        return "redirect:/marcas";
    }

    @GetMapping("/modificarMarca/{idMarca}")
    public String modificarMarca(Marca marca, Model model) {
        marca = marcaService.buscarMarca(marca);
        model.addAttribute("marca", marca);
        model.addAttribute("modo", "editar");
        return "agregarModificarMarca";
    }

    @GetMapping("/eliminarMarca/{idMarca}")
    public String eliminarMarca(Marca marca) {
        marcaService.eliminar(marca);
        return "redirect:/marcas";
    }
}
