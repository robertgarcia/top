package com.rgarcia.top;

import java.util.Objects;

public class Artistas {
    public static final String ORDEN = "orden";
    private long id;
    private String nombre;
    private String apellidos;
    private long fechaNacimiento;
    private String lugarNacimientos;
    private short estatura;
    private String notas;
    private int orden;
    private String fotoUrl;

    public Artistas(){}

    public Artistas(long id, String nombre, String apellidos, long fechaNacimiento, String lugarNacimientos, short estatura, String notas, int orden, String fotoUrl) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.lugarNacimientos = lugarNacimientos;
        this.estatura = estatura;
        this.notas = notas;
        this.orden = orden;
        this.fotoUrl = fotoUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public long getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(long fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getLugarNacimientos() {
        return lugarNacimientos;
    }

    public void setLugarNacimientos(String lugarNacimientos) {
        this.lugarNacimientos = lugarNacimientos;
    }

    public short getEstatura() {
        return estatura;
    }

    public void setEstatura(short estatura) {
        this.estatura = estatura;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String  getNombreCompleto() {
        return this.nombre + " " + this.apellidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artistas artistas = (Artistas) o;
        return id == artistas.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
