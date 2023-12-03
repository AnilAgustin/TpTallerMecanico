package com.TP.TallerMecanico.servicio;
import java.time.LocalDate;
import java.util.Map;

public interface IEstadisticaService {
    Map<String, Double> obtenerEstadisticasIngresosMensuales(int year);
    
    Map<String, Map<String, Double>> obtenerEstadisticasPorServicioEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin);

    Double calcularMontoTotalEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin);

    Map<String, Double> findServicioMasRecaudo(LocalDate fechaInicio, LocalDate fechaFin);
    Map<String, Double> findServicioMasUtilizado(LocalDate fechaInicio, LocalDate fechaFin);
    Map<String, Double> findMesMasRecaudado(int year);

    
}