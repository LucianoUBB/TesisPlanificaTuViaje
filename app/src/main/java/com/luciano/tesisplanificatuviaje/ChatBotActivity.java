package com.luciano.tesisplanificatuviaje;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.luciano.tesisplanificatuviaje.api.MessageRequest;
import com.luciano.tesisplanificatuviaje.api.MessageResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatBotActivity extends AppCompatActivity {

    private EditText inputUser;
    private Button btnSend;
    private TextView chatOutput;
    private ScrollView scrollView;
    private BottomNavigationView bottomNavigationView;
    
    private static final String TAG = "ChatBotActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        // 🔐 Verificar sesión activa
        verificarSesionActiva();

        inputUser = findViewById(R.id.editTextUserMessage);
        btnSend = findViewById(R.id.btnSendMessage);
        chatOutput = findViewById(R.id.textViewChat);
        scrollView = findViewById(R.id.scrollChat);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 🧭 Configurar barra de navegación
        setupBottomNavigation();

        // 🔙 Configurar manejo del botón atrás
        setupOnBackPressed();

        btnSend.setOnClickListener(v -> {
            String userMessage = inputUser.getText().toString().trim();
            if (!TextUtils.isEmpty(userMessage)) {
                appendMessage("🧑 Tú: " + userMessage);
                enviarMensajeADialogflow(userMessage);
                inputUser.setText("");
            }
        });
    }
    
    /**
     * 🔐 Verificar que haya una sesión activa
     */
    private void verificarSesionActiva() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // No hay sesión activa, redirigir al login
            Log.w(TAG, "❌ No hay sesión activa, redirigiendo al login");
            Intent intent = new Intent(ChatBotActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "✅ Sesión activa verificada: " + currentUser.getEmail());
        }
    }

    private void appendMessage(String message) {
        chatOutput.append(message + "\n\n");
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
    }

    private void enviarMensajeADialogflow(String mensaje) {
        // 🤖 Método temporal con respuestas simuladas hasta configurar DialogFlow
        // TODO: Reemplazar con DialogFlow real cuando tengas las credenciales
        
        // Simular delay de red
        new android.os.Handler().postDelayed(() -> {
            String respuesta = generarRespuestaTemporalChatbot(mensaje);
            appendMessage("🤖 Asistente: " + respuesta);
        }, 1000);
        
        // 📝 Código para DialogFlow real (descomentarás cuando tengas las credenciales):
        /*
        MessageRequest request = new MessageRequest(mensaje);
        
        DialogflowClient.getRestService().sendMessage(request).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String reply = response.body().getFulfillmentText();
                    appendMessage("🤖 Asistente: " + reply);
                } else {
                    appendMessage("❌ Error al obtener respuesta del servidor.");
                    Log.e(TAG, "Error DialogFlow: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                appendMessage("⚠️ Error de conexión con DialogFlow: " + t.getMessage());
                Log.e(TAG, "Error de red DialogFlow", t);
            }
        });
        */
    }
    
    /**
     * 🎯 Generador de respuestas temporal para el chatbot
     * (Reemplazar cuando DialogFlow esté configurado)
     */
    private String generarRespuestaTemporalChatbot(String mensaje) {
        String mensajeLower = mensaje.toLowerCase();
        
        // Respuestas básicas por palabras clave
        if (mensajeLower.contains("hola") || mensajeLower.contains("buenos")) {
            return "¡Hola! Soy tu asistente de viajes. ¿En qué puedo ayudarte hoy?";
        } else if (mensajeLower.contains("recomendación") || mensajeLower.contains("recomendar")) {
            return "Te puedo recomendar lugares increíbles. ¿Qué tipo de experiencia buscas: aventura, relajación, cultura o gastronomía?";
        } else if (mensajeLower.contains("hotel") || mensajeLower.contains("alojamiento")) {
            return "¿En qué ciudad necesitas alojamiento? Te ayudo a encontrar las mejores opciones según tu presupuesto.";
        } else if (mensajeLower.contains("restaurante") || mensajeLower.contains("comida")) {
            return "¡Perfecto! ¿Qué tipo de cocina prefieres? Tengo excelentes recomendaciones gastronómicas.";
        } else if (mensajeLower.contains("transporte") || mensajeLower.contains("cómo llegar")) {
            return "Te ayudo con las mejores rutas. ¿Desde dónde y hacia dónde necesitas viajar?";
        } else if (mensajeLower.contains("gracias")) {
            return "¡De nada! Estoy aquí para hacer tu viaje inolvidable. ¿Algo más en lo que pueda ayudarte?";
        } else {
            return "Interesante pregunta. Estoy aquí para ayudarte con recomendaciones de viajes, hoteles, restaurantes y rutas. ¿Podrías ser más específico?";
        }
    }

    /**
     * 🧭 Configurar la barra de navegación inferior
     */
    private void setupBottomNavigation() {
                bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.seasonal_nav_color));
        bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.seasonal_nav_color));

        // Configurar fondo blanco para todas las actividades
        bottomNavigationView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        // Configurar colores específicos para ChatBot
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.nav_chatbot_color));
        bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.nav_chatbot_color));
        
        // Marcar el item actual como seleccionado
        bottomNavigationView.setSelectedItemId(R.id.nav_chatbot);
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_inicio) {
                // 🏠 Ir a MapsActivity
                startActivity(new Intent(ChatBotActivity.this, MapsActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_perfil) {
                // 👤 Ir a PerfilActivity
                startActivity(new Intent(ChatBotActivity.this, PerfilActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.nav_chatbot) {
                // 🤖 Ya estamos en ChatBot, no hacer nada
                return true;
            } else if (itemId == R.id.nav_historial) {
                // 📋 Ir a HistorialActivity
                startActivity(new Intent(ChatBotActivity.this, HistorialActivity.class));
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
                android.content.Intent intent = new android.content.Intent(ChatBotActivity.this, MapsActivity.class);
                intent.setFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP | android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
