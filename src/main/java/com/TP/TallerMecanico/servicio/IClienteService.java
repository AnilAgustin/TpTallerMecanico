package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Cliente;

import java.util.List;
public interface IClienteService {

    public List<Cliente> listarClientes();
    public void guardar(Cliente cliente);
    public void eliminar(Cliente cliente);
    public Cliente buscarCliente(Cliente cliente);
}
