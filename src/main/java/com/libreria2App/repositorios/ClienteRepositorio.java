package com.libreria2App.repositorios;

import com.libreria2App.entidades.Cliente;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Aleidy Alfonzo
 */
@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String> {

    @Query("SELECT c FROM Cliente c WHERE c.dni = :dni")
    public Cliente buscarClientePorDni(@Param("dni") Long dni);
    
    @Query("SELECT c FROM Cliente c WHERE c.id = :id")
    public Cliente buscarPorId(@Param("id") String id);
    
    @Query("SELECT c FROM Cliente c WHERE c.alta = true ")
    public List<Cliente> buscarActivos();
    
     @Query("SELECT c FROM Cliente c WHERE c.dni = :dni")
    public Optional<Cliente> validarDni(@Param("dni") Long dni);
    
}
