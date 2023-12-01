package com.TP.TallerMecanico.servicio;
import java.util.Map;

public interface IEstadisticaService {
    Map<String, Double> obtenerEstadisticasIngresosMensuales(int year);
}