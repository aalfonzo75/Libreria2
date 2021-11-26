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

    @GetMapping("/creareditorial") //localhost:8080/editorial/registro
    public String guardarEditorial(ModelMap model) {
        return "creareditorial";
    }

    @PostMapping("/creareditorial")
    public String guardarEditorial(ModelMap model, @RequestParam String nombre) {
        try {
            editorialServicio.crearEditorial(nombre);
            model.put("exito", "Registro exitoso");
            return "creareditorial";
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "creareditorial";
        }
    }

    @GetMapping("/editareditorial/{id}") //PATHVARIABLE
    public String modificarEditorial(@PathVariable String id, ModelMap model) {
        model.put("editorial", editorialServicio.getOne(id));
        return "editareditorial";
    }

    @PostMapping("/editareditorial/{id}")
    public String modificarEditorial(ModelMap model, @PathVariable String id, @RequestParam String nombre) {
        try {
            editorialServicio.modificarEditorial(id, nombre);
            model.put("exito", "Modificacion exitosa");

            return "listaeditorial";
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "editareditorial";
        }
    }

    @GetMapping("/listaeditorial")
    public String lista(ModelMap modelo) {
        List<Editorial> listaEditorial = editorialServicio.listarTodos();
        modelo.addAttribute("editoriales", listaEditorial);
        return "listaeditorial";
    }
    
    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id) {

        try {
            editorialServicio.baja(id);
            return "redirect:/editorial/listaeditorial";
        } catch (Exception e) {
            return "redirect:/";
        }
    }
    
    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id) {

        try {
            editorialServicio.alta(id);
            return "redirect:/editorial/listaeditorial";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

}
