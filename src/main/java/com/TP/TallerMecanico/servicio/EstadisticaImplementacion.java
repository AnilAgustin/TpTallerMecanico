package com.TP.TallerMecanico.servicio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.interfaz.IDetalleOrdenDao;
import com.TP.TallerMecanico.interfaz.IOrdenDao;

@Service
public class EstadisticaImplementacion implements IEstadisticaService {
    @Autowired
    private IOrdenDao ordenDao;

    @Autowired
    private IDetalleOrdenDao detalleOrdenDao;

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

    @Override
    public Map<String, Map<String, Double>> obtenerEstadisticasPorServicioEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin, Long tecnicoId) {
        Map<String, Map<String, Double>> estadisticasPorServicio = new HashMap<>();
        Double montoTotal = detalleOrdenDao.calcularMontoTotalEnPeriodo(fechaInicio, fechaFin, tecnicoId);
        List<Object[]> resultados = detalleOrdenDao.obtenerIngresosPorServicio(fechaInicio, fechaFin, tecnicoId);
        for (Object[] resultado : resultados) {
            String nombreServicio = (String) resultado[0];
            Double cantidadUtilizada = ((Number) resultado[1]).doubleValue();
            Double recaudacionServicio =((Number) resultado[2]).doubleValue();

            Double porcentaje = (recaudacionServicio / montoTotal) * 100;

            Map<String, Double> estadisticasServicio = new HashMap<>();
            estadisticasServicio.put("cantidadUtilizada", cantidadUtilizada);
            estadisticasServicio.put("recaudacionServicio", recaudacionServicio);
            estadisticasServicio.put("porcentaje", porcentaje);


            estadisticasPorServicio.put(nombreServicio, estadisticasServicio);
        }

        return estadisticasPorServicio;
    }

    @Override
    public Double calcularMontoTotalEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin, Long tecnicoId){
        Double ingresosTotales = detalleOrdenDao.calcularMontoTotalEnPeriodo(fechaInicio, fechaFin, tecnicoId);
        return ingresosTotales;
    }

    @Override
    public Map<String, Double> findServicioMasRecaudo(LocalDate fechaInicio, LocalDate fechaFin, Long tecnicoId) {
        List<Object[]> resultados = detalleOrdenDao.findServicioMasRecaudo(fechaInicio,fechaFin, tecnicoId);

        // Convertir resultados a Map
        Map<String, Double> masRecaudado = new HashMap<>();

        for (Object[] resultado : resultados) {
            String nombreServicio = (String) resultado[0];
            Double recaudacionServicio =((Number) resultado[1]).doubleValue();

            masRecaudado.put(nombreServicio,recaudacionServicio);


        }

        return masRecaudado;
    }

    @Override
    public Map<String, Double> findServicioMasUtilizado(LocalDate fechaInicio, LocalDate fechaFin, Long tecnicoId) {
        List<Object[]> resultados = detalleOrdenDao.findServicioMasUtilizado(fechaInicio,fechaFin, tecnicoId);

        // Convertir resultados a Map
        Map<String, Double> masUtilizado = new HashMap<>();

        for (Object[] resultado : resultados) {
            String nombreServicio = (String) resultado[0];
            Double recaudacionServicio =((Number) resultado[1]).doubleValue();

            masUtilizado.put(nombreServicio,recaudacionServicio);


        }

        return masUtilizado;


    }

    @Override
    public Map<String, Double> findMesMasRecaudado(int year) {
        List<Object[]> resultados = ordenDao.findMesMasRecaudado(year);

        
        // Convertir resultados a Map
        Map<String, Double> masRecaudado = new HashMap<>();

        for (Object[] resultado : resultados) {
            int mes = (int) resultado[0];
            Double recaudacionMes =((Number) resultado[1]).doubleValue();

            masRecaudado.put(String.format("%02d", mes),recaudacionMes);


        }

        return masRecaudado;


    }

    @Override
    public Map<String, Map<String, Double>> obtenerEstadisticasPorServicioEnPeriodoSinFiltro(LocalDate fechaInicio,
            LocalDate fechaFin) {
                Map<String, Map<String, Double>> estadisticasPorServicio = new HashMap<>();
                Double montoTotal = detalleOrdenDao.calcularMontoTotalEnPeriodoSinFiltro(fechaInicio, fechaFin);
                List<Object[]> resultados = detalleOrdenDao.obtenerIngresosPorServicioSinFiltro(fechaInicio, fechaFin);
                for (Object[] resultado : resultados) {
                    String nombreServicio = (String) resultado[0];
                    Double cantidadUtilizada = ((Number) resultado[1]).doubleValue();
                    Double recaudacionServicio =((Number) resultado[2]).doubleValue();
        
                    Double porcentaje = (recaudacionServicio / montoTotal) * 100;
        
                    Map<String, Double> estadisticasServicio = new HashMap<>();
                    estadisticasServicio.put("cantidadUtilizada", cantidadUtilizada);
                    estadisticasServicio.put("recaudacionServicio", recaudacionServicio);
                    estadisticasServicio.put("porcentaje", porcentaje);
        
        
                    estadisticasPorServicio.put(nombreServicio, estadisticasServicio);
                }
        
                return estadisticasPorServicio;
    }

    @Override
    public Double calcularMontoTotalEnPeriodoSinFiltro(LocalDate fechaInicio, LocalDate fechaFin) {
        Double ingresosTotales = detalleOrdenDao.calcularMontoTotalEnPeriodoSinFiltro(fechaInicio, fechaFin);
        return ingresosTotales;
    }

    @Override
    public Map<String, Double> findServicioMasRecaudoSinFiltro(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Object[]> resultados = detalleOrdenDao.findServicioMasRecaudoSinFiltro(fechaInicio,fechaFin);

        // Convertir resultados a Map
        Map<String, Double> masRecaudado = new HashMap<>();

        for (Object[] resultado : resultados) {
            String nombreServicio = (String) resultado[0];
            Double recaudacionServicio =((Number) resultado[1]).doubleValue();

            masRecaudado.put(nombreServicio,recaudacionServicio);
        }

        return masRecaudado;
    }

    @Override
    public Map<String, Double> findServicioMasUtilizadoSinFiltro(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Object[]> resultados = detalleOrdenDao.findServicioMasUtilizadoSinFiltro(fechaInicio,fechaFin);

        // Convertir resultados a Map
        Map<String, Double> masUtilizado = new HashMap<>();

        for (Object[] resultado : resultados) {
            String nombreServicio = (String) resultado[0];
            Double recaudacionServicio =((Number) resultado[1]).doubleValue();

            masUtilizado.put(nombreServicio,recaudacionServicio);


        }

        return masUtilizado;
    }
    
}