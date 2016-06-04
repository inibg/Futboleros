package com.futboleros.servicios.rest;

/**
 *
 * @author inibg
 */
public class MensajeResponse {
    
    private Boolean exito;
    private String Mensaje;

    public Boolean getExito() {
        return exito;
    }

    private void setExito(Boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return Mensaje;
    }

    private void setMensaje(String Mensaje) {
        this.Mensaje = Mensaje;
    }
    
    public MensajeResponse(Boolean exito, String Mensaje){
        setExito(exito);
        setMensaje(Mensaje);
    }
}
