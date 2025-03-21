package es.uniovi.eii.asturcovid.ui;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import es.uniovi.eii.asturcovid.R;
import es.uniovi.eii.asturcovid.datos.AreaSanitariaDataSource;
import es.uniovi.eii.asturcovid.datos.FechaDataSource;
import es.uniovi.eii.asturcovid.datos.MyDBHelper;
import es.uniovi.eii.asturcovid.modelo.AreaSanitaria;
import es.uniovi.eii.asturcovid.modelo.Hospital;
import es.uniovi.eii.asturcovid.ui.areas.ListaAreasFragment;
import es.uniovi.eii.asturcovid.ui.areas.MapaFragment;

public class MainActivity extends AppCompatActivity {

    /**
     * Tarea asíncrona de carga de base de datos en caso de haber novedades a descargar
     */
    private class DownloadFilesTask extends AsyncTask<Void, Integer, String> {

        // Barra de progreso
        private ProgressDialog progressDialog;

        // Variables de cálculo de porcentaje de progreso
        private float datosALeer = 240.0f;
        float numeroDatosLeidos = 0.0f;

        /**
         * Método previo a la ejecución de la tarea asíncrona
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Establecemos las propiedades de la barra de progreso
            this.progressDialog = new ProgressDialog(MainActivity.this);
            this.progressDialog.setMessage("Recuperando información...");
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Tarea principal a ejecutar por la tarea asíncrona
         * @param voids parámetros
         * @return mensaje de carga correcta o incorrecta de datos
         */
        @Override
        protected String doInBackground(Void... voids) {
            String mensaje;

            try {
                cargarAreasSanitarias();
                mensaje = "Database actualizada";
            } catch (Exception e) {
                mensaje = "Error en la carga de la base de datos.";
            }

            return mensaje;
        }

        /**
         * Método que va actualizando la barra de progreso
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        /**
         * Tras finalizar la ejecución, se cierra el diálogo de progreso
         * @param s
         */
        @Override
        protected void onPostExecute(String s) {
            this.progressDialog.dismiss();
            cargarView();
        }

        private void cargarAreasSanitarias() {
            AreaSanitaria area = null;
            InputStream file = null;
            InputStreamReader reader = null;
            BufferedReader bufferedReader = null;

            try {
                URL url12 = new URL("https://www.dropbox.com/s/4d95qmhwktn0chj/areas_sanitarias.csv?dl=1");
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
                fecha = data[28];
                publishProgress(10);

                boolean hayDatosNuevos = false;

                FechaDataSource fechaDataSource = new FechaDataSource(getApplicationContext());
                fechaDataSource.open();

                String ultimaActualizacion = fechaDataSource.getFechaUltimaActualizacion();
                publishProgress(20);
                try {
                    if (ultimaActualizacion == null) {
                        hayDatosNuevos = true;
                        ultimaActualizacion = fecha;
                    } else {
                        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
                        Date fechaAntigua = formateador.parse(ultimaActualizacion);
                        Date fechaNueva = formateador.parse(fecha);

                        hayDatosNuevos = fechaNueva.after(fechaAntigua);
                    }

                } catch (java.text.ParseException e) {
                    Snackbar.make(findViewById(R.id.layout_principal), "Formato de datos corrupto. Por favor, reinstale la aplicación.", Snackbar.LENGTH_LONG).show();
                }

                if (hayDatosNuevos) {
                    //Borramos los datos existentes en la base de datos para reemplazarlos por los nuevos
                    vaciarBaseDatos();
                    //A partir de aquí leemos a partir de la segunda línea.
                    while ((line = bufferedReader.readLine()) != null) {
                        data = line.split(";");
                        if (data != null) { //El segundo condicional se va a cumplir siempre. Podemos quitarlo
                            int id = Integer.parseInt(data[0]);
                            numeroDatosLeidos++;
                            publishProgress((int) ((numeroDatosLeidos / datosALeer) * 100));
                            String nombre_area = data[1];
                            numeroDatosLeidos++;
                            publishProgress((int) ((numeroDatosLeidos / datosALeer) * 100));
                            String nombre_hospital = data[2];
                            numeroDatosLeidos++;
                            publishProgress((int) ((numeroDatosLeidos / datosALeer) * 100));
                            String ubicacion = data[3];
                            numeroDatosLeidos++;
                            publishProgress((int) ((numeroDatosLeidos / datosALeer) * 100));
                            long telefono = Long.parseLong(data[4]);
                            numeroDatosLeidos++;
                            publishProgress((int) ((numeroDatosLeidos / datosALeer) * 100));
                            List<Integer> listaPruebas = new ArrayList<>();
                            List<Integer> listaCasos = new ArrayList<>();
                            List<Integer> listaMuertes = new ArrayList<>();
                            for (int i=5; i<26; i++){
                                if(i < 12){
                                    listaPruebas.add(Integer.parseInt(data[i]));
                                }else if(i >= 19){
                                    listaMuertes.add(Integer.parseInt(data[i]));
                                }else{
                                    listaCasos.add(Integer.parseInt(data[i]));
                                }

                                numeroDatosLeidos++;
                                publishProgress((int) ((numeroDatosLeidos / datosALeer) * 100));
                            }

                            String imagen_hospital = data[26];
                            numeroDatosLeidos++;
                            publishProgress((int) ((numeroDatosLeidos / datosALeer) * 100));
                            String web_hospital = data[27];
                            numeroDatosLeidos++;
                            publishProgress((int) ((numeroDatosLeidos / datosALeer) * 100));
                            double latitud = Double.parseDouble(data[28]);
                            numeroDatosLeidos++;
                            publishProgress((int) ((numeroDatosLeidos / datosALeer) * 100));
                            double longitud = Double.parseDouble(data[29]);
                            numeroDatosLeidos++;
                            publishProgress((int) ((numeroDatosLeidos / datosALeer) * 100));
                            Hospital hospital = new Hospital(nombre_hospital, telefono, ubicacion, imagen_hospital, web_hospital, latitud, longitud);

                            area = new AreaSanitaria(id, nombre_area, hospital);

                            area.setListaPruebas(listaPruebas);
                            area.setListaCasos(listaCasos);
                            area.setListaMuertes(listaMuertes);

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
                    //Refrescamos la ultima fecha de actualizacion
                    fechaDataSource.insertarFechaUltimaActualizacion(ultimaActualizacion);
                    publishProgress(100);
                } else {
                    publishProgress(40);
                    publishProgress(60);
                    publishProgress(80);
                    publishProgress(100);
                }
                fechaDataSource.close();
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
    }

    public class DownloadAsturiasDataTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... voids) {

            try {
                realizarPeticionDatosAsturias();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public void realizarPeticionDatosAsturias() {
            try {

                // Obtenemos las fechas
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);

                Date date_to = cal.getTime();

                cal.add(Calendar.DATE, -7);

                Date date_from = cal.getTime();

                // Las convertimos a String para introducir en la petición de la API
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                String from = dateFormat.format(date_from);
                String to = dateFormat.format(date_to);

                URL url = new URL("https://api.covid19tracking.narrativa.com/api/country/spain/region/asturias?date_from=" + from + "&date_to=" + to);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                //Getting the response code
                int responsecode = conn.getResponseCode();

                if (responsecode != 200) {
                    throw new RuntimeException("HttpResponseCode: " + responsecode);
                } else {

                    String inline = "";
                    Scanner scanner = new Scanner(url.openStream());

                    //Write all the JSON data into a string using a scanner
                    while (scanner.hasNext()) {
                        inline += scanner.nextLine();
                    }

                    //Close the scanner
                    scanner.close();

                    //Using the JSON simple library parse the string into a json object
                    JSONObject data_obj = new JSONObject(inline);

                    JSONObject fechas = (JSONObject) data_obj.getJSONObject("dates");

                    //HashMap<String, List<Integer>> map = new HashMap<>();
                    Iterator<String> iterator = fechas.keys();
                    int count = 0;

                    while (iterator.hasNext() && count < 7) {
                        // Obtenemos el día en String
                        String dia = iterator.next();
                        // Obtenemos el objeto JSON asociado a ese día
                        JSONObject dia_obj = fechas.getJSONObject(dia);
                        // Accedemos al array regions
                        JSONObject countries = dia_obj.getJSONObject("countries");
                        JSONObject spain = countries.getJSONObject("Spain");
                        JSONArray regions = spain.getJSONArray("regions");
                        JSONObject asturias = (JSONObject) regions.get(0);

                        List<Integer> datos = new ArrayList<Integer>();
                        // Obtenemos los datos que nos interesan
                        // today_new_confirmed
                        int confirmed = Integer.parseInt(asturias.getString("today_new_confirmed"));
                        // today_new_deaths
                        int deaths = Integer.parseInt(asturias.getString("today_new_deaths"));
                        // today_new_hospitalised_patients_with_symptoms
                        int symptoms = Integer.parseInt(asturias.getString("today_new_hospitalised_patients_with_symptoms"));
                        // today_new_recovered
                        int recovered = Integer.parseInt(asturias.getString("today_new_recovered"));
                        // today_new_intensive_care
                        int uci = Integer.parseInt(asturias.getString("today_new_intensive_care"));
                        // today_new_open_cases
                        int open_cases = Integer.parseInt(asturias.getString("today_new_open_cases"));

                        datos.add(confirmed);
                        datos.add(deaths);
                        datos.add(symptoms);
                        datos.add(recovered);
                        datos.add(uci);
                        datos.add(open_cases);

                        //mapAsturias.put(dia, datos);
                        fechasAsturias.add(dia);
                        //inicializamos matriz bidimensional
                        for (int i = 0; i < 7; i++) {
                            datosAsturias.add(new ArrayList<Integer>());
                        }
                        datosAsturias.set(count, datos);

                        count++;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class DownloadEspanaDataTask extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... voids) {

            try {
                realizarPeticionDatosEspana();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public void realizarPeticionDatosEspana() {
            try {

                // Obtenemos las fechas
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -1);

                Date date_to = cal.getTime();

                cal.add(Calendar.DATE, -7);

                Date date_from = cal.getTime();

                // Las convertimos a String para introducir en la petición de la API
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                String from = dateFormat.format(date_from);
                String to = dateFormat.format(date_to);

                URL url = new URL("https://api.covid19tracking.narrativa.com/api/country/spain?date_from=" + from + "&date_to=" + to);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                //Getting the response code
                int responsecode = conn.getResponseCode();

                if (responsecode != 200) {
                    throw new RuntimeException("HttpResponseCode: " + responsecode);
                } else {

                    String inline = "";
                    Scanner scanner = new Scanner(url.openStream());

                    //Write all the JSON data into a string using a scanner
                    while (scanner.hasNext()) {
                        inline += scanner.nextLine();
                    }

                    //Close the scanner
                    scanner.close();

                    //Using the JSON simple library parse the string into a json object
                    JSONObject data_obj = new JSONObject(inline);

                    JSONObject fechas = (JSONObject) data_obj.getJSONObject("dates");

                    //HashMap<String, List<Integer>> map = new HashMap<>();
                    Iterator<String> iterator = fechas.keys();
                    int count = 0;

                    while (iterator.hasNext() && count < 7) {
                        // Obtenemos el día en String
                        String dia = iterator.next();
                        // Obtenemos el objeto JSON asociado a ese día
                        JSONObject dia_obj = fechas.getJSONObject(dia);
                        // Accedemos al array regions
                        JSONObject countries = dia_obj.getJSONObject("countries");
                        JSONObject spain = countries.getJSONObject("Spain");

                        List<Integer> datos = new ArrayList<Integer>();
                        // Obtenemos los datos que nos interesan
                        // today_new_confirmed
                        int confirmed = Integer.parseInt(spain.getString("today_new_confirmed"));
                        // today_new_deaths
                        int deaths = Integer.parseInt(spain.getString("today_new_deaths"));
                        // today_new_hospitalised_patients_with_symptoms
                        int symptoms = Integer.parseInt(spain.getString("today_new_hospitalised_patients_with_symptoms"));
                        // today_new_recovered
                        int recovered = Integer.parseInt(spain.getString("today_new_recovered"));
                        // today_new_intensive_care
                        int uci = Integer.parseInt(spain.getString("today_new_intensive_care"));
                        // today_new_open_cases
                        int open_cases = Integer.parseInt(spain.getString("today_new_open_cases"));

                        datos.add(confirmed);
                        datos.add(deaths);
                        datos.add(symptoms);
                        datos.add(recovered);
                        datos.add(uci);
                        datos.add(open_cases);

                        //mapEspana.put(dia, datos);
                        fechasEspana.add(dia);
                        //inicializamos matriz bidimensional
                        for (int i = 0; i < 7; i++) {
                            datosEspana.add(new ArrayList<Integer>());
                        }
                        datosEspana.set(count, datos);

                        count++;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public static String fecha;

    public static SharedPreferences sharedPreferencesMainActivity;

    public String areaPreferida = "-1";

    // Identificadores de activity
    private static final int GESTION_AREA_PREFERIDA = 1;
    private DrawerLayout drawer;

    private List<List<Integer>> datosAsturias = new ArrayList<>();
    private List<List<Integer>> datosEspana = new ArrayList<>();

    private ArrayList<String> fechasAsturias = new ArrayList<>();
    private ArrayList<String> fechasEspana = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargando);

        // Cargar datos de áreas sanitarias
        DownloadFilesTask task = new DownloadFilesTask();
        task.execute();

        // Cargar datos de Asturias (API)
        DownloadAsturiasDataTask taskAsturias = new DownloadAsturiasDataTask();
        taskAsturias.execute();

        // Cargar datos de España (API)
        DownloadEspanaDataTask taskEspana = new DownloadEspanaDataTask();
        taskEspana.execute();
    }

    protected void cargarView() {
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_areas, R.id.nav_asturias, R.id.nav_españa)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        AreaSanitariaDataSource dataSource = new AreaSanitariaDataSource(getApplicationContext());
        dataSource.open();

        listaAreasSanitarias = dataSource.getAllValorations();

        dataSource.close();
    }

    public List<List<Integer>> getDatosAsturias() {
        return datosAsturias;
    }

    public List<List<Integer>> getDatosEspana() {
        return datosEspana;
    }

    public List<String> getFechasAsturias() {
        return fechasAsturias;
    }

    public List<String> getFechasEspana() {
        return fechasEspana;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private int numeroDeElementos;
        private String[] titulos = new String[]{"Mapa", "Lista Áreas"};

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
            numeroDeElementos = behavior;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titulos[position];
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
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

        // Recuperar los ajustes del usuario
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

    /**
     * Opciones del menú
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    /**
     * Acción a realizar al volver de SettingsActivity
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

    private void vaciarBaseDatos() {
        MyDBHelper dbHelper = new MyDBHelper(getApplicationContext(), null, null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete FROM " + MyDBHelper.TABLA_AREAS_SANITARIAS);
        db.execSQL("delete FROM " + MyDBHelper.TABLA_FECHA);
    }

    public void clickOnItem(View v) {
        int id_area = Integer.parseInt(v.getTag().toString());
        AreaSanitaria area = listaAreasSanitarias.get(id_area - 1);
        Intent intent = new Intent(MainActivity.this, AreaSanitariaActivity.class);
        intent.putExtra(AREA_SANITARIA_SELECCIONADA, area);
        intent.putExtra(FECHA_ACTUALIZACION, fecha);
        // Transición de barrido
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    public void mostrarAcercaDe() {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("AsturCOVID V1.0");
        alert.setMessage("Aplicación desarrollada por estudiantes de Software para Dispositivos Móviles.\n\n"
                + "Esta aplicación utiliza información del Principado y de APIs externas para mostrar "
                + "información relevante sobre el estado actual de la pandemia por COVID-19.\n\n"
                + "Autores:\n· Samuel Rodríguez Ares\n· Luis Fernández Suárez\n· Sofía Suárez Fernández\n\n"
                + "Curso 2020-21");
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