package com.example.practicajsonpeliculas.modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name="multas")
public class Multas implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "id_multa")
    private int id_multa;

    @Column(name = "precio")
    private int precio;

    @Column(name = "fecha")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name="matricula",referencedColumnName="matricula")
    private Coche coche;

    public Multas(int id_multa, int precio, String matricula, LocalDate fecha, Coche coche) {
        this.id_multa = id_multa;
        this.precio = precio;
        this.fecha = fecha;
        this.coche = coche;
    }

    public Multas() {
    }

    public int getId_multa() {
        return id_multa;
    }

    public void setId_multa(int id_multa) {
        this.id_multa = id_multa;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Multas{" +
                "id_multa=" + id_multa +
                ", precio=" + precio +
                ", fecha=" + fecha +
                '}';
    }
    public Coche getCoche() {
        return coche;
    }

    public void setCoche(Coche coche) {
        this.coche = coche;
    }

}
