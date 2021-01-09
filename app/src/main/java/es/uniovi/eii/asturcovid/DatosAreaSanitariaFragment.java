package es.uniovi.eii.asturcovid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import es.uniovi.eii.asturcovid.datos.DatosAreaSanitariaCovidFecha;

public class DatosAreaSanitariaFragment extends DialogFragment {
    private DatosAreaSanitariaCovidFecha datos;

    public DatosAreaSanitariaFragment(DatosAreaSanitariaCovidFecha datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.fragment_datos_area_sanitaria, null);
        builder.setView(root)
                .setTitle("Datos del día " + datos.getFecha())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });

        establecerImagenes(root);

        return builder.create();
    }

    private void establecerImagenes(View itemView) {
        TextView confirmados = (TextView) itemView.findViewById(R.id.id_confirmados_texto_area_sanitaria);
        TextView fallecidos = (TextView) itemView.findViewById(R.id.id_fallecidos_texto_area_sanitaria);
        TextView pruebas = (TextView) itemView.findViewById(R.id.id_pruebas_texto_area_sanitaria);
        ImageView confirmados_icono = (ImageView) itemView.findViewById(R.id.id_confirmados_icono_area_sanitaria);
        ImageView fallecidos_icono = (ImageView) itemView.findViewById(R.id.id_fallecidos_icono_area_sanitaria);
        ImageView pruebas_icono = (ImageView) itemView.findViewById(R.id.id_pruebas_icono_area_sanitaria);

        confirmados.setText("Casos: " + datos.getConfirmados());
        pruebas.setText("Pruebas: " + datos.getPruebas() + "");
        fallecidos.setText("Fallecidos: " + datos.getFallecidos() + "");
        confirmados_icono.setBackgroundResource(R.drawable.icono_confirmados);
        pruebas_icono.setBackgroundResource(R.drawable.icono_pruebas);
        fallecidos_icono.setBackgroundResource(R.drawable.fallecidos_icono);
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.popupArea_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popupArea_height);
        getDialog().getWindow().setLayout(width, height);
    }
}
