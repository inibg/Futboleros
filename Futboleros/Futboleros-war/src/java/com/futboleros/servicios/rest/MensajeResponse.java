package com.futboleros.servicios.rest;

public class MensajeResponse {

  private Boolean exito;
  private String mensaje;

  public Boolean getExito() {
    return exito;
  }

  private void setExito(Boolean exito) {
    this.exito = exito;
  }

  public String getMensaje() {
    return mensaje;
  }

  private void setMensaje(String mensaje) {
    this.mensaje = mensaje;
  }

  public MensajeResponse(Boolean exito, String mensaje) {

    setExito(exito);
    setMensaje(mensaje);
  }
    
  public MensajeResponse() {}
  
}
