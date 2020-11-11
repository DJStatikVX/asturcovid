package es.uniovi.eii.asturcovid.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Hospital implements Parcelable {

    private String nombre_hospital;
    private long telefono;
    private String direccion_hospital;
    private String imagen_hospital;
    private String web_hospital;



    public Hospital(String nombre_hospital, long telefono, String direccion_hospital, String imagen_hospital, String web_hospital) {
        this.nombre_hospital = nombre_hospital;
        this.telefono = telefono;
        this.direccion_hospital = direccion_hospital;
        this.imagen_hospital = imagen_hospital;
        this.web_hospital = web_hospital;
    }

    public String getNombre_hospital() {
        return nombre_hospital;
    }

    public long getTelefono() {
        return telefono;
    }

    public String getImagen_hospital() {
        return imagen_hospital;
    }

    public String getDireccion_hospital() {
        return direccion_hospital;
    }

    public String getWeb_hospital() {
        return web_hospital;
    }

    protected Hospital(Parcel in) {
        nombre_hospital = in.readString();
        telefono = in.readLong();
        direccion_hospital = in.readString();
        imagen_hospital = in.readString();
        web_hospital = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombre_hospital);
        dest.writeLong(telefono);
        dest.writeString(direccion_hospital);
        dest.writeString(imagen_hospital);
        dest.writeString(web_hospital);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        @Override
        public Hospital createFromParcel(Parcel in) {
            return new Hospital(in);
        }

        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };
}
