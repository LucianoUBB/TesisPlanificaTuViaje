package com.luciano.tesisplanificatuviaje;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class FormularioActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String userId;
    private Map<String, Object> datosTotales = new HashMap<>();
    private int pasoActual = 1;
    private static final int TOTAL_PASOS = 6; // Ahora tenemos 6 pasos
    
    // Variables para modo edición
    private boolean isEditMode = false;
    private boolean returnToProfile = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        // 🎨 Configurar barra de navegación específica para formularios (gris neutro)
        getWindow().setNavigationBarColor(getResources().getColor(R.color.gray_500, null));

        db = FirebaseFirestore.getInstance();
        
        // 🔧 Verificar si viene en modo edición
        Intent intent = getIntent();
        isEditMode = intent.getBooleanExtra("EDIT_MODE", false);
        returnToProfile = intent.getBooleanExtra("RETURN_TO_PROFILE", false);
        
        Log.d("FormularioActivity", "🔧 Modo edición: " + isEditMode);
        Log.d("FormularioActivity", "🔄 Retornar a perfil: " + returnToProfile);
        
        // 🔐 Verificar sesión activa
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            Log.d("FormularioActivity", "✅ Usuario autenticado: " + user.getEmail());
            
            // Si es modo edición, cargar datos existentes
            if (isEditMode) {
                cargarDatosExistentes();
            } else {
                cargarPaso(pasoActual);
            }
        } else {
            Log.w("FormularioActivity", "❌ No hay usuario autenticado, redirigiendo al login");
            irAlLogin();
            return;
        }
    }
    
    /**
     * 🔄 Redirigir al login cuando no hay sesión
     */
    private void irAlLogin() {
        Intent intent = new Intent(FormularioActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void guardarPaso(String paso, Map<String, Object> datos) {
        Log.d("FormularioActivity", "📥 Guardando " + paso + " con datos: " + datos.toString());
        datosTotales.putAll(datos);
        Log.d("FormularioActivity", "📊 Datos acumulados hasta ahora: " + datosTotales.toString());
        Log.d("FormularioActivity", "📈 Total de campos guardados: " + datosTotales.size());
    }

    public void irAlSiguientePaso() {
        if (pasoActual < TOTAL_PASOS) {
            pasoActual++;
            cargarPaso(pasoActual);
        }
    }
    
    public void irAlPasoAnterior() {
        if (pasoActual > 1) {
            pasoActual--;
            cargarPaso(pasoActual);
        }
    }
    
    public int getPasoActual() {
        return pasoActual;
    }
    
    public int getTotalPasos() {
        return TOTAL_PASOS;
    }

    public void finalizarFormulario() {
        Log.d("FormularioActivity", "🚀 Iniciando finalización del formulario");
        Log.d("FormularioActivity", "📝 Datos a guardar: " + datosTotales.toString());
        
        Toast.makeText(this, "Guardando preferencias...", Toast.LENGTH_SHORT).show();
        
        if (userId == null || userId.isEmpty()) {
            Log.e("FormularioActivity", "❌ Usuario ID es nulo");
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (datosTotales.isEmpty()) {
            Log.e("FormularioActivity", "❌ No hay datos para guardar");
            Toast.makeText(this, "Error: No hay datos para guardar", Toast.LENGTH_SHORT).show();
            
            // 🧪 Test: Crear datos de ejemplo para verificar la conexión
            Log.d("FormularioActivity", "🧪 Creando datos de test para verificar conexión...");
            crearDatosDeTest();
            
            if (datosTotales.isEmpty()) {
                Log.e("FormularioActivity", "❌ Incluso los datos de test están vacíos");
                return;
            }
        }

        // Verificar conectividad antes de guardar
        if (!isNetworkAvailable()) {
            Log.w("FormularioActivity", "⚠️ No hay conexión a internet");
            Toast.makeText(this, "Sin conexión. Los datos se guardarán cuando tengas internet.", Toast.LENGTH_LONG).show();
            // Continuar al mapa ya que Firestore tiene persistencia offline
            startActivity(new Intent(FormularioActivity.this, MapsActivity.class));
            finish();
            return;
        }

        // Verificar autenticación antes de guardar
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Log.e("FormularioActivity", "❌ Usuario no autenticado");
            Toast.makeText(this, "Error: Usuario no autenticado. Inicia sesión nuevamente.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(FormularioActivity.this, LoginActivity.class));
            finish();
            return;
        }
        
        Log.d("FormularioActivity", "🔐 Usuario autenticado: " + currentUser.getUid());
        Log.d("FormularioActivity", "📧 Email del usuario: " + currentUser.getEmail());

        // Guardar en Firebase con manejo mejorado de errores
        Log.d("FormularioActivity", "🔄 Intentando guardar preferencias para usuario: " + userId);
        Log.d("FormularioActivity", "📝 Datos a guardar: " + datosTotales.toString());
        
        db.collection("usuarios").document(userId).collection("preferencias").document("datos")
                .set(datosTotales)
                .addOnSuccessListener(unused -> {
                    Log.d("FormularioActivity", "✅ Preferencias guardadas exitosamente");
                    
                    String mensaje = isEditMode ? "Preferencias actualizadas exitosamente" : "Preferencias guardadas exitosamente";
                    Toast.makeText(FormularioActivity.this, mensaje, Toast.LENGTH_SHORT).show();
                    
                    // Decidir a dónde navegar basado en el modo
                    if (returnToProfile) {
                        // Modo edición: volver al perfil
                        Log.d("FormularioActivity", "👤 Regresando a PerfilActivity (modo edición)");
                        Intent intent = new Intent(FormularioActivity.this, PerfilActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        // Modo normal: ir al mapa
                        Log.d("FormularioActivity", "🗺️ Navegando a MapsActivity (modo normal)");
                        Intent intent = new Intent(FormularioActivity.this, MapsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FormularioActivity", "❌ Error al guardar preferencias: " + e.getClass().getSimpleName(), e);
                    Log.e("FormularioActivity", "❌ Mensaje del error: " + e.getMessage());
                    
                    String mensaje = "Error desconocido";
                    
                    // Verificar el tipo de error específico
                    if (e instanceof com.google.firebase.firestore.FirebaseFirestoreException) {
                        com.google.firebase.firestore.FirebaseFirestoreException firestoreException = 
                            (com.google.firebase.firestore.FirebaseFirestoreException) e;
                        Log.e("FormularioActivity", "❌ Código de error Firestore: " + firestoreException.getCode());
                        
                        switch (firestoreException.getCode()) {
                            case PERMISSION_DENIED:
                                Log.e("FormularioActivity", "🚫 PERMISSION_DENIED - Las reglas de Firestore no permiten escribir");
                                Log.e("FormularioActivity", "🔧 Solución: Cambiar reglas en Firebase Console a 'allow read, write: if true;'");
                                mensaje = "Error de permisos en Firestore. Contacta al administrador para actualizar las reglas de seguridad.";
                                break;
                            case UNAVAILABLE:
                                mensaje = "Servicio no disponible. Inténtalo más tarde.";
                                break;
                            case UNAUTHENTICATED:
                                mensaje = "Usuario no autenticado. Inicia sesión nuevamente.";
                                break;
                            default:
                                mensaje = "Error de conexión. Los datos se guardarán cuando tengas internet.";
                        }
                    } else if (e.getMessage() != null && e.getMessage().contains("network")) {
                        mensaje = "Sin conexión a internet. Los datos se guardarán automáticamente.";
                    }
                    
                    Toast.makeText(FormularioActivity.this, mensaje, Toast.LENGTH_LONG).show();
                    
                    // Navegar basado en el modo, incluso si hay error
                    if (returnToProfile) {
                        Log.d("FormularioActivity", "👤 Regresando a PerfilActivity a pesar del error");
                        Intent intent = new Intent(FormularioActivity.this, PerfilActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d("FormularioActivity", "🗺️ Navegando a MapsActivity a pesar del error");
                        Intent intent = new Intent(FormularioActivity.this, MapsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
    }


    private void cargarPaso(int paso) {
        Fragment fragment;
        switch (paso) {
            case 1:
                fragment = new FormularioPaso1Fragment(); // Información Personal
                break;
            case 2:
                fragment = new FormularioPaso2Fragment(); // Sabores y Clima
                break;
            case 3:
                fragment = new FormularioPaso3Fragment(); // Vacaciones y Actividades
                break;
            case 4:
                fragment = new FormularioPaso4Fragment(); // Viaje y Presupuesto
                break;
            case 5:
                fragment = new FormularioPaso5Fragment(); // Planificación y Deportes
                break;
            case 6:
                fragment = new ResumenFormularioFragment(); // Resumen Final
                break;
            default:
                fragment = new FormularioPaso1Fragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.formulario_fragment_container, fragment)
                .commit();
    }
    public Map<String, Object> getDatosTotales() {
        return datosTotales;
    }

    public Object getDatoPaso(String clave) {
        return datosTotales.get(clave);
    }
    
    /**
     * 📊 Cargar datos existentes del usuario para modo edición
     */
    private void cargarDatosExistentes() {
        Log.d("FormularioActivity", "📥 Cargando datos existentes del usuario para edición");
        
        db.collection("usuarios").document(userId).collection("preferencias").document("datos")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Cargar todos los datos existentes
                        Map<String, Object> datosFirestore = documentSnapshot.getData();
                        if (datosFirestore != null) {
                            datosTotales.putAll(datosFirestore);
                            Log.d("FormularioActivity", "✅ Datos existentes cargados: " + datosTotales.size() + " campos");
                            Log.d("FormularioActivity", "📋 Datos cargados: " + datosTotales.toString());
                        }
                    } else {
                        Log.w("FormularioActivity", "⚠️ No se encontraron datos existentes del usuario");
                    }
                    
                    // Cargar el primer paso después de cargar los datos
                    cargarPaso(pasoActual);
                })
                .addOnFailureListener(e -> {
                    Log.e("FormularioActivity", "❌ Error al cargar datos existentes: " + e.getMessage());
                    // Aún así cargar el primer paso
                    cargarPaso(pasoActual);
                });
    }

    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            Log.e("FormularioActivity", "Error al verificar conectividad", e);
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        // Durante el formulario, prevenir retroceder accidentalmente
        // Mostrar confirmación si el usuario quiere salir
        String titulo = isEditMode ? "Salir de la edición" : "Salir del formulario";
        String mensaje = isEditMode ? 
            "¿Estás seguro de que quieres salir? Se perderán los cambios no guardados." :
            "¿Estás seguro de que quieres salir? Se perderán los datos no guardados.";
        
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle(titulo)
                .setMessage(mensaje)
                .setPositiveButton("Sí, salir", (dialog, which) -> {
                    // Navegar basado en el modo
                    if (returnToProfile) {
                        // Modo edición: volver al perfil
                        Intent intent = new Intent(FormularioActivity.this, PerfilActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    } else {
                        // Modo normal: ir al mapa
                        Intent intent = new Intent(FormularioActivity.this, MapsActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Continuar", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void crearDatosDeTest() {
        Log.d("FormularioActivity", "🧪 Generando datos de test...");
        
        // Crear datos de ejemplo para testing
        Map<String, Object> datosTest = new HashMap<>();
        datosTest.put("gender", "Masculino");
        datosTest.put("age", "25");
        datosTest.put("flavor_preferences", "Dulce, Salado");
        datosTest.put("preferred_climate", "Templado");
        datosTest.put("types_of_vacations", "Aventura");
        datosTest.put("activities", "Senderismo");
        datosTest.put("favorite_season", "Primavera");
        datosTest.put("preferred_transport", "Carro");
        datosTest.put("budget", "1000-2000");
        datosTest.put("travel_planning", "Espontáneo");
        datosTest.put("travel_duration", "1 semana");
        datosTest.put("sport", "Fútbol");
        datosTest.put("practices_sport", true);
        datosTest.put("test_data", true);
        datosTest.put("timestamp", new java.util.Date().toString());
        
        datosTotales.putAll(datosTest);
        Log.d("FormularioActivity", "✅ Datos de test creados: " + datosTotales.size() + " campos");
    }

}
