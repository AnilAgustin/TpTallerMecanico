package com.TP.TallerMecanico.gestor;

import com.TP.TallerMecanico.entidad.Cliente;
import com.TP.TallerMecanico.entidad.Marca;
import com.TP.TallerMecanico.entidad.Orden;
import com.TP.TallerMecanico.interfaz.IClienteDao;
import com.TP.TallerMecanico.servicio.IClienteService;
import com.TP.TallerMecanico.servicio.IOrdenService;
import jakarta.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class GestorCliente {

    //El Autowired sirve para la inyeccion de dependencias 
    @Autowired
    private IClienteService clienteService;

    @Autowired
    private IClienteDao clienteDao;

    @Autowired
    private IOrdenService ordenService;

    //Listar todos los clientes cuando la URL sea /clientes
    @GetMapping("/clientes")
    public String listarClientes(Model model) {
        var cliente = clienteService.listarClientes();
        model.addAttribute("cliente", cliente);
        return "clientes";
    }

    @GetMapping("/clientesEliminados")
    public String clientesEliminados(Model model, @RequestParam(name = "dni", required = false) Long dniNumero){
        List<Cliente> clientes;

        String dni = null;
        if (dniNumero != null) {
            // Si se proporciona un DNI numérico, conviértelo a una cadena
            dni = Long.toString(dniNumero);
            clientes = clienteDao.findByDniStartingWithAndEstadoFalse(dni);

        }else{
            clientes = clienteDao.findByEstadoFalse();
            
        }

        model.addAttribute("cliente", clientes);
        
        return "clientesEliminados";
    }

    //Cuando se presiona el boton editar se retorna el html con todos los datos de la marca seleccionada para modificar 
    @GetMapping("/recuperarCliente/{idCliente}")
    public String recuperarCliente(@PathVariable Long idCliente, Model model) {
        Cliente cliente = clienteDao.findByIdCliente(idCliente);
        clienteService.activarCliente(cliente);
        return "redirect:/clientes";
    }


    @GetMapping("/buscarClientes")
    public String buscarNombreFechaCliente(@RequestParam(name = "nombre", required = false) String nombre,
                                        @RequestParam(name = "fechaDesde", required = false) LocalDate fechaDesde,
                                        @RequestParam(name = "fechaHasta", required = false) LocalDate fechaHasta,
                                        @RequestParam(name = "dni", required = false) Long dniNumero,
                                        Model model) {
    
        if (nombre != null) {
            nombre = nombre.toUpperCase();
        }

        Set<Cliente> clientesSet = new HashSet<>();  // Usar un Set para clientes únicos

        //Casteo para poder trabajar con dni como String
        String dni = null;
        if (dniNumero != null) {
            // Si se proporciona un DNI numérico, conviértelo a una cadena
            dni = Long.toString(dniNumero);
        }
    
        if (nombre != null && !nombre.isEmpty() && dni != null && (fechaDesde != null || fechaHasta != null)) {
            // Si se proporciona tanto un nombre como una fechaDesde y una fechaHasta, se buscan los clientes
            // con ese nombre que tengan órdenes en la fecha especificada.
            List<Orden> ordenes = ordenService.listarOrdenesFecha(fechaDesde, fechaHasta);
    
            for (Orden orden : ordenes) {
                if (orden.getVehiculo().getCliente().getNombre().startsWith(nombre)) {
                    if (orden.getVehiculo().getCliente().getDni().startsWith(dni)) {
                        clientesSet.add(orden.getVehiculo().getCliente());
                    }
                    
                }
            }
    
        }else if (nombre != null && !nombre.isEmpty() && dni == null && (fechaDesde != null || fechaHasta != null)) {
            List<Orden> ordenes = ordenService.listarOrdenesFecha(fechaDesde, fechaHasta);
    
            for (Orden orden : ordenes) {
                if (orden.getVehiculo().getCliente().getNombre().startsWith(nombre)) {
                    clientesSet.add(orden.getVehiculo().getCliente());
                }
            }
            
        }else if (nombre.isEmpty()  && dni != null && (fechaDesde != null || fechaHasta != null)) {
            List<Orden> ordenes = ordenService.listarOrdenesFecha(fechaDesde, fechaHasta);

            for (Orden orden : ordenes) {
                if (orden.getVehiculo().getCliente().getDni().startsWith(dni)) {
                    clientesSet.add(orden.getVehiculo().getCliente());
                }
            }
        }else if ((fechaDesde != null || fechaHasta != null)) {
            // Si solo se proporciona una fecha, puedes buscar los clientes que tienen órdenes
            // en la fecha especificada.
            List<Orden> ordenes = ordenService.listarOrdenesFecha(fechaDesde, fechaHasta);
    
            for (Orden orden : ordenes) {
                clientesSet.add(orden.getVehiculo().getCliente());
            }
        } else if (nombre != null && !nombre.isEmpty()) {
            // Si solo se proporciona un nombre, puedes buscar los clientes por ese nombre.
            clientesSet.addAll(clienteService.buscarClienteNombre(nombre));
        } else if (dni != null) {
            clientesSet.addAll(clienteService.buscarClienteDni(dni));
        }else {
            // Si no se proporciona ni un nombre ni una fecha, puedes listar todos los clientes.
            clientesSet.addAll(clienteService.listarClientes());
        }
    
        List<Cliente> clientes = new ArrayList<>(clientesSet);  // Convertir Set a List
    
        model.addAttribute("cliente", clientes);
        model.addAttribute("nombre", nombre);
        model.addAttribute("dni", dni);
        model.addAttribute("fechaDesde", fechaDesde);
        model.addAttribute("fechaHasta", fechaHasta);
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