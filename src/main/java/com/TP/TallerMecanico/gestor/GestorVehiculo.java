package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.*;
import com.TP.TallerMecanico.servicio.IClienteService;
import com.TP.TallerMecanico.servicio.IModeloService;
import com.TP.TallerMecanico.servicio.ITecnicoService;
import com.TP.TallerMecanico.servicio.IVehiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class GestorVehiculo {

    @Autowired
    private IVehiculoService vehiculoService;

    @Autowired
    private IModeloService modeloService;

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private ITecnicoService tecnicoService;

    @GetMapping("/agregarVehiculo")
    public String agregarVehiculos(Model model) {
        var vehiculo = new Vehiculo();
        List<Modelo> modelos = modeloService.listarModelos();
        List<Cliente> clientes = clienteService.listarClientes();
        List<Tecnico> tecnicos = tecnicoService.listarTecnicos();
        model.addAttribute("modelos", modelos);
        model.addAttribute("clientes", clientes);
        model.addAttribute("tecnicos", tecnicos);
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("modo", "nuevo");
        return "agregarModificarVehiculo";
    }

    @GetMapping("/vehiculos")
    public String listarVehiculos(Model model) {
        var vehiculo = vehiculoService.listarVehiculos();
        model.addAttribute("vehiculo", vehiculo);
        return "vehiculos";
    }

    @PostMapping("/guardarVehiculo")
    public String guardarVehiculo(@Valid Vehiculo vehiculo, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","nuevo");
            List<Modelo> modelos = modeloService.listarModelos();
            List<Cliente> clientes = clienteService.listarClientes();
            List<Tecnico> tecnicos = tecnicoService.listarTecnicos();
            model.addAttribute("modelos", modelos);
            model.addAttribute("clientes", clientes);
            model.addAttribute("tecnicos", tecnicos);
            return "agregarModificarVehiculo";
        }
        vehiculoService.guardar(vehiculo);
        return "redirect:/vehiculos";
    }

    @PostMapping("/actualizarVehiculo")
    public String actualizarVehiculo(@Valid @ModelAttribute Vehiculo vehiculo, BindingResult error, Model model) {
        if (error.hasErrors()) {
            model.addAttribute("modo","editar");
            List<Modelo> modelos = modeloService.listarModelos();
            List<Cliente> clientes = clienteService.listarClientes();
            List<Tecnico> tecnicos = tecnicoService.listarTecnicos();
            model.addAttribute("modelos", modelos);
            model.addAttribute("clientes", clientes);
            model.addAttribute("tecnicos", tecnicos);
            return "agregarModificarVehiculo";
        }
        vehiculoService.actualizar(vehiculo);
        return "redirect:/vehiculos";
    }

    @GetMapping("/modificarVehiculo/{idVehiculo}")
    public String mostrarFormularioEditar(@PathVariable Long idVehiculo, Model model) {
        var vehiculo = vehiculoService.buscarVehiculo(idVehiculo);
        List<Modelo> modelos = modeloService.listarModelos();
        List<Cliente> clientes = clienteService.listarClientes();
        List<Tecnico> tecnicos = tecnicoService.listarTecnicos();
        model.addAttribute("modelos", modelos);
        model.addAttribute("clientes", clientes);
        model.addAttribute("tecnicos", tecnicos);
        model.addAttribute("vehiculo", vehiculo);
        model.addAttribute("modo", "editar");
        return "agregarModificarVehiculo";
    }

    @GetMapping("/eliminarVehiculo/{idVehiculo}")
    public String eliminarVehiculo(Vehiculo vehiculo) {

        vehiculoService.eliminar(vehiculo);
        return "redirect:/vehiculos";
    }

    
}