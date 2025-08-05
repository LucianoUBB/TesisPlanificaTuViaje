package com.luciano.tesisplanificatuviaje;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 2000; // 2 segundos
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            verificarEstadoUsuario();
        }, SPLASH_DELAY);
    }

    private void verificarEstadoUsuario() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        
        Log.d("SplashActivity", "🔍 Verificando estado del usuario...");
        
        // 🧪 TEMPORAL: Para testing, cerrar sesión automáticamente
        // TODO: Quitar esta línea cuando ya no sea necesario hacer testing del login
        if (currentUser != null) {
            Log.d("SplashActivity", "🧪 MODO TESTING: Cerrando sesión automáticamente para testing");
            mAuth.signOut();
            currentUser = null;
        }
        
        if (currentUser != null) {
            // 🎯 Usuario ya logueado, verificar si tiene preferencias
            Log.d("SplashActivity", "✅ Usuario logueado encontrado: " + currentUser.getEmail());
            Log.d("SplashActivity", "🆔 UID del usuario: " + currentUser.getUid());
            verificarPreferenciasUsuario(currentUser.getUid());
        } else {
            // 🔑 No hay usuario logueado, ir a Login
            Log.d("SplashActivity", "❌ No hay usuario logueado, redirigiendo a LoginActivity");
            Log.d("SplashActivity", "🔀 Iniciando LoginActivity...");
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    }
    
    private void verificarPreferenciasUsuario(String userId) {
        // 📊 Verificar si el usuario ya completó sus preferencias
        Log.d("SplashActivity", "📊 Verificando preferencias del usuario: " + userId);
        
        db.collection("usuarios").document(userId).collection("preferencias")
                .document("datos")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // ✅ Usuario tiene preferencias, ir directamente a Maps
                        Log.d("SplashActivity", "✅ Usuario con preferencias encontradas, redirigiendo a MapsActivity");
                        startActivity(new Intent(SplashActivity.this, MapsActivity.class));
                    } else {
                        // 📝 Usuario sin preferencias, ir a Formulario
                        Log.d("SplashActivity", "📝 Usuario SIN preferencias, redirigiendo a FormularioActivity");
                        Log.d("SplashActivity", "🔀 Iniciando FormularioActivity...");
                        startActivity(new Intent(SplashActivity.this, FormularioActivity.class));
                    }
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("SplashActivity", "❌ Error verificando preferencias: " + e.getMessage(), e);
                    // En caso de error, ir a Formulario por seguridad
                    Log.d("SplashActivity", "🛡️ Error en verificación, redirigiendo a FormularioActivity por seguridad");
                    startActivity(new Intent(SplashActivity.this, FormularioActivity.class));
                    finish();
                });
    }
}
