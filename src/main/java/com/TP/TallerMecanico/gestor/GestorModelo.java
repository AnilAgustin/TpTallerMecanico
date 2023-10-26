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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GestorModelo {

    //El Autowired sirve para la inyeccion de dependencias 
    @Autowired
    private IModeloService modeloService; // Inyeccion de la clase (modeloImplementacion) a la interfaz modeloService

    @Autowired
    private IMarcaService marcaService;

    //Listar todos los modelos cuando la URL sea /modelos
    @GetMapping("/modelos")
    public String listarModelos(@RequestParam(name = "marca", required = false) Long marcaId, @RequestParam(name = "nombre", required = false) String nombre, Model model) {
        //var modelo = modeloService.listarModelos();

        List<Modelo> modelos;
        var marca = marcaService.listarMarcas();

        if (nombre != null) {
            nombre = nombre.toUpperCase();
        }

        //System.out.println(nombre);

        if ((marcaId!=null) || (nombre!=null)) {
            modelos = modeloService.filtrarModelos(marcaId, nombre);
        }else{
            modelos= modeloService.listarModelos(); 
        }

        model.addAttribute("modelo", modelos);
        model.addAttribute("nombre", nombre);
        model.addAttribute("marca", marca);
        model.addAttribute("idMarca", marcaId);
        return "modelos";
    }

    //Permitir agregar un modelo cuando la URL sea /agregarModelo
    @GetMapping("/agregarModelo")
    public String agregarModelo(Model model) {
        var modelo = new Modelo(); // Crear una nueva instancia de Modelo
        List<Marca> marcas = marcaService.listarMarcas(); // Obtener marcas activas
        model.addAttribute("modelo", modelo);
        model.addAttribute("marcas", marcas); // Agregar la lista de marcas al modelo
        model.addAttribute("modo", "nuevo");
        return "agregarModificarModelo";
    }

    //Permite guardar un modelo cuando la solicitud POST sea guardarModelo 
    @PostMapping("/guardarModelo")
    public String guardarModelo(@Valid Modelo modelo, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","nuevo");
            List<Marca> marcas = marcaService.listarMarcas(); // Obtener marcas activas
            model.addAttribute("marcas", marcas);
            return "agregarModificarModelo";
        }
        //Se llama a la logica guardar definida en IModeloService, pero en realidad es ModeloImplementacion
        modeloService.guardar(modelo);
        return "redirect:/modelos";
    }

    //Permite actualizar un modelo cuando la solicitud POST sea actualizarModelo
    @PostMapping("/actualizarModelo")
    public String actualizarModelo(@Valid @ModelAttribute Modelo modelo, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","editar");
            List<Marca> marcas = marcaService.listarMarcas();
            model.addAttribute("marcas", marcas);
            return "agregarModificarModelo";
        }
        modeloService.actualizar(modelo);
        return "redirect:/modelos";
    }

    //Cuando se presiona el boton editar se retorna el html con todos los datos del modelo seleccionado para modificar 
    @GetMapping("/modificarModelo/{idModelo}")
    public String modificarModelo(Modelo modelo, Model model) {
        modelo = modeloService.buscarModelo(modelo);
        model.addAttribute("modelo", modelo);
        List<Marca> marcas = marcaService.listarMarcas(); // Obtener marcas activas
        model.addAttribute("marcas", marcas);
        model.addAttribute("modo", "editar");
        return "agregarModificarModelo";
    }

    //Cuando se presiona el boton eliminar se pasa el ID del modelo y este se elimina (Soft Delete)
    @GetMapping("/eliminarModelo/{idModelo}")
    public String eliminarModelo(Modelo modelo) {
        modeloService.eliminar(modelo);
        return "redirect:/modelos";
    }
}
