package com.libreria2App.repositorios;

import com.libreria2App.entidades.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Aleidy Alfonzo
 */
@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, String> {
    
   @Query("SELECT p FROM Prestamo p WHERE p.id = :id")
    public Prestamo buscarPorId(@Param("id") String id);
    
     @Query("SELECT p FROM Prestamo p WHERE p.alta = true")
    public List<Prestamo> buscarActivos();

}
