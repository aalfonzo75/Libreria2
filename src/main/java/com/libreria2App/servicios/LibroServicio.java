package com.libreria2App.servicios;

import com.libreria2App.entidades.Libro;
import com.libreria2App.errores.ErrorServicio;
import com.libreria2App.repositorios.LibroRepositorio;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Aleidy Alfonzo
 */
@Service
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;
  
    @Transactional
    public void crearLibro(Libro lib) throws ErrorServicio {
        
        //valido todos los datos        
         validarDatos(lib);
       
        Libro libro = new Libro();
        libro.setIsbn(lib.getIsbn());
        libro.setTitulo(lib.getTitulo());
        libro.setAnio(lib.getAnio());
        libro.setEjemplares(lib.getEjemplares());
        libro.setePrestados(0);
        libro.seteRestantes(lib.getEjemplares());
        libro.setAutor(lib.getAutor());
        libro.setEditorial(lib.getEditorial());
        libro.setAlta(true);
        
        libroRepositorio.save(libro);
    }

    @Transactional  //Si se ejecuta sin hacer excepciones hace comit a la BD y se aplican cambios
    public void modificarLibro(Libro libroEditado) throws ErrorServicio {
       
        //busco el libro para modificarlo
        Libro libroEnBD = libroRepositorio.buscarPorId(libroEditado.getId());

        if (libroEnBD != null) {            
            //Valido y comparo los cambios entre los dos libros, el que viene por parametro y el de la BD
            validarCambios(libroEnBD, libroEditado);
            //Modificamos los valores   
            libroEnBD.setIsbn(libroEditado.getIsbn());
            libroEnBD.setTitulo(libroEditado.getTitulo());
            libroEnBD.setAnio(libroEditado.getAnio());
            libroEnBD.setEjemplares(libroEditado.getEjemplares());           
            libroEnBD.seteRestantes(libroEditado.getEjemplares()-libroEnBD.getePrestados());
            
            libroEnBD.setAutor(libroEditado.getAutor());
            libroEnBD.setEditorial(libroEditado.getEditorial());
            
            libroRepositorio.save(libroEnBD);
        } else {
            throw new ErrorServicio("No se encontro el libro con el id solicitado");
        }
    }

    @Transactional
    public void eliminarLibro(Libro libro) throws ErrorServicio {
        //busco el libro
        Libro lib = libroRepositorio.buscarPorId(libro.getId());
        if (lib != null) {
            libroRepositorio.delete(lib);
        } else {
            throw new ErrorServicio("No se encontro el libro con el id solicitado");
        }
    }

    @Transactional
    public Libro baja(String id) {
        Libro libro = libroRepositorio.getOne(id);
        libro.setAlta(false);
        return libroRepositorio.save(libro);
      
    }

    @Transactional
    public Libro alta(String id) {
        Libro libro = libroRepositorio.getOne(id);
        libro.setAlta(true);
        return libroRepositorio.save(libro);
    }

    @Transactional(readOnly = true)
    public List<Libro> listaTodosLibros() {
        return libroRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Libro> listaBuscarLibro(String buscarLibro) {
        return libroRepositorio.buscar(buscarLibro);
    }
    
    @Transactional(readOnly = true)
    public Libro getOne(String id) {
        return libroRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Libro> listarActivos() {
        return libroRepositorio.buscarActivos();
    }

    @Transactional(readOnly = true)
    public Libro buscarLibro(String id) {
        return libroRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public Libro buscarPorId(String id) {
        return libroRepositorio.buscarPorId(id);
    }   
    
    public Libro validarDatos(Libro libro) throws ErrorServicio {

         //Valido que el dni no esta repetido
        Optional<Libro> rspta = libroRepositorio.validaIsbn(libro.getIsbn());
        if (rspta.isPresent()) {
            throw new ErrorServicio("El ISBN ya se encuentra registrado");
        }
        if (libro.getIsbn() == null || libro.getIsbn().toString().length() < 8) {
            throw new ErrorServicio("El ISBN es invalido");
        }
        if (libro.getTitulo() == null || libro.getTitulo().isEmpty()) {
            throw new ErrorServicio("El Titulo del libro no puede ser nulo");
        }
        if (libro.getAnio().toString().length() != 4 || libro.getAnio() < Calendar.YEAR) {
            throw new ErrorServicio("Anio invalido");
        }
        if (libro.getEjemplares() == null || libro.getEjemplares() < 1) {
            throw new ErrorServicio("Los Ejemplares del libro no puede ser nulo");
        }
        return libro;
    }
    
    private Libro validarCambios(Libro libroEnBD, Libro libroEditado) throws ErrorServicio{
        //Si los datos son iguales, no existen cambios a modificar
        if (libroEnBD.getIsbn().equals(libroEditado.getIsbn())
                && libroEnBD.getTitulo().equals(libroEditado.getTitulo())
                && libroEnBD.getAnio().equals(libroEditado.getAnio())
                && libroEnBD.getEjemplares().equals(libroEditado.getEjemplares())
                && libroEnBD.getAutor().equals(libroEditado.getAutor())
                && libroEnBD.getEditorial().equals(libroEditado.getEditorial())) {
            throw new ErrorServicio("No existen cambios para editar");
        }
        
        //La cantidad de ejemplares NO debe ser menor a la cantidad de ePrestados ni nulo
        if (libroEditado.getEjemplares() < libroEnBD.getePrestados()) {
            throw new ErrorServicio("La cantidad de ejemplares no puede ser menor a " + libroEnBD.getePrestados());
        }
        
        //El ISBN no puede pertenecer a otro libro que no sea el mismo que se modifica
        if (libroRepositorio.validaIsbn(libroEditado.getIsbn()) != null && !(libroEditado.getIsbn().equals(libroEnBD.getIsbn()))) {
            throw new ErrorServicio("El ISBN ingresado ya pertenece a otro libro");
        }  
        return libroEnBD;
    }  
    
    @Transactional(readOnly = true)
    public Libro actualizarBD(Libro libro) {
        return  libroRepositorio.save(libro);
    }
}
