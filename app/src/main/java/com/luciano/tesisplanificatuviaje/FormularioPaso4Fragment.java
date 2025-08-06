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

public class FormularioPaso4Fragment extends Fragment {

    // TipoP2: RadioGroups (selección única)
    private RadioGroup radioEstacion, radioTransporte, radioPresupuesto;
    private EditText edtTransporteOtro;
    
    private Button btnAnterior, btnSiguiente;
    private TextView tvTitulo, tvProgreso;

    public FormularioPaso4Fragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        android.util.Log.d("FormularioPaso4", "🔧 onCreateView iniciado");
        
        View view = inflater.inflate(R.layout.fragment_formulario_paso4, container, false);

        try {
            // Inicializar vistas
            tvTitulo = view.findViewById(R.id.tvTituloPaso4);
            tvProgreso = view.findViewById(R.id.tvProgreso);
            
            // RadioGroups (TipoP2)
            radioEstacion = view.findViewById(R.id.radioEstacion);
            radioTransporte = view.findViewById(R.id.radioTransporte);
            radioPresupuesto = view.findViewById(R.id.radioPresupuesto);
            
            // EditText para "Otro" transporte
            edtTransporteOtro = view.findViewById(R.id.edtTransporteOtro);

            // Botones de navegación
            btnAnterior = view.findViewById(R.id.btnAnterior);
            btnSiguiente = view.findViewById(R.id.btnSiguiente);

            // Configurar progreso
            if (getActivity() instanceof FormularioActivity) {
                FormularioActivity activity = (FormularioActivity) getActivity();
                tvProgreso.setText("Paso " + activity.getPasoActual() + " de " + activity.getTotalPasos());
            }

            // Configurar título
            if (tvTitulo != null) {
                tvTitulo.setText("Preferencias de Viaje");
            }

            // Configurar listener para "Otro" en transporte
            if (radioTransporte != null && edtTransporteOtro != null) {
                radioTransporte.setOnCheckedChangeListener((group, checkedId) -> {
                    if (checkedId == R.id.rbTransporteOtro) {
                        edtTransporteOtro.setVisibility(View.VISIBLE);
                    } else {
                        edtTransporteOtro.setVisibility(View.GONE);
                        edtTransporteOtro.setText("");
                    }
                });
            }

            // Verificar que todos los elementos se encontraron
            if (radioEstacion != null && radioTransporte != null && radioPresupuesto != null && 
                btnAnterior != null && btnSiguiente != null) {
                
                android.util.Log.d("FormularioPaso4", "Todas las vistas inicializadas correctamente");
                
                // Configurar listeners
                btnAnterior.setOnClickListener(v -> irAlPasoAnterior());
                btnSiguiente.setOnClickListener(v -> validarYContinuar());
                
                // Restaurar datos guardados previamente
                restaurarDatos();
                
            } else {
                android.util.Log.e("FormularioPaso4", "Error: Algunas vistas no se pudieron inicializar");
            }

        } catch (Exception e) {
            android.util.Log.e("FormularioPaso4", "Error en onCreateView: " + e.getMessage());
        }

        return view;
    }

    private void irAlPasoAnterior() {
        if (getActivity() instanceof FormularioActivity) {
            ((FormularioActivity) getActivity()).irAlPasoAnterior();
        }
    }

    private void validarYContinuar() {
        android.util.Log.d("FormularioPaso4", "Iniciando validación del Paso 4");

        // Validar estación
        int selectedEstacionId = radioEstacion.getCheckedRadioButtonId();
        if (selectedEstacionId == -1) {
            Toast.makeText(getContext(), "Por favor selecciona una estación del año", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar transporte
        int selectedTransporteId = radioTransporte.getCheckedRadioButtonId();
        if (selectedTransporteId == -1) {
            Toast.makeText(getContext(), "Por favor selecciona un medio de transporte", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar si se seleccionó "Otro" en transporte pero no se escribió nada
        if (selectedTransporteId == R.id.rbTransporteOtro) {
            String otroTransporte = edtTransporteOtro.getText().toString().trim();
            if (TextUtils.isEmpty(otroTransporte)) {
                edtTransporteOtro.setError("Especifica qué otro medio de transporte utilizas");
                edtTransporteOtro.requestFocus();
                return;
            }
        }

        // Validar presupuesto
        int selectedPresupuestoId = radioPresupuesto.getCheckedRadioButtonId();
        if (selectedPresupuestoId == -1) {
            Toast.makeText(getContext(), "Por favor selecciona un rango de presupuesto", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener valores seleccionados
        RadioButton rbEstacion = getView().findViewById(selectedEstacionId);
        RadioButton rbPresupuesto = getView().findViewById(selectedPresupuestoId);

        String estacion = rbEstacion.getText().toString();
        
        String transporte;
        if (selectedTransporteId == R.id.rbTransporteOtro) {
            transporte = edtTransporteOtro.getText().toString().trim();
        } else {
            RadioButton rbTransporte = getView().findViewById(selectedTransporteId);
            transporte = rbTransporte.getText().toString();
        }
        
        String presupuesto = rbPresupuesto.getText().toString();

        // Guardar datos
        if (getActivity() instanceof FormularioActivity) {
            Map<String, Object> datosPaso4 = new HashMap<>();
            datosPaso4.put("estacion", estacion);
            datosPaso4.put("transporte", transporte);
            datosPaso4.put("presupuesto", presupuesto);

            android.util.Log.d("FormularioPaso4", "Datos del Paso 4: " + datosPaso4.toString());

            ((FormularioActivity) getActivity()).guardarPaso("paso4", datosPaso4);
            ((FormularioActivity) getActivity()).irAlSiguientePaso();
        }
    }

    private void restaurarDatos() {
        try {
            if (getActivity() instanceof FormularioActivity) {
                FormularioActivity activity = (FormularioActivity) getActivity();
                
                // Restaurar estación
                Object estacionObj = activity.getDatoPaso("estacion");
                if (estacionObj instanceof String) {
                    String estacion = (String) estacionObj;
                    restaurarRadioSelection(radioEstacion, estacion);
                }
                
                // Restaurar transporte
                Object transporteObj = activity.getDatoPaso("transporte");
                if (transporteObj instanceof String) {
                    String transporte = (String) transporteObj;
                    
                    // Verificar si es una opción predefinida
                    boolean encontrado = restaurarRadioSelection(radioTransporte, transporte);
                    
                    if (!encontrado) {
                        // Es un valor personalizado, seleccionar "Otro" y poner el texto
                        RadioButton rbOtro = getView().findViewById(R.id.rbTransporteOtro);
                        if (rbOtro != null) {
                            rbOtro.setChecked(true);
                            edtTransporteOtro.setText(transporte);
                            edtTransporteOtro.setVisibility(View.VISIBLE);
                        }
                    }
                }
                
                // Restaurar presupuesto
                Object presupuestoObj = activity.getDatoPaso("presupuesto");
                if (presupuestoObj instanceof String) {
                    String presupuesto = (String) presupuestoObj;
                    restaurarRadioSelection(radioPresupuesto, presupuesto);
                }
                
                android.util.Log.d("FormularioPaso4", "Datos restaurados correctamente");
            }
        } catch (Exception e) {
            android.util.Log.e("FormularioPaso4", "Error al restaurar datos: " + e.getMessage());
        }
    }

    private boolean restaurarRadioSelection(RadioGroup radioGroup, String valorBuscado) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View child = radioGroup.getChildAt(i);
            if (child instanceof RadioButton) {
                RadioButton rb = (RadioButton) child;
                if (rb.getText().toString().equals(valorBuscado)) {
                    rb.setChecked(true);
                    return true;
                }
            }
        }
        return false;
    }
}

