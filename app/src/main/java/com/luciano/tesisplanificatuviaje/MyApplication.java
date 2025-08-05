package com.luciano.tesisplanificatuviaje;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class MyApplication extends Application {
    
    private static final String TAG = "MyApplication";
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        Log.d(TAG, "🚀 Inicializando aplicación");
        
        // Verificar Google Play Services
        checkGooglePlayServices();
        
        // Verificar conectividad
        if (isNetworkAvailable()) {
            Log.d(TAG, "🌐 Conexión a internet disponible");
        } else {
            Log.w(TAG, "⚠️ No hay conexión a internet disponible");
        }
        
        try {
            // Inicializar Firebase
            FirebaseApp.initializeApp(this);
            Log.d(TAG, "✅ Firebase inicializado correctamente");
            
            // Configurar Firestore con configuraciones mejoradas
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true) // Habilitar persistencia offline
                    .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED) // Cache ilimitado
                    .build();
            firestore.setFirestoreSettings(settings);
            
            Log.d(TAG, "✅ Firestore configurado correctamente con persistencia offline");
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Error al inicializar Firebase", e);
        }
    }
    
    private void checkGooglePlayServices() {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        
        switch (resultCode) {
            case ConnectionResult.SUCCESS:
                Log.d(TAG, "✅ Google Play Services está disponible y actualizado");
                break;
            case ConnectionResult.SERVICE_MISSING:
                Log.w(TAG, "⚠️ Google Play Services no está instalado");
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                Log.w(TAG, "⚠️ Google Play Services necesita actualización");
                break;
            case ConnectionResult.SERVICE_DISABLED:
                Log.w(TAG, "⚠️ Google Play Services está deshabilitado");
                break;
            default:
                Log.w(TAG, "⚠️ Google Play Services error: " + resultCode);
                break;
        }
    }
    
    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            Log.e(TAG, "Error al verificar conectividad", e);
            return false;
        }
    }
}
