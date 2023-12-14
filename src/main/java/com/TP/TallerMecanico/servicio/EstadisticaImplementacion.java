package com.TP.TallerMecanico.servicio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.TP.TallerMecanico.entidad.Estado;
import com.TP.TallerMecanico.interfaz.IDetalleOrdenDao;
import com.TP.TallerMecanico.interfaz.IEstadoDao;
import com.TP.TallerMecanico.interfaz.IOrdenDao;

@Service
public class EstadisticaImplementacion implements IEstadisticaService {
    @Autowired
    private IOrdenDao ordenDao;

    @Autowired
    private IDetalleOrdenDao detalleOrdenDao;

    @Autowired
    private IEstadoDao estadoDao;
    @Override
    public Map<String, Map<String, Double>> obtenerEstadisticasIngresosMensuales(int year) {
        String nombreFacturada = "FACTURADA";
        Estado estadoActual = estadoDao.findByNombre(nombreFacturada);
        List<Object[]> resultados = ordenDao.obtenerIngresosYOrdenesMensuales(year,estadoActual);
    
        // Convertir resultados a Map
        Map<String, Map<String, Double>> estadisticas = new HashMap<>();
        double ingresoTotalAnual = 0.0;
        
        for (Object[] resultado : resultados) {
            int mes = (int) resultado[0];
            long cantidadOrdenes = (long) resultado[1];
            double recaudacionMensual = (double) resultado[2];
            
            // Agregar el recaudo mensual y la cantidad de órdenes al mapa
            Map<String, Double> datosMensuales = new HashMap<>();
            datosMensuales.put("RecaudacionMensual", recaudacionMensual);
            datosMensuales.put("CantidadOrdenes", (double) cantidadOrdenes);
            estadisticas.put(String.format("%02d", mes), datosMensuales);
            
            // Acumular el recaudo mensual al total anual
            ingresoTotalAnual += recaudacionMensual;
        }
    
        // Agregar el total anual al mapa
        Map<String, Double> totalAnual = new HashMap<>();
        totalAnual.put("IngresoTotalAnual", ingresoTotalAnual);
        estadisticas.put("TotalAnual", totalAnual);
    
        return estadisticas;
    }

    @Override
    public Map<String, Map<String, Double>> obtenerEstadisticasPorServicioEnPeriodo(LocalDate fechaInicio, LocalDate fechaFin, Long tecnicoId) {
        Map<String, Map<String, Double>> estadisticasPorServicio = new HashMap<>();
        String nombreFacturada = "FACTURADA";
        Estado estadoActual = estadoDao.findByNombre(nombreFacturada);
        Double montoTotal = detalleOrdenDao.calcularMontoTotalEnPeriodo(fechaInicio, fechaFin, tecnicoId,estadoActual);
        List<Object[]> resultados = detalleOrdenDao.obtenerIngresosPorServicio(fechaInicio, fechaFin, tecnicoId,estadoActual);
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
        String nombreFacturada = "FACTURADA";
        Estado estadoActual = estadoDao.findByNombre(nombreFacturada);
        Double ingresosTotales = detalleOrdenDao.calcularMontoTotalEnPeriodo(fechaInicio, fechaFin, tecnicoId,estadoActual);
        return ingresosTotales;
    }

    @Override
    public Map<String, Double> findServicioMasRecaudo(LocalDate fechaInicio, LocalDate fechaFin, Long tecnicoId) {
        String nombreFacturada = "FACTURADA";
        Estado estadoActual = estadoDao.findByNombre(nombreFacturada);
        List<Object[]> resultados = detalleOrdenDao.findServicioMasRecaudo(fechaInicio,fechaFin, tecnicoId,estadoActual);

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
        String nombreFacturada = "FACTURADA";
        Estado estadoActual = estadoDao.findByNombre(nombreFacturada);
        List<Object[]> resultados = detalleOrdenDao.findServicioMasUtilizado(fechaInicio,fechaFin, tecnicoId,estadoActual);

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
    public Integer obtenerCantidadTotalServicios(LocalDate fechaInicio, LocalDate fechaFin, Long tecnicoId) {
        String nombreFacturada = "FACTURADA";
        Estado estadoActual = estadoDao.findByNombre(nombreFacturada);
        Integer cantidadTotalServicios = detalleOrdenDao.obtenerCantidadTotalServicios(fechaInicio, fechaFin, tecnicoId,estadoActual);
        return cantidadTotalServicios;
    }

    @Override
    public Map<String, Double> findMesMasRecaudado(int year) {
        String nombreFacturada = "FACTURADA";
        Estado estadoActual = estadoDao.findByNombre(nombreFacturada);
        List<Object[]> resultados = ordenDao.findMesMasRecaudado(year,estadoActual);
        
        
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
                String nombreFacturada = "FACTURADA";
                Estado estadoActual = estadoDao.findByNombre(nombreFacturada);
                Double montoTotal = detalleOrdenDao.calcularMontoTotalEnPeriodoSinFiltro(fechaInicio, fechaFin,estadoActual);
                List<Object[]> resultados = detalleOrdenDao.obtenerIngresosPorServicioSinFiltro(fechaInicio, fechaFin,estadoActual);
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
        String nombreFacturada = "FACTURADA";
        Estado estadoActual = estadoDao.findByNombre(nombreFacturada);
        Double ingresosTotales = detalleOrdenDao.calcularMontoTotalEnPeriodoSinFiltro(fechaInicio, fechaFin,estadoActual);
        return ingresosTotales;
    }

    @Override
    public Map<String, Double> findServicioMasRecaudoSinFiltro(LocalDate fechaInicio, LocalDate fechaFin) {
        String nombreFacturada = "FACTURADA";
        Estado estadoActual = estadoDao.findByNombre(nombreFacturada);
        List<Object[]> resultados = detalleOrdenDao.findServicioMasRecaudoSinFiltro(fechaInicio,fechaFin,estadoActual);

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
        String nombreFacturada = "FACTURADA";
        Estado estadoActual = estadoDao.findByNombre(nombreFacturada);
        List<Object[]> resultados = detalleOrdenDao.findServicioMasUtilizadoSinFiltro(fechaInicio,fechaFin,estadoActual);

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
    public Integer obtenerCantidadTotalServiciosSinFiltro(LocalDate fechaInicio, LocalDate fechaFin) {
        String nombreFacturada = "FACTURADA";
        Estado estadoActual = estadoDao.findByNombre(nombreFacturada);
        Integer cantidadTotalServicios = detalleOrdenDao.obtenerCantidadTotalServiciosSinFiltro(fechaInicio, fechaFin, estadoActual);
        return cantidadTotalServicios;
    }


    
}