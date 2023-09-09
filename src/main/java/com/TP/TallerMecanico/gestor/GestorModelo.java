package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.Marca;
import com.TP.TallerMecanico.entidad.Modelo;
import com.TP.TallerMecanico.servicio.IMarcaService;
import com.TP.TallerMecanico.servicio.IModeloService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GestorModelo {
    
    @Autowired
    private IModeloService modeloService; //Inyeccion de la clase (modeloImplementacion) a la interfaz modeloService
    
    // @GetMapping("/")
    // public String inicio() {
    //     return "index";
    // }

    @Autowired
    private IMarcaService marcaService;

    @GetMapping("/agregarModelo")
    public String agregarModelo(Model model) {
        var modelo = new Modelo(); // Crear una nueva instancia de Modelo
        List<Marca> marcas = marcaService.listarMarcas(); // Obtener marcas activas
        model.addAttribute("modelo", modelo);
        model.addAttribute("marcas", marcas); // Agregar la lista de marcas al modelo
        return "agregarModificarModelo";
    }

    @GetMapping("/modelos")
    public String listarModelos(Model model) {
        var modelo = modeloService.listarModelos();
        model.addAttribute("modelo", modelo);
        return "modelos";
    }

    @PostMapping("/guardarModelo")
    public String guardarModelo(@Valid Modelo modelo, BindingResult error, Model model) {
        if (error.hasErrors()) {
            List<Marca> marcas = marcaService.listarMarcas(); // Obtener marcas activas
            model.addAttribute("marcas", marcas);

            return "agregarModificarModelo";
        }

        modeloService.guardar(modelo);
        return "redirect:/modelos";
    }

    @GetMapping("/modificarModelo/{idModelo}")
    public String modificarModelo(Modelo modelo, Model model) {
        modelo = modeloService.buscarModelo(modelo);
        model.addAttribute("modelo", modelo);

        List<Marca> marcas = marcaService.listarMarcas(); // Obtener marcas activas
        model.addAttribute("marcas", marcas);

        return "agregarModificarModelo";
    }

    @GetMapping("/eliminarModelo/{idModelo}")
    public String eliminarModelo(Modelo modelo) {
        modeloService.eliminar(modelo);
        return "redirect:/modelos";
    }
}
