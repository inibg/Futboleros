package com.futboleros.usuario;

import java.util.Date;

public class SesionDto {
  private Long id;
  private String token;
  private UsuarioDto usuarioDto;
  private Date fechaInicio;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UsuarioDto getUsuarioDto() {
    return usuarioDto;
  }

  public void setUsuarioDto(UsuarioDto usuarioDto) {
    this.usuarioDto = usuarioDto;
  }

  public Date getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(Date fechaInicio) {
    this.fechaInicio = fechaInicio;
  }
    
  public SesionDto() {}
  
  public SesionDto(Long id, String token, UsuarioDto usuarioDto,Date fechaInicio) {
    setId(id);
    setToken(token);
    setFechaInicio(fechaInicio);
    setUsuarioDto(usuarioDto);
  }
  
  @Override
  public String toString() {
    return "com.futboleros.dto.SesionDto[ id=" + id + " ]";
  }
  
}
