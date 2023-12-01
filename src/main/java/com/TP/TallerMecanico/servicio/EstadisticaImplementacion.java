@Service
public class EstadisticasServiceImpl implements EstadisticasService {
    @Autowired
    private IOrdenDao ordenDao;

    @Override
    public Map<String, Double> obtenerEstadisticasIngresosMensuales(int year) {
        // Esquema de consulta JPQL
        String jpql = "SELECT MONTH(o.fechaRegistro) AS mes, SUM(d.subtotal) AS recaudacion_total FROM Orden o JOIN o.detallesOrden d WHERE YEAR(o.fechaRegistro) = :year GROUP BY MONTH(o.fechaRegistro) ORDER BY MONTH(o.fechaRegistro)";

        // Ejecutar la consulta
        List<Object[]> resultados = ordenDao.entityManager().createQuery(jpql)
                .setParameter("year", year)
                .getResultList();

        // Convertir resultados a Map
        Map<String, Double> estadisticas = new HashMap<>();
        for (Object[] resultado : resultados) {
            int mes = (int) resultado[0];
            Double ingresos = (Double) resultado[1];
            estadisticas.put(String.format("%02d", mes), ingresos);
        }

        return estadisticas;
    }
}