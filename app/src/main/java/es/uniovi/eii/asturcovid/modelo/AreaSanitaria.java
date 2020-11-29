package es.uniovi.eii.asturcovid.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class AreaSanitaria implements Parcelable {

    private int id;
    private String nombre_area;
    private Hospital hospital;
    private int casos_totales;
    private int casos_hoy;
    private int numero_incidencia;

    public AreaSanitaria(){}

    public AreaSanitaria(int id, String nombre_area, Hospital hospital, int casos_totales, int casos_hoy) {
        this.id = id;
        this.nombre_area = nombre_area;
        this.hospital = hospital;
        this.casos_totales = casos_totales;
        this.casos_hoy = casos_hoy;
    }

    protected AreaSanitaria(Parcel in) {
        id = in.readInt();
        nombre_area = in.readString();
        hospital = in.readParcelable(Hospital.class.getClassLoader());
        casos_totales = in.readInt();
        casos_hoy = in.readInt();
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

    public int getCasos_totales() {
        return casos_totales;
    }

    public int getCasos_hoy() {
        return casos_hoy;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre_area);
        dest.writeParcelable(hospital, flags);
        dest.writeInt(casos_totales);
        dest.writeInt(casos_hoy);
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

    public void setCasos_totales(int casos_totales) {
        this.casos_totales = casos_totales;
    }

    public void setCasos_hoy(int casos_hoy) {
        this.casos_hoy = casos_hoy;
    }

    public int getNumero_incidencia() {
        return numero_incidencia;
    }

    public void setNumero_incidencia(int numero_incidencia) {
        this.numero_incidencia = numero_incidencia;
    }
}
