package com.TP.TallerMecanico.gestor;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.TP.TallerMecanico.servicio.IEstadisticaService;

@Controller
public class GestorEstadistica{
    @Autowired
    private IEstadisticaService estadisticaService;


    @GetMapping("/estadisticas")
    public String mostrarEstadisticas(Model model) {
        int year = 2023; // Puedes obtener esto según tus necesidades

        Map<String, Double> estadisticas = estadisticaService.obtenerEstadisticasIngresosMensuales(year);
        model.addAttribute("estadisticas", estadisticas);

        return "estadisticas"; // Este es el nombre de tu plantilla Thymeleaf (sin extensión)
    }


}