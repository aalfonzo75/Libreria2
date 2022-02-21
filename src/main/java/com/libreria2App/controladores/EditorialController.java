package com.libreria2App.controladores;

import com.libreria2App.entidades.Editorial;
import com.libreria2App.errores.ErrorServicio;
import com.libreria2App.servicios.EditorialServicio;
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
@RequestMapping("/editorial")
public class EditorialController {

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/crear_editorial") //localhost:8080/editorial/registro
    public String guardarEditorial(ModelMap model) {
        return "crear_editorial";
    }

    @PostMapping("/crear_editorial")
    public String guardarEditorial(ModelMap model, @RequestParam String nombre) {
        try {
            editorialServicio.crearEditorial(nombre);
            model.put("exito", "Registro exitoso");
            return "crear_editorial";
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "crear_editorial";
        }
    }

    @GetMapping("/editar_editorial/{id}") //PATHVARIABLE
    public String modificarEditorial(@PathVariable String id, ModelMap model) {
        model.put("editorial", editorialServicio.getOne(id));
        return "editar_editorial";
    }

    @PostMapping("/editar_editorial/{id}")
    public String modificarEditorial(ModelMap model, @PathVariable String id, @RequestParam String nombre) {
        try {
            editorialServicio.modificarEditorial(id, nombre);
            model.put("exito", "Modificacion exitosa");
            return lista(model);
//            return "redirect:/editorial/listaeditorial";
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "editar_editorial";
        }
    }

    @GetMapping("/lista_editorial")
    public String lista(ModelMap modelo) {
        List<Editorial> listaEditorial = editorialServicio.listarTodos();
        modelo.addAttribute("editoriales", listaEditorial);
        modelo.addAttribute("title", "Lista de Editoriales Registradas");
        return "lista_editorial";
    }

    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id) {

        try {
            editorialServicio.baja(id);
            return "redirect:/editorial/lista_editorial";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id) {

        try {
            editorialServicio.alta(id);
            return "redirect:/editorial/lista_editorial";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

}
