package es.uniovi.eii.asturcovid.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.asturcovid.modelo.AreaSanitaria;
import es.uniovi.eii.asturcovid.modelo.Hospital;

public class AreaSanitariaDataSource {
    /**
     * Referencia para manejar la base de datos. Este objeto lo obtenemos a partir de MyDBHelper
     * y nos proporciona metodos para hacer operaciones
     * CRUD (create, read, update and delete)
     */
    private SQLiteDatabase database;

    /**
     * Referencia al helper que se encarga de crear y actualizar la base de datos.
     */
    private MyDBHelper dbHelper;


    /**
     * Columnas de la tabla
     */
    private final String[] allColumns = { MyDBHelper.COLUMNA_ID_AREAS_SANITARIAS, MyDBHelper.COLUMNA_NOMBRE_AREA_AREAS_SANITARIAS,
            MyDBHelper.COLUMNA_NOMBRE_HOSPITAL_AREAS_SANITARIAS, MyDBHelper.COLUMNA_DIRECCION_HOSPITAL_AREAS_SANITARIAS,
            MyDBHelper.COLUMNA_TELEFONO_AREA_AREAS_SANITARIAS, MyDBHelper.COLUMNA_CASOS_TOTALES_AREAS_SANITARIAS,
            MyDBHelper.COLUMNA_CASOS_HOY_AREAS_SANITARIAS, MyDBHelper.COLUMNA_IMAGEN_HOSPITAL_AREAS_SANITARIAS, MyDBHelper.COLUMNA_WEB_HOSPITAL_AREAS_SANITARIAS
    };

    /**
     * Constructor.
     *
     * @param context
     */
    public AreaSanitariaDataSource(Context context) {
        // el último parámetro es la versión
        dbHelper = new MyDBHelper(context, null, null, 1);
    }

    /**
     * Abre una conexion para escritura con la base de datos.
     * Esto lo hace a traves del helper con la llamada a getWritableDatabase. Si la base de
     * datos no esta creada, el helper se encargara de llamar a onCreate
     *
     * @throws SQLException
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        //database.execSQL(dbHelper.DATABASE_DROP_AREAS_SANITARIAS);
        //database.execSQL(dbHelper.CREATE_TABLA_AREAS_SANITARIAS);
    }

    /**
     * Cierra la conexion con la base de datos
     */
    public void close() {
        dbHelper.close();
    }

    public long createAreaSanitaria(AreaSanitaria areaSanitariaToInsert) {
        // Establecemos los valores que se insertaran
        ContentValues values = new ContentValues();

        values.put(MyDBHelper.COLUMNA_ID_AREAS_SANITARIAS, areaSanitariaToInsert.getId());
        values.put(MyDBHelper.COLUMNA_NOMBRE_AREA_AREAS_SANITARIAS, areaSanitariaToInsert.getNombre_area());
        values.put(MyDBHelper.COLUMNA_NOMBRE_HOSPITAL_AREAS_SANITARIAS, areaSanitariaToInsert.getHospital().getNombre_hospital());
        values.put(MyDBHelper.COLUMNA_DIRECCION_HOSPITAL_AREAS_SANITARIAS, areaSanitariaToInsert.getHospital().getDireccion_hospital());
        values.put(MyDBHelper.COLUMNA_TELEFONO_AREA_AREAS_SANITARIAS, areaSanitariaToInsert.getHospital().getTelefono());
        values.put(MyDBHelper.COLUMNA_CASOS_TOTALES_AREAS_SANITARIAS, areaSanitariaToInsert.getCasos_totales());
        values.put(MyDBHelper.COLUMNA_CASOS_HOY_AREAS_SANITARIAS, areaSanitariaToInsert.getCasos_hoy());
        values.put(MyDBHelper.COLUMNA_IMAGEN_HOSPITAL_AREAS_SANITARIAS, areaSanitariaToInsert.getHospital().getImagen_hospital());
        values.put(MyDBHelper.COLUMNA_WEB_HOSPITAL_AREAS_SANITARIAS, areaSanitariaToInsert.getHospital().getWeb_hospital());

        // Insertamos la valoracion
        long insertId = database.insert(MyDBHelper.TABLA_AREAS_SANITARIAS, null, values);

        return insertId;
    }

    /**
     * Obtiene todas las valoraciones andadidas por los usuarios. Sin ninguna restricción SQL
     *
     * @return Lista de objetos de tipo Pelicula
     *
     */
    public List<AreaSanitaria> getAllValorations() {
        // Lista que almacenara el resultado
        List<AreaSanitaria> areaSanitariaList = new ArrayList<AreaSanitaria>();
        //hacemos una query porque queremos devolver un cursor
        Cursor cursor = database.query(MyDBHelper.TABLA_AREAS_SANITARIAS, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            final AreaSanitaria areaSanitaria = new AreaSanitaria();
            areaSanitaria.setId(cursor.getInt(0));
            areaSanitaria.setNombre_area(cursor.getString(1));
            String nombre = cursor.getString(2);
            String ubicacion = cursor.getString(3);
            Long telefono = cursor.getLong(4);
            areaSanitaria.setCasos_totales(cursor.getInt(5));
            areaSanitaria.setCasos_hoy(cursor.getInt(6));
            String imagen = cursor.getString(7);
            String web_hospital = cursor.getString(8);

            areaSanitaria.setHospital(new Hospital(nombre,telefono,ubicacion, imagen, web_hospital));

            areaSanitariaList.add(areaSanitaria);
            cursor.moveToNext();
        }

        cursor.close();

        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.
        return areaSanitariaList;
    }
}
