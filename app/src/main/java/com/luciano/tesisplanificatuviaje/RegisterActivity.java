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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button btnRegister;
    private TextView linkLogin;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // � Verificar Google Play Services al inicio
        checkGooglePlayServices();

        // �🔐 Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // 🧾 Referencias a los elementos de la interfaz
        editTextEmail = findViewById(R.id.editTextRegisterEmail);
        editTextPassword = findViewById(R.id.editTextRegisterPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        linkLogin = findViewById(R.id.linkLogin);

        // 🎯 Acción del botón
        btnRegister.setOnClickListener(v -> registrarUsuario());
        
        // 🔄 Acción del link para volver al login
        linkLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Cerrar la actividad de registro
        });
    }

    /**
     * 🔧 Verificar estado de Google Play Services
     */
    private void checkGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        
        switch (resultCode) {
            case ConnectionResult.SUCCESS:
                Log.d("RegisterActivity", "✅ Google Play Services disponible");
                break;
            case ConnectionResult.SERVICE_MISSING:
                Log.w("RegisterActivity", "⚠️ Google Play Services no instalado");
                Toast.makeText(this, "Google Play Services no está instalado", Toast.LENGTH_LONG).show();
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Log.w("RegisterActivity", "⚠️ Google Play Services desactualizado");
                if (googleApiAvailability.isUserResolvableError(resultCode)) {
                    googleApiAvailability.getErrorDialog(this, resultCode, 9000).show();
                }
                break;
            case ConnectionResult.SERVICE_DISABLED:
                Log.w("RegisterActivity", "⚠️ Google Play Services deshabilitado");
                Toast.makeText(this, "Google Play Services está deshabilitado", Toast.LENGTH_LONG).show();
                break;
            default:
                Log.w("RegisterActivity", "⚠️ Google Play Services error: " + resultCode);
                break;
        }
    }

    private void registrarUsuario() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Correo no válido");
            editTextEmail.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Contraseña mínima de 6 caracteres");
            editTextPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Las contraseñas no coinciden");
            editTextConfirmPassword.requestFocus();
            return;
        }

        // 📡 Crear usuario con Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        android.util.Log.d("RegisterActivity", "✅ Usuario registrado exitosamente con Firebase Auth");
                        Toast.makeText(RegisterActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                        // El usuario ya está automáticamente logueado tras el registro exitoso
                        // Redirigir directamente al formulario de preferencias
                        startActivity(new Intent(RegisterActivity.this, FormularioActivity.class));
                        finish();
                    } else {
                        android.util.Log.e("RegisterActivity", "❌ Error al registrar usuario", task.getException());
                        Toast.makeText(RegisterActivity.this, "Error al registrar: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}

