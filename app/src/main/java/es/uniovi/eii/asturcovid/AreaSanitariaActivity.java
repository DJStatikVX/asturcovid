package es.uniovi.eii.asturcovid;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.uniovi.eii.asturcovid.datos.DatosAreaSanitariaCovidFecha;
import es.uniovi.eii.asturcovid.datos.DatosCovidFecha;
import es.uniovi.eii.asturcovid.modelo.AreaSanitaria;

import static es.uniovi.eii.asturcovid.MainActivity.AREA_SANITARIA_SELECCIONADA;

public class AreaSanitariaActivity extends AppCompatActivity {

    private AreaSanitaria area;

    private TextView nombre_hospital;
    private TextView ubicacion_hospital;
    private TextView telefono_hospital;
    private ImageView imagen_hospital;
    private CollapsingToolbarLayout toolBarLayout;
    private TextView txtDatosActualizados;

    private String fechaActualizacion;

    private List<DatosAreaSanitariaCovidFecha> datosAreaSanitariaCovidFechaList = new ArrayList<>();
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_sanitaria);

        //Recepción datos como activity secundaria
        Intent intentAreaSanitaria = getIntent();
        area = intentAreaSanitaria.getParcelableExtra(AREA_SANITARIA_SELECCIONADA);
        fechaActualizacion = intentAreaSanitaria.getStringExtra(MainActivity.FECHA_ACTUALIZACION);

        Date fecha = null;
        try {
            fecha = new SimpleDateFormat("dd/MM/yyyy").parse(fechaActualizacion);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);

        for (int i = 6; i >= 0; i--) {
            String fechaStr = new SimpleDateFormat("dd/MM/yyyy").format(cal.getTime());
            datosAreaSanitariaCovidFechaList.add(new DatosAreaSanitariaCovidFecha(fechaStr, area.getListaCasos().get(i), area.getListaMuertes().get(i), area.getListaPruebas().get(i)));
            cal.add(Calendar.DATE, -1);
        }

        // Gestion barra de la app
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(getTitle());
        imagen_hospital = (ImageView) findViewById(R.id.imagenFondo);

        if (area != null) {
            int id = area.getId();
            String nombre_area = area.getNombre_area();
            toolBarLayout.setTitle(nombre_area + " (" + id + ")");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_datos_area_sanitaria);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        establecerAdapter(datosAreaSanitariaCovidFechaList);

        // Gestión de los controles que contienen los datos de la película
        nombre_hospital = (TextView) findViewById(R.id.hospital);
        ubicacion_hospital = (TextView) findViewById(R.id.ubicacion);
        telefono_hospital = (TextView) findViewById(R.id.telefono);
        txtDatosActualizados = (TextView) findViewById(R.id.txtDatosActualizados);

        if (area != null) //apertura en modo consulta
            mostrarDatos(area);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // en la ActionBar activar una flecha para volver hacia atrás
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Gestión del FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                //visitarWeb(area.getHospital().getWeb_hospital());
                mostrarMapa();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void establecerAdapter(List<DatosAreaSanitariaCovidFecha> datosAreaSanitariaCovidFecha) {
        ListaDatosAreaSanitariaAdapter ldcfAdapter = new ListaDatosAreaSanitariaAdapter(datosAreaSanitariaCovidFecha,
                new ListaDatosAreaSanitariaAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(DatosAreaSanitariaCovidFecha datos) {
                        DatosAreaSanitariaFragment frag = new DatosAreaSanitariaFragment(datos);
                        frag.show(getSupportFragmentManager(), DatosAreaSanitariaFragment.class.getCanonicalName());
                    }
                });

        recyclerView.setAdapter(ldcfAdapter);
    }

    private void mostrarMapa() {
        Intent intent = new Intent(AreaSanitariaActivity.this, GoogleMapsActivity.class);
        intent.putExtra(AREA_SANITARIA_SELECCIONADA, area);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    // Carga los datos que tenemos en la instancia en los componentes de la activity para mostrarlos
    public void mostrarDatos(AreaSanitaria area) {
        if (!area.getNombre_area().isEmpty()) { //apertura en modo consulta
            //Actualizar componentes con valores de la pelicula específica
            int id = area.getId();
            String nombre_area = area.getNombre_area();
            toolBarLayout.setTitle(nombre_area + " (" + id + ")");


            nombre_hospital.setText(area.getHospital().getNombre_hospital());
            ubicacion_hospital.setText(area.getHospital().getDireccion_hospital());
            telefono_hospital.setText("" + (long) area.getHospital().getTelefono());
            txtDatosActualizados.setText("Datos actualizados a " + fechaActualizacion);

            String url = area.getHospital().getImagen_hospital();
            // Imagen de fondo
            Picasso.get()
                    .load(url).into(imagen_hospital);
        }
    }
}