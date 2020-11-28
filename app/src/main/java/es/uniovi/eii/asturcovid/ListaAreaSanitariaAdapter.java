package es.uniovi.eii.asturcovid;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.uniovi.eii.asturcovid.modelo.AreaSanitaria;

public class ListaAreaSanitariaAdapter extends RecyclerView.Adapter<ListaAreaSanitariaAdapter.AreaSanitariaViewHolder> {

    // Interfaz para manejar el evento click sobre un elemento
    public interface OnItemClickListener {
        void onItemClick(AreaSanitaria item);
    }

    private List<AreaSanitaria> listaAreasSanitarias;
    private final OnItemClickListener listener;
    private String fechaActualizacion;

    public ListaAreaSanitariaAdapter(List<AreaSanitaria> listaAreasSanitarias, String fechaActualizacion, OnItemClickListener listener) {
        this.listaAreasSanitarias = listaAreasSanitarias;
        this.fechaActualizacion = fechaActualizacion;
        this.listener = listener;
    }

    /* Indicamos el layout a "inflar" para usar en la vista
     */
    @NonNull
    @Override
    public AreaSanitariaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creamos la vista con el layout para un elemento
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.linea_recycler_view_area_sanitaria, parent, false);
        return new AreaSanitariaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AreaSanitariaViewHolder holder, int position) {
        // Extrae de la lista el elemento indicado por posición
        AreaSanitaria area = listaAreasSanitarias.get(position);
        Log.i("Lista","Visualiza elemento: " + area);
        // llama al método de nuestro holder para asignar valores a los componentes
        // además, pasamos el listener del evento onClick
        holder.bindUser(area, listener);
    }

    @Override
    public int getItemCount() {
        return listaAreasSanitarias.size();
    }

    /*Clase interna que define los compoonentes de la vista*/
    public static class AreaSanitariaViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre_area;
        private TextView numero_area;

        public AreaSanitariaViewHolder(View itemView) {
            super(itemView);

            nombre_area = (TextView)itemView.findViewById(R.id.nombreAreaSanitaria);
            numero_area = (TextView)itemView.findViewById(R.id.numeroAreaSanitaria);
        }

        // asignar valores a los componentes
        public void bindUser(final AreaSanitaria area, final OnItemClickListener listener) {
            nombre_area.setText(area.getNombre_area());
            numero_area.setText("Número " + area.getId());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(area);
                }
            });
        }
    }
}