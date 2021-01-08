package es.uniovi.eii.asturcovid.datos;

public class DatosCovidFecha {
    private String fecha;
    private int confirmados;
    private int fallecidos;
    private int hospitalizados;
    private int recuperados;
    private int uci;
    private int casos_abiertos;

    public DatosCovidFecha(String fecha, int confirmados, int fallecidos, int hospitalizados, int recuperados, int uci, int casos_abiertos) {
        this.fecha = fecha;
        this.confirmados = confirmados;
        this.fallecidos = fallecidos;
        this.hospitalizados = hospitalizados;
        this.recuperados = recuperados;
        this.uci = uci;
        this.casos_abiertos = casos_abiertos;
    }

    public String getFecha() {
        return fecha;
    }

    public int getConfirmados() {
        return confirmados;
    }

    public int getFallecidos() {
        return fallecidos;
    }

    public int getHospitalizados() {
        return hospitalizados;
    }

    public int getRecuperados() {
        return recuperados;
    }

    public int getUci() {
        return uci;
    }

    public int getCasos_abiertos() {
        return casos_abiertos;
    }
}
