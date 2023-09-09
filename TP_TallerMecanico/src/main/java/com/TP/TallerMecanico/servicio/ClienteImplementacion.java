package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Cliente;
import com.TP.TallerMecanico.interfaz.IClienteDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteImplementacion implements ClienteService {

    @Autowired
    private IClienteDao clienteDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() { return clienteDao.findByEstadoTrue(); }

    @Override
    @Transactional
    public void guardar(Cliente cliente) {
        Integer razonCliente = cliente.getrazonSocial();
        Cliente clienteExistente = clienteDao.findByRazonSocial(razonCliente);
        Cliente clienteRegistrado = clienteDao.findByRazonSocialAndEstadoTrue(razonCliente);

        if (clienteExistente == null) {
            clienteDao.save(cliente);
        } else {
            if (clienteRegistrado == null) {
                clienteDao.marcarComoActivo(clienteExistente.getidCliente());
                }
            }
    }

    @Override
    @Transactional
    public void eliminar(Cliente cliente) {
        clienteDao.marcarComoEliminado(cliente.getidCliente());
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarCliente(Cliente cliente) {
        return clienteDao.findById(cliente.getidCliente()).orElse(null);
    }
}
