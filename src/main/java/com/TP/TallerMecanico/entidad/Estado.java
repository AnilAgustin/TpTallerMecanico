package com.TP.TallerMecanico.entidad;
import jakarta.persistence.*;
import java.io.Serializable;
import lombok.Data;
@Data
@Entity
@Table(name="estado")
public class Estado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEstado;

    private String nombre;

    public Estado(String nombre) {
        this.nombre = nombre;
    }

    public Estado(){}


}
