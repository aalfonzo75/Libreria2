package com.libreria2App.util;

import com.libreria2App.entidades.Libro;
import com.libreria2App.errores.ErrorServicio;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
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
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

/**
 * @author Aleidy Alfonzo
 */
public class LibroPdfExportar {

    private List<Libro> listaLibros; 

    //CONSTRUCTOR
    public LibroPdfExportar(List<Libro> listaLibros) {
        this.listaLibros = listaLibros;
    }

    private void tablaEncabezado(PdfPTable tabla) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.LIGHT_GRAY); // color de fondo
        cell.setPadding(5); //ancho de la fila

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.WHITE); //color letra
        font.setSize(12); //tamaño letra

        cell.setPhrase(new Phrase("ID", font));
        tabla.addCell(cell);
        cell.setPhrase(new Phrase("Título", font));
        tabla.addCell(cell);
        cell.setPhrase(new Phrase("Autor", font));
        tabla.addCell(cell);
        cell.setPhrase(new Phrase("Editorial", font));
        tabla.addCell(cell);
        cell.setPhrase(new Phrase("E", font));
        tabla.addCell(cell);
        cell.setPhrase(new Phrase("P", font));
        tabla.addCell(cell);
        cell.setPhrase(new Phrase("R", font));
        tabla.addCell(cell);
        cell.setPhrase(new Phrase("Activos", font));
        tabla.addCell(cell);
    }
    
    private void TabladeDatos(PdfPTable tabla) {        
        for (int i = 0; i < listaLibros.size(); i++) {
            tabla.addCell(String.valueOf(i + 1));//Contador de libros
            tabla.addCell(listaLibros.get(i).getTitulo());
            tabla.addCell(listaLibros.get(i).getAutor().getNombre());
            tabla.addCell(listaLibros.get(i).getEditorial().getNombre());
            tabla.addCell(listaLibros.get(i).getEjemplares().toString());
            tabla.addCell(listaLibros.get(i).getePrestados().toString());
            tabla.addCell(listaLibros.get(i).geteRestantes().toString());
            tabla.addCell(listaLibros.get(i).getAlta().toString());
        } 
}
    
    public void exportarPdf(HttpServletResponse response) throws ErrorServicio, IOException {
        //Configuro TIPO DE HOJA, rotar y MARGENES del documento
        
        Document document = new Document(PageSize.A4.rotate());
        //document.setMargins(-5, -5, 10, 10); // Margenes: IZQ, DER, SUP, INF
        PdfWriter.getInstance(document, response.getOutputStream());
         
        document.open();
        
        document.add(titulodeTabla()); 
        
         //Creo un objeto de PdfPTable de 8 columnas
        PdfPTable tabla = new PdfPTable(8); //Asigno el numero de columnas
        tabla.setWidthPercentage(100f); //Ancho
        tabla.setWidths(new float[] {0.6f, 4.5f, 3.0f, 3.0f, 0.6f, 0.6f, 0.6f, 1.3f}); //Ancho de columnas
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
         
        Paragraph p = new Paragraph("LISTADO DE LIBROS", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
        return p;
    }
}




//OTRA FORMA DE COLOCAR TITULO CON FONDO DE COLOR
//    private void tablaTitulo(PdfPTable tabla) {
//        //color y tipo de fuente del Titulo
//        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
//        font.setSize(16);
//        font.setColor(Color.BLACK);
//         
//        //TABLA TITULO del pdf
//        PdfPTable tablaTitulo = new PdfPTable(1);
//
//        //Creo CELDA titulo y le asigno formato
//        PdfPCell titulo = new PdfPCell(new Phrase("LISTADO DE LIBROS", fuenteTitulo));// agrego nombre al titulo y fuenteTitulo
//        titulo.setBorder(0); //Sin borde de titulo
//        //titulo.setBackgroundColor(new Color(105, 105, 105)); // color de fondo del titulo
//        titulo.setHorizontalAlignment(Element.ALIGN_CENTER);
//        titulo.setVerticalAlignment(Element.ALIGN_CENTER);
//        titulo.setPadding(25);//tamaño-ancho de la celda del titulo
//
//        //Le asigno a la tabla titulo la celda titulo
//        tablaTitulo.addCell(titulo);
//        tablaTitulo.setSpacingAfter(10);// asigno un espacio entre el titulo y la tabla
//       
//    }