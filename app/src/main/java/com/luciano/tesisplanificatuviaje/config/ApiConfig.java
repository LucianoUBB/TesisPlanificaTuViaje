package com.luciano.tesisplanificatuviaje.config;

public class ApiConfig {
    
    // 🌐 URLs del backend - cambiar según el entorno
    
    // Para desarrollo local (cuando pruebes en tu PC)
    public static final String LOCAL_BASE_URL = "http://10.0.2.2:3000/api/"; // Android Emulator
    public static final String LOCAL_BASE_URL_DEVICE = "http://192.168.1.100:3000/api/"; // Dispositivo físico
    
    // Para servidor universitario (cuando subas el backend)
    public static final String UNIVERSITY_BASE_URL = "https://servidor-universidad.edu.ar/api/"; // URL real del servidor
    
    // Para producción (si decides usar otro hosting)
    public static final String PRODUCTION_BASE_URL = "https://tu-backend-produccion.com/api/";
    
    // 🔧 URL activa (cambiar según el entorno que uses)
    public static final String CURRENT_BASE_URL = UNIVERSITY_BASE_URL; // Cambiar cuando sea necesario
    
    // 🤖 Configuración de DialogFlow
    public static final String DIALOGFLOW_PROJECT_ID = "tu-proyecto-chatbot"; // Cambiar por tu Project ID real
    public static final String DIALOGFLOW_LANGUAGE = "es";
    
    // 🔐 Headers comunes para el backend
    public static final String CONTENT_TYPE = "application/json";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    
    // ⏱️ Timeouts
    public static final int CONNECT_TIMEOUT = 30; // segundos
    public static final int READ_TIMEOUT = 30; // segundos
    
    // 📱 Métodos utilitarios
    public static String getBaseUrl() {
        return CURRENT_BASE_URL;
    }
    
    public static boolean isLocalEnvironment() {
        return CURRENT_BASE_URL.contains("10.0.2.2") || CURRENT_BASE_URL.contains("192.168");
    }
    
    public static boolean isUniversityEnvironment() {
        return CURRENT_BASE_URL.equals(UNIVERSITY_BASE_URL);
    }
}

