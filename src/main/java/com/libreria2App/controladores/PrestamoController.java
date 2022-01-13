package com.libreria2App.controladores;

import com.libreria2App.entidades.Cliente;
import com.libreria2App.entidades.Libro;
import com.libreria2App.entidades.Prestamo;
import com.libreria2App.errores.ErrorServicio;
import com.libreria2App.servicios.ClienteServicio;
import com.libreria2App.servicios.LibroServicio;
import com.libreria2App.servicios.PrestamoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Aleidy Alfonzo
 */
@Controller
@RequestMapping("/prestamo")
public class PrestamoController {

    @Autowired
    private PrestamoServicio prestamoServicio;
    @Autowired
    private ClienteServicio clienteServicio;
    @Autowired
    private LibroServicio libroServicio;

    @GetMapping("/crear_prestamo")
    public String guardarPrestamo(ModelMap model) {
        //Se listan los clientes y los libros para crear el prestamo
        List<Cliente> clientes = clienteServicio.listarActivos();
        List<Libro> libros = libroServicio.listarActivos();
        model.put("lista_cliente", clientes);
        model.put("lista_libro", libros);
        return "crear_prestamo";  //retorno esa vista
    }

    @PostMapping("/crear_prestamo")
    public String guardarPrestamo(ModelMap model, @RequestParam String idLibro, @RequestParam String idCliente) {
        try {
            prestamoServicio.crearPrestamo(idLibro, idCliente);
            model.put("exito", "Prestamo realizado con éxito");
            return "crear_prestamo";
//            return listaPrestamos(model);
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "crear_prestamo";
        }
    }

    @GetMapping("/editar_prestamo/{id}") //PATHVARIABLE
    public String modificarPrestamo(@PathVariable String id, ModelMap model) {
        model.put("prestamo", prestamoServicio.getOne(id));
        return "editar_prestamo";
    }

    @PostMapping("/editar_prestamo/{id}")
    public String modificarPrestamo(ModelMap model, @PathVariable String id, @ModelAttribute Prestamo prestamo) {
        try {
            prestamoServicio.modificarPrestamo(prestamo);
            model.put("exito", "Modificacion exitosa");
            return listaPrestamos(model);
//            return "redirect:/prestamo/lista_prestamo";
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "editar_prestamo";
        }
    }

    @GetMapping("/lista_prestamo")
    public String listaPrestamos(ModelMap model) {
        List<Prestamo> todos = prestamoServicio.listaTodosPrestamos();
        model.addAttribute("prestamos", todos);
        return "lista_prestamo";  //retorno esa vista
    }

    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id) {

        try {
            prestamoServicio.baja(id);
            return "redirect:/prestamo/lista_prestamo";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id) {

        try {
            prestamoServicio.alta(id);
            return "redirect:/prestamo/lista_prestamo";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

}
