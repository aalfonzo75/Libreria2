package com.libreria2App.repositorios;
import com.libreria2App.entidades.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Aleidy Alfonzo
 */
@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, String> {
    
     @Query("SELECT a FROM Autor a WHERE a.nombre = :nombre")
    public Editorial buscarEditorialPorNombre(@Param("nombre") String nombre);

    @Query("SELECT a FROM Autor a WHERE a.id = :id")
    public Editorial buscarEditorialPorId(@Param("id") String id);
    
    @Query("SELECT a FROM Autor a")
    public List<Editorial> listarEditoriales();


}

