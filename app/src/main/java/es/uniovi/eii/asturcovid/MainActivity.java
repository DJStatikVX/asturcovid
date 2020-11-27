package es.uniovi.eii.asturcovid;

import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.asturcovid.datos.AreaSanitariaDataSource;
import es.uniovi.eii.asturcovid.modelo.AreaSanitaria;
import es.uniovi.eii.asturcovid.modelo.Hospital;

public class MainActivity extends AppCompatActivity {

    private class DownloadFilesTask extends AsyncTask<Void, Integer, String>{

        @Override
        protected String doInBackground(Void... voids) {
            String mensaje;
            try {
                cargarAreasSanitarias();
                mensaje = "Database actualizada";
            }catch (Exception e){
                mensaje = "Error en la carga de la base de datos.";
            }
            return mensaje;
        }
    }

    private AppBarConfiguration mAppBarConfiguration;

    private List<AreaSanitaria> listaAreasSanitarias;

    public static final String AREA_SANITARIA_SELECCIONADA = "area_seleccionada";

    public static final String FECHA_ACTUALIZACION = "fecha_actualizacion";

    private String fecha;

    SharedPreferences sharedPreferencesMainActivity;

    public static String areaPreferida = "-1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //FloatingActionButton fab = findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);
        //ListView lista = (ListView) findViewById(R.id.listaMenu);
        List<String> indice = new ArrayList<>();
        indice.add("Hello World");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, indice);
        //lista.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //cargarAreasSanitarias();
        DownloadFilesTask task = new DownloadFilesTask();
        task.execute();

        AreaSanitariaDataSource dataSource = new AreaSanitariaDataSource(getApplicationContext());
        dataSource.open();

        listaAreasSanitarias = dataSource.getAllValorations();

        dataSource.close();

        sharedPreferencesMainActivity =
                PreferenceManager.getDefaultSharedPreferences(this);
        areaPreferida = sharedPreferencesMainActivity.getString("keyAreaSanitaria", "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.settings:
                Intent intentSettingsActivity = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentSettingsActivity);

                return true;
            case R.id.action_about:
                mostrarAcercaDe();
                return true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void cargarAreasSanitarias() {
        AreaSanitaria area = null;
        InputStream file = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;

        try {
            URL url12  = new URL("https://drive.google.com/uc?export=download&id=1jvvVSSqoRYsTbDHschXUsUrtzqQhFdd6" );
            URLConnection uc = url12.openConnection();

            reader = new InputStreamReader(uc.getInputStream());
            /////
            //file = getAssets().open("areas_sanitarias.csv");
            //reader = new InputStreamReader(file);
            bufferedReader = new BufferedReader(reader);
            String line = null;

            //Leemos la primera línea que es encabezado y por tanto no nos aporta información útil.
            line = bufferedReader.readLine();
            String[] data = line.split(";");
            fecha = data[9];

            //A partir de aquí leemos a partir de la segunda línea.
            while ((line = bufferedReader.readLine()) != null) {
                data = line.split(";");
                if (data != null) { //El segundo condicional se va a cumplir siempre. Podemos quitarlo

                    int id = Integer.parseInt(data[0]);
                    String nombre_area = data[1];
                    String nombre_hospital = data[2];
                    String ubicacion = data[3];
                    long telefono = Long.parseLong(data[4]);
                    int casos_total = Integer.parseInt(data[5]);
                    int casos_hoy = Integer.parseInt(data[6]);
                    String imagen_hospital = data[7];
                    String web_hospital = data[8];
                    Hospital hospital = new Hospital(nombre_hospital, telefono, ubicacion, imagen_hospital,web_hospital);

                    area = new AreaSanitaria(id, nombre_area, hospital, casos_total, casos_hoy);

                    Log.d("cargarAreasSanitarias", area.toString());

                    //Ya no lo añadimos a la Lista de Películas, pasa antes por la base de datos donde queda almacenado.
                    //  listaPeli.add(peli);
                    //Metemos la película en la base de datos:
                    AreaSanitariaDataSource areasSanitariasDataSource = new AreaSanitariaDataSource(getApplicationContext());
                    areasSanitariasDataSource.open();
                    areasSanitariasDataSource.createAreaSanitaria(area);
                    areasSanitariasDataSource.close();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void clickOnItem(View v) {
        int id_area = Integer.parseInt(v.getTag().toString());
        AreaSanitaria area = listaAreasSanitarias.get(id_area-1);

        Log.i("Click adapter", "Item Clicked: " + id_area);

        Intent intent = new Intent(MainActivity.this, AreaSanitariaActivity.class);
        intent.putExtra(AREA_SANITARIA_SELECCIONADA, area);
        intent.putExtra(FECHA_ACTUALIZACION, fecha);
        // Transición de barrido
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void mostrarAcercaDe(){
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("AsturCOVID: Sprint 1");
        alert.setMessage("Desarrollado por Samuel, Luis y Sofía.");
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}