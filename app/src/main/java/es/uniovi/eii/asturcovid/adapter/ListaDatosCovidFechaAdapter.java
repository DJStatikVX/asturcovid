package es.uniovi.eii.asturcovid.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uniovi.eii.asturcovid.R;
import es.uniovi.eii.asturcovid.datos.DatosCovidFecha;

public class ListaDatosCovidFechaAdapter extends RecyclerView.Adapter<ListaDatosCovidFechaAdapter.ListaDatosCovidFechaViewHolder> {

    // Interfaz para manejar el evento click sobre un elemento
    public interface OnItemClickListener {
        void onItemClick(DatosCovidFecha datosDia);
    }

    private List<DatosCovidFecha> listaDatosCovidFecha;
    private final ListaDatosCovidFechaAdapter.OnItemClickListener listener;
    private boolean region;

    public ListaDatosCovidFechaAdapter(List<DatosCovidFecha> datosFecha, ListaDatosCovidFechaAdapter.OnItemClickListener listener, boolean region) {
        this.listaDatosCovidFecha = datosFecha;
        this.listener = listener;
        this.region = region;
    }

    /* Indicamos el layout a "inflar" para usar en la vista
     */
    @NonNull
    @Override
    public ListaDatosCovidFechaAdapter.ListaDatosCovidFechaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creamos la vista con el layout para un elemento
        if(region){
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.linea_recycler_view_datos_asturias, parent, false);
            return new ListaDatosCovidFechaAdapter.ListaDatosCovidFechaViewHolder(itemView);
        }else{
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.linea_recycler_view_datos_espana, parent, false);
            return new ListaDatosCovidFechaAdapter.ListaDatosCovidFechaViewHolder(itemView);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ListaDatosCovidFechaAdapter.ListaDatosCovidFechaViewHolder holder, int position) {
        // Extrae de la lista el elemento indicado por posición
        DatosCovidFecha datosFecha = listaDatosCovidFecha.get(position);
        // llama al método de nuestro holder para asignar valores a los componentes
        // además, pasamos el listener del evento onClick
        holder.bindUser(datosFecha, listener);
    }

    @Override
    public int getItemCount() {
        return listaDatosCovidFecha.size();
    }

    /*Clase interna que define los compoonentes de la vista*/
    public static class ListaDatosCovidFechaViewHolder extends RecyclerView.ViewHolder{

        private TextView id_fecha;
        private TextView confirmados;
        private TextView fallecidos;
        private TextView recuperados;
        private ImageView icono_calendario;
        private ImageView icono_confirmados;
        private ImageView icono_fallecidos;
        private ImageView icono_recuperados;

        public ListaDatosCovidFechaViewHolder(View itemView) {
            super(itemView);
            icono_calendario = (ImageView)itemView.findViewById(R.id.id_icono_calendario);
            id_fecha = (TextView)itemView.findViewById(R.id.id_fecha);
            confirmados = (TextView)itemView.findViewById(R.id.id_contagios_texto);
            fallecidos = (TextView) itemView.findViewById(R.id.id_fallecidos_texto);
            recuperados = (TextView) itemView.findViewById(R.id.id_recuperados_texto);
            icono_confirmados = (ImageView)itemView.findViewById(R.id.id_contagios_icono);
            icono_fallecidos = (ImageView)itemView.findViewById(R.id.id_fallecidos_icono);
            icono_recuperados = (ImageView)itemView.findViewById(R.id.id_recuperados_icono);
        }

        // asignar valores a los componentes
        public void bindUser(final DatosCovidFecha datosFecha, final ListaDatosCovidFechaAdapter.OnItemClickListener listener) {
            icono_calendario.setBackgroundResource(R.drawable.icono_calendario);
            id_fecha.setText(datosFecha.getFecha());
            confirmados.setText(datosFecha.getConfirmados() + "");
            fallecidos.setText(datosFecha.getFallecidos() + "");
            recuperados.setText(datosFecha.getRecuperados() + "");
            icono_confirmados.setBackgroundResource(R.drawable.icono_confirmados);
            icono_fallecidos.setBackgroundResource(R.drawable.fallecidos_icono);
            icono_recuperados.setBackgroundResource(R.drawable.recuperados_icono);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(datosFecha);
                }
            });
        }
    }
}
