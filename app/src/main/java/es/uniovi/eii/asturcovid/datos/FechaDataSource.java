package es.uniovi.eii.asturcovid.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.asturcovid.modelo.AreaSanitaria;
import es.uniovi.eii.asturcovid.modelo.Hospital;

public class FechaDataSource {
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
    private final String[] allColumns = { MyDBHelper.COLUMNA_FECHA_ULTIMA_ACTUALIZACION };

    /**
     * Constructor.
     *
     * @param context
     */
    public FechaDataSource(Context context) {
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
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    /**
     * Cierra la conexion con la base de datos
     */
    public void close() {
        dbHelper.close();
    }

    public long insertarFechaUltimaActualizacion(String str) {
        // Establecemos los valores que se insertaran
        ContentValues values = new ContentValues();

        values.put(MyDBHelper.COLUMNA_FECHA_ULTIMA_ACTUALIZACION, str);

        // Insertamos la valoracion
        long insertId = database.insert(MyDBHelper.TABLA_FECHA, null, values);

        return insertId;
    }

    /**
     * Obtiene todas las valoraciones andadidas por los usuarios. Sin ninguna restricción SQL
     *
     * @return Lista de objetos de tipo Pelicula
     *
     */
    public String getFechaUltimaActualizacion() {
        //hacemos una query porque queremos devolver un cursor
        Cursor cursor = database.query(MyDBHelper.TABLA_FECHA, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();

        String fecha;

        try {
            fecha = cursor.getString(0);
        }catch (CursorIndexOutOfBoundsException e){
            fecha = null;
        }

        cursor.close();

        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.
        return fecha;
    }
}
