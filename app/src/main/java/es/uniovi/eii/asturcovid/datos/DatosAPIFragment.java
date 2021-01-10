package es.uniovi.eii.asturcovid.datos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import es.uniovi.eii.asturcovid.R;
import es.uniovi.eii.asturcovid.datos.DatosCovidFecha;

public class DatosAPIFragment extends DialogFragment {

    private DatosCovidFecha datos;

    public DatosAPIFragment(DatosCovidFecha datos) {
        this.datos = datos;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.fragment_datos_api, null);
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
        TextView confirmados = (TextView)itemView.findViewById(R.id.id_confirmados_texto_api);
        TextView fallecidos = (TextView) itemView.findViewById(R.id.id_fallecidos_texto_api);
        TextView hospitalizados = (TextView) itemView.findViewById(R.id.id_hospitalizados_texto_api);
        TextView recuperados = (TextView) itemView.findViewById(R.id.id_recuperados_texto_api);
        TextView uci = (TextView) itemView.findViewById(R.id.id_uci_texto_api);
        TextView casos_abiertos = (TextView) itemView.findViewById(R.id.id_casos_abiertos_texto_api);
        ImageView confirmados_icono = (ImageView)itemView.findViewById(R.id.id_confirmados_icono_api);
        ImageView fallecidos_icono = (ImageView)itemView.findViewById(R.id.id_fallecidos_icono_api);
        ImageView hospitalizados_icono = (ImageView)itemView.findViewById(R.id.id_hospitalizados_icono_api);
        ImageView recuperados_icono = (ImageView)itemView.findViewById(R.id.id_recuperados_icono_api);
        ImageView uci_icono = (ImageView)itemView.findViewById(R.id.id_uci_icono_api);
        ImageView casos_abiertos_icono = (ImageView)itemView.findViewById(R.id.id_casos_abiertos_icono_api);

        confirmados.setText("Confirmados: " + datos.getConfirmados());
        fallecidos.setText("Fallecidos: " + datos.getFallecidos() + "");
        hospitalizados.setText("Hospitalizados: " + datos.getHospitalizados() + "");
        recuperados.setText("Recuperados: " + datos.getRecuperados() + "");
        uci.setText("UCI: " + datos.getUci() + "");
        casos_abiertos.setText("Casos abiertos: " + datos.getCasos_abiertos() + "");
        confirmados_icono.setBackgroundResource(R.drawable.icono_confirmados);
        fallecidos_icono.setBackgroundResource(R.drawable.fallecidos_icono);
        hospitalizados_icono.setBackgroundResource(R.drawable.hospital_icono);
        recuperados_icono.setBackgroundResource(R.drawable.recuperados_icono);
        uci_icono.setBackgroundResource(R.drawable.uci_icono);
        casos_abiertos_icono.setBackgroundResource(R.drawable.icono_casosabiertos);
    }

    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);
    }
}