package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Cliente;
import com.TP.TallerMecanico.interfaz.IClienteDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteImplementacion implements IClienteService {

    @Autowired
    private IClienteDao clienteDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() { return clienteDao.findByEstadoTrue(); }

    @Override
    @Transactional
    public void guardar(Cliente cliente) {
        String dni = cliente.getDni();
        Cliente clienteExistente = clienteDao.findByDni(dni);
        Cliente clienteRegistrado = clienteDao.findByDniAndEstadoTrue(dni);

        if (clienteExistente == null) {
            clienteDao.save(cliente);
        } else {
            if (clienteRegistrado == null) {
                clienteDao.marcarComoActivo(clienteExistente.getIdCliente());
                }
            }
    }

    @Override
    @Transactional
    public void eliminar(Cliente cliente) {
        clienteDao.marcarComoEliminado(cliente.getIdCliente());
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarCliente(Cliente cliente) {
        return clienteDao.findById(cliente.getIdCliente()).orElse(null);
    }
}