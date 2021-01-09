package es.uniovi.eii.asturcovid.datos;

public class DatosAreaSanitariaCovidFecha {
    private String fecha;
    private int confirmados;
    private int fallecidos;
    private int pruebas;

    public DatosAreaSanitariaCovidFecha(String fecha, int confirmados, int fallecidos, int pruebas) {
        this.fecha = fecha;
        this.confirmados = confirmados;
        this.fallecidos = fallecidos;
        this.pruebas = pruebas;
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

    public int getPruebas() {
        return pruebas;
    }
}
