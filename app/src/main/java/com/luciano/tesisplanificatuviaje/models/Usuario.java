package com.luciano.tesisplanificatuviaje.models;

public class Usuario {
    private String id;
    private String email;
    private String nombre;
    private String password;
    private Preferencias preferencias;
    
    public Usuario() {
        // Constructor vacío requerido para Gson
    }
    
    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }
    
    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Preferencias getPreferencias() { return preferencias; }
    public void setPreferencias(Preferencias preferencias) { this.preferencias = preferencias; }
}

