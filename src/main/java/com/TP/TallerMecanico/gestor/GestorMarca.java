package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.Cliente;
import com.TP.TallerMecanico.entidad.Marca;
import com.TP.TallerMecanico.entidad.Orden;
import com.TP.TallerMecanico.interfaz.IMarcaDao;

import jakarta.validation.Valid;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.TP.TallerMecanico.servicio.IMarcaService;

@Controller
public class GestorMarca {

    //Inyeccion de la clase (MarcaImplementacion) a la interfaz MarcaService
    @Autowired
    private IMarcaService marcaService; 

    @Autowired
    private IMarcaDao marcaDao;

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

    @GetMapping("/marcasEliminadas")
    public String marcasEliminadas(@RequestParam(name = "nombre", required = false) String nombre, Model model){
        List<Marca> marcas;

        if (nombre != null) {
            nombre = nombre.toUpperCase();
            marcas = marcaDao.findMarcaByNombreAndEstadoFalse(nombre);
        }else{
            marcas = marcaDao.findByEstadoFalse();
        }

        
        model.addAttribute("marca", marcas);
        
        return "marcasEliminadas";
    }


    @GetMapping("/buscarMarcas")
    public String buscarMarca(@RequestParam(name = "nombre", required = false) String nombre, Model model){

        if (nombre != null) {
            nombre = nombre.toUpperCase();
        }
        //Set<Marca> marcasSet = new HashSet<>();  // Usar un Set para clientes únicos

        List<Marca> marcas;

        if (nombre != null) {
            marcas = marcaService.buscarMarcaPorNombre(nombre);
        }else{
            marcas = marcaService.listarMarcas();
        }
        
        model.addAttribute("nombre", nombre);
        model.addAttribute("marca", marcas);
        
        return "marcas";
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

    //Cuando se presiona el boton editar se retorna el html con todos los datos de la marca seleccionada para modificar 
    @GetMapping("/recuperarMarca/{idMarca}")
    public String recuperarMarca(@PathVariable Long idMarca, Model model) {
        Marca marca = marcaDao.findByIdMarca(idMarca);
        marcaService.activarMarca(marca);
        return "redirect:/marcas";
    }

    //Cuando se presiona el boton eliminar se pasa el ID de la marca y esta se elimina (Soft Delete)
    @GetMapping("/eliminarMarca/{idMarca}")
    public String eliminarMarca(Marca marca) {
        marcaService.eliminar(marca);
        return "redirect:/marcas";
    }
}
