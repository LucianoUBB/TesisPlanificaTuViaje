package com.luciano.tesisplanificatuviaje.models;

public class Preferencias {
    private String tipoViaje;
    private String presupuesto;
    private String duracion;
    private String intereses;
    private String ubicacionPreferida;
    
    public Preferencias() {
        // Constructor vacío requerido para Gson
    }
    
    // Getters y Setters
    public String getTipoViaje() { return tipoViaje; }
    public void setTipoViaje(String tipoViaje) { this.tipoViaje = tipoViaje; }
    
    public String getPresupuesto() { return presupuesto; }
    public void setPresupuesto(String presupuesto) { this.presupuesto = presupuesto; }
    
    public String getDuracion() { return duracion; }
    public void setDuracion(String duracion) { this.duracion = duracion; }
    
    public String getIntereses() { return intereses; }
    public void setIntereses(String intereses) { this.intereses = intereses; }
    
    public String getUbicacionPreferida() { return ubicacionPreferida; }
    public void setUbicacionPreferida(String ubicacionPreferida) { this.ubicacionPreferida = ubicacionPreferida; }
}

