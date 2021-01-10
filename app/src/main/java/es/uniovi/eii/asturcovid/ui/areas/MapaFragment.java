package es.uniovi.eii.asturcovid.ui.areas;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import es.uniovi.eii.asturcovid.R;
import es.uniovi.eii.asturcovid.datos.AreaSanitariaDataSource;
import es.uniovi.eii.asturcovid.modelo.AreaSanitaria;

public class MapaFragment extends Fragment {
    View root;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.mapa_fragment, container, false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<AreaSanitaria> areasSanitarias = new ArrayList<>();
        AreaSanitariaDataSource dataSource = new AreaSanitariaDataSource(root.getContext());
        dataSource.open();
        areasSanitarias = dataSource.getAllValorations();
        dataSource.close();

        //Calculo maximo casos totales
        int max = 0;
        for (AreaSanitaria a : areasSanitarias) {
            int casos_totales = a.getCasosTotales();
            if (casos_totales > max) {
                max = casos_totales;
            }
        }

        //Calcular nivel de incidencia de cada area
        for (AreaSanitaria a : areasSanitarias) {
            double valor = (a.getCasosTotales()*1.0)/max;
            if(valor >= 0 && valor <= 0.33){
                a.setNumero_incidencia(0);
            }else if(valor > 0.33 && valor <= 0.66){
                a.setNumero_incidencia(1);
            }else if(valor > 0.66 && valor <= 1){
                a.setNumero_incidencia(2);
            }
        }

        List<ImageView> lista_iconos = new ArrayList<>();
        ImageView icono_jarrio = (ImageView) getView().findViewById(R.id.marcadorJarrio);
        ImageView icono_narcea = (ImageView) getView().findViewById(R.id.marcadorNarcea);
        ImageView icono_aviles = (ImageView) getView().findViewById(R.id.marcadorAviles);
        ImageView icono_oviedo = (ImageView) getView().findViewById(R.id.marcadorOviedo);
        ImageView icono_gijon = (ImageView) getView().findViewById(R.id.marcadorGijon);
        ImageView icono_arriondas = (ImageView) getView().findViewById(R.id.marcadorArriondas);
        ImageView icono_mieres = (ImageView) getView().findViewById(R.id.marcadorMieres);
        ImageView icono_langreo = (ImageView) getView().findViewById(R.id.marcadorLangreo);
        lista_iconos.add(icono_jarrio);
        lista_iconos.add(icono_narcea);
        lista_iconos.add(icono_aviles);
        lista_iconos.add(icono_oviedo);
        lista_iconos.add(icono_gijon);
        lista_iconos.add(icono_arriondas);
        lista_iconos.add(icono_mieres);
        lista_iconos.add(icono_langreo);

        for (AreaSanitaria a : areasSanitarias) {
            for (ImageView image: lista_iconos) {
                if(("" + a.getId()).equals(image.getTag())){
                    switch (a.getNumero_incidencia()){
                        case 0:
                            image.setBackgroundResource(R.drawable.verde_marcador);
                            break;
                        case 1:
                            image.setBackgroundResource(R.drawable.amarillo_marcador);
                            break;
                        case 2:
                            image.setBackgroundResource(R.drawable.rojo_marcador);
                            break;
                    }
                    break;
                }
            }
        }
    }
}
