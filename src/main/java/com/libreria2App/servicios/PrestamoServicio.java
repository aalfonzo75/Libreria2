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
        
        //Buscamos el libro que se va a prestar al cliente
        Libro libro = libroServicio.buscarPorId(idLibro);
        Cliente cliente = clienteServicio.buscarPorId(idCliente);

        Prestamo prestamo = new Prestamo();
        //valido que el libro este disponible para el prestamo
        validarDatos(libro, cliente, prestamo);
        prestamo.setFechaPrestamo(new Date());

        prestamo.setFechaDevolucion(null);
        prestamo.setAlta(true);

        prestamoRepositorio.save(prestamo);
    }

    @Transactional
    public void modificarPrestamo(String idprestamo, String idLibro, String idCliente) throws ErrorServicio {
       
        //Buscamos el libro que se va a prestar al cliente
        Libro libroNew = libroServicio.buscarPorId(idLibro);
        Cliente clienteNew = clienteServicio.buscarPorId(idCliente);

        //busco el prestamo para modificarlo
        Prestamo prestamoEnBD = prestamoRepositorio.buscarPorId(idprestamo);
        //Buscamos el libro y cliente en base de datos
        Libro libroBD = prestamoEnBD.getLibro();
        Cliente clienteBD = prestamoEnBD.getCliente();

        if (prestamoEnBD != null) {
            //valido todos los datos que no son objetos
            prestamoEnBD = validarCambios(libroNew, clienteNew, prestamoEnBD, libroBD, clienteBD);
            //Modificamos los valores 
            prestamoEnBD.setLibro(libroNew);
            prestamoEnBD.setCliente(clienteNew);
            prestamoEnBD.setFechaPrestamo(new Date());
            prestamoEnBD.setFechaDevolucion(null);
            prestamoRepositorio.save(prestamoEnBD);
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
        libro.setePrestados(libro.getePrestados() - 1);
        libro.seteRestantes(libro.geteRestantes() + 1);
        libroServicio.actualizarBD(libro);

        //Actualizamos la cantidad de prestamos del cliente y persistimos en la BD
        cliente.setCantidadPrestamos(cliente.getCantidadPrestamos() - 1);

        clienteServicio.actualizarBD(cliente);

        return prestamoRepositorio.save(prestamo);
       
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
            libro.setePrestados(libro.getePrestados() + 1);
            libro.seteRestantes(libro.geteRestantes() - 1);
            prestamo.setLibro(libro);
            libroServicio.actualizarBD(libro);
        } else {
            throw new ErrorServicio("No hay ejemplares disponible de este libro para realizar el prestamo.");
        }

        //Actualizamos la cantidad de prestamos del cliente y persistimos en la BD
        cliente.setCantidadPrestamos(cliente.getCantidadPrestamos() + 1);
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
    public Prestamo validarDatos(Libro libro, Cliente cliente, Prestamo prestamo) throws ErrorServicio {

        //El libro no puede estar repetido ni ser nulo        
        if (libro == null) {
            throw new ErrorServicio("No se encontro el libro solicitado o el campo está vacio.");
        }

        if (cliente == null) {
            throw new ErrorServicio("No se encontro el cliente solicitadoo el campo está vacio.");
        }

        //Validamos que existan libros disponibles para el prestamo
        if (libro.geteRestantes() > 0) {
            libro.setePrestados(libro.getePrestados() + 1);
            libro.seteRestantes(libro.geteRestantes() - 1);
            prestamo.setLibro(libro);
            libroServicio.actualizarBD(libro);
        } else {
            throw new ErrorServicio("No hay ejemplares disponible de este libro para realizar el prestamo.");
        }
        //Actualizamos la cantidad de prestamos del cliente y persistimos en la BD
        cliente.setCantidadPrestamos(cliente.getCantidadPrestamos() + 1);

        prestamo.setCliente(cliente);
        clienteServicio.actualizarBD(cliente);
        return prestamo;
    }

    @Transactional
    private Prestamo validarCambios(Libro libro, Cliente cliente, Prestamo prestamoEnBD, Libro libroBD, Cliente clienteBD) throws ErrorServicio {
        //El libro y el cliente no pueden estar vacio ni ser nulo        
        if (libro == null) {
            throw new ErrorServicio("No se encontro el libro solicitado o el campo está vacio.");
        }

        if (cliente == null) {
            throw new ErrorServicio("No se encontro el cliente solicitadoo el campo está vacio.");
        }
        //Si los datos son iguales, no existen cambios a modificar
        if (prestamoEnBD.getCliente().equals(cliente)
                && prestamoEnBD.getLibro().equals(libro)) {
            throw new ErrorServicio("No existen cambios para editar");
        }

        if (!(prestamoEnBD.getCliente().equals(cliente))
                || !(prestamoEnBD.getLibro().equals(libro))) {

            //validamos que el prestamo este ativo
            if (prestamoEnBD.getAlta()) {
                //Validamos que existan libros disponibles para el prestamo
                if (libro.geteRestantes() > 0) {
                    libro.setePrestados(libro.getePrestados() + 1);
                    libro.seteRestantes(libro.geteRestantes() - 1);
                    libroBD.setePrestados(libroBD.getePrestados() - 1);
                    libroBD.seteRestantes(libroBD.geteRestantes() + 1);

                    libroServicio.actualizarBD(libro);
                    libroServicio.actualizarBD(libroBD);

                    //Actualizamos la cantidad de prestamos del cliente y persistimos en la BD
                    cliente.setCantidadPrestamos(cliente.getCantidadPrestamos() + 1);
                    clienteBD.setCantidadPrestamos(clienteBD.getCantidadPrestamos() - 1);
                    
                    clienteServicio.actualizarBD(cliente);
                    clienteServicio.actualizarBD(clienteBD);
                } else {
                    throw new ErrorServicio("No hay ejemplares disponible de este libro para realizar el prestamo.");
                }
            }
            prestamoEnBD.setLibro(libro);

            prestamoEnBD.setCliente(cliente);
            
        }

        return prestamoEnBD;

    }
}

