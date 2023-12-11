package com.TP;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.TP.TallerMecanico.entidad.DetalleOrden;
import com.TP.TallerMecanico.entidad.Orden;

public class TestOrden {
    @Test
	public void testCalcularTotalSinImpuesto() {
        //Creo array
        List<DetalleOrden> listaDetalles = new ArrayList<>();
		//Creo objetos y asigno valores
		Orden orden = new Orden();
        DetalleOrden detalle1 = new DetalleOrden();
        detalle1.setEstado(true);
        detalle1.setSubtotal(4000);
        DetalleOrden detalle2 = new DetalleOrden();
        detalle2.setEstado(true);
        detalle2.setSubtotal(12000);

        listaDetalles.add(detalle1);
        listaDetalles.add(detalle2);
		
        orden.setDetallesOrden(listaDetalles);
		//Se invoca el metodo para realizar el calculo
		int totalCalculado = orden.calcularTotal();

		//Se comprueba si el resultado obtenido esta bien calculado
		assertEquals(16000, totalCalculado);
	}
}
