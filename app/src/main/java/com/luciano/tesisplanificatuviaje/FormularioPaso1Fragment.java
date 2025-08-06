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
        android.util.Log.d("FormularioPaso1", "🔧 onCreateView iniciado");
        
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
        android.util.Log.d("FormularioPaso1", "🔄 Iniciando validación del Paso 1");
        
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
            
            android.util.Log.d("FormularioPaso1", "📋 Datos del Paso 1: " + datosPaso1.toString());
            
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
                
                // Verificar si hay datos de edad ya guardados (modo edición)
                Object edadObj = activity.getDatoPaso("edad");
                
                if (edadObj instanceof Integer) {
                    // Hay edad guardada, está en modo edición
                    android.util.Log.d("FormularioPaso1", "Modo edición detectado - Bloqueando edición de edad");
                    
                    // Deshabilitar la edición del campo edad
                    edtEdad.setEnabled(false);
                    edtEdad.setFocusable(false);
                    edtEdad.setClickable(false);
                    
                    // Cambiar el color de fondo para indicar que no es editable
                    edtEdad.setAlpha(0.6f);
                    
                    // Agregar hint explicativo
                    edtEdad.setHint("Edad no editable");
                    
                    android.util.Log.d("FormularioPaso1", "Campo edad bloqueado para edición");
                } else {
                    // No hay edad guardada, es registro nuevo
                    android.util.Log.d("FormularioPaso1", "Registro nuevo - Edad editable");
                }
            }
        } catch (Exception e) {
            android.util.Log.e("FormularioPaso1", "Error configurando modo edición: " + e.getMessage());
        }
    }
}

