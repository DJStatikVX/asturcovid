package es.uniovi.eii.asturcovid;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import es.uniovi.eii.asturcovid.modelo.AreaSanitaria;

import static es.uniovi.eii.asturcovid.MainActivity.AREA_SANITARIA_SELECCIONADA;

public class AreaSanitariaActivity extends AppCompatActivity {

    private AreaSanitaria area;

    private TextView nombre_hospital;
    private TextView ubicacion_hospital;
    private TextView telefono_hospital;
    private TextView casos_totales;
    private TextView casos_hoy;
    private ImageView imagen_hospital;
    private CollapsingToolbarLayout toolBarLayout;
    private TextView txtDatosActualizados;

    private String fechaActualizacion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_sanitaria);

        //Recepción datos como activity secundaria
        Intent intentAreaSanitaria = getIntent();
        area = intentAreaSanitaria .getParcelableExtra(AREA_SANITARIA_SELECCIONADA);
        fechaActualizacion = intentAreaSanitaria.getStringExtra(MainActivity.FECHA_ACTUALIZACION);

        //Gestion barra de la app
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

        // Gestión de los controles que contienen los datos de la película
        nombre_hospital = (TextView) findViewById(R.id.hospital);
        ubicacion_hospital = (TextView) findViewById(R.id.ubicacion);
        telefono_hospital = (TextView) findViewById(R.id.telefono);
        casos_totales = (TextView) findViewById(R.id.numeroCasosTotales);
        casos_hoy = (TextView) findViewById(R.id.numeroCasosHoy);
        txtDatosActualizados = (TextView) findViewById(R.id.txtDatosActualizados);

        if (area!=null) //apertura en modo consulta
            mostrarDatos(area);

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

    private void mostrarMapa() {
        Intent intent = new Intent(AreaSanitariaActivity.this, GoogleMapsActivity.class);
        intent.putExtra(AREA_SANITARIA_SELECCIONADA, area);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    // Carga los datos que tenemos en la instancia en los componentes de la activity para mostrarlos
    public void mostrarDatos(AreaSanitaria area){
        if (!area.getNombre_area().isEmpty()) { //apertura en modo consulta
            //Actualizar componentes con valores de la pelicula específica
            int id = area.getId();
            String nombre_area = area.getNombre_area();
            toolBarLayout.setTitle(nombre_area + " (" + id + ")");


            nombre_hospital.setText(area.getHospital().getNombre_hospital());
            ubicacion_hospital.setText(area.getHospital().getDireccion_hospital());
            telefono_hospital.setText("" + (long) area.getHospital().getTelefono());
            casos_totales.setText("" + (int) area.getCasos_totales());
            casos_hoy.setText("" + (int) area.getCasos_hoy());
            txtDatosActualizados.setText("Datos actualizados a " + fechaActualizacion);

            String url = area.getHospital().getImagen_hospital();
            // Imagen de fondo
            Picasso.get()
                    .load(url).into(imagen_hospital);
        }
    }
}