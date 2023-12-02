package com.TP.TallerMecanico.servicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TP.TallerMecanico.interfaz.IOrdenDao;

@Service
public class EstadisticaImplementacion implements IEstadisticaService {
    @Autowired
    private IOrdenDao ordenDao;

    @Override
    public Map<String, Double> obtenerEstadisticasIngresosMensuales(int year) {
        List<Object[]> resultados = ordenDao.obtenerIngresosMensuales(year);

        // Convertir resultados a Map
        Map<String, Double> estadisticas = new HashMap<>();
        double ingresoTotalAnual = 0.0;
        
        for (Object[] resultado : resultados) {
            int mes = (int) resultado[0];
            double recaudacionMensual = (double) resultado[1];
            
            // Agregar el recaudo mensual al mapa
            estadisticas.put(String.format("%02d", mes), recaudacionMensual);
            
            // Acumular el recaudo mensual al total anual
            ingresoTotalAnual += recaudacionMensual;
        }

        // Agregar el total anual al mapa
        estadisticas.put("IngresoTotalAnual", ingresoTotalAnual);

        return estadisticas;
    }
}