package com.libreria2App.servicios;
import com.libreria2App.entidades.Autor;
import com.libreria2App.entidades.Libro;
import com.libreria2App.errores.ErrorServicio;
import com.libreria2App.repositorios.AutorRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Aleidy Alfonzo
 */
@Service
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional
    public void crearAutor(String nombre) throws ErrorServicio {
        validarDatos(nombre);

        Autor autor = new Autor();
//        List<Libro> listaLibros = new ArrayList();
        autor.setNombre(nombre);
        autor.setAlta(true);

        autorRepositorio.save(autor);
    }

    @Transactional
    public Autor modificarAutor(String id, String nombre) throws ErrorServicio {
        validarDatos(nombre);

        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setNombre(nombre);
          
            return autorRepositorio.save(autor);

        } else {
            throw new ErrorServicio("No se encontro el autor con el id solicitado");
        }

    }

   @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Autor baja(String id) throws ErrorServicio {

        Optional<Autor> respuesta = autorRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Autor autor = respuesta.get();
            autor.setAlta(false);

            return autorRepositorio.save(autor);

        } else {
            throw new ErrorServicio("No se encontro el autor con el id solicitado");
        }
    }
    
     @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class})
    public Autor alta(String id) {
         Autor autor = autorRepositorio.getOne(id);
        autor.setAlta(true);
        return autorRepositorio.save(autor);
    }

    public void validarDatos(String nombre) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre del autor no puede ser nulo");
        }
    }

    @Transactional(readOnly = true)
    public List<Autor> listarTodos() {
        return autorRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Autor buscarAutorPorNombre(String nombre) {
        return autorRepositorio.buscarAutorPorNombre(nombre);
    }

    @Transactional(readOnly = true)
    public Autor buscarAutorPorId(String id) {
        return autorRepositorio.buscarAutorPorId(id);
    }

    @Transactional(readOnly = true)
    public Autor getOne(String id) {
        return autorRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Autor> listarActivos() {
        List<Autor> todos = listarTodos();
        List<Autor> activos = new ArrayList();
        for (Autor autor : todos) {
            if (autor.getAlta()) {
                activos.add(autor);
            }
        }
        return activos;
    }

}

