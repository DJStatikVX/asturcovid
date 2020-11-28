package es.uniovi.eii.asturcovid;

import android.app.ActivityOptions;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ListaAreasFragment extends Fragment {
    View root;
    private String areaSanitariaPreferida;
    private AreaSanitaria preferida;
    private String fechaActualizacion;
    private RecyclerView recyclerView;
    List<AreaSanitaria> listaAreasSanitarias = new ArrayList<>();

    public ListaAreasFragment(String areaSanitariaPreferida, String fechaActualizacion) {
        this.areaSanitariaPreferida = areaSanitariaPreferida;
        this.fechaActualizacion = fechaActualizacion;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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

            for (AreaSanitaria a : listaAreasSanitarias) {
                if (a.getId() == Integer.parseInt(areaSanitariaPreferida)) {
                    preferida = a;
                    listaAreasSanitarias.remove(a);
                    break;
                }
            }

            TextView nombreAreaSanitaria = root.findViewById(R.id.nombreAreaSanitaria);
            TextView numeroAreaSanitaria = root.findViewById(R.id.numeroAreaSanitaria);

            nombreAreaSanitaria.setText(preferida.getNombre_area());
            numeroAreaSanitaria.setText("Número " + preferida.getId());

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

        return root;
    }
}