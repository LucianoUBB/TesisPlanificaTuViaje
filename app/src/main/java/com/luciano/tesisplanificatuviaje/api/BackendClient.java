package com.luciano.tesisplanificatuviaje.api;

import com.luciano.tesisplanificatuviaje.config.ApiConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackendClient {
    
    private static BackendService service;
    private static OkHttpClient httpClient;
    
    public static BackendService getInstance() {
        if (service == null) {
            createService();
        }
        return service;
    }
    
    private static void createService() {
        // 🔧 Configurar cliente HTTP con timeouts y logging
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(ApiConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(ApiConfig.READ_TIMEOUT, TimeUnit.SECONDS);
        
        // 📝 Agregar logging solo en desarrollo
        if (ApiConfig.isLocalEnvironment()) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClientBuilder.addInterceptor(loggingInterceptor);
        }
        
        httpClient = httpClientBuilder.build();
        
        // 🚀 Crear cliente Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.getBaseUrl())
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
                
        service = retrofit.create(BackendService.class);
    }
    
    // 🔄 Método para cambiar la URL dinámicamente
    public static void switchToLocalEnvironment() {
        service = null; // Reset
        // Actualizar la configuración y recrear
        createService();
    }
    
    public static void switchToUniversityEnvironment() {
        service = null; // Reset
        createService();
    }
    
    // 📊 Métodos de utilidad
    public static String getCurrentBaseUrl() {
        return ApiConfig.getBaseUrl();
    }
    
    public static boolean isConnectedToUniversity() {
        return ApiConfig.isUniversityEnvironment();
    }
}

