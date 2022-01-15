package com.libreria2App.util;

import com.libreria2App.entidades.Libro;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

/**
 * @author Aleidy Alfonzo
 */
@Component("lista_libro")
public class ListaLibrosPdf extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {

        List<Libro> listaLibros = (List<Libro>) model.get("libros");
//        float[] anchoColumnas = {8f, 60f, 50f, 50f, 10f, 10f, 10f, 10f};
        float[] anchoColumnas = {0.4f, 1f, 0.5f, 0.5f, 0.2f, 0.2f, 0.2f, 0.4f};

        // color y tipo de fuente
        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, Color.BLACK);
        Font fuenteTituloColumna = FontFactory.getFont(FontFactory.HELVETICA_BOLD,12, Color.BLACK);
        Font fuenteDatos = FontFactory.getFont(FontFactory.HELVETICA, 12, Color.BLACK);

        //Configuro TIPO DE HOJA, rotar y MARGENES del documento
        document.setPageSize(PageSize.A4.rotate());
        document.setMargins(-5, -5, 10, 10); // Margenes: IZQ, DER, SUP, INF
        document.open();

        //TABLA TITULO del pdf
        PdfPTable tablaTitulo = new PdfPTable(1);

        //Creo CELDA titulo y le asigno formato
        PdfPCell titulo = new PdfPCell(new Phrase("LISTADO DE LIBROS", fuenteTitulo));// agrego nombre al titulo y fuenteTitulo
        titulo.setBorder(0); //Sin borde de titulo
        //titulo.setBackgroundColor(new Color(105, 105, 105)); // color de fondo del titulo
        titulo.setHorizontalAlignment(Element.ALIGN_CENTER);
        titulo.setVerticalAlignment(Element.ALIGN_CENTER);
        titulo.setPadding(25);//tamaño-ancho de la celda del titulo

        //Le asigno a la tabla titulo la celda titulo
        tablaTitulo.addCell(titulo);
        tablaTitulo.setSpacingAfter(10);// asigno un espacio entre el titulo y la tabla

        //TABLA PARA MOSTRAR listado LIBROS: Indico la cantidad de columnas a mostrar de mi tabla Objeto
        PdfPTable tablaLibros = new PdfPTable(8);
        tablaLibros.setWidths(anchoColumnas);

        //TABLA para Nombre de Columnas(10)
        PdfPCell tituloColumna = null;
//        PdfPTable tablaNombreColumnas = new PdfPTable(10);
        PdfPTable tablaNombreColumnas = new PdfPTable(8);
        tablaNombreColumnas.setWidths(anchoColumnas);
        tablaNombreColumnas.addCell(formatoColumnas(new PdfPCell(new Phrase("ID", fuenteTituloColumna))));
//        tablaNombreColumnas.addCell(formatoColumnas(new PdfPCell(new Phrase("ISBN", fuenteTituloColumna))));
        tablaNombreColumnas.addCell(formatoColumnas(new PdfPCell(new Phrase("Título", fuenteTituloColumna))));
        tablaNombreColumnas.addCell(formatoColumnas(new PdfPCell(new Phrase("Autor", fuenteTituloColumna))));
        tablaNombreColumnas.addCell(formatoColumnas(new PdfPCell(new Phrase("Editorial", fuenteTituloColumna))));
//        tablaNombreColumnas.addCell(formatoColumnas(new PdfPCell(new Phrase("Año", fuenteTituloColumna))));
        tablaNombreColumnas.addCell(formatoColumnas(new PdfPCell(new Phrase("E", fuenteTituloColumna))));
        tablaNombreColumnas.addCell(formatoColumnas(new PdfPCell(new Phrase("P", fuenteTituloColumna))));
        tablaNombreColumnas.addCell(formatoColumnas(new PdfPCell(new Phrase("R", fuenteTituloColumna))));
        
        tablaNombreColumnas.addCell(formatoColumnas(new PdfPCell(new Phrase("Activos", fuenteTituloColumna))));

        // TABLA LIBROS uso el contador del for para el "Nro."
        for (Libro libro : listaLibros) {
//            int i = 0;
            tablaLibros.addCell(formatoDatosCentrado(new PdfPCell(new Phrase(libro.getId().toString(), fuenteDatos))));
//            tablaLibros.addCell(formatoDatosAlignIzq(new PdfPCell(new Phrase(libro.getIsbn().toString(), fuenteDatos))));
            tablaLibros.addCell(formatoDatosAlignIzq(new PdfPCell(new Phrase(libro.getTitulo(), fuenteDatos))));
            tablaLibros.addCell(formatoDatosAlignIzq(new PdfPCell(new Phrase(libro.getAutor().getNombre(), fuenteDatos))));
            tablaLibros.addCell(formatoDatosAlignIzq(new PdfPCell(new Phrase(libro.getEditorial().getNombre(), fuenteDatos))));
//            tablaLibros.addCell(formatoDatosCentrado(new PdfPCell(new Phrase(libro.getAnio().toString(), fuenteDatos))));
            tablaLibros.addCell(formatoDatosCentrado(new PdfPCell(new Phrase(libro.getEjemplares().toString(), fuenteDatos))));
            tablaLibros.addCell(formatoDatosCentrado(new PdfPCell(new Phrase(libro.getePrestados().toString(), fuenteDatos))));
            tablaLibros.addCell(formatoDatosCentrado(new PdfPCell(new Phrase(libro.geteRestantes().toString(), fuenteDatos))));
            
            tablaLibros.addCell(formatoDatosCentrado(new PdfPCell(new Phrase(libro.getAlta().toString(), fuenteDatos))));

        }

//        listaLibros.forEach(libro -> {
//            tablaLibros.addCell(libro.getIsbn().toString());
//            tablaLibros.addCell(libro.getTitulo());
//            tablaLibros.addCell(libro.getAnio().toString());
//            tablaLibros.addCell(libro.getEjemplares().toString());
//            tablaLibros.addCell(libro.getePrestados().toString());
//            tablaLibros.addCell(libro.geteRestantes().toString());
//            tablaLibros.addCell(libro.getAutor().getNombre());
//            tablaLibros.addCell(libro.getEditorial().getNombre());
//        });
        
        document.add(tablaTitulo);//elemento tabla titulo
        document.add(tablaNombreColumnas);
        document.add(tablaLibros);
        document.close();

        // link de icono pdf: <a href="https://www.flaticon.com/free-icons/pdf" title="pdf icons">Pdf icons created by Freepik - Flaticon</a>
        // link de icono printer: /<a href="https://www.flaticon.com/free-icons/print" title="print icons">Print icons created by Dimitry Miroliubov - Flaticon</a>   
    }

    public PdfPCell formatoColumnas(PdfPCell tituloColumna) {
        tituloColumna.setBackgroundColor(Color.LIGHT_GRAY); // color de fondo del titulo
        tituloColumna.setHorizontalAlignment(Element.ALIGN_CENTER);
        tituloColumna.setVerticalAlignment(Element.ALIGN_CENTER);
        tituloColumna.setPadding(10);//ancho de la fila del titulo
        return tituloColumna;
    }
    
    public PdfPCell formatoDatosCentrado(PdfPCell celdaDatos) {        
        celdaDatos.setHorizontalAlignment(Element.ALIGN_CENTER);
        celdaDatos.setVerticalAlignment(Element.ALIGN_CENTER);
        celdaDatos.setPadding(4);//ancho de cada fila de tabla de Datos
        return celdaDatos;
    }
    public PdfPCell formatoDatosAlignIzq(PdfPCell celdaDatos) {   
        celdaDatos.setHorizontalAlignment(Element.ALIGN_LEFT);
        celdaDatos.setVerticalAlignment(Element.ALIGN_CENTER);
        celdaDatos.setPadding(4);//ancho de cada fila de tabla de Datos
        return celdaDatos;
    }

}
