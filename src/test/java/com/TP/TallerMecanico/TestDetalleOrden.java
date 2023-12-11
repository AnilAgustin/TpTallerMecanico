package com.TP.TallerMecanico;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.TP.TallerMecanico.entidad.DetalleOrden;

public class TestDetalleOrden {
    
    @Test
	public void testCalcularSubTotal() {
		//Creo objeto detalle orden
		DetalleOrden detalleOrden = new DetalleOrden();

		//Seteo los valores correspondientes
		detalleOrden.setCantidad("2");
		detalleOrden.setPrecioFinalServicio(4000);
		
		//Se invoca el metodo para realizar el calculo
		int subtotalCalculado = detalleOrden.calcularSubtotal();

		//Se comprueba si el resultado obtenido esta bien calculado
		assertEquals(8000, subtotalCalculado);
	}
    
    @Test
    public void cantidadVacio(){
        DetalleOrden detalleOrden = new DetalleOrden();
        detalleOrden.setCantidad("3");

        //Compruebo si la cantidad es vacia
        assertTrue(!detalleOrden.getCantidad().isEmpty());
    }

    @Test
    public void cantidadLimite(){
        DetalleOrden detalleOrden = new DetalleOrden();
        detalleOrden.setCantidad("300");

        //Compruebo si la cantidad es vacia
        assertTrue(Integer.parseInt(detalleOrden.getCantidad()) <= 300);
    }
}
