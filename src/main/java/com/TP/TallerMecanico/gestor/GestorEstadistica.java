package com.TP.TallerMecanico.gestor;
import java.time.LocalDate;
import java.time.Year;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.TP.TallerMecanico.servicio.IEstadisticaService;

@Controller
public class GestorEstadistica{
    @Autowired
    private IEstadisticaService estadisticaService;


    @GetMapping("/estadisticas")
    public String mostrarEstadisticas(@RequestParam(required = false) Integer year, Model model) {

        if (year == null) {
            // Si el año no se proporciona en la solicitud, puedes establecer un valor predeterminado o manejarlo según tus necesidades.
            year = Year.now().getValue(); // Obtener el año actual si no se proporciona
        }
         // Puedes obtener esto según tus necesidades

        Map<String, Double> estadisticas = estadisticaService.obtenerEstadisticasIngresosMensuales(year);

         
        model.addAttribute("estadisticas", estadisticas);
        model.addAttribute("year", year); // Agregar el año al modelo


        Map<String, Double> mesMasRecaudado = estadisticaService.findMesMasRecaudado(year);
        model.addAttribute("mesMasRecaudado", mesMasRecaudado);
        return "estadisticas";
    }


    @GetMapping("/estadisticas_por_servicio")
    public String mostrarEstadisticasPorServicio(
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
        Model model) {

        if (year == null) {
            year = LocalDate.now().getYear();
        }

        if (month == null) {
            month = LocalDate.now().getMonthValue();
        }

        if (fechaInicio == null || fechaFin == null) {
            // Lógica para manejar fechas de inicio y fin predeterminadas
            fechaInicio = LocalDate.of(year, month, 1);
            fechaFin = fechaInicio.withDayOfMonth(fechaInicio.lengthOfMonth());
        }

        Map<String, Map<String, Double>> estadisticasPorServicio = estadisticaService.obtenerEstadisticasPorServicioEnPeriodo(fechaInicio, fechaFin);

        double sumaTotal = estadisticasPorServicio.values().stream()
        .flatMap(innerMap -> innerMap.values().stream())
        .mapToDouble(Double::doubleValue)
        .sum();



        model.addAttribute("estadisticasPorServicio", estadisticasPorServicio);
        model.addAttribute("sumaTotal", sumaTotal);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);

        Map<String,Double> servicioMasRecaudo = estadisticaService.findServicioMasRecaudo(fechaInicio, fechaFin);
        Map<String,Double> servicioMasUtilizado = estadisticaService.findServicioMasUtilizado(fechaInicio, fechaFin);



        model.addAttribute("masRecaudo", servicioMasRecaudo);
        model.addAttribute("masUtilizado", servicioMasUtilizado);

        return "estadisticas_por_servicio";
    }


}