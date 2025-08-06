package com.luciano.tesisplanificatuviaje;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PerfilActivity extends AppCompatActivity {

    private TextView txtNombre, txtSexo, txtEdad;
    private Button btnEditarPreferencias, btnCerrarSesion, btnRegresar;
    private BottomNavigationView bottomNavigationView;
    
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userId;
    
    private static final String TAG = "PerfilActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        // 🔐 Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // 🧾 Referencias UI
        txtNombre = findViewById(R.id.txtNombre);
        txtSexo = findViewById(R.id.txtSexo);
        txtEdad = findViewById(R.id.txtEdad);
        btnEditarPreferencias = findViewById(R.id.btnEditarPreferencias);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnRegresar = findViewById(R.id.btnRegresar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 🧭 Configurar barra de navegación
        setupBottomNavigation();

        // 👤 Verificar usuario autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
            cargarDatosUsuario();
        } else {
            // Si no hay usuario, ir al login
            irAlLogin();
            return;
        }

        // 🎯 Configurar botones
        btnEditarPreferencias.setOnClickListener(v -> editarPreferencias());
        btnCerrarSesion.setOnClickListener(v -> confirmarCerrarSesion());
        btnRegresar.setOnClickListener(v -> finish());

        // 🔙 Configurar manejo del botón atrás
        setupOnBackPressed();
    }

    private void cargarDatosUsuario() {
        Log.d(TAG, "🔍 Cargando datos del usuario: " + userId);
        
        db.collection("usuarios").document(userId).collection("preferencias").document("datos")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Obtener datos del documento usando las claves correctas del formulario
                        String nombre = documentSnapshot.getString("nombreUsuario"); // Cambio: usar "nombreUsuario" del formulario
                        String sexo = documentSnapshot.getString("sexo");
                        Long edadLong = documentSnapshot.getLong("edad");
                        
                        // Mostrar datos o valores por defecto
                        txtNombre.setText(nombre != null ? nombre : "No especificado");
                        txtSexo.setText(sexo != null ? sexo : "No especificado");
                        txtEdad.setText(edadLong != null ? edadLong + " años" : "No especificado");
                        
                        Log.d(TAG, "✅ Datos cargados exitosamente: " + nombre + ", " + sexo + ", " + edadLong);
                    } else {
                        Log.w(TAG, "⚠️ No se encontraron datos del usuario");
                        txtNombre.setText("No especificado");
                        txtSexo.setText("No especificado");
                        txtEdad.setText("No especificado");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "❌ Error al cargar datos del usuario", e);
                    txtNombre.setText("Error al cargar");
                    txtSexo.setText("Error al cargar");
                    txtEdad.setText("Error al cargar");
                    Toast.makeText(this, "Error al cargar datos del perfil", Toast.LENGTH_SHORT).show();
                });
    }

    private void editarPreferencias() {
        Log.d(TAG, "✏️ Navegando a editar preferencias");
        
        // Crear intent para ir al FormularioActivity en modo edición
        Intent intent = new Intent(PerfilActivity.this, FormularioActivity.class);
        intent.putExtra("EDIT_MODE", true); // Indicar que es modo edición
        intent.putExtra("RETURN_TO_PROFILE", true); // Indicar que debe volver al perfil al finalizar
        
        startActivity(intent);
        
        // No llamar finish() para mantener el perfil en el stack y poder volver
        Log.d(TAG, "🔄 FormularioActivity iniciado en modo edición");
    }

    private void confirmarCerrarSesion() {
        new AlertDialog.Builder(this)
                .setTitle("Cerrar Sesión")
                .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                .setPositiveButton("Sí", (dialog, which) -> cerrarSesion())
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void cerrarSesion() {
        Log.d(TAG, "🚪 Cerrando sesión del usuario");
        
        // 🔐 Cerrar sesión de Firebase Auth
        mAuth.signOut();
        
        // 🧹 Limpiar datos de sesión si es necesario (ej: SharedPreferences)
        // SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        // prefs.edit().clear().apply();
        
        Log.d(TAG, "✅ Sesión cerrada exitosamente");
        Toast.makeText(this, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show();
        
        irAlLogin();
    }

    private void irAlLogin() {
        // 🔄 Limpiar completamente el stack de actividades y ir al Login
        Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * 🧭 Configurar la barra de navegación inferior
     */
    private void setupBottomNavigation() {
        // ❄️ Configurar colores estacionales - INVIERNO para PerfilActivity
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.seasonal_nav_color));
        bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.seasonal_nav_color));
        
        // Configurar fondo blanco para la barra de navegación
        bottomNavigationView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        
        // Destacar el ícono actual con color invernal
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.nav_perfil_color));
        bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.nav_perfil_color));
        
        // Marcar el item actual como seleccionado
        bottomNavigationView.setSelectedItemId(R.id.nav_perfil);
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_inicio) {
                // 🏠 Ir a MapsActivity
                startActivity(new Intent(PerfilActivity.this, MapsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_perfil) {
                // 👤 Ya estamos en Perfil, no hacer nada
                return true;
            } else if (itemId == R.id.nav_chatbot) {
                // 🤖 Ir a ChatBotActivity
                startActivity(new Intent(PerfilActivity.this, ChatBotActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_historial) {
                // 📋 Ir a HistorialActivity
                startActivity(new Intent(PerfilActivity.this, HistorialActivity.class));
                finish();
                return true;
            }
            
            return false;
        });
    }

    /**
     * 🔙 Configurar manejo moderno del botón atrás
     */
    private void setupOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Retroceder siempre a MapsActivity
                Intent intent = new Intent(PerfilActivity.this, MapsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}

