package com.luciano.tesisplanificatuviaje;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormularioPaso5Fragment extends Fragment {

    // TipoP2: RadioGroup para planificación
    private RadioGroup radioPlanificacion;

    // TipoP1: CheckBoxes para deportes
    private CheckBox cbDeportesFutbol, cbDeportesBasket, cbDeportesTenis, cbDeportesNatacion, 
                     cbDeportesVoley, cbDeportesCorrer, cbDeportesCiclismo, cbDeportesSenderismo,
                     cbDeportesOtro, cbDeportesNinguno;
    private EditText edtDeportesOtro;

    private Button btnAnterior, btnSiguiente;
    private TextView tvTitulo, tvProgreso;

    public FormularioPaso5Fragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        android.util.Log.d("FormularioPaso5", "🔧 onCreateView iniciado");
        
        View view = inflater.inflate(R.layout.fragment_formulario_paso5, container, false);

        try {
            // Inicializar vistas
            tvTitulo = view.findViewById(R.id.tvTituloPaso5);
            tvProgreso = view.findViewById(R.id.tvProgreso);
            
            // RadioGroup para planificación
            radioPlanificacion = view.findViewById(R.id.radioPlanificacion);

            // CheckBoxes para deportes
            cbDeportesFutbol = view.findViewById(R.id.cbFutbol);
            cbDeportesNatacion = view.findViewById(R.id.cbNatacion);
            cbDeportesCorrer = view.findViewById(R.id.cbCorrer);
            cbDeportesCiclismo = view.findViewById(R.id.cbCiclismo);
            cbDeportesBasket = view.findViewById(R.id.cbBasket);
            cbDeportesTenis = view.findViewById(R.id.cbTenis);
            cbDeportesVoley = view.findViewById(R.id.cbGimnasio);
            cbDeportesSenderismo = view.findViewById(R.id.cbSenderismo);
            cbDeportesOtro = view.findViewById(R.id.cbDeportesOtro);
            edtDeportesOtro = view.findViewById(R.id.edtDeportesOtro);
            cbDeportesNinguno = view.findViewById(R.id.cbNinguno);
            cbDeportesNinguno = view.findViewById(R.id.cbNinguno);

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
                tvTitulo.setText("Planificación y Deportes");
            }

            // Verificar que todos los elementos se encontraron
            if (radioPlanificacion != null && cbDeportesFutbol != null && 
                btnAnterior != null && btnSiguiente != null) {

                android.util.Log.d("FormularioPaso5", "Todas las vistas inicializadas correctamente");

                // Configurar listeners
                configurarListenersDeportes();
                btnAnterior.setOnClickListener(v -> irAlPasoAnterior());
                btnSiguiente.setOnClickListener(v -> validarYContinuar());
                
                // Restaurar datos guardados previamente
                restaurarDatos();
                
            } else {
                android.util.Log.e("FormularioPaso5", "Error: Algunas vistas no se pudieron inicializar");
            }

        } catch (Exception e) {
            android.util.Log.e("FormularioPaso5", "Error en onCreateView: " + e.getMessage());
        }

        return view;
    }

    private void configurarListenersDeportes() {
        try {
            // Cuando se selecciona "Ninguno", deseleccionar otros deportes
            cbDeportesNinguno.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    cbDeportesFutbol.setChecked(false);
                    cbDeportesBasket.setChecked(false);
                    cbDeportesTenis.setChecked(false);
                    cbDeportesNatacion.setChecked(false);
                    cbDeportesVoley.setChecked(false);
                    cbDeportesCorrer.setChecked(false);
                    cbDeportesCiclismo.setChecked(false);
                    cbDeportesSenderismo.setChecked(false);
                    cbDeportesOtro.setChecked(false);
                    edtDeportesOtro.setVisibility(View.GONE);
                    edtDeportesOtro.setText("");
                }
            });

            // Configurar comportamiento del campo "Otro"
            cbDeportesOtro.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    cbDeportesNinguno.setChecked(false);
                    edtDeportesOtro.setVisibility(View.VISIBLE);
                } else {
                    edtDeportesOtro.setVisibility(View.GONE);
                    edtDeportesOtro.setText("");
                }
            });

            // Cuando se selecciona cualquier deporte, deseleccionar "Ninguno"
            View.OnClickListener deselectNinguno = v -> {
                if (((CheckBox) v).isChecked()) {
                    cbDeportesNinguno.setChecked(false);
                }
            };

            cbDeportesFutbol.setOnClickListener(deselectNinguno);
            cbDeportesBasket.setOnClickListener(deselectNinguno);
            cbDeportesTenis.setOnClickListener(deselectNinguno);
            cbDeportesNatacion.setOnClickListener(deselectNinguno);
            cbDeportesVoley.setOnClickListener(deselectNinguno);
            cbDeportesCorrer.setOnClickListener(deselectNinguno);
            cbDeportesCiclismo.setOnClickListener(deselectNinguno);
            cbDeportesSenderismo.setOnClickListener(deselectNinguno);
            cbDeportesOtro.setOnClickListener(deselectNinguno);
            
            android.util.Log.d("FormularioPaso5", "Listeners de deportes configurados");
            
        } catch (Exception e) {
            android.util.Log.e("FormularioPaso5", "Error configurando listeners: " + e.getMessage());
        }
    }

    private void irAlPasoAnterior() {
        if (getActivity() instanceof FormularioActivity) {
            ((FormularioActivity) getActivity()).irAlPasoAnterior();
        }
    }

    private void validarYContinuar() {
        android.util.Log.d("FormularioPaso5", "Iniciando validación del Paso 5");

        // Validar planificación (TipoP2 - selección única obligatoria)
        int selectedPlanificacionId = radioPlanificacion.getCheckedRadioButtonId();
        if (selectedPlanificacionId == -1) {
            Toast.makeText(getContext(), "Por favor selecciona tu estilo de planificación", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar deportes (TipoP1 - al menos uno debe estar seleccionado)
        // Validar si se seleccionó "Otro" pero no se escribió nada
        if (cbDeportesOtro.isChecked()) {
            String otroDeporte = edtDeportesOtro.getText().toString().trim();
            if (TextUtils.isEmpty(otroDeporte)) {
                edtDeportesOtro.setError("Especifica qué otro deporte practicas");
                edtDeportesOtro.requestFocus();
                return;
            }
        }

        List<String> deportesSeleccionados = obtenerDeportesSeleccionados();
        if (deportesSeleccionados.isEmpty()) {
            Toast.makeText(getContext(), "Por favor selecciona al menos una opción de deportes", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener valor seleccionado de planificación
        RadioButton rbPlanificacion = getView().findViewById(selectedPlanificacionId);
        String planificacion = rbPlanificacion.getText().toString();

        // Guardar datos
        if (getActivity() instanceof FormularioActivity) {
            Map<String, Object> datosPaso5 = new HashMap<>();
            datosPaso5.put("planificacion", planificacion);
            datosPaso5.put("deportes", deportesSeleccionados);

            android.util.Log.d("FormularioPaso5", "Datos del Paso 5: " + datosPaso5.toString());

            ((FormularioActivity) getActivity()).guardarPaso("paso5", datosPaso5);
            ((FormularioActivity) getActivity()).irAlSiguientePaso();
        }
    }

    private List<String> obtenerDeportesSeleccionados() {
        List<String> deportes = new ArrayList<>();
        
        if (cbDeportesNinguno.isChecked()) {
            deportes.add("Ninguno");
        } else {
            if (cbDeportesFutbol.isChecked()) deportes.add("Fútbol");
            if (cbDeportesBasket.isChecked()) deportes.add("Básquet");
            if (cbDeportesTenis.isChecked()) deportes.add("Tenis");
            if (cbDeportesNatacion.isChecked()) deportes.add("Natación");
            if (cbDeportesVoley.isChecked()) deportes.add("Voley");
            if (cbDeportesCorrer.isChecked()) deportes.add("Correr");
            if (cbDeportesCiclismo.isChecked()) deportes.add("Ciclismo");
            if (cbDeportesSenderismo.isChecked()) deportes.add("Senderismo");

            if (cbDeportesOtro.isChecked()) {
                String otroDeporte = edtDeportesOtro.getText().toString().trim();
                if (!TextUtils.isEmpty(otroDeporte)) {
                    deportes.add(otroDeporte);
                }
            }
        }
        
        return deportes;
    }

    private void restaurarDatos() {
        try {
            if (getActivity() instanceof FormularioActivity) {
                FormularioActivity activity = (FormularioActivity) getActivity();
                
                // Restaurar planificación
                Object planificacionObj = activity.getDatoPaso("planificacion");
                if (planificacionObj instanceof String) {
                    String planificacion = (String) planificacionObj;
                    
                    // Buscar el RadioButton que corresponde a la planificación guardada
                    for (int i = 0; i < radioPlanificacion.getChildCount(); i++) {
                        View child = radioPlanificacion.getChildAt(i);
                        if (child instanceof RadioButton) {
                            RadioButton rb = (RadioButton) child;
                            if (rb.getText().toString().equals(planificacion)) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }
                }
                
                // Restaurar deportes
                Object deportesObj = activity.getDatoPaso("deportes");
                if (deportesObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<String> deportes = (List<String>) deportesObj;
                    
                    for (String deporte : deportes) {
                        switch (deporte) {
                            case "Ninguno":
                                cbDeportesNinguno.setChecked(true);
                                break;
                            case "Fútbol":
                                cbDeportesFutbol.setChecked(true);
                                break;
                            case "Básquet":
                                cbDeportesBasket.setChecked(true);
                                break;
                            case "Tenis":
                                cbDeportesTenis.setChecked(true);
                                break;
                            case "Natación":
                                cbDeportesNatacion.setChecked(true);
                                break;
                            case "Voley":
                                cbDeportesVoley.setChecked(true);
                                break;
                            case "Correr":
                                cbDeportesCorrer.setChecked(true);
                                break;
                            case "Ciclismo":
                                cbDeportesCiclismo.setChecked(true);
                                break;
                            case "Senderismo":
                                cbDeportesSenderismo.setChecked(true);
                                break;
                            default:
                                // Si no es una opción predefinida, es un valor personalizado
                                cbDeportesOtro.setChecked(true);
                                edtDeportesOtro.setText(deporte);
                                edtDeportesOtro.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }

                android.util.Log.d("FormularioPaso5", "Datos restaurados correctamente");
            }
        } catch (Exception e) {
            android.util.Log.e("FormularioPaso5", "Error al restaurar datos: " + e.getMessage());
        }
    }
}
