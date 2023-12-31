package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.Cliente;
import com.TP.TallerMecanico.servicio.IClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class GestorCliente {


    //El Autowired sirve para la inyeccion de dependencias 
    @Autowired
    private IClienteService clienteService;

    //Listar todos los clientes cuando la URL sea /clientes
    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        var cliente = clienteService.listarClientes();
        model.addAttribute("cliente", cliente);
        return "clientes";
    }


    //Permitir agregar un cliente cuando la URL sea /agregarCliente
    @GetMapping("/agregarCliente")
    public String agregarCliente(Model model){
        var cliente = new Cliente();
        model.addAttribute("cliente", cliente);
        model.addAttribute("modo", "nuevo");
        return "agregarModificarCliente";
    }

    //Permite guardar un cliente cuando la solicitud POST sea guardarCliente 
    @PostMapping("/guardarCliente")
    public String guardarCliente(@Valid Cliente cliente, BindingResult error, Model model){
        if(error.hasErrors()){   
            model.addAttribute("modo","nuevo");
            return "agregarModificarCliente";
        }
        //Se llama a la logica guardar definida en IClienteService, pero en realidad es ClienteImplementacion
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    //Permite actualizar un cliente cuando la solicitud POST sea actualizarCliente
    @PostMapping("/actualizarCliente")
    public String actualizarVehiculo(@Valid @ModelAttribute Cliente cliente, BindingResult error,Model model) {
        if(error.hasErrors()) {
            model.addAttribute("modo","editar");
            return "agregarModificarCliente";
        }
        clienteService.actualizar(cliente);
        return "redirect:/clientes";
    }

    //Cuando se presiona el boton editar se retorna el html con todos los datos del cliente seleccionado para modificar 
    @GetMapping("/modificarCliente/{idCliente}")
    public String modificarCliente(Cliente cliente, Model model){
        cliente = clienteService.buscarCliente(cliente);
        model.addAttribute("cliente", cliente);
        model.addAttribute("modo", "editar");
        return "agregarModificarCliente";
    }

    //Cuando se presiona el boton eliminar se pasa el ID del cliente y este se elimina (Soft Delete)
    @GetMapping("/eliminarCliente/{idCliente}")
    public String eliminarCliente(Cliente cliente){
        clienteService.eliminar(cliente);
        return "redirect:/clientes";
    }
}