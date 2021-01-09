package es.uniovi.eii.asturcovid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uniovi.eii.asturcovid.datos.DatosAreaSanitariaCovidFecha;

public class ListaDatosAreaSanitariaAdapter extends RecyclerView.Adapter<ListaDatosAreaSanitariaAdapter.ListaDatosAreaSanitariaCovidFechaViewHolder> {

    // Interfaz para manejar el evento click sobre un elemento
    public interface OnItemClickListener {
        void onItemClick(DatosAreaSanitariaCovidFecha datosDia);
    }

    private List<DatosAreaSanitariaCovidFecha> listaDatosAreaSanitariaCovidFecha;
    private final OnItemClickListener listener;

    public ListaDatosAreaSanitariaAdapter(List<DatosAreaSanitariaCovidFecha> datosFecha, OnItemClickListener listener) {
        this.listaDatosAreaSanitariaCovidFecha = datosFecha;
        this.listener = listener;
    }

    /* Indicamos el layout a "inflar" para usar en la vista
     */
    @NonNull
    @Override
    public ListaDatosAreaSanitariaCovidFechaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creamos la vista con el layout para un elemento
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linea_recycler_view_datos_area_sanitaria, parent, false);
        return new ListaDatosAreaSanitariaAdapter.ListaDatosAreaSanitariaCovidFechaViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull ListaDatosAreaSanitariaAdapter.ListaDatosAreaSanitariaCovidFechaViewHolder holder, int position) {
        // Extrae de la lista el elemento indicado por posición
        DatosAreaSanitariaCovidFecha datosFecha = listaDatosAreaSanitariaCovidFecha.get(position);
        // llama al método de nuestro holder para asignar valores a los componentes
        // además, pasamos el listener del evento onClick
        holder.bindUser(datosFecha, listener);
    }

    @Override
    public int getItemCount() {
        return listaDatosAreaSanitariaCovidFecha.size();
    }

    /*Clase interna que define los compoonentes de la vista*/
    public static class ListaDatosAreaSanitariaCovidFechaViewHolder extends RecyclerView.ViewHolder {

        private TextView id_fecha;
        private TextView confirmados;
        private TextView fallecidos;
        private TextView pruebas;
        private ImageView icono_calendario;
        private ImageView icono_confirmados;
        private ImageView icono_fallecidos;
        private ImageView icono_pruebas;

        public ListaDatosAreaSanitariaCovidFechaViewHolder(View itemView) {
            super(itemView);
            icono_calendario = (ImageView) itemView.findViewById(R.id.icono_calendario_area_sanitaria);
            id_fecha = (TextView) itemView.findViewById(R.id.fecha_area_sanitaria);
            confirmados = (TextView) itemView.findViewById(R.id.id_contagios_texto);
            fallecidos = (TextView) itemView.findViewById(R.id.id_fallecidos_texto);
            pruebas = (TextView) itemView.findViewById(R.id.id_pruebas_texto);
            icono_confirmados = (ImageView) itemView.findViewById(R.id.id_contagios_icono);
            icono_fallecidos = (ImageView) itemView.findViewById(R.id.id_fallecidos_icono);
            icono_pruebas = (ImageView) itemView.findViewById(R.id.id_pruebas_icono);
        }

        // asignar valores a los componentes
        public void bindUser(final DatosAreaSanitariaCovidFecha datosFecha, final ListaDatosAreaSanitariaAdapter.OnItemClickListener listener) {
            icono_calendario.setBackgroundResource(R.drawable.icono_calendario);
            id_fecha.setText(datosFecha.getFecha());
            confirmados.setText(datosFecha.getConfirmados() + "");
            fallecidos.setText(datosFecha.getFallecidos() + "");
            pruebas.setText(datosFecha.getPruebas() + "");
            icono_confirmados.setBackgroundResource(R.drawable.icono_confirmados);
            icono_fallecidos.setBackgroundResource(R.drawable.fallecidos_icono);
            icono_pruebas.setBackgroundResource(R.drawable.icono_pruebas);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(datosFecha);
                }
            });
        }
    }
}
