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

public class FormularioPaso2Fragment extends Fragment {

    // TipoP1: CheckBoxes para sabores (múltiple selección)
    private CheckBox cbDulce, cbSalado, cbPicante, cbAmargo, cbAgrio, cbSaboresOtro;
    private EditText edtSaboresOtro;

    // TipoP2: RadioGroup para clima (única selección)
    private RadioGroup radioClima;

    private Button btnAnterior, btnSiguiente;
    private TextView tvTitulo, tvProgreso;

    public FormularioPaso2Fragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        android.util.Log.d("FormularioPaso2", "🔧 onCreateView iniciado");

        View view = inflater.inflate(R.layout.fragment_formulario_paso2, container, false);

        try {
            // Inicializar vistas
            tvTitulo = view.findViewById(R.id.tvTituloPaso2);
            tvProgreso = view.findViewById(R.id.tvProgreso);

            // CheckBoxes para sabores (TipoP1)
            cbDulce = view.findViewById(R.id.cbDulce);
            cbSalado = view.findViewById(R.id.cbSalado);
            cbPicante = view.findViewById(R.id.cbPicante);
            cbAmargo = view.findViewById(R.id.cbAmargo);
            cbAgrio = view.findViewById(R.id.cbAgrio);
            cbSaboresOtro = view.findViewById(R.id.cbSaboresOtro);
            edtSaboresOtro = view.findViewById(R.id.edtSaboresOtro);

            // RadioGroup para clima (TipoP2)
            radioClima = view.findViewById(R.id.radioClima);

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
                tvTitulo.setText("Preferencias de Sabores y Clima");
            }

            // Configurar listener para "Otro" en sabores
            if (cbSaboresOtro != null && edtSaboresOtro != null) {
                cbSaboresOtro.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    edtSaboresOtro.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                    if (!isChecked) {
                        edtSaboresOtro.setText("");
                    }
                });
            }

            // Configurar listeners de navegación
            if (btnAnterior != null) {
                btnAnterior.setOnClickListener(v -> {
                    if (getActivity() instanceof FormularioActivity) {
                        ((FormularioActivity) getActivity()).irAlPasoAnterior();
                    }
                });
            }

            if (btnSiguiente != null) {
                btnSiguiente.setOnClickListener(v -> validarYContinuar());
            }

            // Restaurar datos guardados previamente
            restaurarDatos();

            android.util.Log.d("FormularioPaso2", "Vista del Paso 2 inicializada correctamente");

        } catch (Exception e) {
            android.util.Log.e("FormularioPaso2", "Error en onCreateView: " + e.getMessage());
        }

        return view;
    }

    private void validarYContinuar() {
        android.util.Log.d("FormularioPaso2", "Iniciando validación del Paso 2");

        // Validar sabores (TipoP1 - al menos uno debe estar seleccionado)
        List<String> saboresSeleccionados = new ArrayList<>();

        if (cbDulce.isChecked()) saboresSeleccionados.add("Dulce");
        if (cbSalado.isChecked()) saboresSeleccionados.add("Salado");
        if (cbPicante.isChecked()) saboresSeleccionados.add("Picante");
        if (cbAmargo.isChecked()) saboresSeleccionados.add("Amargo");
        if (cbAgrio.isChecked()) saboresSeleccionados.add("Agrio");

        if (cbSaboresOtro.isChecked()) {
            String otroSabor = edtSaboresOtro.getText().toString().trim();
            if (!TextUtils.isEmpty(otroSabor)) {
                saboresSeleccionados.add(otroSabor);
            } else {
                edtSaboresOtro.setError("Especifica el otro sabor");
                edtSaboresOtro.requestFocus();
                return;
            }
        }

        if (saboresSeleccionados.isEmpty()) {
            Toast.makeText(getContext(), "Por favor selecciona al menos un sabor", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar clima (TipoP2 - exactamente uno debe estar seleccionado)
        int selectedClimaId = radioClima.getCheckedRadioButtonId();
        if (selectedClimaId == -1) {
            Toast.makeText(getContext(), "Por favor selecciona un tipo de clima", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton rbClima = getView().findViewById(selectedClimaId);
        String clima = rbClima.getText().toString();

        // Guardar datos
        if (getActivity() instanceof FormularioActivity) {
            Map<String, Object> datosPaso2 = new HashMap<>();
            datosPaso2.put("sabores", saboresSeleccionados);
            datosPaso2.put("clima", clima);

            android.util.Log.d("FormularioPaso2", "Datos del Paso 2: " + datosPaso2.toString());

            ((FormularioActivity) getActivity()).guardarPaso("paso2", datosPaso2);
            ((FormularioActivity) getActivity()).irAlSiguientePaso();
        }
    }

    private void restaurarDatos() {
        try {
            if (getActivity() instanceof FormularioActivity) {
                FormularioActivity activity = (FormularioActivity) getActivity();
                
                // Restaurar sabores
                Object saboresObj = activity.getDatoPaso("sabores");
                if (saboresObj instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<String> sabores = (List<String>) saboresObj;
                    
                    for (String sabor : sabores) {
                        switch (sabor) {
                            case "Dulce":
                                cbDulce.setChecked(true);
                                break;
                            case "Salado":
                                cbSalado.setChecked(true);
                                break;
                            case "Picante":
                                cbPicante.setChecked(true);
                                break;
                            case "Amargo":
                                cbAmargo.setChecked(true);
                                break;
                            case "Agrio":
                                cbAgrio.setChecked(true);
                                break;
                            default:
                                // Si no es una opción predefinida, es un valor personalizado
                                cbSaboresOtro.setChecked(true);
                                edtSaboresOtro.setText(sabor);
                                edtSaboresOtro.setVisibility(View.VISIBLE);
                                break;
                        }
                    }
                }
                
                // Restaurar clima
                Object climaObj = activity.getDatoPaso("clima");
                if (climaObj instanceof String) {
                    String clima = (String) climaObj;
                    
                    // Buscar el RadioButton que corresponde al clima guardado
                    for (int i = 0; i < radioClima.getChildCount(); i++) {
                        View child = radioClima.getChildAt(i);
                        if (child instanceof RadioButton) {
                            RadioButton rb = (RadioButton) child;
                            if (rb.getText().toString().equals(clima)) {
                                rb.setChecked(true);
                                break;
                            }
                        }
                    }
                }
                
                android.util.Log.d("FormularioPaso2", "Datos restaurados correctamente");
            }
        } catch (Exception e) {
            android.util.Log.e("FormularioPaso2", "Error al restaurar datos: " + e.getMessage());
        }
    }
}

