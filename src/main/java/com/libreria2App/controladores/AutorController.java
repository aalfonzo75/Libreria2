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
        
    
    @GetMapping("/crearautor")
    public String guardarAutor(ModelMap model) {
        return "crearautor";  //retorno esa vista
    }
    
    @PostMapping("/crearautor")
    public String guardarAutor(ModelMap model, @RequestParam String nombre) {
        try {
            autorServicio.crearAutor(nombre);
            model.put("exito", "Registro exitoso");
            return "crearautor";
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "crearautor";
        }
    }
    
     @GetMapping("/editarautor/{id}") //PATHVARIABLE
    public String modificarAutor(@PathVariable String id, ModelMap model) {
        model.put("autor", autorServicio.getOne(id));
        return "editarautor";
    }

    @PostMapping("/editarautor/{id}")
    public String modificarAutor(ModelMap model, @PathVariable String id, @RequestParam String nombre) {
        try {
            autorServicio.modificarAutor(id, nombre);
            model.put("exito", "Modificacion exitosa");

            return "listaautor";
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "editarautor";
        }
    }
    
    @GetMapping("/listaautor")
    public String lista(ModelMap model) {
        List<Autor> todos = autorServicio.listarTodos();
        model.addAttribute("autores", todos);
        return "listaautor";  //retorno esa vista
    }
    
    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id) {

        try {
            autorServicio.baja(id);
            return "redirect:/autor/listaautor";
        } catch (Exception e) {
            return "redirect:/";
        }
    }
    
    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id) {

        try {
            autorServicio.alta(id);
            return "redirect:/autor/listaautor";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

}
