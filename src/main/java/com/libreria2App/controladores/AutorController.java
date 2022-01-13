package com.libreria2App.controladores;

import com.libreria2App.entidades.Autor;
import com.libreria2App.errores.ErrorServicio;
import com.libreria2App.servicios.AutorServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Aleidy Alfonzo
 */
@Controller
@RequestMapping("/autor")
public class AutorController {

    @Autowired
    private AutorServicio autorServicio;

    @GetMapping("/crear_autor")
    public String guardarAutor(ModelMap model) {
        return "crear_autor";  //retorno esa vista
    }

    @PostMapping("/crear_autor")
    public String guardarAutor(ModelMap model, @RequestParam String nombre) {
        try {
            autorServicio.crearAutor(nombre);
            model.put("exito", "Registro exitoso");
            return "crear_autor";
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "crear_autor";
        }
    }

    @GetMapping("/editar_autor/{id}") //PATHVARIABLE
    public String modificarAutor(@PathVariable String id, ModelMap model) {
        model.put("autor", autorServicio.getOne(id));
        return "editar_autor";
    }

    @PostMapping("/editar_autor/{id}")
    public String modificarAutor(ModelMap model, @PathVariable String id, @RequestParam String nombre) {
        try {
            autorServicio.modificarAutor(id, nombre);
            model.put("exito", "Modificacion exitosa");
            return lista(model);
//            return "redirect:/autor/lista_autor";
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "editar_autor";
        }
    }

    @GetMapping("/lista_autor")
    public String lista(ModelMap model) {
        List<Autor> todos = autorServicio.listarTodos();
        model.addAttribute("autores", todos);
        return "lista_autor";  //retorno esa vista
    }

    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id) {

        try {
            autorServicio.baja(id);
            return "redirect:/autor/lista_autor";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id) {

        try {
            autorServicio.alta(id);
            return "redirect:/autor/lista_autor";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

}
