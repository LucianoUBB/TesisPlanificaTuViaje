package com.luciano.tesisplanificatuviaje;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;
    private TextView linkRegister;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // � Verificar si el usuario ya está logueado al iniciar
        verificarSesionExistente();

        // �🔗 Referencias UI
        editTextEmail = findViewById(R.id.editTextLoginEmail);
        editTextPassword = findViewById(R.id.editTextLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        linkRegister = findViewById(R.id.linkRegister);

        btnLogin.setOnClickListener(v -> iniciarSesion());

        linkRegister.setOnClickListener(v -> {
            // Ir a pantalla de registro (sin finish, para poder volver)
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    /**
     * 🔍 Verificar si ya hay una sesión activa
     */
    private void verificarSesionExistente() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("LoginActivity", "✅ Usuario ya logueado: " + currentUser.getEmail());
            verificarPreferenciasYRedirigir(currentUser.getUid());
        } else {
            Log.d("LoginActivity", "❌ No hay sesión activa");
        }
    }

    /**
     * 📊 Verificar preferencias y redirigir apropiadamente
     */
    private void verificarPreferenciasYRedirigir(String userId) {
        db.collection("usuarios").document(userId).collection("preferencias")
                .document("datos")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // ✅ Usuario con preferencias, ir a Maps
                        Log.d("LoginActivity", "✅ Usuario con preferencias, redirigiendo a Maps");
                        startActivity(new Intent(LoginActivity.this, MapsActivity.class));
                        finish();
                    } else {
                        // 📝 Usuario sin preferencias, ir a Formulario
                        Log.d("LoginActivity", "📝 Usuario sin preferencias, redirigiendo a Formulario");
                        startActivity(new Intent(LoginActivity.this, FormularioActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("LoginActivity", "❌ Error verificando preferencias", e);
                    // En caso de error, ir a Formulario por seguridad
                    startActivity(new Intent(LoginActivity.this, FormularioActivity.class));
                    finish();
                });
    }

    private void iniciarSesion() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Correo inválido");
            editTextEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Contraseña mínima de 6 caracteres");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        android.util.Log.d("LoginActivity", "✅ Inicio de sesión exitoso con Firebase Auth");
                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                        
                        // 🔄 Reusar la función de verificación de preferencias
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            verificarPreferenciasYRedirigir(user.getUid());
                        }
                    } else {
                        android.util.Log.e("LoginActivity", "❌ Error al iniciar sesión", task.getException());
                        Toast.makeText(this, "Error al iniciar sesión: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // ⚠️ FUNCIÓN ANTIGUA - mantener por compatibilidad pero usar verificarPreferenciasYRedirigir
    private void verificarPreferenciasUsuario() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            verificarPreferenciasYRedirigir(user.getUid());
        }
    }
}

