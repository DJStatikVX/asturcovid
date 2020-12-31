package es.uniovi.eii.asturcovid;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.asturcovid.datos.AreaSanitariaDataSource;
import es.uniovi.eii.asturcovid.modelo.AreaSanitaria;

import static es.uniovi.eii.asturcovid.MainActivity.AREA_SANITARIA_SELECCIONADA;
import static es.uniovi.eii.asturcovid.MainActivity.FECHA_ACTUALIZACION;
import static es.uniovi.eii.asturcovid.MainActivity.fecha;

public class ListaAreasFragment extends Fragment {
    View root;
    private String areaSanitariaPreferida;
    private AreaSanitaria preferida;
    private String fechaActualizacion;
    private RecyclerView recyclerView;
    List<AreaSanitaria> listaAreasSanitarias = new ArrayList<>();

    public ListaAreasFragment(){
        this.fechaActualizacion = "no definida";
    }

    public ListaAreasFragment(String areaSanitariaPreferida, String fechaActualizacion) {
        this.areaSanitariaPreferida = areaSanitariaPreferida;
        this.fechaActualizacion = fechaActualizacion;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.areaSanitariaPreferida = MainActivity.sharedPreferencesMainActivity.getString("keyAreaSanitaria", "");
        // Hay área sanitaria preferida especificada
        if (!areaSanitariaPreferida.equals("-1") && !areaSanitariaPreferida.equals("")) {
            root = inflater.inflate(R.layout.lista_areas_fragment_selected, container, false);
            recyclerView = (RecyclerView) root.findViewById(R.id.recyclerViewSelected);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setHasFixedSize(true);

            AreaSanitariaDataSource areaSanitariaDataSource = new AreaSanitariaDataSource(root.getContext());
            areaSanitariaDataSource.open();

            listaAreasSanitarias = areaSanitariaDataSource.getAllValorations();

            areaSanitariaDataSource.close();

            calcularIncidencias(listaAreasSanitarias);

            excluirAreaPreferida();

            asignarDatosAreaPreferida(preferida);

            View lineaAreaPreferida = (View) root.findViewById(R.id.linea_area_preferida);
            lineaAreaPreferida.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AreaSanitariaActivity.class);
                    intent.putExtra(AREA_SANITARIA_SELECCIONADA, preferida);
                    intent.putExtra(FECHA_ACTUALIZACION, MainActivity.fecha);
                    // Transición de barrido
                    startActivity(intent);
                }
            });

            establecerAdapter(listaAreasSanitarias, fechaActualizacion);

        } else {
            root = inflater.inflate(R.layout.lista_areas_fragment_unselected, container, false);
            recyclerView = (RecyclerView) root.findViewById(R.id.recyclerViewUnselected);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext());
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setHasFixedSize(true);

            AreaSanitariaDataSource areaSanitariaDataSource = new AreaSanitariaDataSource(root.getContext());
            areaSanitariaDataSource.open();

            listaAreasSanitarias = areaSanitariaDataSource.getAllValorations();

            areaSanitariaDataSource.close();

            calcularIncidencias(listaAreasSanitarias);

            establecerAdapter(listaAreasSanitarias, fechaActualizacion);
        }

        return root;
    }

    private void excluirAreaPreferida() {
        // Eliminar area seleccionada de la lista
        for (AreaSanitaria a : listaAreasSanitarias) {
            if (a.getId() == Integer.parseInt(areaSanitariaPreferida)) {
                preferida = a;
                listaAreasSanitarias.remove(a);
                break;
            }
        }
    }

    private void calcularIncidencias(List<AreaSanitaria> listaAreasSanitarias) {
        //Calculo maximo casos totales
        int max = 0;
        for (AreaSanitaria a : listaAreasSanitarias) {
            int casos_totales = a.getCasos_totales();
            if (casos_totales > max) {
                max = casos_totales;
            }
        }

        //Calcular nivel de incidencia de cada area
        for (AreaSanitaria a : listaAreasSanitarias) {
            double valor = (a.getCasos_totales()*1.0)/max;
            if(valor >= 0 && valor <= 0.33){
                a.setNumero_incidencia(0);
            }else if(valor > 0.33 && valor <= 0.66){
                a.setNumero_incidencia(1);
            }else if(valor > 0.66 && valor <= 1){
                a.setNumero_incidencia(2);
            }
        }
    }

    private void asignarDatosAreaPreferida(AreaSanitaria preferida) {
        TextView nombreAreaSanitaria = root.findViewById(R.id.nombreAreaSanitaria);
        TextView numeroAreaSanitaria = root.findViewById(R.id.numeroAreaSanitaria);
        ImageView icono_nivel_incidencia = root.findViewById(R.id.iconoAreaSanitaria);

        nombreAreaSanitaria.setText(preferida.getNombre_area());
        numeroAreaSanitaria.setText("Número " + preferida.getId());
        switch (preferida.getNumero_incidencia()){
            case 0:
                icono_nivel_incidencia.setBackgroundResource(R.drawable.verde_marcador);
                break;
            case 1:
                icono_nivel_incidencia.setBackgroundResource(R.drawable.amarillo_marcador);
                break;
            case 2:
                icono_nivel_incidencia.setBackgroundResource(R.drawable.rojo_marcador);
                break;
        }
    }

    private void establecerAdapter(List<AreaSanitaria> listaAreasSanitarias, String fechaActualizacion) {
        ListaAreaSanitariaAdapter lasAdapter = new ListaAreaSanitariaAdapter(listaAreasSanitarias, fechaActualizacion,
                new ListaAreaSanitariaAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(AreaSanitaria areaSanitaria) {
                        Intent intent = new Intent(getActivity(), AreaSanitariaActivity.class);
                        intent.putExtra(AREA_SANITARIA_SELECCIONADA, areaSanitaria);
                        intent.putExtra(FECHA_ACTUALIZACION, MainActivity.fecha);
                        // Transición de barrido
                        startActivity(intent);
                    }
                });

        recyclerView.setAdapter(lasAdapter);
    }
}