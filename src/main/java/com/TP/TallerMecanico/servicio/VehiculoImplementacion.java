package com.TP.TallerMecanico.servicio;


import com.TP.TallerMecanico.entidad.Vehiculo;
import com.TP.TallerMecanico.interfaz.IVehiculoDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VehiculoImplementacion implements IVehiculoService {

    @Autowired
    private IVehiculoDao vehiculoDao;

    @Override
    @Transactional(readOnly = true)
    public List<Vehiculo> listarVehiculos() { return vehiculoDao.findByEstadoTrue(); }

    @Override
    @Transactional
    public void guardar(Vehiculo vehiculo) {
        String patente = vehiculo.getPatente();
        Vehiculo patenteExistente = vehiculoDao.findByPatente(patente);
        Vehiculo vehiculoActivado = vehiculoDao.findByPatenteAndEstadoTrue(patente);

        if (patenteExistente == null) { //Chequeamos si la patente ya existe, y si no existe guardamos directamente
            vehiculoDao.save(vehiculo);
        } else {
            if (vehiculoActivado == null) {
                vehiculoDao.marcarComoActivo(patenteExistente.getIdVehiculo());
                if (!patenteExistente.equals(vehiculo)){
                    vehiculo.setIdVehiculo(patenteExistente.getIdVehiculo());
                    vehiculoDao.save(vehiculo);
                }
            }
        }
    } 

    @Override
    @Transactional
    public void actualizar(Vehiculo vehiculo){
        Long vehiculoId = vehiculo.getIdVehiculo();
        Vehiculo vehiculoExistente = vehiculoDao.findById(vehiculoId).orElse(null);
        if (vehiculoExistente != null) {
            // Verificar si la patente ha cambiado
            String nuevaPatente = vehiculo.getPatente();
            String patenteExistente = vehiculoExistente.getPatente();

            if (nuevaPatente.equals(patenteExistente) || !patenteExisteEnBaseDeDatos(nuevaPatente)) {
                // La patente es igual a la existente o no existe en la base de datos, actualizar valores
                vehiculoDao.save(vehiculo);
            }
        }
    }

    private boolean patenteExisteEnBaseDeDatos(String patente) {
        return vehiculoDao.findByPatente(patente) != null;
    }

    // Resto de métodos del servicio


    @Override
    @Transactional
    public void eliminar(Vehiculo vehiculo) {
        vehiculoDao.marcarComoEliminado(vehiculo.getIdVehiculo());
    }

    @Override
    @Transactional(readOnly = true)
    public Vehiculo buscarVehiculo(Long vehiculo) {
        return vehiculoDao.findById(vehiculo).orElse(null);
    }

    @Override
    public void activarVehiculo(Vehiculo vehiculo) {
        vehiculoDao.marcarComoActivo(vehiculo.getIdVehiculo());
    }
}