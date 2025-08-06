package com.luciano.tesisplanificatuviaje;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Map;

public class ResumenFormularioFragment extends Fragment {

    private TextView resumenTextView, tvTitulo, tvProgreso;
    private Button btnAnterior, btnFinalizar;

    public ResumenFormularioFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resumen_formulario, container, false);

        // Inicializar vistas
        tvTitulo = view.findViewById(R.id.tvTituloResumen);
        tvProgreso = view.findViewById(R.id.tvProgreso);
        resumenTextView = view.findViewById(R.id.txtResumenFormulario);
        btnAnterior = view.findViewById(R.id.btnAnterior);
        btnFinalizar = view.findViewById(R.id.btnFinalizar);

        // Configurar progreso y título
        if (getActivity() instanceof FormularioActivity) {
            FormularioActivity activity = (FormularioActivity) getActivity();
            if (tvProgreso != null) {
                tvProgreso.setText("Paso " + activity.getPasoActual() + " de " + activity.getTotalPasos());
            }
            if (tvTitulo != null) {
                tvTitulo.setText("Resumen de Preferencias");
            }

            // Mostrar resumen de datos
            Map<String, Object> datos = activity.getDatosTotales();
            StringBuilder resumen = new StringBuilder();
            resumen.append("📝 Resumen de tus preferencias:\n\n");
            
            for (Map.Entry<String, Object> entry : datos.entrySet()) {
                resumen.append("🔹 ").append(formatearClave(entry.getKey())).append(":\n");
                resumen.append("   ").append(entry.getValue()).append("\n\n");
            }
            
            if (resumenTextView != null) {
                resumenTextView.setText(resumen.toString());
            }
        }

        // Configurar listeners
        if (btnAnterior != null) {
            btnAnterior.setOnClickListener(v -> irAlPasoAnterior());
        }
        
        if (btnFinalizar != null) {
            btnFinalizar.setOnClickListener(v -> {
                if (getActivity() instanceof FormularioActivity) {
                    ((FormularioActivity) getActivity()).finalizarFormulario();
                }
            });
        }

        return view;
    }

    private void irAlPasoAnterior() {
        if (getActivity() instanceof FormularioActivity) {
            ((FormularioActivity) getActivity()).irAlPasoAnterior();
        }
    }

    private String formatearClave(String clave) {
        switch (clave) {
            case "paso1": return "Información Personal";
            case "paso2": return "Sabores y Clima";
            case "paso3": return "Vacaciones y Actividades";
            case "paso4": return "Viaje y Presupuesto";
            case "paso5": return "Planificación y Deportes";
            default: return clave;
        }
    }
}

