package es.uniovi.eii.asturcovid.datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Random;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AsturCovid.db";
    private static final int DATABASE_VERSION = 1;

    // Tablas
    public static final String TABLA_AREAS_SANITARIAS = "tabla_areas_sanitarias";
    public static final String TABLA_FECHA = "tabla_fecha";

    // Columnas de tabla_areas_sanitarias
    public static final String COLUMNA_ID_AREAS_SANITARIAS = "id_area";
    public static final String COLUMNA_NOMBRE_AREA_AREAS_SANITARIAS = "nombre_area";
    public static final String COLUMNA_NOMBRE_HOSPITAL_AREAS_SANITARIAS = "nombre_hospital";
    public static final String COLUMNA_DIRECCION_HOSPITAL_AREAS_SANITARIAS = "direccion_hospital";
    public static final String COLUMNA_TELEFONO_AREA_AREAS_SANITARIAS = "telefono";
    public static final String COLUMNA_CASOS_TOTALES_AREAS_SANITARIAS = "casos_totales";
    public static final String COLUMNA_CASOS_HOY_AREAS_SANITARIAS = "casos_hoy";
    public static final String COLUMNA_IMAGEN_HOSPITAL_AREAS_SANITARIAS = "imagen_hospital";
    public static final String COLUMNA_WEB_HOSPITAL_AREAS_SANITARIAS = "web_hospital";
    public static final String COLUMNA_LATITUD_HOSPITAL_AREAS_SANITARIAS = "latitud_hospital";
    public static final String COLUMNA_LONGITUD_HOSPITAL_AREAS_SANITARIAS = "longitud_hospital";

    public static final String COLUMNA_FECHA_ULTIMA_ACTUALIZACION = "fecha_ultima_actualizacion";

    /**
     * Script para crear la base datos en SQL
     */
    private static final String CREATE_TABLA_AREAS_SANITARIAS = "create table if not exists " + TABLA_AREAS_SANITARIAS
            + "( " +
            COLUMNA_ID_AREAS_SANITARIAS + " " + "integer primary key, " +
            COLUMNA_NOMBRE_AREA_AREAS_SANITARIAS + " text not null, " +
            COLUMNA_NOMBRE_HOSPITAL_AREAS_SANITARIAS + " text not null, " +
            COLUMNA_DIRECCION_HOSPITAL_AREAS_SANITARIAS + " text, " +
            COLUMNA_TELEFONO_AREA_AREAS_SANITARIAS + " long, " +
            COLUMNA_CASOS_TOTALES_AREAS_SANITARIAS + "  integer not null, " +
            COLUMNA_CASOS_HOY_AREAS_SANITARIAS + " integer not null," +
            COLUMNA_IMAGEN_HOSPITAL_AREAS_SANITARIAS + " text not null," +
            COLUMNA_WEB_HOSPITAL_AREAS_SANITARIAS + " text not null," +
            COLUMNA_LATITUD_HOSPITAL_AREAS_SANITARIAS + " double not null," +
            COLUMNA_LONGITUD_HOSPITAL_AREAS_SANITARIAS + " double not null" +
            ");";

    private static final String CREATE_TABLA_FECHA = "create table if not exists " + TABLA_FECHA +
            "( " +
            COLUMNA_FECHA_ULTIMA_ACTUALIZACION + " " + "text not null" + ");";


    private static final String DATABASE_DROP_AREAS_SANITARIAS = "DROP TABLE IF EXISTS " + TABLA_AREAS_SANITARIAS;

    private static final String DATABASE_DROP_FECHA = "DROP TABLE IF EXISTS " + TABLA_FECHA;

    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(DATABASE_DROP_AREAS_SANITARIAS);
        db.execSQL(CREATE_TABLA_AREAS_SANITARIAS);
        db.execSQL(CREATE_TABLA_FECHA);
        Log.i("ONCREATE", "EJECUTO CREACIÓN");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DROP_AREAS_SANITARIAS);
        db.execSQL(DATABASE_DROP_FECHA);
        this.onCreate(db);
    }
}
