package com.luciano.tesisplanificatuviaje.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * 🔧 Helper para manejar Google Play Services
 * Ayuda a verificar si están disponibles y manejar errores
 */
public class GooglePlayServicesHelper {
    
    private static final String TAG = "GooglePlayServicesHelper";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    
    /**
     * Verifica si Google Play Services está disponible
     * @param context Contexto de la aplicación
     * @return true si está disponible, false en caso contrario
     */
    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        
        if (resultCode != ConnectionResult.SUCCESS) {
            Log.w(TAG, "⚠️ Google Play Services no está disponible. Código: " + resultCode);
            return false;
        }
        
        Log.d(TAG, "✅ Google Play Services está disponible");
        return true;
    }
    
    /**
     * Verifica y trata de resolver problemas con Google Play Services
     * @param activity Activity desde donde se llama
     * @return true si está disponible o se puede resolver, false en caso contrario
     */
    public static boolean checkAndResolvePlayServices(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        
        if (resultCode != ConnectionResult.SUCCESS) {
            Log.w(TAG, "⚠️ Google Play Services problema detectado. Código: " + resultCode);
            
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                Log.i(TAG, "🔄 Intentando resolver problema de Google Play Services");
                googleApiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
                return false;
            } else {
                Log.e(TAG, "❌ Este dispositivo no es compatible con Google Play Services");
                return false;
            }
        }
        
        Log.d(TAG, "✅ Google Play Services está disponible y funcional");
        return true;
    }
    
    /**
     * Obtiene un mensaje descriptivo del error de Google Play Services
     * @param context Contexto de la aplicación
     * @return Mensaje descriptivo del estado
     */
    public static String getPlayServicesStatusMessage(Context context) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context);
        
        switch (resultCode) {
            case ConnectionResult.SUCCESS:
                return "✅ Google Play Services está disponible";
            case ConnectionResult.SERVICE_MISSING:
                return "❌ Google Play Services no está instalado";
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                return "🔄 Google Play Services necesita actualización";
            case ConnectionResult.SERVICE_DISABLED:
                return "⚠️ Google Play Services está deshabilitado";
            case ConnectionResult.SERVICE_INVALID:
                return "❌ Google Play Services no es válido";
            default:
                return "⚠️ Error desconocido con Google Play Services (código: " + resultCode + ")";
        }
    }
}
