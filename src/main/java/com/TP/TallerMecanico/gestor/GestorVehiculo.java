package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.*;
import com.TP.TallerMecanico.interfaz.IVehiculoDao;
import com.TP.TallerMecanico.servicio.IClienteService;
import com.TP.TallerMecanico.servicio.IMarcaService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GestorVehiculo {
    //El Autowired sirve para la inyeccion de dependencias 
    @Autowired
    private IVehiculoService vehiculoService;

    @Autowired
    private IModeloService modeloService;

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private ITecnicoService tecnicoService;

    @Autowired
    private IMarcaService marcaService;

    @GetMapping("/vehiculos")
    public String listarVehiculos(@RequestParam(name = "patente", required = false) String patente, 
                                  @RequestParam(name = "marca", required = false) Long marcaId, 
                                  @RequestParam(name = "modelo", required = false) Long modeloId,  Model model) {
        List<Vehiculo> vehiculos;


        //Lógica para filtrar
        if (patente!=null) {
            patente = patente.toUpperCase();
        }else{
            patente=null;
        }

        if ((patente != null) || (marcaId != null) || (modeloId != null)) {
            // Si se envió algún parámetro de búsqueda (patente, marca o modelo), realiza la búsqueda
            System.out.println(patente);
            System.out.println(marcaId);
            System.out.println(modeloId);

            vehiculos = vehiculoService.filtrarVehiculos(patente, marcaId, modeloId);
        } else {
            // Si no se enviaron parámetros de búsqueda, lista todos los vehículos
            vehiculos = vehiculoService.listarVehiculos();
        }

        
        //Lógica para mostrar todos 
        List<Marca> marcas = marcaService.listarMarcas();
        List<Modelo> modelos = modeloService.listarModelos();

        model.addAttribute("patente", patente);
        model.addAttribute("idModelo", modeloId);
        model.addAttribute("idMarca", marcaId);

        model.addAttribute("vehiculo", vehiculos);
        model.addAttribute("marcas", marcas);
        model.addAttribute("modelos", modelos);

        return "vehiculos";
    }


    //Permitir agregar un vehiculo cuando la URL sea /agregarVehiculo
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


    //Permite guardar un cliente cuando la solicitud POST sea guardarVehiculo 
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

        //Se llama a la logica guardar definida en IVehiculoService, pero en realidad es VehiculoImplementacion
        vehiculoService.guardar(vehiculo);
        return "redirect:/vehiculos";
    }

    //Permite actualizar un vehiculo cuando la solicitud POST sea actualizarVehiculo
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

    //Cuando se presiona el boton editar se retorna el html con todos los datos del vehiculo seleccionado para modificar 
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

    //Cuando se presiona el boton eliminar se pasa el ID del vehiculo y este se elimina (Soft Delete)
    @GetMapping("/eliminarVehiculo/{idVehiculo}")
    public String eliminarVehiculo(Vehiculo vehiculo) {

        vehiculoService.eliminar(vehiculo);
        return "redirect:/vehiculos";
    }

    
}