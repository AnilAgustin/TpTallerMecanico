package com.TP.TallerMecanico.servicio;

import com.TP.TallerMecanico.entidad.Marca;
import java.util.List;

public interface MarcaService {
    
    public List<Marca> listarMarcas();
    public void guardar(Marca marca);
    public void eliminar(Marca marca);
    public Marca buscarMarca(Marca marca);
}
