package com.libreria2App.entidades;

import com.sun.istack.NotNull;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Aleidy Alfonzo
 */
@Entity
public class Cliente {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    
    @NotNull
    @Column(unique = true)
    private Long dni;
    
    @Column(nullable = false, length = 50)
    private String nombre;    
    @Column(nullable = false, length = 50)
    private String apellido;    
    @Column(nullable = false, length = 50)
    private String telefono;
    
    private Boolean alta;
    
     //RELACIONES
    @OneToMany(mappedBy = "cliente")
    private List<Prestamo> prestamos;
    
    private Integer cantidadPrestamos;

    //CONSTRUCTOR con cantidadPrestamos INICIALIZADO
    public Cliente() {
        this.cantidadPrestamos = 0;
    }  
    
    
    //GETTERS AND SETTERS

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }    

    public Long getDni() {
        return dni;
    }

    public void setDni(Long dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    public Integer getCantidadPrestamos() {
        return cantidadPrestamos;
    }

    public void setCantidadPrestamos(Integer cantidadPrestamos) {
        this.cantidadPrestamos = cantidadPrestamos;
    }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + id + ", dni=" + dni + ", nombre=" + nombre + ", apellido=" + apellido + ", telefono=" + telefono + ", alta=" + alta + ", prestamos=" + prestamos + ", cantidadPrestamos=" + cantidadPrestamos + '}';
    }
    
    

}
