package com.TP.TallerMecanico.util;
import java.awt.Color;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.TP.TallerMecanico.entidad.DetalleOrden;
import com.TP.TallerMecanico.entidad.Servicio;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

public class FacturaExporterPDF {
    
    Servicio servicio = new Servicio();
    String nombre = servicio.getNombre();

    DetalleOrden detalleOrden = new DetalleOrden();
              
    private List<DetalleOrden> listaDetallesOrdenes;

    public FacturaExporterPDF(List<DetalleOrden> listaDetallesOrdenes){
        super();
        this.listaDetallesOrdenes = listaDetallesOrdenes;
    }

    //Metodo para formatear las cabeceras de la tabla que contenera los datos
    private void escribirCabeceraTabla(PdfPTable tabla){
        PdfPCell celda = new PdfPCell();
        
        celda.setBackgroundColor(Color.white);
        celda.setPadding(5);

        Font fuente = FontFactory.getFont(FontFactory.HELVETICA);
        fuente.setColor(Color.black);

        celda.setPhrase(new Phrase("Servicio",fuente));
        tabla.addCell(celda);
    
        celda.setPhrase(new Phrase("Cantidad",fuente));
        tabla.addCell(celda);
    
        celda.setPhrase(new Phrase("Valor unitario",fuente));
        tabla.addCell(celda);
        
    }

    //Metodo para rellenar la tabla con el nombre del servicio, cantidad y precio
    private void escribirDatosTabla(PdfPTable tabla){
        for (DetalleOrden detalleOrden : listaDetallesOrdenes){
            tabla.addCell(detalleOrden.getServicio().getNombre());
            tabla.addCell(detalleOrden.getCantidad());
            tabla.addCell(String.valueOf(detalleOrden.getPrecioFinalServicio()));
        }
    }
        
    //Variables locales para almacenar el nombre y apellido del cliente
    private String nombreCliente;
    private String apellidoCliente;
    private Double impuestoMarca;

    //Metodo para obtener estos datos
    public void obtenerDatosCliente(String nombreCliente, String apellidoCliente, Double impuestoMarca) {
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.impuestoMarca = impuestoMarca;
    }
     

    //Metodo para la creacion del documento pdf en donde se le agregan los datos obtenidos
    public void exportarPDF(HttpServletResponse response) throws DocumentException, IOException{
        Document documento = new Document(PageSize.A4);
        PdfWriter.getInstance(documento, response.getOutputStream());

        //Se abre el documento para agregarle la informacion
        documento.open();

        //Fuente que se va a utilizar
        Font fuente = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fuente.setColor(Color.black);
        fuente.setSize(16);

        //Encabezado
        Paragraph titulo = new Paragraph("Factura Electronica",fuente);
        titulo.setAlignment(Paragraph.ALIGN_LEFT);
        documento.add(titulo);

        // Agrega un espacio en blanco
        documento.add(new Paragraph("\n"));              
        
        Paragraph cliente = new Paragraph("Cliente: "+ nombreCliente+" "+ apellidoCliente);
        cliente.setAlignment(Paragraph.ALIGN_LEFT);
        documento.add(cliente);

        //Fecha de emision        
        DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());

        Paragraph fechaEmision = new Paragraph("Fecha de Emision: "+ fechaActual);
        fechaEmision.setAlignment(Paragraph.ALIGN_LEFT);
        documento.add(fechaEmision);

        //Tabla de servicios
        PdfPTable tabla = new PdfPTable(3);
        tabla.setWidthPercentage(100);
        tabla.setSpacingBefore(15);
        tabla.setWidths(new float[]{4.5f,2.3f,2.3f});
        tabla.setWidthPercentage(110);
     
        // Agrega un espacio en blanco
        documento.add(new Paragraph("\n")); 
        
        // Agrego los datos de la tabla
        escribirCabeceraTabla(tabla);
        escribirDatosTabla(tabla);
        documento.add(tabla);

        // Agrega un espacio en blanco
        documento.add(new Paragraph("\n"));   

        // Calcular la suma de los precios
        Double sumaPrecios = 0.0;
                for (DetalleOrden detalleOrden : listaDetallesOrdenes) {
            int cantidad = Integer.parseInt(detalleOrden.getCantidad());
            sumaPrecios += (detalleOrden.getPrecioFinalServicio() * cantidad);
        }

        // Agregar el texto con la suma de precios antes del impuesto
        Paragraph sumaPreciosParrafo = new Paragraph("Costo sin impuesto: $" + sumaPrecios);
        sumaPreciosParrafo.setAlignment(Paragraph.ALIGN_RIGHT); // Alinea a la derecha para un aspecto más ordenado
        documento.add(sumaPreciosParrafo);
     
        //Se calcula el monto de Impuesto
        Double impuestoSobrePrecio = sumaPrecios * (impuestoMarca/100);
        Paragraph impuesto = new Paragraph("Impuesto: $"+ impuestoSobrePrecio);
        impuesto. setAlignment(Paragraph.ALIGN_RIGHT);
        documento.add(impuesto);

        //Agrega un espacio en blanco
        documento.add(new Paragraph("\n"));
        
        //Calculo del Monto total + incorporacion en el documento
        Double montoTotal = impuestoSobrePrecio + sumaPrecios;
        Paragraph parrafoMontoTotal = new Paragraph("Total: $"+ montoTotal);
        parrafoMontoTotal.setAlignment(Paragraph.ALIGN_RIGHT);
        documento.add(parrafoMontoTotal);

        //Cierre del documento luego de ser editado
        documento.close();
    }
   
}
