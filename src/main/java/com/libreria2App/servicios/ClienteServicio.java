package com.libreria2App.servicios;

import com.libreria2App.entidades.Cliente;
import com.libreria2App.entidades.Prestamo;
import com.libreria2App.errores.ErrorServicio;
import com.libreria2App.repositorios.ClienteRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Aleidy Alfonzo
 */
@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Autowired
    private PrestamoServicio prestamoServicio;

    @Transactional
    public void crearCliente(Cliente cli) throws ErrorServicio {

        Cliente cliente = new Cliente();
        //valido todos los datos que no son objetos
        validarDatos(cli.getDni(), cli.getNombre(), cli.getApellido(), cli.getTelefono());
        cliente.setDni(cli.getDni());
        cliente.setNombre(cli.getNombre());
        cliente.setApellido(cli.getApellido());
        cliente.setTelefono(cli.getTelefono());
        cliente.setAlta(true);
        clienteRepositorio.save(cliente);
    }

    @Transactional
    public Cliente modificarCliente(Cliente clienteEditado) throws ErrorServicio {       
        //busco el cliente para modificarlo
        Cliente clienteEnBD = clienteRepositorio.buscarPorId(clienteEditado.getId());

        if (clienteEnBD != null) {
            validarCambios(clienteEnBD, clienteEditado);
            clienteEnBD.setDni(clienteEditado.getDni());
            clienteEnBD.setNombre(clienteEditado.getNombre());
            clienteEnBD.setApellido(clienteEditado.getApellido());
            clienteEnBD.setTelefono(clienteEditado.getTelefono());
            
            return clienteRepositorio.save(clienteEnBD);
        } else {
            throw new ErrorServicio("No se encontro el cliente con el id solicitado");
        }
    }

    @Transactional
    public void eliminarCliente(Cliente cli) throws ErrorServicio {
        //busco el Cliente
        Cliente cliente = clienteRepositorio.buscarPorId(cli.getId());
        if (cliente != null) {
            clienteRepositorio.delete(cliente);
        } else {
            throw new ErrorServicio("No se encontro el cliente con el id solicitado");
        }
    }

    @Transactional
    public Cliente baja(String id) {
        Cliente cliente = clienteRepositorio.getOne(id);
        cliente.setAlta(false);
        return clienteRepositorio.save(cliente);
        //throw new ErrorServicio("No se encontro el libro con el id solicitado");
    }

    @Transactional
    public Cliente alta(String id) {
        Cliente cliente = clienteRepositorio.getOne(id);
        cliente.setAlta(true);
        return clienteRepositorio.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listaTodosClientes() {
        return clienteRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarActivos() {
        return clienteRepositorio.buscarActivos();
    }

    @Transactional(readOnly = true)
    public List<Prestamo> listarPrestamosActivos(Cliente cliente) {
        return prestamoServicio.listarPrestamosActivos(cliente);
    }

    @Transactional(readOnly = true)
    public Cliente buscarClientePorDni(Long dni) {
        return clienteRepositorio.buscarClientePorDni(dni);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(String id) {
        return clienteRepositorio.buscarPorId(id);
    }

    @Transactional(readOnly = true)
    public Cliente actualizarBD(Cliente cliente) {
        return clienteRepositorio.save(cliente);
    }

    @Transactional(readOnly = true)
    public Cliente getOne(String id) {
        return clienteRepositorio.getOne(id);
    }

    @Transactional
    public void validarDatos(Long dni, String nombre, String apellido, String telefono) throws ErrorServicio {
        //Removemos espacios innecesarios
        nombre = nombre.trim();
        apellido = apellido.trim();
        telefono = telefono.trim();
        //Valido que el dni no esta repetido
        Optional<Cliente> rspta = clienteRepositorio.validarDni(dni);
        if (rspta.isPresent()) {
            throw new ErrorServicio("El DNI ya se encuentra registrado");
        }
        if (dni == null || dni < 0) {
            throw new ErrorServicio("El dni del cliente es invalido");
        }

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del cliente no puede ser nulo");
        }
        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apellido del cliente no puede ser nulo");
        }
        if (telefono == null || telefono.isEmpty()) {
            throw new ErrorServicio("El telefono del cliente no puede ser nulo");
        }
    }
    
    @Transactional
    private void validarCambios(Cliente clienteEnBD, Cliente clienteEditado) throws ErrorServicio{
        //Si los datos son iguales, no existen cambios a modificar
        if (clienteEnBD.getDni().equals(clienteEditado.getDni())
                && clienteEnBD.getNombre().equals(clienteEditado.getNombre())
                && clienteEnBD.getApellido().equals(clienteEditado.getApellido())
                && clienteEnBD.getTelefono().equals(clienteEditado.getTelefono())) {
            throw new ErrorServicio("No existen cambios para editar");
        }
        
        //El DNI no puede pertenecer a otro cliente que no sea el mismo que se modifica
        if (clienteRepositorio.validarDni(clienteEditado.getDni()) != null && !(clienteEditado.getDni().equals(clienteEnBD.getDni()))) {
            throw new ErrorServicio("El DNI ingresado ya pertenece a otro cliente");
        }        
    }  

}
