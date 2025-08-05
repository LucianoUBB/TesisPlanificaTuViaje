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

public class FormularioPaso3Fragment extends Fragment {

    //CheckBoxes para vacaciones
    private CheckBox cbVacacionesPlaya, cbVacacionesMontana, cbVacacionesCiudad, cbVacacionesCampo, 
                     cbVacacionesCamping, cbVacacionesOtro;
    private EditText edtVacacionesOtro;

    //CheckBoxes para actividades
    private CheckBox cbActividadesCulturales, cbActividadesAventura, cbActividadesRelax, 
                     cbActividadesGastronomia, cbActividadesBailar, cbActividadesCocinar, 
                     cbActividadesOtro;
    private EditText edtActividadesOtro;

    private Button btnAnterior, btnSiguiente;
    private TextView tvTitulo, tvProgreso;

    public FormularioPaso3Fragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        android.util.Log.d("FormularioPaso3", "🔧 onCreateView iniciado");
        
        View view = inflater.inflate(R.layout.fragment_formulario_paso3, container, false);

        try {
            // Inicializar vistas
            tvTitulo = view.findViewById(R.id.tvTituloPaso3);
            tvProgreso = view.findViewById(R.id.tvProgreso);
            
            // CheckBoxes para vacaciones 
            cbVacacionesPlaya = view.findViewById(R.id.cbPlaya);
            cbVacacionesMontana = view.findViewById(R.id.cbMontana);
            cbVacacionesCiudad = view.findViewById(R.id.cbCiudad);
            cbVacacionesCampo = view.findViewById(R.id.cbCampo);
            cbVacacionesCamping = view.findViewById(R.id.cbCamping);
            cbVacacionesOtro = view.findViewById(R.id.cbVacacionesOtro);
            edtVacacionesOtro = view.findViewById(R.id.edtVacacionesOtro);

            // CheckBoxes para actividades 
            cbActividadesCulturales = view.findViewById(R.id.cbDescansar);
            cbActividadesAventura = view.findViewById(R.id.cbCaminar);
            cbActividadesRelax = view.findViewById(R.id.cbLeer);
            cbActividadesGastronomia = view.findViewById(R.id.cbNadar);
            cbActividadesBailar = view.findViewById(R.id.cbBailar);
            cbActividadesCocinar = view.findViewById(R.id.cbCocinar);
            cbActividadesOtro = view.findViewById(R.id.cbActividadesOtro);
            edtActividadesOtro = view.findViewById(R.id.edtActividadesOtro);

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
                tvTitulo.setText("Vacaciones y Actividades");
            }

            // Verificar que todos los elementos se encontraron
            if (cbVacacionesPlaya != null && cbActividadesCulturales != null && btnAnterior != null && btnSiguiente != null) {
                android.util.Log.d("FormularioPaso3", "✅ Todas las vistas inicializadas correctamente");
                
                // Configurar listeners
                configurarListeners();
                btnAnterior.setOnClickListener(v -> irAlPasoAnterior());
                btnSiguiente.setOnClickListener(v -> validarYContinuar());
                
                // Restaurar datos guardados previamente
                restaurarDatos();
                
            } else {
                android.util.Log.e("FormularioPaso3", "❌ Error: Algunas vistas no se pudieron inicializar");
            }

        } catch (Exception e) {
            android.util.Log.e("FormularioPaso3", "❌ Error en onCreateView: " + e.getMessage());
        }

        return view;
    }

    private void configurarListeners() {
        try {
            // Configurar comportamiento del campo "Otro" para vacaciones
            cbVacacionesOtro.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    edtVacacionesOtro.setVisibility(View.VISIBLE);
                } else {
                    edtVacacionesOtro.setVisibility(View.GONE);
                    edtVacacionesOtro.setText("");
                }
            });

            // Configurar comportamiento del campo "Otro" para actividades
            cbActividadesOtro.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    edtActividadesOtro.setVisibility(View.VISIBLE);
                } else {
                    edtActividadesOtro.setVisibility(View.GONE);
                    edtActividadesOtro.setText("");
                }
            });
            
            android.util.Log.d("FormularioPaso3", "✅ Listeners configurados");
            
        } catch (Exception e) {
            android.util.Log.e("FormularioPaso3", "❌ Error configurando listeners: " + e.getMessage());
        }
    }

    private void irAlPasoAnterior() {
        if (getActivity() instanceof FormularioActivity) {
            ((FormularioActivity) getActivity()).irAlPasoAnterior();
        }
    }

    private void validarYContinuar() {
        android.util.Log.d("FormularioPaso3", "🔄 Iniciando validación del Paso 3");

        // Validar si se seleccionó "Otro" en vacaciones pero no se escribió nada
        if (cbVacacionesOtro.isChecked()) {
            String otroVacacion = edtVacacionesOtro.getText().toString().trim();
            if (TextUtils.isEmpty(otroVacacion)) {
                edtVacacionesOtro.setError("Especifica qué otro tipo de vacaciones prefieres");
                edtVacacionesOtro.requestFocus();
                return;
            }
        }

        // Validar si se seleccionó "Otro" en actividades pero no se escribió nada
        if (cbActividadesOtro.isChecked()) {
            String otroActividad = edtActividadesOtro.getText().toString().trim();
            if (TextUtils.isEmpty(otroActividad)) {
                edtActividadesOtro.setError("Especifica qué otra actividad prefieres");
                edtActividadesOtro.requestFocus();
                return;
            }
        }

        // Validar vacaciones
        List<String> vacacionesSeleccionadas = obtenerVacacionesSeleccionadas();
        if (vacacionesSeleccionadas.isEmpty()) {
            Toast.makeText(getContext(), "Por favor selecciona al menos un tipo de vacaciones", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar actividades
        List<String> actividadesSeleccionadas = obtenerActividadesSeleccionadas();
        if (actividadesSeleccionadas.isEmpty()) {
            Toast.makeText(getContext(), "Por favor selecciona al menos un tipo de actividad", Toast.LENGTH_SHORT).show();
            return;
        }

        // Guardar datos
        if (getActivity() instanceof FormularioActivity) {
            Map<String, Object> datosPaso3 = new HashMap<>();
            datosPaso3.put("vacaciones", vacacionesSeleccionadas);
            datosPaso3.put("actividades", actividadesSeleccionadas);

            android.util.Log.d("FormularioPaso3", "🔍 Datos del Paso 3: " + datosPaso3.toString());

            ((FormularioActivity) getActivity()).guardarPaso("paso3", datosPaso3);
            ((FormularioActivity) getActivity()).irAlSiguientePaso();
        }
    }

    private List<String> obtenerVacacionesSeleccionadas() {
        List<String> vacaciones = new ArrayList<>();
        
        if (cbVacacionesPlaya.isChecked()) vacaciones.add("Playa");
        if (cbVacacionesMontana.isChecked()) vacaciones.add("Montaña");
        if (cbVacacionesCiudad.isChecked()) vacaciones.add("Ciudad");
        if (cbVacacionesCampo.isChecked()) vacaciones.add("Campo");
        if (cbVacacionesCamping.isChecked()) vacaciones.add("Camping");
        
        if (cbVacacionesOtro.isChecked()) {
            String otroVacacion = edtVacacionesOtro.getText().toString().trim();
            if (!TextUtils.isEmpty(otroVacacion)) {
                vacaciones.add(otroVacacion);
            }
        }
        
        return vacaciones;
    }

    private List<String> obtenerActividadesSeleccionadas() {
        List<String> actividades = new ArrayList<>();
        
        if (cbActividadesCulturales.isChecked()) actividades.add("Descansar");
        if (cbActividadesAventura.isChecked()) actividades.add("Caminar");
        if (cbActividadesRelax.isChecked()) actividades.add("Leer");
        if (cbActividadesGastronomia.isChecked()) actividades.add("Nadar");
        if (cbActividadesBailar.isChecked()) actividades.add("Bailar");
        if (cbActividadesCocinar.isChecked()) actividades.add("Cocinar");
        
        if (cbActividadesOtro.isChecked()) {
            String otroActividad = edtActividadesOtro.getText().toString().trim();
            if (!TextUtils.isEmpty(otroActividad)) {
                actividades.add(otroActividad);
            }
        }
        
        return actividades;
    }

    private void restaurarDatos() {
        try {
            if (getActivity() instanceof FormularioActivity) {
                FormularioActivity activity = (FormularioActivity) getActivity();
                
                // Restaurar vacaciones
                Object vacacionesObj = activity.getDatoPaso("vacaciones");
                if (vacacionesObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<String> vacaciones = (List<String>) vacacionesObj;
                    
                    for (String vacacion : vacaciones) {
                        switch (vacacion) {
                            case "Playa":
                                cbVacacionesPlaya.setChecked(true);
                                break;
                            case "Montaña":
                                cbVacacionesMontana.setChecked(true);
                                break;
                            case "Ciudad":
                                cbVacacionesCiudad.setChecked(true);
                                break;
                            case "Campo":
                                cbVacacionesCampo.setChecked(true);
                                break;
                            case "Camping":
                                cbVacacionesCamping.setChecked(true);
                                break;
                            default:
                                // Si no es una opción predefinida, es un valor personalizado
                                cbVacacionesOtro.setChecked(true);
                                edtVacacionesOtro.setText(vacacion);
                                edtVacacionesOtro.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }
                
                // Restaurar actividades
                Object actividadesObj = activity.getDatoPaso("actividades");
                if (actividadesObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<String> actividades = (List<String>) actividadesObj;
                    
                    for (String actividad : actividades) {
                        switch (actividad) {
                            case "Descansar":
                                cbActividadesCulturales.setChecked(true);
                                break;
                            case "Caminar":
                                cbActividadesAventura.setChecked(true);
                                break;
                            case "Leer":
                                cbActividadesRelax.setChecked(true);
                                break;
                            case "Nadar":
                                cbActividadesGastronomia.setChecked(true);
                                break;
                            case "Bailar":
                                cbActividadesBailar.setChecked(true);
                                break;
                            case "Cocinar":
                                cbActividadesCocinar.setChecked(true);
                                break;
                            default:
                                // Si no es una opción predefinida, es un valor personalizado
                                cbActividadesOtro.setChecked(true);
                                edtActividadesOtro.setText(actividad);
                                edtActividadesOtro.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }
                
                android.util.Log.d("FormularioPaso3", "Datos restaurados correctamente");
            }
        } catch (Exception e) {
            android.util.Log.e("FormularioPaso3", "Error al restaurar datos: " + e.getMessage());
        }
    }
}
