package com.libreria2App.servicios;

import com.libreria2App.entidades.Libro;
import com.libreria2App.entidades.Cliente;
import com.libreria2App.entidades.Prestamo;
import com.libreria2App.errores.ErrorServicio;
import com.libreria2App.repositorios.PrestamoRepositorio;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Aleidy Alfonzo
 */
@Service
public class PrestamoServicio {
    
    @Autowired 
    private PrestamoRepositorio prestamoRepositorio;
    
    @Autowired 
    private LibroServicio libroServicio;
    
    @Autowired 
    private ClienteServicio clienteServicio;
    
    @Transactional
    public void crearPrestamo(String idLibro, String idCliente) throws ErrorServicio {        
         //Si ID fuese Integer:
        // Libro libro=libroServicio.buscarPorId(Integer.parseInt(idLibro));
        // Cliente cliente = clienteServicio.buscarPorId(Integer.parseInt(idCliente));

        //Buscamos el libro que se va a prestar al cliente
        Libro libro=libroServicio.buscarPorId(idLibro);
        Cliente cliente = clienteServicio.buscarPorId(idCliente);
        
        Prestamo prestamo = new Prestamo();
        //valido que el libro este disponible para el prestamo
        buscarYvalidarDisponibilidad(libro,cliente,prestamo);
        prestamo.setFechaPrestamo(new Date());
        
        prestamo.setFechaDevolucion(null);
        prestamo.setAlta(true);
        
        prestamoRepositorio.save(prestamo);
    }
    
    

    @Transactional
    public Prestamo modificarPrestamo(Prestamo prestamoEditado) throws ErrorServicio {
        //Si ID fuese Integer:
        //busco el prestamo para modificarlo
    // Prestamo prestamoEnBD = prestamoRepositorio.buscarPorId(Integer.parseInt(prestamoEditado.getId()));

        //busco el prestamo para modificarlo
       Prestamo prestamoEnBD = prestamoRepositorio.buscarPorId(prestamoEditado.getId());

        if (prestamoEnBD != null) {
            //valido todos los datos que no son objetos
            buscarYvalidarDisponibilidad(prestamoEditado.getLibro(),prestamoEditado.getCliente(),prestamoEnBD);
            //Modificamos los valores 
            prestamoEnBD.setFechaPrestamo(new Date());
        prestamoEnBD.setFechaDevolucion(null);
        return prestamoRepositorio.save(prestamoEnBD);
        } else {
            throw new ErrorServicio("No se encontro el Prestamo con el id solicitado");
        }
    }    
   
    @Transactional
    public Prestamo baja(String id) {
        //busco el prestamo 
        Prestamo prestamo = prestamoRepositorio.getOne(id);
        //Traigo el Libro y Cliente de este prestamo
        Libro libro = prestamo.getLibro();
        Cliente cliente = prestamo.getCliente();
        prestamo.setAlta(false);
        prestamo.setFechaDevolucion(new Date());
        
        //Devolvemos el libro a la Biblioteca        
            libro.setePrestados(libro.getePrestados()-1);
            libro.seteRestantes(libro.geteRestantes()+1);
            libroServicio.actualizarBD(libro);
    
        
        //Actualizamos la cantidad de prestamos del cliente y persistimos en la BD
        cliente.setCantidadPrestamos(cliente.getCantidadPrestamos()-1);
        
        clienteServicio.actualizarBD(cliente);
        
        return prestamoRepositorio.save(prestamo);
        //throw new ErrorServicio("No se encontro el libro con el id solicitado");
    }

    @Transactional
    public Prestamo alta(String id) throws ErrorServicio { 
        //busco el prestamo 
        Prestamo prestamo = prestamoRepositorio.getOne(id);
        //Traigo el Libro y Cliente de este prestamo
        Libro libro = prestamo.getLibro();
        Cliente cliente = prestamo.getCliente();
        prestamo.setAlta(true);
        prestamo.setFechaDevolucion(null);
        //Validamos que el libro este disponibles para activar el prestamo
        if (libro.geteRestantes() > 0) {
            libro.setePrestados(libro.getePrestados()+1);
            libro.seteRestantes(libro.geteRestantes()-1);
            prestamo.setLibro(libro);
            libroServicio.actualizarBD(libro);
        } else {
            throw new ErrorServicio("No hay ejemplares disponible de este libro para realizar el prestamo.");
        }
        
        //Actualizamos la cantidad de prestamos del cliente y persistimos en la BD
        cliente.setCantidadPrestamos(cliente.getCantidadPrestamos()+1);
        prestamo.setFechaPrestamo(new Date());
        clienteServicio.actualizarBD(cliente);
        
        return prestamoRepositorio.save(prestamo);
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listaTodosPrestamos() {
        return prestamoRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarActivos() {
        return prestamoRepositorio.buscarActivos();
    }
    
    @Transactional(readOnly = true)
    public Prestamo getOne(String id) {
        return prestamoRepositorio.getOne(id);
    }
    
    @Transactional(readOnly = true)
    public List<Prestamo> listarPrestamosActivos(Cliente cliente) {
        return prestamoRepositorio.buscarActivos();
    }

    @Transactional
    public void buscarYvalidarDisponibilidad(Libro libro, Cliente cliente,Prestamo prestamo) throws ErrorServicio {
       
        //El libro no puede estar repetido ni ser nulo        
        if ( libro == null) {
            throw new ErrorServicio("No se encontro el libro solicitado o el campo está vacio.");
               }

        if (cliente == null) {
            throw new ErrorServicio("No se encontro el cliente solicitadoo el campo está vacio.");
        }  
        
        //Validamos que existan libros disponibles para el prestamo
        if (libro.geteRestantes() > 0) {
            libro.setePrestados(libro.getePrestados()+1);
            libro.seteRestantes(libro.geteRestantes()-1);
            prestamo.setLibro(libro);
            libroServicio.actualizarBD(libro);
        } else {
            throw new ErrorServicio("No hay ejemplares disponible de este libro para realizar el prestamo  .");
        }
        //Actualizamos la cantidad de prestamos del cliente y persistimos en la BD
        cliente.setCantidadPrestamos(cliente.getCantidadPrestamos()+1);
        
        prestamo.setCliente(cliente);
        clienteServicio.actualizarBD(cliente);
    }
    


}
