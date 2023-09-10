package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.Cliente;
import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.servicio.IClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.naming.Binding;

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
    public String agregarCliente(Model model){
        var cliente = new Cliente();
        model.addAttribute("cliente", cliente);
        model.addAttribute("modo", "nuevo");
        return "agregarModificarCliente";
    }

    @PostMapping("/guardarCliente")
    public String guardarCliente(@Valid Cliente cliente, BindingResult error, Model model){
        if(error.hasErrors()){
            model.addAttribute("modo","nuevo");
            return "agregarModificarCliente";
        }
        clienteService.guardar(cliente);
        return "redirect:/clientes";
    }

    @PostMapping("/actualizarCliente")
    public String actualizarVehiculo(@Valid @ModelAttribute Cliente cliente, BindingResult error,Model model) {
        if(error.hasErrors()) {
            model.addAttribute("modo","editar");
            return "agregarModificarCliente";
        }
        clienteService.actualizar(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/modificarCliente/{idCliente}")
    public String modificarCliente(Cliente cliente, Model model){
        cliente = clienteService.buscarCliente(cliente);
        model.addAttribute("cliente", cliente);
        model.addAttribute("modo", "editar");
        return "agregarModificarCliente";
    }
    @GetMapping("/eliminarCliente/{idCliente}")
    public String eliminarCliente(Cliente cliente){
        clienteService.eliminar(cliente);
        return "redirect:/clientes";
    }
}