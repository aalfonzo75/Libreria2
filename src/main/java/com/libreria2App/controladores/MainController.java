package com.libreria2App.controladores;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Aleidy Alfonzo
 */
@Controller
@RequestMapping("/") //localhost:8080/
public class MainController {
    
    @GetMapping("/") //Con el GET consulto informacion al servidor somo el SELECT
    public String inicio(ModelMap modelo){
        return "index";  //retorno esa vista
    }
}
