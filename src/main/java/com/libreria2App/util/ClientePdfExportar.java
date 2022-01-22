package com.libreria2App.util;

import com.libreria2App.entidades.Cliente;
import com.libreria2App.errores.ErrorServicio;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Aleidy Alfonzo
 */
public class ClientePdfExportar {
 private List<Cliente> listaClientes; 

    //CONSTRUCTOR
    public ClientePdfExportar(List<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }    

    private void tablaEncabezado(PdfPTable tabla) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.LIGHT_GRAY); // color de fondo
        cell.setPadding(10); //ancho de la fila

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.WHITE); //color letra
        font.setSize(12); //tamaño letra

        cell.setPhrase(new Phrase("N°", font));
         tabla.addCell(cell);
        cell.setPhrase(new Phrase("DNI", font));
         tabla.addCell(cell);
        cell.setPhrase(new Phrase("Nombre", font));
         tabla.addCell(cell);
        cell.setPhrase(new Phrase("Apellido", font));
         tabla.addCell(cell);
        cell.setPhrase(new Phrase("Teléfono", font));
         tabla.addCell(cell);
        cell.setPhrase(new Phrase("Préstamos", font)); 
         tabla.addCell(cell);
        cell.setPhrase(new Phrase("Activos", font));
        tabla.addCell(cell);
    }
    
    private void TabladeDatos(PdfPTable tabla) {        
        for (int i = 0; i < listaClientes.size(); i++) {
            tabla.addCell(String.valueOf(i + 1));//Contador de Clientes
            tabla.addCell(listaClientes.get(i).getDni().toString());
            tabla.addCell(listaClientes.get(i).getNombre());
            tabla.addCell(listaClientes.get(i).getApellido());
            tabla.addCell(listaClientes.get(i).getTelefono().toString());
            tabla.addCell(listaClientes.get(i).getCantidadPrestamos().toString());           
            tabla.addCell(listaClientes.get(i).getAlta().toString());
        } 
}
    
    public void exportarPdf(HttpServletResponse response) throws ErrorServicio, IOException {
        //Configuro TIPO DE HOJA, rotar y MARGENES del documento
        
        Document document = new Document(PageSize.A4.rotate());
        //document.setMargins(-5, -5, 10, 10); // Margenes: IZQ, DER, SUP, INF
        PdfWriter.getInstance(document, response.getOutputStream());
         
        document.open();
        
        document.add(titulodeTabla()); 
        
         //Creo un objeto de PdfPTable de 7 columnas
        PdfPTable tabla = new PdfPTable(7); //Asigno el numero de columnas
        tabla.setWidthPercentage(100f); //Ancho
        tabla.setWidths(new float[] {0.6f, 2.0f, 3.0f, 3.0f, 3.0f, 1.5f, 1.3f}); //Ancho de columnas
        tabla.setSpacingBefore(10); // asigno un espacio entre el titulo y la tabla
         
        tablaEncabezado(tabla);
        TabladeDatos(tabla);       
          
        document.add(tabla);
         
        document.close();         
    }
    
    private Paragraph titulodeTabla() {
        //color y tipo de fuente del Titulo
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(16);
        font.setColor(Color.BLACK);
         
        Paragraph p = new Paragraph("LISTA DE CLIENTES", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        return p;
    }
}
