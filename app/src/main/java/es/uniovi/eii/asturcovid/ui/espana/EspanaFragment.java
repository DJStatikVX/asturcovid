package es.uniovi.eii.asturcovid.ui.espana;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.asturcovid.datos.DatosAPIFragment;
import es.uniovi.eii.asturcovid.adapter.ListaDatosCovidFechaAdapter;
import es.uniovi.eii.asturcovid.ui.MainActivity;
import es.uniovi.eii.asturcovid.R;
import es.uniovi.eii.asturcovid.datos.DatosCovidFecha;
import es.uniovi.eii.asturcovid.util.ChartUtil;

public class EspanaFragment extends Fragment {
    private BarChart barChart;

    private RecyclerView recyclerView;
    private List<DatosCovidFecha> datos = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_espana, container, false);
        barChart = root.findViewById(R.id.barChartEspana_view);
        ChartUtil.initBarChart(barChart);

        try {
            showBarChart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_datos_espana);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        establecerAdapter(datos);

        return root;
    }

    private void showBarChart() throws InterruptedException {
        try {
            ArrayList<Integer> valueList = new ArrayList<Integer>();
            ArrayList<BarEntry> entries = new ArrayList<>();
            String title = "Casos confirmados";

            List<List<Integer>> datos = ((MainActivity) getActivity()).getDatosEspana();

            // input data
            for (int i = 0; i < 7; i++) {
                valueList.add(datos.get(i).get(0));
            }

            List<String> dias = new ArrayList<>();
            List<String> fechas = ((MainActivity) getActivity()).getFechasEspana();
            for (String dia : fechas) {
                dias.add(dia);
            }

            String[] labels = new String[]{dias.get(0).substring(5), dias.get(1).substring(5),
                    dias.get(2).substring(5), dias.get(3).substring(5), dias.get(4).substring(5),
                    dias.get(5).substring(5), dias.get(6).substring(5)};

            // fit the data into a bar
            for (int i = 0; i < valueList.size(); i++) {
                BarEntry barEntry = new BarEntry(i, valueList.get(i).intValue());
                entries.add(barEntry);
            }

            // establecemos etiquetas eje x
            barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

            BarDataSet barDataSet = new BarDataSet(entries, title);
            ChartUtil.initBarDataSet(getContext(), barDataSet);
            BarData data = new BarData(barDataSet);
            barChart.setData(data);
            barChart.invalidate();

            asociarDatosAFechas(dias, datos);
        } catch (Exception e) {
            Thread.sleep(500);
            showBarChart();
        }
    }

    private void asociarDatosAFechas(List<String> fechas, List<List<Integer>> datos) {
        for (int i = 0; i < 7; i++) {
            String fecha = fechas.get(i);
            int confirmados = datos.get(i).get(0);
            int fallecidos = datos.get(i).get(1);
            int hospitalizados = datos.get(i).get(2);
            int recuperados = datos.get(i).get(3);
            int uci = datos.get(i).get(4);
            int casos_abiertos = datos.get(i).get(5);

            DatosCovidFecha dato = new DatosCovidFecha(fecha, confirmados, fallecidos, hospitalizados, recuperados, uci, casos_abiertos);
            this.datos.add(dato);
        }
    }

    private void establecerAdapter(List<DatosCovidFecha> datosCovidFecha) {
        ListaDatosCovidFechaAdapter ldcfAdapter = new ListaDatosCovidFechaAdapter(datosCovidFecha,
                new ListaDatosCovidFechaAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(DatosCovidFecha datos) {
                        DatosAPIFragment frag = new DatosAPIFragment(datos);
                        frag.show(getActivity().getSupportFragmentManager(), DatosAPIFragment.class.getCanonicalName());
                    }
                }, false);

        recyclerView.setAdapter(ldcfAdapter);
    }

}