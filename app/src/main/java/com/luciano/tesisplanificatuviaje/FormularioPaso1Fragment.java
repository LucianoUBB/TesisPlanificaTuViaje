package com.luciano.tesisplanificatuviaje;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.HashMap;
import java.util.Map;

public class FormularioPaso1Fragment extends Fragment {
    
    private EditText edtNombreUsuario, edtEdad;
    private RadioGroup radioSexo;
    private Button btnSiguiente;
    private TextView tvTitulo, tvProgreso;
    
    public FormularioPaso1Fragment() {}
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        android.util.Log.d("FormularioPaso1", "onCreateView iniciado");
        
        View view = inflater.inflate(R.layout.fragment_formulario_paso1, container, false);
        
        try {
            // Inicializar vistas
            tvTitulo = view.findViewById(R.id.tvTituloPaso1);
            tvProgreso = view.findViewById(R.id.tvProgreso);
            edtNombreUsuario = view.findViewById(R.id.edtNombreUsuario);
            edtEdad = view.findViewById(R.id.edtEdad);
            radioSexo = view.findViewById(R.id.radioSexo);
            btnSiguiente = view.findViewById(R.id.btnSiguiente);
            
            // Configurar progreso
            if (getActivity() instanceof FormularioActivity) {
                FormularioActivity activity = (FormularioActivity) getActivity();
                tvProgreso.setText("Paso " + activity.getPasoActual() + " de " + activity.getTotalPasos());
            }
            
            // Configurar título
            if (tvTitulo != null) {
                tvTitulo.setText("Información Personal");
            }
            
            // Verificar que todos los elementos se encontraron
            if (edtNombreUsuario != null && edtEdad != null && radioSexo != null && btnSiguiente != null) {
                android.util.Log.d("FormularioPaso1", "Todas las vistas inicializadas correctamente");
                
                // Configurar listeners
                btnSiguiente.setOnClickListener(v -> validarYContinuar());
                
                // Verificar si está en modo edición y configurar campos
                configurarModoEdicion();
                
                // Restaurar datos guardados previamente
                restaurarDatos();
                
            } else {
                android.util.Log.e("FormularioPaso1", "Error: Algunas vistas no se pudieron inicializar");
            }
        } catch (Exception e) {
            android.util.Log.e("FormularioPaso1", "Error en onCreateView: " + e.getMessage());
        }
        
        return view;
    }
    
    private void validarYContinuar() {
        android.util.Log.d("FormularioPaso1", "Iniciando validación del Paso 1");
        
        // Validar nombre de usuario
        String nombreUsuario = edtNombreUsuario.getText().toString().trim();
        if (TextUtils.isEmpty(nombreUsuario)) {
            edtNombreUsuario.setError("El nombre es requerido");
            edtNombreUsuario.requestFocus();
            return;
        }
        
        int edad;
        
        // Verificar si el campo edad está habilitado (registro nuevo) o deshabilitado (edición)
        if (edtEdad.isEnabled()) {
            // Campo habilitado: validar edad normalmente
            String edadTexto = edtEdad.getText().toString().trim();
            if (TextUtils.isEmpty(edadTexto)) {
                edtEdad.setError("La edad es requerida");
                edtEdad.requestFocus();
                return;
            }
            
            try {
                edad = Integer.parseInt(edadTexto);
                if (edad < 16 || edad > 100) {
                    edtEdad.setError("Edad debe estar entre 16 y 100 años");
                    edtEdad.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                edtEdad.setError("Ingresa una edad válida");
                edtEdad.requestFocus();
                return;
            }
        } else {
            // Campo deshabilitado: usar edad existente de los datos guardados
            if (getActivity() instanceof FormularioActivity) {
                FormularioActivity activity = (FormularioActivity) getActivity();
                Object edadObj = activity.getDatoPaso("edad");
                if (edadObj instanceof Integer) {
                    edad = (Integer) edadObj;
                    android.util.Log.d("FormularioPaso1", "Usando edad existente (no editable): " + edad);
                } else {
                    // Error: no hay edad guardada pero el campo está deshabilitado
                    android.util.Log.e("FormularioPaso1", "Error: Campo edad deshabilitado pero no hay datos guardados");
                    Toast.makeText(getContext(), "Error en la configuración de edad", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                android.util.Log.e("FormularioPaso1", "Error: No se pudo acceder a FormularioActivity");
                return;
            }
        }
        
        // Validar sexo
        int selectedSexoId = radioSexo.getCheckedRadioButtonId();
        if (selectedSexoId == -1) {
            Toast.makeText(getContext(), "Por favor selecciona tu sexo", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Obtener valor seleccionado
        RadioButton rbSexo = getView().findViewById(selectedSexoId);
        String sexo = rbSexo.getText().toString();
        
        // Guardar datos
        if (getActivity() instanceof FormularioActivity) {
            Map<String, Object> datosPaso1 = new HashMap<>();
            datosPaso1.put("nombreUsuario", nombreUsuario);
            datosPaso1.put("edad", edad);
            datosPaso1.put("sexo", sexo);
            
            // Si es la primera vez que se establece la edad, marcarla como bloqueada
            if (edtEdad.isEnabled()) {
                datosPaso1.put("edadBloqueada", true);
                android.util.Log.d("FormularioPaso1", "Marcando edad como bloqueada permanentemente");
            }
            
            android.util.Log.d("FormularioPaso1", "Datos del Paso 1: " + datosPaso1.toString());
            
            ((FormularioActivity) getActivity()).guardarPaso("paso1", datosPaso1);
            ((FormularioActivity) getActivity()).irAlSiguientePaso();
        }
    }

    private void restaurarDatos() {
        try {
            if (getActivity() instanceof FormularioActivity) {
                FormularioActivity activity = (FormularioActivity) getActivity();
                
                // Restaurar nombre de usuario
                Object nombreObj = activity.getDatoPaso("nombreUsuario");
                if (nombreObj instanceof String) {
                    edtNombreUsuario.setText((String) nombreObj);
                }
                
                // Restaurar edad
                Object edadObj = activity.getDatoPaso("edad");
                if (edadObj instanceof Integer) {
                    edtEdad.setText(String.valueOf(edadObj));
                }
                
                // Restaurar sexo
                Object sexoObj = activity.getDatoPaso("sexo");
                if (sexoObj instanceof String) {
                    String sexo = (String) sexoObj;
                    
                    // Buscar el RadioButton que corresponde al sexo guardado
                    for (int i = 0; i < radioSexo.getChildCount(); i++) {
                        View child = radioSexo.getChildAt(i);
                        if (child instanceof RadioButton) {
                            RadioButton rb = (RadioButton) child;
                            if (rb.getText().toString().equals(sexo)) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }
                }
                
                android.util.Log.d("FormularioPaso1", "Datos restaurados correctamente");
            }
        } catch (Exception e) {
            android.util.Log.e("FormularioPaso1", "Error al restaurar datos: " + e.getMessage());
        }
    }

    private void configurarModoEdicion() {
        try {
            if (getActivity() instanceof FormularioActivity) {
                FormularioActivity activity = (FormularioActivity) getActivity();
                
                // Verificar si la edad ya fue establecida permanentemente
                verificarEdadBloqueada(activity);
            }
        } catch (Exception e) {
            android.util.Log.e("FormularioPaso1", "Error configurando modo edición: " + e.getMessage());
        }
    }
    
    private void verificarEdadBloqueada(FormularioActivity activity) {
        // Primero verificar en datos locales
        Object edadBloqueadaObj = activity.getDatoPaso("edadBloqueada");
        Object edadObj = activity.getDatoPaso("edad");
        
        if ((edadBloqueadaObj instanceof Boolean && (Boolean) edadBloqueadaObj) || 
            (edadObj instanceof Integer)) {
            // La edad ya está bloqueada permanentemente
            bloquearCampoEdad();
            return;
        }
        
        // Verificar en Firebase si la edad ya fue establecida
        verificarEdadEnFirebase();
    }
    
    private void verificarEdadEnFirebase() {
        // Verificar en Firebase si el usuario ya tiene edad establecida
        if (getActivity() instanceof FormularioActivity) {
            FormularioActivity activity = (FormularioActivity) getActivity();
            
            // Obtener referencia al usuario actual
            com.google.firebase.auth.FirebaseUser currentUser = 
                com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
            
            if (currentUser != null) {
                String userId = currentUser.getUid();
                
                com.google.firebase.firestore.FirebaseFirestore.getInstance()
                    .collection("usuarios")
                    .document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Verificar si ya tiene edad establecida
                            Long edadFirebase = documentSnapshot.getLong("edad");
                            Boolean edadBloqueada = documentSnapshot.getBoolean("edadBloqueada");
                            
                            if (edadFirebase != null || (edadBloqueada != null && edadBloqueada)) {
                                // La edad ya existe en Firebase, bloquear permanentemente
                                android.util.Log.d("FormularioPaso1", "Edad encontrada en Firebase - Bloqueando permanentemente");
                                bloquearCampoEdad();
                                
                                // Guardar en datos locales para futuras verificaciones
                                Map<String, Object> datosBloqueo = new HashMap<>();
                                if (edadFirebase != null) {
                                    datosBloqueo.put("edad", edadFirebase.intValue());
                                }
                                datosBloqueo.put("edadBloqueada", true);
                                activity.guardarPaso("edadInfo", datosBloqueo);
                            } else {
                                // Primera vez, edad editable
                                android.util.Log.d("FormularioPaso1", "Primera vez - Edad editable");
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        android.util.Log.e("FormularioPaso1", "Error verificando edad en Firebase: " + e.getMessage());
                        // En caso de error, permitir edición (fail-safe)
                    });
            }
        }
    }
    
    private void bloquearCampoEdad() {
        if (edtEdad != null) {
            android.util.Log.d("FormularioPaso1", "Bloqueando campo edad permanentemente");
            
            // Deshabilitar completamente el campo
            edtEdad.setEnabled(false);
            edtEdad.setFocusable(false);
            edtEdad.setClickable(false);
            
            // Cambiar apariencia para indicar que está bloqueado
            edtEdad.setAlpha(0.5f);
            edtEdad.setHint("Edad establecida permanentemente");
            
            // Opcional: cambiar color de fondo
            edtEdad.setBackgroundColor(0xFFE0E0E0); // Gris claro
        }
    }
}

