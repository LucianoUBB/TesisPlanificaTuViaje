package com.luciano.tesisplanificatuviaje;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.activity.OnBackPressedCallback;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabMyLocation;
    private static final String TAG = "MapsActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // 🔐 Verificar sesión activa al iniciar
        verificarSesionActiva();

        // 🧭 Configurar barra de navegación inferior
        setupBottomNavigation();

        // 📍 Configurar botón de ubicación
        setupLocationButton();

        // Inicializar el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // 🔙 Configurar manejo del botón atrás
        setupOnBackPressed();
    }

    private void setupBottomNavigation() {
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        
        // 🍂 Configurar colores estacionales - OTOÑO para MapsActivity
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.seasonal_nav_color));
        bottomNavigationView.setItemTextColor(ContextCompat.getColorStateList(this, R.color.seasonal_nav_color));

        // Configurar fondo blanco para todas las actividades
        bottomNavigationView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

        // Configurar colores específicos para Maps
        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.color.nav_maps_color));        // Marcar "Inicio" como seleccionado por defecto
        bottomNavigationView.setSelectedItemId(R.id.nav_inicio);
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            
            if (itemId == R.id.nav_inicio) {
                // Ya estamos en la pantalla de inicio (MapsActivity)
                Log.d(TAG, "🏠 Ya estamos en MapsActivity");
                return true;
            } else if (itemId == R.id.nav_perfil) {
                // Navegar a la pantalla de perfil
                Log.d(TAG, "👤 Navegación a Perfil");
                Intent intent = new Intent(MapsActivity.this, PerfilActivity.class);
                startActivity(intent);
                // NO llamar finish() aquí para mantener MapsActivity en el stack
                return true;
            } else if (itemId == R.id.nav_chatbot) {
                // Navegar al chatbot
                Log.d(TAG, "🤖 Navegación a Chatbot");
                Intent intent = new Intent(MapsActivity.this, ChatBotActivity.class);
                startActivity(intent);
                // NO llamar finish() aquí para mantener MapsActivity en el stack
                return true;
            } else if (itemId == R.id.nav_historial) {
                // Navegar a la pantalla de historial
                Log.d(TAG, "📜 Navegación a Historial");
                Intent intent = new Intent(MapsActivity.this, HistorialActivity.class);
                startActivity(intent);
                // NO llamar finish() aquí para mantener MapsActivity en el stack
                return true;
            }
            
            return false;
        });
    }

    private void setupLocationButton() {
        fabMyLocation = findViewById(R.id.fab_my_location);
        
        fabMyLocation.setOnClickListener(v -> {
            Log.d(TAG, "📍 Botón de ubicación presionado");
            if (mMap != null && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                // Activar la ubicación del usuario y mover la cámara
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15f));
            } else {
                // Solicitar permisos si no los tiene
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        
        // Obtener el ID del usuario autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            cargarRutaDesdeFirestore(currentUser.getUid());
        } else {
            Log.w(TAG, "⚠️ No hay usuario autenticado, centrando mapa en ubicación por defecto");
            // Centrar el mapa en una ubicación por defecto si no hay usuario
            LatLng ubicacionDefault = new LatLng(-34.6037, -58.3816); // Buenos Aires como ejemplo
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionDefault, 10f));
        }

        // Verificar y solicitar permiso de ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    private void cargarRutaDesdeFirestore(String userId) {
        db.collection("usuarios")
                .document(userId)
                .collection("rutas")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot rutaDoc = queryDocumentSnapshots.getDocuments().get(0);
                        Object coordenadasObj = rutaDoc.get("coordenadas");
                        
                        // Verificación segura de tipos antes del cast
                        if (coordenadasObj instanceof List<?>) {
                            @SuppressWarnings("unchecked")
                            List<List<Double>> coordenadas = (List<List<Double>>) coordenadasObj;
                            
                            if (coordenadas != null && !coordenadas.isEmpty()) {
                                mostrarRutaEnMapa(coordenadas);
                            } else {
                                Log.w(TAG, "⚠️ Lista de coordenadas está vacía.");
                                // Centrar el mapa en una ubicación por defecto (por ejemplo, tu ciudad)
                                LatLng ubicacionDefault = new LatLng(-34.6037, -58.3816); // Buenos Aires como ejemplo
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionDefault, 10f));
                            }
                        } else {
                            Log.w(TAG, "⚠️ Formato de coordenadas inválido.");
                            // Centrar el mapa en una ubicación por defecto
                            LatLng ubicacionDefault = new LatLng(-34.6037, -58.3816); // Buenos Aires como ejemplo
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionDefault, 10f));
                        }
                    } else {
                        Log.w(TAG, "⚠️ No se encontraron rutas para este usuario.");
                        // Centrar el mapa en una ubicación por defecto
                        LatLng ubicacionDefault = new LatLng(-34.6037, -58.3816); // Buenos Aires como ejemplo
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionDefault, 10f));
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "❌ Error al obtener la ruta", e);
                    // En caso de error, centrar el mapa en una ubicación por defecto
                    LatLng ubicacionDefault = new LatLng(-34.6037, -58.3816); // Buenos Aires como ejemplo
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionDefault, 10f));
                });
    }

    private void mostrarRutaEnMapa(List<List<Double>> coordenadas) {
        List<LatLng> puntosRuta = new ArrayList<>();

        for (List<Double> punto : coordenadas) {
            if (punto.size() == 2) {
                LatLng latLng = new LatLng(punto.get(0), punto.get(1));
                puntosRuta.add(latLng);
            }
        }

        if (!puntosRuta.isEmpty()) {
            PolylineOptions polylineOptions = new PolylineOptions().addAll(puntosRuta).width(8).color(0xFF4A90E2);
            mMap.addPolyline(polylineOptions);

            // Marcar inicio y fin
            mMap.addMarker(new MarkerOptions().position(puntosRuta.get(0)).title("Inicio"));
            mMap.addMarker(new MarkerOptions().position(puntosRuta.get(puntosRuta.size() - 1)).title("Destino"));

            // Centrar la cámara en el primer punto
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntosRuta.get(0), 14f));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (mMap != null) {
                        mMap.setMyLocationEnabled(true);
                    }
                }
            } else {
                Log.w(TAG, "📍 Permiso de ubicación denegado por el usuario.");
            }
        }
    }

    /**
     * 🔙 Configurar manejo moderno del botón atrás
     */
    private void setupOnBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // MapsActivity es la pantalla base - no permitir retroceder más allá
                // En lugar de llamar super.onBackPressed(), minimizar la app
                moveTaskToBack(true);
            }
        });
    }

    /**
     * 🔐 Verificar que haya una sesión activa
     */
    private void verificarSesionActiva() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // No hay sesión activa, redirigir al login
            Log.w(TAG, "❌ No hay sesión activa, redirigiendo al login");
            Intent intent = new Intent(MapsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "✅ Sesión activa verificada: " + currentUser.getEmail());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Verificar sesión cada vez que la actividad vuelve al primer plano
        verificarSesionActiva();
        
        // Asegurar que el item correcto esté seleccionado
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_inicio);
        }
    }

}

