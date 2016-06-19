package com.futboleros.club;

public class ClubDto {
    
  private Long id;
  private String nombre;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public ClubDto(){}

  public ClubDto(Long id, String nombre) {
    setId(id);
    setNombre(nombre);
  }

  @Override
  public String toString() {
    return "com.futboleros.dto.ClubDto[ id=" + id + " ]";
  }
}
