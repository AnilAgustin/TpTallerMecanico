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

    


    @Override
    @Transactional //Anotacion para controlar que las operaciones se ejecuten de manera correcta 
    public void guardar(Cliente cliente) {//Metodo para guardar un nuevo cliente
        cliente.setNombre(cliente.getNombre().toUpperCase());
        cliente.setApellido(cliente.getApellido().toUpperCase());
        String dni = cliente.getDni();
        Cliente dniExistente = clienteDao.findByDni(dni);
        Cliente dniActivado = clienteDao.findByDniAndEstadoTrue(dni);
        
        //Si el DNI ingresado no existe en la BD se guarda el cliente 
        if (dniExistente == null) {
            clienteDao.save(cliente);
        } else {
            //Si existe ser verifica si el cliente con ese DNI esta activado 
            if (dniActivado == null){
                //Se activa el cliente y todos sus vehiculos asociados 
                activarCliente(dniExistente);
                //Metodo para sobreescribir un cliente eliminado con datos diferentes a los cargados (Ver)
                if (!dniExistente.equals(cliente)){
                    cliente.setIdCliente(dniExistente.getIdCliente());
                    clienteDao.save(cliente);
                }
            }
        }
    }


    @Override
    @Transactional
    public void actualizar(Cliente cliente){//Metodo para actualizar un cliente existente activado 
        cliente.setNombre(cliente.getNombre().toUpperCase());
        cliente.setApellido(cliente.getApellido().toUpperCase());
        Long clienteId = cliente.getIdCliente();
        Cliente clienteExistente = clienteDao.findById(clienteId).orElse(null);
        
        Cliente clienteByDni = clienteDao.findByDni(cliente.getDni());
        activarCliente(clienteByDni);

        if (clienteExistente != null) {
            String nuevoDni = cliente.getDni();
            String dniExistente = clienteExistente.getDni();

            //Para controlar que no haya duplicaciones de DNI
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

        @Override
    @Transactional
    public void activarCliente(Cliente cliente){
        clienteDao.marcarComoActivo(cliente.getIdCliente());
        for (Vehiculo vehiculo : cliente.getVehiculos()){
            vehiculoService.activarVehiculo(vehiculo);
        }
    }
}