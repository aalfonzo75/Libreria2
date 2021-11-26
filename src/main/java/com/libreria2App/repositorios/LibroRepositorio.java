package com.libreria2App.repositorios;
import com.libreria2App.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Aleidy Alfonzo
 */
@Repository
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    @Query("SELECT l FROM Libro l WHERE l.id = :id")
    public Libro buscarPorId(@Param("id") String id);

    @Query("SELECT l FROM Libro l WHERE l.isbn = :isbn")
    public Libro buscarLibroPorIsbn(@Param("isbn") Long isbn);
    
     @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    public List<Libro> buscarLibroPorTitulo(@Param("titulo") String titulo);    

    @Query("SELECT l FROM Libro l WHERE l.anio = :anio")
    public List<Libro> buscarLibroPorAnio(@Param("anio") Integer anio);

    @Query("SELECT l FROM Libro l WHERE l.autor.nombre = :nombreA")
    public List<Libro> listarLibrosporNombreAutor(@Param("nombreA") String autor);

    @Query("SELECT l FROM Libro l WHERE l.editorial.nombre = :nombreE")
    public List<Libro> listarLibrosporNombreEditorial(@Param("nombreE") String editorial);

    @Query("SELECT l from Libro l WHERE l.alta = true ")
    public List<Libro> buscarActivos();
    
    
    // con esta query se obtiene contenido parecido a, LIKE %?1% remplaza a LIKE :variable
    @Query("SELECT p from Libro p WHERE p.titulo LIKE %?1% or p.autor.nombre LIKE %?1% or p.editorial.nombre LIKE %?1%")
    List<Libro> buscar(@Param("buscar") String buscar);
    
//de esta manera al buscar para encontrar resultado se debe usar contenido exacto    
//    @Query("SELECT p from Libro p WHERE p.titulo LIKE :buscar or p.autor.nombre LIKE :buscar or p.editorial.nombre LIKE :buscar")
//    List<Libro> buscar(@Param("buscar") String buscar);

}
