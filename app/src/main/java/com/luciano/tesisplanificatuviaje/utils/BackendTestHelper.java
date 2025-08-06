package com.luciano.tesisplanificatuviaje.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.luciano.tesisplanificatuviaje.api.BackendClient;
import com.luciano.tesisplanificatuviaje.models.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackendTestHelper {
    
    private static final String TAG = "BackendTest";
    
    /**
     * 🔧 Probar conexión con el backend
     */
    public static void testBackendConnection(Context context) {
        Log.d(TAG, "🚀 Probando conexión con backend: " + BackendClient.getCurrentBaseUrl());
        
        // Ejemplo: Probar endpoint de salud/ping
        // BackendClient.getInstance().healthCheck().enqueue(new Callback<Object>() { ... });
        
        // Por ahora, solo mostrar la URL configurada
        String message = "Backend configurado: " + BackendClient.getCurrentBaseUrl();
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        Log.d(TAG, message);
    }
    
    /**
     * 📡 Ejemplo de cómo usar el backend para registrar usuario
     */
    public static void ejemploRegistroBackend(String email, String password, Context context) {
        Usuario usuario = new Usuario(email, password);
        
        BackendClient.getInstance().registrarUsuario(usuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Usuario registrado en backend: " + response.body().getEmail());
                    Toast.makeText(context, "Usuario registrado en backend", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "❌ Error registrando en backend: " + response.code());
                    Toast.makeText(context, "Error en backend: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e(TAG, "❌ Error de conexión con backend", t);
                Toast.makeText(context, "Sin conexión con backend: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    
    /**
     * 📊 Información del entorno actual
     */
    public static void mostrarInfoEntorno(Context context) {
        StringBuilder info = new StringBuilder();
        info.append("🌐 URL Backend: ").append(BackendClient.getCurrentBaseUrl()).append("\n");
        info.append("🏫 ¿Servidor Universidad?: ").append(BackendClient.isConnectedToUniversity() ? "Sí" : "No").append("\n");
        info.append("💻 ¿Entorno Local?: ").append(!BackendClient.isConnectedToUniversity() ? "Sí" : "No");
        
        Log.d(TAG, info.toString());
        Toast.makeText(context, info.toString(), Toast.LENGTH_LONG).show();
    }
}

