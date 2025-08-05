package com.luciano.tesisplanificatuviaje;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HistorialActivity extends AppCompatActivity {

    private static final String TAG = "HistorialActivity";
    
    private BottomNavigationView bottomNavigationView;
    private TextView txtNoHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_historial);
            Log.d(TAG, "✅ Layout cargado correctamente");
            
            // 🔐 Verificar sesión activa
            if (!verificarSesionActiva()) {
                return; // Si no hay sesión, se redirige automáticamente
            }

            // 🧭 Configurar referencias de UI
            initializeViews();
            
            // 🧭 Configurar barra de navegación
            setupBottomNavigation();
            
            // 🔙 Configurar manejo del botón atrás
            setupOnBackPressed();
            
            Log.d(TAG, "✅ HistorialActivity inicializada correctamente");
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Error fatal en onCreate", e);
            // En caso de error crítico, volver a MapsActivity
            volverAMaps();
        }
    }

    private void initializeViews() {
        try {
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            txtNoHistorial = findViewById(R.id.txtNoHistorial);
            
            // Mostrar mensaje de no hay historial por defecto
            if (txtNoHistorial != null) {
                txtNoHistorial.setVisibility(View.VISIBLE);
            }
            
            Log.d(TAG, "✅ Views inicializadas correctamente");
        } catch (Exception e) {
            Log.e(TAG, "❌ Error inicializando views", e);
        }
    }

    /**
     * 🧭 Configurar la barra de navegación inferior
     */
    private void setupBottomNavigation() {
        if (bottomNavigationView == null) {
            Log.e(TAG, "❌ BottomNavigationView es null");
            return;
        }
        
        try {
            // ☀️ Configurar colores estacionales - VERANO para HistorialActivity
            bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.seasonal_nav_color));
            bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.seasonal_nav_color));
            
            // Configurar fondo blanco para todas las actividades
            bottomNavigationView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            
            // Destacar el ícono actual con color veraniego
            bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.nav_historial_color));
            bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.nav_historial_color));
            
            // Marcar el item actual como seleccionado
            bottomNavigationView.setSelectedItemId(R.id.nav_historial);
            
            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                
                if (itemId == R.id.nav_inicio) {
                    Log.d(TAG, "🏠 Navegando a MapsActivity");
                    volverAMaps();
                    return true;
                } else if (itemId == R.id.nav_perfil) {
                    Log.d(TAG, "👤 Navegando a PerfilActivity");
                    Intent intent = new Intent(HistorialActivity.this, PerfilActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_chatbot) {
                    Log.d(TAG, "🤖 Navegando a ChatBotActivity");
                    Intent intent = new Intent(HistorialActivity.this, ChatBotActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.nav_historial) {
                    Log.d(TAG, "📋 Ya estamos en Historial");
                    return true;
                }
                
                return false;
            });
            
            Log.d(TAG, "✅ Navegación configurada correctamente");
        } catch (Exception e) {
            Log.e(TAG, "❌ Error configurando navegación", e);
        }
    }

    /**
     * � Verificar que haya una sesión activa
     */
    private boolean verificarSesionActiva() {
        try {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Log.w(TAG, "❌ No hay sesión activa, redirigiendo al login");
                Intent intent = new Intent(HistorialActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return false;
            } else {
                Log.d(TAG, "✅ Sesión activa verificada: " + currentUser.getEmail());
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "❌ Error verificando sesión", e);
            volverAMaps();
            return false;
        }
    }

    /**
     * 🏠 Volver a MapsActivity de forma segura
     */
    private void volverAMaps() {
        try {
            Intent intent = new Intent(HistorialActivity.this, MapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Log.e(TAG, "❌ Error volviendo a Maps", e);
            finish(); // Como último recurso, solo cerrar esta actividad
        }
    }

    /**
     * 🔙 Configurar manejo moderno del botón atrás
     */
    private void setupOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d(TAG, "🔙 Back button presionado - volviendo a MapsActivity");
                volverAMaps();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        verificarSesionActiva();
    }
}
