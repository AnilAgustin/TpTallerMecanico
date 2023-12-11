package com.TP.TallerMecanico;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.TP.TallerMecanico.entidad.Tecnico;
import com.TP.TallerMecanico.interfaz.ITecnicoDao;

@SpringBootTest
public class TestTecnico {

    @Autowired
    private ITecnicoDao tecnicoDao;

    @Test
    public void testGuardarTecnico() {
        // Crear un objeto Tecnico
        String legajo = "13901";
        Tecnico tecnico = new Tecnico();
        tecnico.setLegajo(legajo);
        tecnico.setEstado(true);
        tecnico.setNombre("Agustin");
        tecnico.setApellido("Anil");

        Tecnico tecnicoExistente = tecnicoDao.findByLegajo(legajo);

        if(tecnicoExistente == null){
            // Guardar el objeto en la base de datos
            tecnicoDao.save(tecnico);

            // Recuperar el objeto de la base de datos
            Tecnico tecnicoRecuperado = tecnicoDao.findByLegajo(legajo);
            
            // Verificar que el objeto se haya almacenado correctamente
            assertNotNull(tecnicoRecuperado);
            assertEquals(tecnico.getNombre(), tecnicoRecuperado.getNombre());
        }else{
            System.out.println("El Tecnico ya existe en la base de datos");
        }
    
    }
    
    @Test
    public void testValidarNombreNumeros(){
        // Crear un objeto Tecnico
        Tecnico tecnicoNombreNumero = new Tecnico();
        tecnicoNombreNumero.setNombre("Agustin");
        //Si el nombre no tiene numeros la condicion pasa
        assertTrue(tecnicoNombreNumero.getNombre().matches("[a-zA-Z]+") );
    }

    @Test
    public void testValidarNombreVacio(){
        Tecnico tecnicoNombreVacio = new Tecnico();
        tecnicoNombreVacio.setNombre("Agustin");
        assertTrue(!tecnicoNombreVacio.getNombre().isEmpty());
    }

    @Test
    public void testValidarApellidoNumeros(){
        Tecnico tecnicoApellidoNumero = new Tecnico();
        tecnicoApellidoNumero.setApellido("Anil");
        assertTrue(tecnicoApellidoNumero.getApellido().matches("[a-zA-Z]+") );
    }

    @Test
    public void testValidarApellidoVacio(){
        // Crear un objeto Tecnico
        Tecnico tecnicoApellidoVacio = new Tecnico();
        tecnicoApellidoVacio.setApellido("Anil");
        //Si el nombre no tiene numeros la condicion pasa
        assertTrue(!tecnicoApellidoVacio.getApellido().isEmpty());
    }

    @Test
    public void testLegajoNumero(){
        Tecnico tecnicoLegajoNumero = new Tecnico();
        tecnicoLegajoNumero.setLegajo("13901");
        assertTrue(tecnicoLegajoNumero.getLegajo().matches("\\d+") );
    }

    @Test
    public void testValidarLegajoVacio(){
        Tecnico tecnicoLegajoVacio = new Tecnico();
        tecnicoLegajoVacio.setLegajo("13901");
        assertTrue(!tecnicoLegajoVacio.getLegajo().isEmpty());
    }

}

