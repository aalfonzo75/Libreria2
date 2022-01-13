package com.libreria2App.controladores;

import com.libreria2App.entidades.Cliente;
import com.libreria2App.errores.ErrorServicio;
import com.libreria2App.servicios.ClienteServicio;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author Aleidy Alfonzo
 */
@Controller
@RequestMapping("/cliente")
public class ClienteController {
    
    @Autowired
    private ClienteServicio clienteServicio;
    
    @GetMapping("/crear_cliente")
    public String guardarCliente(ModelMap model) {
        return "crear_cliente";  //retorno esa vista
    }

    @PostMapping("/crear_cliente")
    public String guardarCliente(ModelMap model, @ModelAttribute Cliente cliente) {
        try {
            clienteServicio.crearCliente(cliente);
            model.put("exito", "Registro exitoso");
            return listaClientes(model);
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "crear_cliente";
        }
    }

    @GetMapping("/editar_cliente/{id}") //PATHVARIABLE
    public String modificarCliente(@PathVariable String id, ModelMap model) {
        model.put("cliente", clienteServicio.getOne(id));
        return "editar_cliente";
    }

    @PostMapping("/editar_cliente/{id}")
    public String modificarCliente(ModelMap model, @PathVariable String id, @ModelAttribute Cliente cliente) {
        try {
            clienteServicio.modificarCliente(cliente);
            model.put("exito", "Modificacion exitosa");
            return listaClientes(model);
//            return "redirect:/prestamo/lista_prestamo";
        } catch (ErrorServicio e) {
            model.put("error", "Falto algun dato");
            return "editar_cliente";
        }
    }
    
//    @PostMapping("/editar_cliente/{id}")
//    public String modificarCliente(ModelMap model, @PathVariable String id, @RequestParam Long dni, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String telefono) {
//        try {
//            clienteServicio.modificarCliente(id, dni, nombre, apellido, telefono);
//            model.put("exito", "Modificacion exitosa");
//            return listaClientes(model);
////            return "redirect:/cliente/lista_cliente";
//        } catch (ErrorServicio e) {
//            model.put("error", "Falto algun dato");
//            return "editar_cliente";
//        }
//    }

    @GetMapping("/lista_cliente")
    public String listaClientes(ModelMap model) {
        List<Cliente> todos = clienteServicio.listaTodosClientes();
        model.addAttribute("clientes", todos);
        return "lista_cliente";  //retorno esa vista
    }

    @GetMapping("/baja/{id}")
    public String baja(@PathVariable String id) {

        try {
            clienteServicio.baja(id);
            return "redirect:/cliente/lista_cliente";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/alta/{id}")
    public String alta(@PathVariable String id) {

        try {
            clienteServicio.alta(id);
         return "redirect:/cliente/lista_cliente";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

}
