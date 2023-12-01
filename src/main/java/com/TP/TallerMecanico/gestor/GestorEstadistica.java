@Controller
public class GestorEstadisitca{
    @Autowired
    private IEstadisticasService estadisticaService;


    @GetMapping("/estadisticas")
    public String mostrarEstadisticas(Model model) {
        int year = 2023; // Puedes obtener esto según tus necesidades

        Map<String, Double> estadisticas = estadisticasService.obtenerEstadisticasIngresosMensuales(year);
        model.addAttribute("estadisticas", estadisticas);

        return "estadisticas"; // Este es el nombre de tu plantilla Thymeleaf (sin extensión)
    }


}