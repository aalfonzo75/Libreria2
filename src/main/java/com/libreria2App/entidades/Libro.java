package com.libreria2App.entidades;
import com.sun.istack.NotNull;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Aleidy Alfonzo
 */
@Entity
public class Libro {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @Column(unique = true)
    private Long isbn;
    
     @Column(nullable = false, length = 75)
    private String titulo;
    @Column(nullable = false)
    private Integer anio;
    @Column(nullable = false)
    private Integer ejemplares;
   @Column(nullable = false)
    private Integer ePrestados;
   @Column(nullable = false)
    private Integer eRestantes;

    private Boolean alta;

    @ManyToOne
    private Autor autor;

    @ManyToOne
    private Editorial editorial;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getIsbn() {
        return isbn;
    }

    public void setIsbn(Long isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getEjemplares() {
        return ejemplares;
    }

    public void setEjemplares(Integer ejemplares) {
        this.ejemplares = ejemplares;
    }

    public Integer getePrestados() {
        return ePrestados;
    }

    public void setePrestados(Integer ePrestados) {
        this.ePrestados = ePrestados;
    }

    public Integer geteRestantes() {
        return eRestantes;
    }

    public void seteRestantes(Integer eRestantes) {
        this.eRestantes = eRestantes;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    @Override
    public String toString() {
        return "Libro{" + "id=" + id + ", isbn=" + isbn + ", titulo=" + titulo + ", anio=" + anio + ", ejemplares=" + ejemplares + ", ePrestados=" + ePrestados + ", eRestantes=" + eRestantes + ", alta=" + alta + ", autor=" + autor + ", editorial=" + editorial + '}';
    }

}
