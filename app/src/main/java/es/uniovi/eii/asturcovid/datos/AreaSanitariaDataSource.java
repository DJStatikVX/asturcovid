package es.uniovi.eii.asturcovid.datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import es.uniovi.eii.asturcovid.modelo.AreaSanitaria;
import es.uniovi.eii.asturcovid.modelo.Hospital;

public class AreaSanitariaDataSource {
    /***
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
            MyDBHelper.COLUMNA_TELEFONO_AREA_AREAS_SANITARIAS,
            MyDBHelper.COLUMNA_PRUEBAS0_AREAS_SANITARIAS,MyDBHelper.COLUMNA_PRUEBAS1_AREAS_SANITARIAS,MyDBHelper.COLUMNA_PRUEBAS2_AREAS_SANITARIAS,MyDBHelper.COLUMNA_PRUEBAS3_AREAS_SANITARIAS,MyDBHelper.COLUMNA_PRUEBAS4_AREAS_SANITARIAS,MyDBHelper.COLUMNA_PRUEBAS5_AREAS_SANITARIAS,MyDBHelper.COLUMNA_PRUEBAS6_AREAS_SANITARIAS,
            MyDBHelper.COLUMNA_CASOS0_AREAS_SANITARIAS, MyDBHelper.COLUMNA_CASOS1_AREAS_SANITARIAS,MyDBHelper.COLUMNA_CASOS2_AREAS_SANITARIAS,MyDBHelper.COLUMNA_CASOS3_AREAS_SANITARIAS,MyDBHelper.COLUMNA_CASOS4_AREAS_SANITARIAS,MyDBHelper.COLUMNA_CASOS5_AREAS_SANITARIAS, MyDBHelper.COLUMNA_CASOS6_AREAS_SANITARIAS,
            MyDBHelper.COLUMNA_MUERTES0_AREAS_SANITARIAS, MyDBHelper.COLUMNA_MUERTES1_AREAS_SANITARIAS, MyDBHelper.COLUMNA_MUERTES2_AREAS_SANITARIAS, MyDBHelper.COLUMNA_MUERTES3_AREAS_SANITARIAS, MyDBHelper.COLUMNA_MUERTES4_AREAS_SANITARIAS, MyDBHelper.COLUMNA_MUERTES5_AREAS_SANITARIAS, MyDBHelper.COLUMNA_MUERTES6_AREAS_SANITARIAS,
            MyDBHelper.COLUMNA_IMAGEN_HOSPITAL_AREAS_SANITARIAS, MyDBHelper.COLUMNA_WEB_HOSPITAL_AREAS_SANITARIAS,
            MyDBHelper.COLUMNA_LATITUD_HOSPITAL_AREAS_SANITARIAS, MyDBHelper.COLUMNA_LONGITUD_HOSPITAL_AREAS_SANITARIAS
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
        values.put(MyDBHelper.COLUMNA_PRUEBAS0_AREAS_SANITARIAS, areaSanitariaToInsert.getListaPruebas().get(0));
        values.put(MyDBHelper.COLUMNA_PRUEBAS1_AREAS_SANITARIAS, areaSanitariaToInsert.getListaPruebas().get(1));
        values.put(MyDBHelper.COLUMNA_PRUEBAS2_AREAS_SANITARIAS, areaSanitariaToInsert.getListaPruebas().get(2));
        values.put(MyDBHelper.COLUMNA_PRUEBAS3_AREAS_SANITARIAS, areaSanitariaToInsert.getListaPruebas().get(3));
        values.put(MyDBHelper.COLUMNA_PRUEBAS4_AREAS_SANITARIAS, areaSanitariaToInsert.getListaPruebas().get(4));
        values.put(MyDBHelper.COLUMNA_PRUEBAS5_AREAS_SANITARIAS, areaSanitariaToInsert.getListaPruebas().get(5));
        values.put(MyDBHelper.COLUMNA_PRUEBAS6_AREAS_SANITARIAS, areaSanitariaToInsert.getListaPruebas().get(6));
        values.put(MyDBHelper.COLUMNA_CASOS0_AREAS_SANITARIAS, areaSanitariaToInsert.getListaCasos().get(0));
        values.put(MyDBHelper.COLUMNA_CASOS1_AREAS_SANITARIAS, areaSanitariaToInsert.getListaCasos().get(1));
        values.put(MyDBHelper.COLUMNA_CASOS2_AREAS_SANITARIAS, areaSanitariaToInsert.getListaCasos().get(2));
        values.put(MyDBHelper.COLUMNA_CASOS3_AREAS_SANITARIAS, areaSanitariaToInsert.getListaCasos().get(3));
        values.put(MyDBHelper.COLUMNA_CASOS4_AREAS_SANITARIAS, areaSanitariaToInsert.getListaCasos().get(4));
        values.put(MyDBHelper.COLUMNA_CASOS5_AREAS_SANITARIAS, areaSanitariaToInsert.getListaCasos().get(5));
        values.put(MyDBHelper.COLUMNA_CASOS6_AREAS_SANITARIAS, areaSanitariaToInsert.getListaCasos().get(6));
        values.put(MyDBHelper.COLUMNA_MUERTES0_AREAS_SANITARIAS, areaSanitariaToInsert.getListaMuertes().get(0));
        values.put(MyDBHelper.COLUMNA_MUERTES1_AREAS_SANITARIAS, areaSanitariaToInsert.getListaMuertes().get(1));
        values.put(MyDBHelper.COLUMNA_MUERTES2_AREAS_SANITARIAS, areaSanitariaToInsert.getListaMuertes().get(2));
        values.put(MyDBHelper.COLUMNA_MUERTES3_AREAS_SANITARIAS, areaSanitariaToInsert.getListaMuertes().get(3));
        values.put(MyDBHelper.COLUMNA_MUERTES4_AREAS_SANITARIAS, areaSanitariaToInsert.getListaMuertes().get(4));
        values.put(MyDBHelper.COLUMNA_MUERTES5_AREAS_SANITARIAS, areaSanitariaToInsert.getListaMuertes().get(5));
        values.put(MyDBHelper.COLUMNA_MUERTES6_AREAS_SANITARIAS, areaSanitariaToInsert.getListaMuertes().get(6));
        values.put(MyDBHelper.COLUMNA_IMAGEN_HOSPITAL_AREAS_SANITARIAS, areaSanitariaToInsert.getHospital().getImagen_hospital());
        values.put(MyDBHelper.COLUMNA_WEB_HOSPITAL_AREAS_SANITARIAS, areaSanitariaToInsert.getHospital().getWeb_hospital());
        values.put(MyDBHelper.COLUMNA_LATITUD_HOSPITAL_AREAS_SANITARIAS,areaSanitariaToInsert.getHospital().getLatitud());
        values.put(MyDBHelper.COLUMNA_LONGITUD_HOSPITAL_AREAS_SANITARIAS,areaSanitariaToInsert.getHospital().getLongitud());

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
            final AreaSanitaria areaSanitaria = crearAreaSanitaria(cursor);

            areaSanitariaList.add(areaSanitaria);
            cursor.moveToNext();
        }

        cursor.close();

        // Una vez obtenidos todos los datos y cerrado el cursor, devolvemos la
        // lista.
        return areaSanitariaList;
    }

    /**
     * Devuelve el área sanitaria correspondiente al ID pasado como parámetro
     * @param id ID del área sanitaria a buscar, de tipo int
     * @return Área Sanitaria
     */
    public AreaSanitaria getAreaSanitaria(int id) {
        Cursor cursor = database.rawQuery("SELECT * FROM TABLA_AREAS_SANITARIAS WHERE ID_AREA = ?", null);
        cursor.moveToFirst();

        final AreaSanitaria areaSanitaria = crearAreaSanitaria(cursor);

        cursor.close();

        return areaSanitaria;
    }

    @NotNull
    private AreaSanitaria crearAreaSanitaria(Cursor cursor) {
        final AreaSanitaria areaSanitaria = new AreaSanitaria();
        areaSanitaria.setId(cursor.getInt(0));
        areaSanitaria.setNombre_area(cursor.getString(1));
        String nombre = cursor.getString(2);
        String ubicacion = cursor.getString(3);
        Long telefono = cursor.getLong(4);

        List<Integer> listaPruebas = new ArrayList<>();
        List<Integer> listaCasos = new ArrayList<>();
        List<Integer> listaMuertes = new ArrayList<>();
        for (int i=5; i<26; i++){
            if(i < 12){
                listaPruebas.add(cursor.getInt(i));
            }else if(i >= 19){
                listaMuertes.add(cursor.getInt(i));
            }else{
                listaCasos.add(cursor.getInt(i));
            }
        }
        areaSanitaria.setListaPruebas(listaPruebas);
        areaSanitaria.setListaCasos(listaCasos);
        areaSanitaria.setListaMuertes(listaMuertes);

        String imagen = cursor.getString(26);
        String web_hospital = cursor.getString(27);
        double latitud = cursor.getDouble(28);
        double longitud = cursor.getDouble(29);

        areaSanitaria.setHospital(new Hospital(nombre,telefono,ubicacion, imagen, web_hospital,latitud,longitud));
        return areaSanitaria;
    }
}
