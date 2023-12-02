package com.TP.TallerMecanico.gestor;
import java.time.Year;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
 
         return "estadisticas";
    }


}