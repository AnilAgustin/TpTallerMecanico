package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Cliente;
import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.entidad.Vehiculo;
import com.TP.TallerMecanico.interfaz.IClienteDao;
import java.util.List;

import com.TP.TallerMecanico.interfaz.IVehiculoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteImplementacion implements IClienteService {

    @Autowired
    private IVehiculoDao vehiculoDao;

    @Autowired
    private IVehiculoService vehiculoService;

    @Autowired
    private IClienteDao clienteDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() { return clienteDao.findByEstadoTrue(); }

    //private List<Vehiculo> vehiculosAntesDeEliminar;

    @Override
    @Transactional
    public void guardar(Cliente cliente) {
        cliente.setNombre(cliente.getNombre().toUpperCase());
        cliente.setApellido(cliente.getApellido().toUpperCase());
        String dni = cliente.getDni();
        Cliente dniExistente = clienteDao.findByDni(dni);
        Cliente dniActivado = clienteDao.findByDniAndEstadoTrue(dni);

        if (dniExistente == null) {
            clienteDao.save(cliente);
        } else {
            if (dniActivado == null){
                clienteDao.marcarComoActivo(dniExistente.getIdCliente());
                for (Vehiculo vehiculo : dniExistente.getVehiculos()){
                    vehiculoService.activarVehiculo(vehiculo);
                }
                if (!dniExistente.equals(cliente)){
                    cliente.setIdCliente(dniExistente.getIdCliente());
                    clienteDao.save(cliente);
                }
            }
        }
    }


    @Override
    @Transactional
    public void actualizar(Cliente cliente){
        cliente.setNombre(cliente.getNombre().toUpperCase());
        cliente.setApellido(cliente.getApellido().toUpperCase());
        Long clienteId = cliente.getIdCliente();
        Cliente clienteExistente = clienteDao.findById(clienteId).orElse(null);
        if (clienteExistente != null) {
            String nuevoDni = cliente.getDni();
            String dniExistente = clienteExistente.getDni();

            if (nuevoDni.equals(dniExistente) || !dniExisteEnBaseDeDatos(nuevoDni)) {
                clienteDao.save(cliente);
            }
        }
    }

    private boolean dniExisteEnBaseDeDatos(String dniCliente) {
        return clienteDao.findByDni(dniCliente) != null;
    }

    @Override
    @Transactional
    public void eliminar(Cliente cliente) {
        clienteDao.marcarComoEliminado(cliente.getIdCliente());
        for (Vehiculo vehiculo : vehiculoDao.findByClienteAndEstadoTrue(cliente)){
            vehiculoService.eliminar(vehiculo);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarCliente(Cliente cliente) {
        return clienteDao.findById(cliente.getIdCliente()).orElse(null);
    }
}