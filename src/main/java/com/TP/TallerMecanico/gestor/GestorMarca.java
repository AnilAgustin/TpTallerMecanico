package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.Marca;
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

    //Inyeccion de la clase (MarcaImplementacion) a la interfaz MarcaService
    @Autowired
    private IMarcaService marcaService; 


    //Cuando ingresamos a la URL del puerto nos dirigimos al index 
    @GetMapping("/")
    public String inicio() {
        return "index";
    }


    //Listar todos las marcas cuando la URL sea /marcas
    @GetMapping("/marcas")
    public String listarMarcas(Model model) {
        var marca = marcaService.listarMarcas();
        model.addAttribute("marca", marca);
        return "marcas";
    }

    //Permitir agregar una marca cuando la URL sea /agregarMarca
    @GetMapping("/agregarMarca")
    public String agregarMarca(Model model) {
        var marca = new Marca();
        model.addAttribute("marca", marca);
        model.addAttribute("modo", "nuevo");
        return "agregarModificarMarca";
    }


    //Permite guardar una marca cuando la solicitud POST sea guardarMarca
    @PostMapping("/guardarMarca")
    public String guardarMarca(@Valid Marca marca, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","nuevo");
            return "agregarModificarMarca";
        }
        //Se llama a la logica guardar definida en IMarcaService, pero en realidad es MarcaImplementacion
        marcaService.guardar(marca);
        return "redirect:/marcas";
    }

    //Permite actualizar una marca cuando la solicitud POST sea actualizarMarca
    @PostMapping("/actualizarMarca")
    public String actualizarMarca(@Valid @ModelAttribute Marca marca, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","editar");
            return "agregarModificarMarca";
        }
        marcaService.actualizar(marca);
        return "redirect:/marcas";
    }

    //Cuando se presiona el boton editar se retorna el html con todos los datos de la marca seleccionada para modificar 
    @GetMapping("/modificarMarca/{idMarca}")
    public String modificarMarca(Marca marca, Model model) {
        marca = marcaService.buscarMarca(marca);
        model.addAttribute("marca", marca);
        model.addAttribute("modo", "editar");
        return "agregarModificarMarca";
    }

    //Cuando se presiona el boton eliminar se pasa el ID de la marca y esta se elimina (Soft Delete)
    @GetMapping("/eliminarMarca/{idMarca}")
    public String eliminarMarca(Marca marca) {
        marcaService.eliminar(marca);
        return "redirect:/marcas";
    }
}
