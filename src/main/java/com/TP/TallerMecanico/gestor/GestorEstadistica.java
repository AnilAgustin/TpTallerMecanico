package com.TP.TallerMecanico.gestor;
import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.servicio.IEstadisticaService;
import com.TP.TallerMecanico.servicio.ITecnicoService;

@Controller
public class GestorEstadistica{
    @Autowired
    private IEstadisticaService estadisticaService;

    @Autowired 
    private ITecnicoService tecnicoService;

    @GetMapping("/estadisticas")
    public String mostrarEstadisticas(@RequestParam(required = false) Integer year, Model model) {

        if (year == null) {
            // Si el año no se proporciona en la solicitud, puedes establecer un valor predeterminado o manejarlo según tus necesidades.
            year = Year.now().getValue(); // Obtener el año actual si no se proporciona
        }
         // Puedes obtener esto según tus necesidades

         Map<String, Map<String, Double>> estadisticas = estadisticaService.obtenerEstadisticasIngresosMensuales(year);

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
        @RequestParam(name = "tecnico", required = false) Long tecnicoId,
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

        if (fechaInicio.compareTo(fechaFin) > 0){
            return "redirect:/estadisticas_por_servicio";
        }


        List<Tecnico> tecnicos = tecnicoService.listarTecnicos();


        if (tecnicoId != null && tecnicoId > 0) {
            // Si se seleccionó un técnico específico, filtrar por ese técnico

            Map<String, Map<String, Double>> estadisticasPorServicio = estadisticaService.obtenerEstadisticasPorServicioEnPeriodo(fechaInicio, fechaFin, tecnicoId);

            estadisticasPorServicio = estadisticaService.obtenerEstadisticasPorServicioEnPeriodo(fechaInicio, fechaFin, tecnicoId);

            Map<String,Double> servicioMasRecaudo = estadisticaService.findServicioMasRecaudo(fechaInicio, fechaFin, tecnicoId);
            Map<String,Double> servicioMasUtilizado = estadisticaService.findServicioMasUtilizado(fechaInicio, fechaFin, tecnicoId);

            model.addAttribute("masRecaudo", servicioMasRecaudo);
            model.addAttribute("masUtilizado", servicioMasUtilizado);

            double sumaTotal = estadisticasPorServicio.values().stream()
            .flatMap(innerMap -> innerMap.values().stream())
            .mapToDouble(Double::doubleValue)
            .sum();

            Integer cantidadTotalServicios = estadisticaService.obtenerCantidadTotalServicios(fechaInicio, fechaFin,tecnicoId);
            model.addAttribute("cantidadTotalServicios",cantidadTotalServicios);

            model.addAttribute("estadisticasPorServicio", estadisticasPorServicio);
            model.addAttribute("sumaTotal", sumaTotal);

        } else {
            // Si no se seleccionó ningún técnico, obtener estadísticas sin filtrar por técnico

            Map<String, Map<String, Double>> estadisticasPorServicio = estadisticaService.obtenerEstadisticasPorServicioEnPeriodoSinFiltro(fechaInicio, fechaFin);

            estadisticasPorServicio = estadisticaService.obtenerEstadisticasPorServicioEnPeriodoSinFiltro(fechaInicio, fechaFin);

            Map<String,Double> servicioMasRecaudo = estadisticaService.findServicioMasRecaudoSinFiltro(fechaInicio, fechaFin);
            Map<String,Double> servicioMasUtilizado = estadisticaService.findServicioMasUtilizadoSinFiltro(fechaInicio, fechaFin);

            model.addAttribute("masRecaudo", servicioMasRecaudo);
            model.addAttribute("masUtilizado", servicioMasUtilizado);
            
            double sumaTotal = estadisticasPorServicio.values().stream()
            .flatMap(innerMap -> innerMap.values().stream())
            .mapToDouble(Double::doubleValue)
            .sum();

            Integer cantidadTotalServicios = estadisticaService.obtenerCantidadTotalServiciosSinFiltro(fechaInicio, fechaFin);
            model.addAttribute("cantidadTotalServicios",cantidadTotalServicios);

            model.addAttribute("estadisticasPorServicio", estadisticasPorServicio);
            model.addAttribute("sumaTotal", sumaTotal);
        }
        

        model.addAttribute("tecnicos", tecnicos);
        model.addAttribute("idTecnico", tecnicoId);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);


        return "estadisticas_por_servicio";
    }


}