package com.libreria2App.controladores;

import com.libreria2App.entidades.Autor;
import com.libreria2App.entidades.Editorial;
import com.libreria2App.entidades.Libro;
import com.libreria2App.errores.ErrorServicio;
import com.libreria2App.servicios.AutorServicio;
import com.libreria2App.servicios.EditorialServicio;
import com.libreria2App.servicios.LibroServicio;
import com.libreria2App.util.LibroPdfExportar;
import com.libreria2App.util.LibroPdfExportar;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Aleidy Alfonzo
 */
@Controller
@RequestMapping("/libro")
public class LibroController {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/crear_libro")
    public String guardarLibro(ModelMap model, @RequestParam(required = false) String id) {

        if (id != null) {
            Libro libro = libroServicio.buscarPorId(id);

            if (libro != null) {
                model.addAttribute("libro", libro);
            } else {
                return "redirect:/libro/lista_libro";
            }

        } else {
            Libro libro = new Libro();
            model.addAttribute("libro", libro);           
        }
        List<Autor> autores = autorServicio.listarTodos(); //Se listan los autores
        List<Editorial> editoriales = editorialServicio.listarTodos();  //Se listan las editoriales

        model.addAttribute("lista_autor", autores); //Se guardan los autores en el modelo para que mi pag web llene ese combo con las guardadas en BD
        model.addAttribute("lista_editorial", editoriales);
        return "crear_libro";  //retorno esa vista
    }

    @PostMapping("/crear_libro")
    public String guardarLibro(ModelMap model, RedirectAttributes redirectAtr, @ModelAttribute Libro libro) {
        try {
            libroServicio.crearLibro(libro);
            model.put("exito", "Registro exitoso");
            return "redirect:/libro/lista_libro";  //retorno esa vista
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "crear_libro";  //retorno esa vista
        }
    }
//    @GetMapping("/editarlibro")

    @GetMapping("/editar_libro/{id}") // PATHVARIABLE: anotacion para configurar variables dentro de los propios segmentos de la URL para enviarlos
    public String modificar(@PathVariable String id, ModelMap model) {

        List<Autor> autores = autorServicio.listarActivos(); //Se listan los autores activos
        List<Editorial> editoriales = editorialServicio.listarActivos();
        model.put("lista_autor", autores);
        model.put("lista_editorial", editoriales);
        model.put("libro", libroServicio.getOne(id));

        return "editar_libro";  //retorno esa vista
    }

//     @PostMapping("/editar_libro")
    @PostMapping("/editar_libro/{id}")
    public String modificar(ModelMap model, @PathVariable String id, @ModelAttribute Libro libro) {
        try {
            libroServicio.modificarLibro(libro);
            model.put("exito", "Modificacion exitosa");
//            return "redirect:/libro/lista_libro";
            return listaLibros(model);
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "editar_libro";
        }
    }

    @GetMapping("/lista_libro")
    public String listaLibros(ModelMap model) {

        List<Libro> todos = libroServicio.listaTodosLibros();
        model.addAttribute("libros", todos);
        model.addAttribute("title", "Listado de Libros");
        return "lista_libro";  //retorno esa vista
    }

    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id) {

        try {
            libroServicio.baja(id);
            return "redirect:/libro/lista_libro";
        } catch (Exception e) {
            return "redirect:/";
        }

    }

    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id) {

        try {
            libroServicio.alta(id);
            return "redirect:/libro/lista_libro";
        } catch (Exception e) {
            return "redirect:/";
        }
    }
    
    @GetMapping("/exportar/pdf")
    public void exportarToPDF(HttpServletResponse response) throws ErrorServicio, IOException {
        response.setContentType("application/pdf");
        //Genero el nombre del archivo PDF con la fecha y hora actual
        DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaACtual = formatoFecha.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "filename=Listado_Libros_" + fechaACtual + ".pdf";
               
        //String headerValue = "attachment; filename=Listado_Libros_" + fechaACtual + ".pdf"; descarga el archivo
        
        response.setHeader(headerKey, headerValue);
        
         List<Libro> listaLibros = libroServicio.listaTodosLibros();        
         
        LibroPdfExportar exportar = new LibroPdfExportar(listaLibros);
        exportar.exportarPdf(response);
         
    }

}
