package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.Cliente;
import com.TP.TallerMecanico.servicio.IClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GestorCliente {

    @Autowired
    private IClienteService clienteService;

    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        var cliente = clienteService.listarClientes();
        model.addAttribute("cliente", cliente);
        return "clientes";
    }

    @GetMapping("/agregarCliente")
    public String agregarCliente(Cliente cliente){
        return "agregarModificarCliente";
    }

    @PostMapping("/guardarCliente")
    public String guardarCliente(@Valid Cliente cliente, BindingResult error){
        if(error.hasErrors()){
            return "agregarModificarCliente";
        }
        
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/modificarCliente/{idCliente}")
    public String modificarCliente(Cliente cliente, Model model){
        cliente = clienteService.buscarCliente(cliente);
        model.addAttribute("cliente", cliente);
        return "agregarModificarCliente";
    }
    @GetMapping("/eliminarCliente/{idCliente}")
    public String eliminarCliente(Cliente cliente){
        clienteService.eliminar(cliente);
        return "redirect:/clientes";
    }
}