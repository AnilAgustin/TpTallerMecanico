package com.TP.TallerMecanico.gestor;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.TP.TallerMecanico.entidad.DetalleOrden;
import com.TP.TallerMecanico.entidad.Imagen;
import com.TP.TallerMecanico.entidad.Orden;
import com.TP.TallerMecanico.servicio.IOrdenService;
import com.TP.TallerMecanico.servicio.IImagenService;

import jakarta.validation.Valid;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class GestorImagen {

    @Autowired
    private IOrdenService ordenService;

    @Autowired
    private IImagenService imagenService;

    @GetMapping("/ordenes/agregarImagenesOrden/{idOrden}")
    public String agregarImagenOrden(Model model, @PathVariable ("idOrden") Long id){
        var imagen = new Imagen(); //Nueva instancia de imagen
        Orden orden = ordenService.buscarOrden(id);
        model.addAttribute("orden", orden);
        model.addAttribute("imagen", imagen);
        return "agregarImagenesOrden";
    }

    @PostMapping("/ordenes/guardarImagenOrden/{idOrden}")
    public String guardarImagenOrden(@PathVariable ("idOrden") Long id, @Valid Imagen imagenObjeto,
                                     BindingResult error, Model model, @RequestParam("file") MultipartFile imagen){
        try{
            byte [] imagenData = imagen.getBytes();

            imagen.transferTo(new File("C://TPTaller//Recursos//" + imagen.getOriginalFilename()));

            var orden = ordenService.buscarOrden(id);
            imagenObjeto.setOrden(orden);
            imagenObjeto.setNombreArchivo(imagen.getOriginalFilename());
            imagenObjeto.setImagen(imagenData);

            imagenService.guardar(imagenObjeto);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String redirectUrl = "redirect:/ordenes/imagenesOrden/" + id;
        return redirectUrl;
    }

    @GetMapping("/ordenes/eliminarImagenOrden/{idImagen}/{idOrden}")
    public String eliminarDetalleOrden(@PathVariable ("idOrden") Long idOrden, Imagen imagen) {
        imagenService.eliminar(imagen);
        var orden = ordenService.buscarOrden(idOrden);
        String redirectUrl = "redirect:/ordenes/imagenesOrden/" + idOrden;
        // Redirige a la URL construida
        return redirectUrl;
    }


}
