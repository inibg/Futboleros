package com.futboleros.dto;

import java.util.Date;

/**
 *
 * @author inibg
 */
public class PartidoDto {
    private Long id;
    private Long clubLocal;
    private Long clubVisitante;
    private Date fechaPartido;
    private Integer golesLocal;
    private Integer golesVisitante;
    private String resultado;
    private ClubDto ganador;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClubLocal() {
        return clubLocal;
    }

    public void setClubLocal(Long clubLocal) {
        this.clubLocal=clubLocal;
    }

    public Long getClubVisitante() {
        return clubVisitante;
    }

    public void setClubVisitante(Long clubVisitante) {
        this.clubVisitante=clubVisitante;
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
    }

    public Integer getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(Integer golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public ClubDto getGanador() {
        return ganador;
    }

    public void setGanador(ClubDto ganador) {
        this.ganador = ganador;
    }
    
    public PartidoDto(){}
    
    public PartidoDto(Long id, Long local, Long visitante,/*ClubDto local, ClubDto visitante,*/ Date fechaPartido,
            Integer golesLocal, Integer golesVisitante){
        setId(id);
        setClubLocal(local);
        setClubVisitante(visitante);
        setFechaPartido(fechaPartido);
        setGolesLocal(golesLocal);
        setGolesVisitante(golesVisitante);
    }

  @Override
    public String toString(){
        return "com.futboleros.dto.PartodpDto[ id=" + id + " ]";
    }
    
}
