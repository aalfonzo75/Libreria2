package com.libreria2App.controladores;
import com.libreria2App.entidades.Autor;
import com.libreria2App.entidades.Editorial;
import com.libreria2App.entidades.Libro;
import com.libreria2App.errores.ErrorServicio;
import com.libreria2App.servicios.AutorServicio;
import com.libreria2App.servicios.EditorialServicio;
import com.libreria2App.servicios.LibroServicio;
import java.util.List;
import java.util.Optional;
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

    @GetMapping("/crearlibro")
    public String guardarLibro(ModelMap model, @RequestParam(required = false) String id) {

        if (id != null) {
            Libro libro = libroServicio.buscarPorId(id);

            if (libro != null) {
                model.addAttribute("libro", libro);
            } else {
                return "redirect:/libro/listalibro";
            }

        } else {
            Libro libro = new Libro();
            model.addAttribute("libro", libro);
            System.out.println(libro.toString());
        }
        List<Autor> autores = autorServicio.listarTodos(); //Se listan los autores
        List<Editorial> editoriales = editorialServicio.listarTodos();  //Se listan las editoriales

        model.addAttribute("listaautor", autores); //Se guardan los autores en el modelo para que mi pag web llene ese combo con las guardadas en BD
        model.addAttribute("listaeditorial", editoriales);
        return "crearlibro";  //retorno esa vista
    }

//    @GetMapping("/crearlibro")
//    public String guardarLibro(ModelMap model, @RequestParam(required = false) String id) {
//                System.out.println("hola");
//
//        List<Autor> autores = autorServicio.listarTodos(); //Se listan los autores
//        List<Editorial> editoriales = editorialServicio.listarTodos();  //Se listan las editoriales
//
//        model.addAttribute("listaautor", autores); //Se guardan los autores en el modelo para que mi pag web llene ese combo con las guardadas en BD
//        model.addAttribute("listaeditorial", editoriales);
//
//        if (id != null) {
//            Libro libro = libroServicio.buscarPorId(id);
//            model.addAttribute("libro", libro);
//            return "crearlibro";
//
//        } else {
//            Libro libro = new Libro();
//            libro.setAlta(true);
//            model.addAttribute("libro", libro);
//                    System.out.println(libro.toString());
//
//        }
//
//        return "crearlibro";  //retorno esa vista
//    }
    @PostMapping("/crearlibro")
    public String guardarLibro(ModelMap model, RedirectAttributes redirectAtr, @ModelAttribute Libro libro) {
        System.out.println("isbn" + libro.getIsbn());
        System.out.println("titulo" + libro.getTitulo());
        System.out.println("anio" + libro.getAnio());
        System.out.println("ejemplares" + libro.getEjemplares());
        System.out.println("autor" + libro.getAutor());
        System.out.println("editorial" + libro.getEditorial());

        try {

            libroServicio.crearLibro(libro);
            model.put("exito", "Registro exitoso");
            return "redirect:/libro/listalibro";  //retorno esa vista
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "crearlibro";  //retorno esa vista
        }
    }
//    @GetMapping("/editarlibro")
    @GetMapping("/editarlibro/{id}") // PATHVARIABLE: anotacion para configurar variables dentro de los propios segmentos de la URL para enviarlos
    public String modificar(@PathVariable String id, ModelMap model) {

        List<Autor> autores = autorServicio.listarActivos(); //Se listan los autores activos
        List<Editorial> editoriales = editorialServicio.listarActivos();
        model.put("listaautor", autores);
        model.put("listaeditorial", editoriales);
        model.put("libro", libroServicio.getOne(id));

        return "editarlibro";  //retorno esa vista
    }

    
//     @PostMapping("/editarlibro")
    @PostMapping("/editarlibro/{id}")
    public String modificar(ModelMap model, @PathVariable String id, @ModelAttribute Libro libro) {
        try {
            libroServicio.modificarLibro(libro);
            model.put("exito", "Modificacion exitosa");
            System.out.println("aqui modifico mi libro en el controlador");
            System.out.println("titulo" + libro.getTitulo());
        System.out.println("libro" + libro.toString());
            return "redirect:/libro/listalibro";
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "editarlibro";
        }
    }

    @GetMapping("/listalibro")
    public String listaLibros(ModelMap model) {

        List<Libro> todos = libroServicio.listaTodosLibros();
        model.addAttribute("libros", todos);

        return "listalibro";  //retorno esa vista
    }

    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id) {

        try {
            libroServicio.baja(id);
            return "redirect:/libro/listalibro";
        } catch (Exception e) {
            return "redirect:/";
        }

    }

    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id) {

        try {
            libroServicio.alta(id);
            return "redirect:/libro/listalibro";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

//     @GetMapping("/listalibros")
//    public String listaLibros(ModelMap model, @RequestParam(required = false) String buscar) {
//        //si el parametro "buscar" NO es nulo, agrega al modelo una lista de libros buscados
//        if (buscar !=null || buscar.length()<1) {
//            List<Libro> buscarLibro = libroServicio.listaBuscarLibro(buscar);
//           model.addAttribute("libros", buscarLibro);
//        } else {//si no, agrega al modelo una lista con todos los libros
//            List<Libro> todos = libroServicio.listaTodosLibros();
//        model.addAttribute("libros", todos);
//        }        
//        return "listalibros";  //retorno esa vista
//    }
}

// @PostMapping("/registro")
//    public String guardarLibro(ModelMap model, @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam Integer ejemplaresRestantes, @RequestParam String autor, @RequestParam String editorial) {
//
//        try {
//
//            libroServicio.guardar(isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, autor, editorial);
//            model.put("exito", "Registro exitoso");
//            return "form-libro.html";  //retorno esa vista
//        } catch (ErrorServicio e) {
//            model.put("error", "Falto algun dato");
//            return "form-libro.html";  //retorno esa vista
//        }
//    }
//
//    @GetMapping("/modificar/{id}") // PATHVARIABLE: anotacion para configurar variables dentro de los propios segmentos de la URL para enviarlos
//    public String modificar(@PathVariable String id, ModelMap model) {
//
//        model.put("libro", libroServicio.getOne(id));
//
//        return "form-libro-modif";  //retorno esa vista
//    }
//
//    @PostMapping("/modificar/{id}")
//    public String modificar(ModelMap model, @PathVariable String id, @RequestParam Long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam Integer ejemplaresPrestados, @RequestParam Integer ejemplaresRestantes, @RequestParam String autor, @RequestParam String editorial) {
//        try {
//            libroServicio.modificar(id, isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, autor, editorial);
//            model.put("exito", "Modificacion exitosa");
//
//            return "list-libro";
//        } catch (Exception e) {
//            model.put("error", "Falto algun dato");
//            return "form-libro-modif";
//        }

//    }