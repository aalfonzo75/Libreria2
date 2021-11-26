package com.libreria2App.servicios;
import com.libreria2App.entidades.Autor;
import com.libreria2App.entidades.Editorial;
import com.libreria2App.entidades.Libro;
import com.libreria2App.errores.ErrorServicio;
import com.libreria2App.repositorios.AutorRepositorio;
import com.libreria2App.repositorios.EditorialRepositorio;
import com.libreria2App.repositorios.LibroRepositorio;
import java.util.Calendar;
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
public class LibroServicio {

    @Autowired
    private LibroRepositorio libroRepositorio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

//    
    @Transactional
    public void crearLibro(Libro lib) throws ErrorServicio {
        //valido todos los datos que no son objetos
        validarDatos(lib.getIsbn(), lib.getTitulo(), lib.getAnio(), lib.getEjemplares(), lib.getePrestados());

        //valido los atributos que son objetos de otra clase
//        if (libro.getAutor().toString().isEmpty() || libro.getAutor() == null) {
//            throw new ErrorServicio("El Autor no puede ser nulo o vacio");
//        }else{
//            libro.setAutor(autorServicio.buscarAutorPorId(libro.getAutor().getId()));
//        }
//        
//        if (libro.getEditorial().toString().isEmpty() || libro.getEditorial() == null) {
//            throw new ErrorServicio("La Editorial no puede ser nula o vacia");
//        } else{
//             libro.setEditorial(editorialServicio.buscarEditorialPorId(libro.getEditorial().getId()));
//        }
        Libro libro = new Libro();
        libro.setIsbn(lib.getIsbn());
        libro.setTitulo(lib.getTitulo());
        libro.setAnio(lib.getAnio());
        libro.setEjemplares(lib.getEjemplares());
        libro.setePrestados(lib.getePrestados());
        libro.seteRestantes(lib.getEjemplares() - lib.getePrestados());
        libro.setAutor(lib.getAutor());
        libro.setEditorial(lib.getEditorial());
        libro.setAlta(true);

        System.out.println("aqui creo mi libro en servicio");
        System.out.println("libro" + libro.toString());

        libroRepositorio.save(libro);
    }

//     @Transactional
//    public Libro crearLibro(Libro libro) throws ErrorServicio {
//        //valido todos los datos que no son objetos
//        validarDatos(libro.getIsbn(), libro.getTitulo(), libro.getAnio(), libro.getEjemplares(), libro.getePrestados(), libro.geteRestantes());
//
//        //valido los atributos que son objetos de otra clase
//        if (libro.getAutor().toString().isEmpty() || libro.getAutor() == null) {
//            throw new ErrorServicio("El Autor no puede ser nulo o vacio");
//        }else{
//            libro.setAutor(autorServicio.buscarAutorPorId(libro.getAutor().getId()));
//        }
//        
//        if (libro.getEditorial().toString().isEmpty() || libro.getEditorial() == null) {
//            throw new ErrorServicio("La Editorial no puede ser nula o vacia");
//        } else{
//             libro.setEditorial(editorialServicio.buscarEditorialPorId(libro.getEditorial().getId()));
//        }
//        
//        libro.setAlta(true);
//        
//        System.out.println("aqui creo mi libro en servicio");
//        System.out.println("libro" + libro.toString());
//        
//        return libroRepositorio.save(libro);
//    }
//    public Libro crearLibro(Libro libro) throws ErrorServicio {
//        //valido todos los datos que no son objetos
//        validarDatos(libro.getIsbn(), libro.getTitulo(), libro.getAnio(), libro.getEjemplares(), libro.getePrestados(), libro.geteRestantes());
//
//        //valido los atributos que son objetos de otra clase
//        if (libro.getAutor().toString().isEmpty() || libro.getAutor() == null) {
//            throw new ErrorServicio("El Autor no puede ser nulo o vacio");
//        }
//        libro.setAutor(autorServicio.buscarAutorPorId(libro.getAutor().getId()));
//
//        if (libro.getEditorial().toString().isEmpty() || libro.getEditorial() == null) {
//            throw new ErrorServicio("La Editorial no puede ser nula o vacia");
//        }
//        libro.setEditorial(editorialServicio.buscarEditorialPorId(libro.getEditorial().getId()));
//
//         System.out.println("libro"+libro.toString());
//        
//        return libroRepositorio.save(libro);
//    }
//    
    @Transactional  //Si se ejecuta sin hacer excepciones hace comit a la BD y se aplican cambios
    public Libro modificarLibro(Libro lib) throws ErrorServicio {
        //busco el libro
        Libro libro = libroRepositorio.buscarPorId(lib.getId());

        if (libro != null) {
            //valido todos los datos que no son objetos
            validarDatos(lib.getIsbn(), lib.getTitulo(), lib.getAnio(), lib.getEjemplares(), lib.getePrestados());
            //Modificamos los valores   
            libro.setIsbn(lib.getIsbn());
            libro.setTitulo(lib.getTitulo());
            libro.setAnio(lib.getAnio());
            libro.setEjemplares(lib.getEjemplares());
            libro.setePrestados(lib.getePrestados());
            libro.setAutor(lib.getAutor());
            libro.setEditorial(lib.getEditorial());
//            lib.seteRestantes(libro.geteRestantes());

            System.out.println("aqui modifico mi libro en servicio");
            System.out.println("libro" + libro.toString());

            return libroRepositorio.save(libro);
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
        //throw new ErrorServicio("No se encontro el libro con el id solicitado");
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
    public List<Libro> listarActivos() {
        return libroRepositorio.buscarActivos();
    }

    @Transactional(readOnly = true)
    public Libro getOne(String id) {
        return libroRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public Libro buscarPorId(String id) {
        return libroRepositorio.buscarPorId(id);
    }
    //, Integer eRestantes

    public void validarDatos(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ePrestados) throws ErrorServicio {

        if (isbn == null || isbn.toString().length() < 8) {
            throw new ErrorServicio("El ISBN es invalido");
        }
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("El Titulo del libro no puede ser nulo");
        }
        if (anio.toString().length() != 4 || anio < Calendar.YEAR) {
            throw new ErrorServicio("Anio invalido");
        }
        if (ejemplares == null || ejemplares < 1) {
            throw new ErrorServicio("Los Ejemplares del libro no puede ser nulo");
        }
        if (ePrestados == null || ePrestados < 1) {
            throw new ErrorServicio("los EjemplaresPrestados del libro no puede ser nulo");
        }
//        if (eRestantes == null || eRestantes < 1) {
//            throw new ErrorServicio("Los EjemplaresRestantes del libro no puede ser nulo");
//        }

    }

//, String nombreE, String nombreA
//     if (nombreE == null || nombreE.isEmpty()) {
//            throw new ErrorServicio("El Nombre de Editorial del libro no puede ser nulo");
//        }
//        if (nombreA == null || nombreA.isEmpty()) {
//            throw new ErrorServicio("El Nombre de Autor del libro no puede ser nulo");
//        }
}

//@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
//    public Libro guardar(Long isbn, String titulo, Integer anio, Integer ejemplares, Integer ejemplaresPrestados, Integer ejemplaresRestantes, String idAutor, String idEditorial) throws ErrorServicio {
//     
//        Autor autor=autorRepositorio.getById(idAutor);
//        Editorial editorial = editorialRepositorio.getById(idEditorial);
//        validarDatos(isbn, titulo, anio, ejemplares, ejemplaresPrestados, ejemplaresRestantes, idAutor, idEditorial);
//
//        Libro libro = new Libro();
//
//        libro.setIsbn(isbn);
//        libro.setTitulo(titulo);
//        libro.setAnio(anio);
//        libro.setEjemplares(ejemplares);
//        libro.setEjemplaresPrestados(ejemplaresPrestados);
//        libro.setEjemplaresRestantes(ejemplaresRestantes);
//        libro.setAlta(true);
//        
//        libro.setAutor(autor);
//        libro.setEditorial(editorial);
//
////        Autor autor = autorServicio.registrarAutor(nombreA);
//   
////        Editorial editorial = editorialServicio.registrarEditorial(nombreE);
//     
//        return libroRepositorio.save(libro);
//    }
//    @Transactional  //Si se ejecuta sin hacer excepciones hace comit a la BD y se aplican cambios
//    public Libro modificarLibro(Libro libro) throws ErrorServicio {
//        //valido todos los datos que no son objetos
//        
//        
//        validarDatos(libro.getIsbn(), libro.getTitulo(), libro.getAnio(), libro.getEjemplares(), libro.getePrestados(), libro.geteRestantes());
//
//        //Comprobamos si el libro existe
//        Optional<Libro> respuesta = libroRepositorio.findById(id);
//
//        if (respuesta.isPresent()) {
//            //Modificamos los valores
//            Libro libro = respuesta.get();
////            libro.setTitulo(titulo);
////            libro.setIsbn(isbn);
////            libro.setAnio(anio);
////            libro.setEjemplares(ejemplares);
////            libro.setEPrestados(ejemplaresPrestados);
////            libro.setERestantes(ejemplaresRestantes);
//
//            return libroRepositorio.save(libro);
//
//        } else {
//            throw new ErrorServicio("No se encontro el libro con el id solicitado");
//        }
//
//    }
//@Transactional
//    public void eliminar(String id) throws ErrorServicio {
//        Optional<Libro> respuesta = libroRepositorio.findById(id);
//        if (respuesta.isPresent()) {
//            Libro libro = respuesta.get();
//            libroRepositorio.delete(libro);
//        } else {
//            throw new ErrorServicio("No se encontro el libro con el id solicitado");
//        }
//    }


