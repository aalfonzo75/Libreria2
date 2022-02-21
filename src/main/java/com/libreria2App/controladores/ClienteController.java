package com.libreria2App.controladores;

import com.libreria2App.entidades.Cliente;
import com.libreria2App.errores.ErrorServicio;
import com.libreria2App.servicios.ClienteServicio;
import com.libreria2App.util.ClientePdfExportar;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    @GetMapping("/lista_cliente")
    public String listaClientes(ModelMap model) {
        List<Cliente> todos = clienteServicio.listaTodosClientes();
        model.addAttribute("clientes", todos);
        model.addAttribute("title", "Lista de Clientes Registrados");
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
    
    @GetMapping("/exportar/pdf")
    public void exportarToPDF(HttpServletResponse response) throws ErrorServicio, IOException {
        response.setContentType("application/pdf");
        //Genero el nombre del archivo PDF con la fecha y hora actual
        DateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaACtual = formatoFecha.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "filename=Listado_Clientes_" + fechaACtual + ".pdf";        
  
        response.setHeader(headerKey, headerValue);
        
         List<Cliente> listaClientes = clienteServicio.listaTodosClientes();        
         
        ClientePdfExportar exportar = new ClientePdfExportar(listaClientes);
        exportar.exportarPdf(response);
         
    }

}
