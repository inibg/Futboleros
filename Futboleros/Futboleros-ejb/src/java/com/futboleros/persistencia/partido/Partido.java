package com.futboleros.persistencia.partido;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity 
public class Partido implements Serializable {

  private static final long serialVersionUID = 1L;
  
  @Id
  @Column(name = "PartidoId")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
    
  @NotNull
  @Column(name = "PartidoClubLocalId")
  private Long clubLocal;
    
  @Column(name = "PartidoClubVisitanteId")
  @NotNull
  private Long clubVisitante;
    
  @Temporal(TemporalType.DATE)
  @NotNull
  @Column(name = "PartidoFecha")
  private Date fechaPartido;
    
  @Column(name = "PartidoGolesLocal")
  private Integer golesLocal;
    
  @Column(name = "PartidoGolesVisitante")
  private Integer golesVisitante;
    
  @Transient
  private String resultado;
    
  @Transient
  private Long ganador;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

    
  public void setClubLocal(Long clubLocal) {
    this.clubLocal = clubLocal;
  }

  public Long getClubLocal() {
    return this.clubLocal;
  }

    
  public Long getClubVisitante() {
    return clubVisitante;
  }

  public void setClubVisitante(Long clubVisitante) {
    this.clubVisitante = clubVisitante;
  }

  public Date getFechaPartido() {
    return fechaPartido;
  }

  public void setFechaPartido(Date fechaPartido) {
    this.fechaPartido = fechaPartido;
  }

  public Integer getGolesLocal() {
    return golesLocal;
  }

  public void setGolesLocal(Integer golesLocal) {
    this.golesLocal = golesLocal;
    calcularResultado();
  }

  public Integer getGolesVisitante() {
    return golesVisitante;
  }

  public void setGolesVisitante(Integer golesVisitante) {
    this.golesVisitante = golesVisitante;
    calcularResultado();
  }

  public String getResultado() {
    return resultado;
  }

  private void setResultado(String resultado) {
    this.resultado = resultado;
  }

  public Long getGanador() {
    return ganador;
  }

  private void setGanador(Long ganador) {
    this.ganador = ganador;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof Partido)) {
      return false;
    }
    Partido other = (Partido) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.futboleros.club.Partido[ id=" + id + " ]";
  }
    
  public Partido(){}
    
  public Partido(Long id, Long local, Long visitante, Date fechaPartido,
        Integer golesLocal, Integer golesVisitante) {
    setId(id);
    setClubLocal(local);
    setClubVisitante(visitante);
    setGolesLocal(golesLocal);
    setGolesVisitante(golesVisitante);
    setFechaPartido(fechaPartido);
  }
    
  private void calcularResultado() {
    if (getGolesLocal() != null && getGolesVisitante() != null) {
      if (getGolesLocal().equals(getGolesVisitante())) {
        setResultado("EMPATE");
        setGanador(null);
      } else {
        if (getGolesLocal() > getGolesVisitante()) {
          setResultado("GANA el club con id: " + getClubLocal().toString());
          setGanador(getClubLocal());
        } else {
          setResultado("GANA el club con id: " + getClubVisitante().toString());
          setGanador(getClubVisitante());
        }
      }
    }
  }
}
