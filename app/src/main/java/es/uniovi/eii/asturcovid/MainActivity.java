package es.uniovi.eii.asturcovid;

import android.app.ActivityOptions;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private TabItem mapa;
    private TabItem listaAreas;

    private AppBarConfiguration mAppBarConfiguration;

    private List<AreaSanitaria> listaAreasSanitarias;

    public static final String AREA_SANITARIA_SELECCIONADA = "area_seleccionada";

    public static final String FECHA_ACTUALIZACION = "fecha_actualizacion";

    protected static String fecha;

    protected static SharedPreferences sharedPreferencesMainActivity;

    public String areaPreferida = "-1";

    // Identificadores de activity
    private static final int GESTION_AREA_PREFERIDA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

        DownloadFilesTask task = new DownloadFilesTask();
        try {
            task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        AreaSanitariaDataSource dataSource = new AreaSanitariaDataSource(getApplicationContext());
        dataSource.open();

        listaAreasSanitarias = dataSource.getAllValorations();

        dataSource.close();
    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {

        //private List<Fragment> fragments = new ArrayList<>();
        private int numeroDeElementos;
        private String[] titulos = new String[]{"Mapa", "Lista Áreas"};

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
            numeroDeElementos = behavior;
        }

        /*public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitles.add(title);
        }*/
        @Override
        public CharSequence getPageTitle(int position){
            return titulos[position];
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new MapaFragment();
                case 1:
                    return new ListaAreasFragment(areaPreferida, fecha);
                default:
                    return null;
            }
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return numeroDeElementos;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sharedPreferencesMainActivity =
                PreferenceManager.getDefaultSharedPreferences(this);
        areaPreferida = sharedPreferencesMainActivity.getString("keyAreaSanitaria", "");

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        mapa = findViewById(R.id.tabitem_mapa);
        listaAreas = findViewById(R.id.tabitem_listaAreas);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        //mapaFragment = new MapaFragment();
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
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
                startActivityForResult(intentSettingsActivity, GESTION_AREA_PREFERIDA);

                return true;
            case R.id.action_about:
                mostrarAcercaDe();
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GESTION_AREA_PREFERIDA && resultCode == RESULT_OK) {
            String nuevoValor = sharedPreferencesMainActivity.getString("keyAreaSanitaria", "");
            if (!areaPreferida.equals(nuevoValor)) {
                viewPager.getAdapter().notifyDataSetChanged();
            }
        }
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
            URL url12  = new URL("https://www.dropbox.com/s/4d95qmhwktn0chj/areas_sanitarias.csv?dl=1" );
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
                    double latitud = Double.parseDouble(data[9]);
                    double longitud = Double.parseDouble(data[10]);
                    Hospital hospital = new Hospital(nombre_hospital, telefono, ubicacion, imagen_hospital,web_hospital, latitud, longitud);

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
        alert.setTitle("AsturCOVID: Sprint 3");
        alert.setMessage("Desarrollado por Samuel, Luis y Sofía.");
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    public void mostrarInfo(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Cálculo de incidencia por áreas");
        alert.setIcon(R.drawable.ayuda);

        alert.setMessage(Html.fromHtml("La incidencia de un área se calcula a partir de la <b>normalización</b> de los datos con respecto al mayor número de casos en la última semana.<br><br>De esta manera:<br>" +
                "<ul> <li> Incidencia <b>< 33%</b>: Marcador <b><font color='#006400'>verde</font></b></li>  <li> <b>33%</b> < Incidencia < <b>66%</b>: Marcador <b><font color='#FFD500'>amarillo</font></b></li>  <li> <b>66%</b> < Incidencia: Marcador <b><font color='#FF0000'>rojo</font></b></li> </ul>", Html.FROM_HTML_MODE_LEGACY));
        alert.setPositiveButton("OK", null);
        alert.show();
    }
}