package es.uniovi.eii.asturcovid.ui.asturias;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

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

import es.uniovi.eii.asturcovid.MainActivity;
import es.uniovi.eii.asturcovid.R;

public class AsturiasFragment extends Fragment {
    private BarChart barChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_asturias, container, false);
        barChart = root.findViewById(R.id.barChartAsturias_view);
        initBarChart();
        showBarChart();

        return root;
    }

    private class LabelFormatter implements IAxisValueFormatter {
        private final String[] mLabels;

        public LabelFormatter(String[] labels) {
            mLabels = labels;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mLabels[(int) value];
        }
    }

    private void showBarChart() {
        ArrayList<Integer> valueList = new ArrayList<Integer>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        String title = "Casos confirmados";

        List<List<Integer>> datos = ((MainActivity) getActivity()).getDatosAsturias();

        // input data
        for (int i = 0; i < 7; i++) {
            valueList.add(datos.get(i).get(0));
        }

        List<String> dias = new ArrayList<>();
        List<String> fechas = ((MainActivity) getActivity()).getFechasAsturias();
        for (String dia : fechas) {
            dias.add(dia);
        }
        String[] labels = new String[]{dias.get(0), dias.get(1), dias.get(2), dias.get(3), dias.get(4), dias.get(5), dias.get(6)};

        // fit the data into a bar
        for (int i = 0; i < valueList.size(); i++) {
            BarEntry barEntry = new BarEntry(i, valueList.get(i).intValue());
            entries.add(barEntry);
        }

        // establecemos etiquetas eje x
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        BarDataSet barDataSet = new BarDataSet(entries, title);
        initBarDataSet(barDataSet);
        BarData data = new BarData(barDataSet);
        barChart.setData(data);
        barChart.invalidate();
    }

    private void initBarDataSet(BarDataSet barDataSet) {
        // Cambiar color de la barra
        barDataSet.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        // Cambiar el tamaño en la leyenda
        barDataSet.setFormSize(15f);
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false);
        //setting the text size of the value of the bar
        barDataSet.setValueTextSize(12f);
    }

    private void initBarChart() {
        //hiding the grey background of the chart, default false if not set
        barChart.setDrawGridBackground(false);
        //remove the bar shadow, default false if not set
        barChart.setDrawBarShadow(false);
        //remove border of the chart, default false if not set
        barChart.setDrawBorders(false);

        //remove the description label text located at the lower right corner
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        barChart.animateY(1000);
        //setting animation for x-axis, the bar will pop up separately within the time we set
        barChart.animateX(1000);

        XAxis xAxis = barChart.getXAxis();
        //change the position of x-axis to the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //set the horizontal distance of the grid line
        xAxis.setGranularity(1f);
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false);
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(8.5f);

        YAxis leftAxis = barChart.getAxisLeft();
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = barChart.getAxisRight();
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false);

        Legend legend = barChart.getLegend();
        //setting the shape of the legend form to line, default square shape
        legend.setForm(Legend.LegendForm.LINE);
        //setting the text size of the legend
        legend.setTextSize(11f);
        //setting the alignment of legend toward the chart
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        //setting the stacking direction of legend
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false);

    }
}