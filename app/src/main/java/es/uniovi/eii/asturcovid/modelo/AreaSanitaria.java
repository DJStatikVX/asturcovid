package es.uniovi.eii.asturcovid.modelo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AreaSanitaria implements Parcelable {

    private int id;
    private String nombre_area;
    private Hospital hospital;
    private int numero_incidencia;
    private List<Integer> listaPruebas;
    private List<Integer> listaCasos;
    private List<Integer> listaMuertes;

    public AreaSanitaria(){}

    public AreaSanitaria(int id, String nombre_area, Hospital hospital) {
        this.id = id;
        this.nombre_area = nombre_area;
        this.hospital = hospital;
        listaPruebas = new ArrayList<>();
        listaCasos = new ArrayList<>();
        listaMuertes = new ArrayList<>();
    }

    protected AreaSanitaria(Parcel in) {
        id = in.readInt();
        nombre_area = in.readString();
        hospital = in.readParcelable(Hospital.class.getClassLoader());
        listaPruebas = in.readArrayList(Integer.class.getClassLoader());
        listaCasos = in.readArrayList(Integer.class.getClassLoader());
        listaMuertes = in.readArrayList(Integer.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public String getNombre_area() {
        return nombre_area;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public List<Integer> getListaPruebas() {
        return listaPruebas;
    }

    public List<Integer> getListaCasos() {
        return listaCasos;
    }

    public List<Integer> getListaMuertes() {
        return listaMuertes;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre_area);
        dest.writeParcelable(hospital, flags);
        dest.writeList(listaPruebas);
        dest.writeList(listaCasos);
        dest.writeList(listaMuertes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AreaSanitaria> CREATOR = new Creator<AreaSanitaria>() {
        @Override
        public AreaSanitaria createFromParcel(Parcel in) {
            return new AreaSanitaria(in);
        }

        @Override
        public AreaSanitaria[] newArray(int size) {
            return new AreaSanitaria[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre_area(String nombre_area) {
        this.nombre_area = nombre_area;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public int getNumero_incidencia() {
        return numero_incidencia;
    }

    public void setNumero_incidencia(int numero_incidencia) {
        this.numero_incidencia = numero_incidencia;
    }

    public void setListaPruebas(List<Integer> listaPruebas) {
        this.listaPruebas = listaPruebas;
    }

    public void setListaCasos(List<Integer> listaCasos) {
        this.listaCasos = listaCasos;
    }

    public void setListaMuertes(List<Integer> listaMuertes) {
        this.listaMuertes = listaMuertes;
    }

    public int getCasosTotales() {
        int casos_totales = 0;
        for(int i=0; i < this.getListaCasos().size(); i++){
            casos_totales += this.getListaCasos().get(i);
        }
        return casos_totales;
    }
}
