package com.TP.TallerMecanico.servicio;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.TP.TallerMecanico.entidad.Orden;
import com.TP.TallerMecanico.interfaz.IOrdenDao;

@Service
public class OrdenFiltrador {
    @Autowired
    private IOrdenDao ordenDao;

    @Autowired
    private IOrdenService ordenService;

    @Autowired
    public OrdenFiltrador(IOrdenDao ordenDao, IOrdenService ordenService) {
        this.ordenDao = ordenDao;
        this.ordenService = ordenService;
    }

    public List<Orden> filtrarOrdenes(Long marcaId, Long modeloId, Long numero, LocalDate fechaDesdeDocumento, LocalDate fechaHastaDocumento) {
        if (marcaId != -1) {
            // Filtrar por marca
            return filtrarPorMarca(marcaId, modeloId, numero, fechaDesdeDocumento, fechaHastaDocumento);
        } else if (modeloId != -1) {
            // Filtrar por modelo
            return filtrarPorModelo(modeloId, numero, fechaDesdeDocumento, fechaHastaDocumento);
        } else if (numero != null) {
            // Filtrar por numero
            return filtrarPorNumero(numero, fechaDesdeDocumento, fechaHastaDocumento);
        } else if (fechaDesdeDocumento != null || fechaHastaDocumento != null) {
            // Filtrar por fecha
            return filtrarPorRangoDeFechas(fechaDesdeDocumento, fechaHastaDocumento);
        } else {
            // Listar todas las órdenes
            return listarOrdenes();
        }
    }
    
    private List<Orden> filtrarPorMarca(Long marcaId, Long modeloId, Long numero, LocalDate fechaDesdeDocumento, LocalDate fechaHastaDocumento) {
        if (modeloId != -1) {
            if (numero != null) {
                if (fechaDesdeDocumento != null && fechaHastaDocumento != null) {
                    // Búsqueda para marca, modelo, número, fechaDesde y fechaHasta
                    return ordenDao.filtrarOrdenPorMarcaYModeloYNumeroYFechaDesdeYFechaHasta(marcaId, modeloId, numero, fechaDesdeDocumento, fechaHastaDocumento);                    
                }else if (fechaDesdeDocumento != null) {
                    return ordenDao.filtrarOrdenPorMarcaYModeloYNumeroYFechaDesde(marcaId, modeloId, numero, fechaDesdeDocumento);
                }else if (fechaHastaDocumento != null) {
                    return ordenDao.filtrarOrdenPorMarcaYModeloYNumeroYFechaHasta(marcaId, modeloId, numero, fechaHastaDocumento);
                }else{
                    // Búsqueda para marca, modelo y número
                    return ordenDao.filtrarOrdenPorMarcaYModeloYNumero(marcaId, modeloId, numero);
                }
            }else if (fechaDesdeDocumento != null && fechaHastaDocumento != null) {
                // Búsqueda para marca, modelo, fechaDesde y fechaHasta
                return ordenDao.filtrarOrdenPorMarcaYModeloYFechaDesdeYFechaHasta(marcaId, modeloId, fechaDesdeDocumento, fechaHastaDocumento);
            }else if (fechaDesdeDocumento != null) {
                return ordenDao.filtrarOrdenPorMarcaYModeloYFechaDesde(marcaId, modeloId, fechaDesdeDocumento);
            }else if (fechaHastaDocumento != null) {
                return ordenDao.filtrarOrdenPorMarcaYModeloYFechaHasta(marcaId, modeloId, fechaHastaDocumento);
            }else{
                //Busqueda pra marca y modelo
                return ordenDao.filtrarOrdenPorMarcaYModelo(marcaId, modeloId);
            }
        } else if (numero != null) {
            if (fechaDesdeDocumento != null && fechaHastaDocumento != null) {
                // Búsqueda para marca, número y fecha
                return ordenDao.filtrarOrdenPorMarcaYNumeroYFechaDesdeYFechaHasta(marcaId, numero, fechaDesdeDocumento,fechaHastaDocumento);
            } else if (fechaDesdeDocumento != null) {
                return ordenDao.filtrarOrdenPorMarcaYNumeroYFechaDesde(marcaId, numero, fechaDesdeDocumento);
            }else if (fechaHastaDocumento != null) {
                return ordenDao.filtrarOrdenPorMarcaYNumeroYFechaHasta(marcaId, numero, fechaHastaDocumento);
            }else{
                //Busqueda por marca y numero
                return ordenDao.filtrarOrdenPorMarcaYNumero(marcaId, numero);
            }

        } else if (fechaDesdeDocumento != null && fechaHastaDocumento != null) {
            // Búsqueda para marca y fecha
            return ordenDao.filtrarOrdenPorMarcaYFechaDesdeYFechaHasta(marcaId, fechaDesdeDocumento, fechaHastaDocumento);
        } else if (fechaDesdeDocumento != null) {
            return ordenDao.filtrarOrdenPorMarcaYFechaDesde(marcaId, fechaDesdeDocumento);
        } else if (fechaHastaDocumento != null) {
            return ordenDao.filtrarOrdenPorMarcaYFechaHasta(marcaId, fechaHastaDocumento);
        }else{
            // Filtrar solo por marca
            return ordenDao.filtrarOrdenPorMarca(marcaId);
        }
    }

    private List<Orden> filtrarPorModelo(Long modeloId, Long numero, LocalDate fechaDesdeDocumento, LocalDate fechaHastaDocumento) {
        if (numero != null){
            if (fechaDesdeDocumento != null && fechaHastaDocumento != null) {
                // Búsqueda para modelo, número y fecha
                return ordenDao.filtrarOrdenPorModeloYNumeroYFechaDesdeYFechaHasta(modeloId, numero, fechaDesdeDocumento, fechaHastaDocumento);
            } else if (fechaDesdeDocumento != null) {
                return ordenDao.filtrarOrdenPorModeloYNumeroYFechaDesde(modeloId, numero, fechaDesdeDocumento);
            }else if (fechaHastaDocumento != null) {
                return ordenDao.filtrarOrdenPorModeloYNumeroYFechaHasta(modeloId, numero, fechaHastaDocumento);
            }else{
                // Búsqueda para modelo y número
                return ordenDao.filtrarOrdenPorModeloYNumero(modeloId, numero);
            }
        }else if (fechaDesdeDocumento != null && fechaHastaDocumento != null) {
            // Búsqueda para modelo, fechaDesde y fechaHasta
            return ordenDao.filtrarOrdenPorModeloYFechaDesdeYFechaHasta(modeloId, fechaDesdeDocumento, fechaHastaDocumento);
        }else if (fechaDesdeDocumento != null) {
            return ordenDao.filtrarOrdenPorModeloYFechaDesde(modeloId, fechaDesdeDocumento);
        }else if (fechaHastaDocumento != null) {
            return ordenDao.filtrarOrdenPorModeloYFechaHasta(modeloId, fechaHastaDocumento);
        }{
            // Filtrar solo por modelo
            return ordenDao.filtrarOrdenPorModelo(modeloId);     
        }
    }

    private List<Orden> filtrarPorNumero(Long numero, LocalDate fechaDesdeDocumento, LocalDate fechaHastaDocumento) {
        if (fechaDesdeDocumento != null && fechaHastaDocumento != null) {
            // Búsqueda por número y fecha
            return ordenDao.filtrarOrdenPorNumeroYFechaDesdeYFechaHasta(numero, fechaDesdeDocumento, fechaHastaDocumento);
        }else if (fechaDesdeDocumento != null ) {
            return ordenDao.filtrarOrdenPorNumeroYFechaDesde(numero, fechaDesdeDocumento);
        }else if (fechaHastaDocumento != null) {
            return ordenDao.filtrarOrdenPorNumeroYFechaHasta(numero, fechaHastaDocumento);
        }else{
            // Búsqueda por número
            return ordenDao.filtrarOrdenPorNumero(numero);     
        }
    }

    private List<Orden> filtrarPorRangoDeFechas(LocalDate fechaDesdeDocumento, LocalDate fechaHastaDocumento) {
        // Filtrar por fecha
        if (fechaDesdeDocumento != null && fechaHastaDocumento != null) {
            return ordenDao.filtrarOrdenPorFechaDesdeYFechaHasta(fechaDesdeDocumento, fechaHastaDocumento);
        }else if (fechaDesdeDocumento != null) {
            return ordenDao.filtrarOrdenPorFechaDesde(fechaDesdeDocumento);
        }else{
            return ordenDao.filtrarOrdenPorFechaHasta(fechaHastaDocumento);
        }
    }

    private List<Orden> listarOrdenes() {
        return ordenService.listarOrdenes();
    }
}
