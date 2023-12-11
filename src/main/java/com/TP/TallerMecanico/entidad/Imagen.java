package com.TP.TallerMecanico.entidad;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;

@Data
@Entity
@Table(name="imagen")
public class Imagen implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImagen;

    @Column(name = "imagen")
    private byte[] imagen;

    private String nombreArchivo;

    @ManyToOne
    @JoinColumn(name = "orden_id")
    private Orden orden;

}
