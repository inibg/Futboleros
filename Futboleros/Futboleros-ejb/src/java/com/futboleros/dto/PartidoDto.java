package com.futboleros.dto;

import java.util.Date;

/**
 *
 * @author inibg
 */
public class PartidoDto {
    private Long id;
    private ClubDto clubLocal;
    private ClubDto clubVisitante;
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

    public ClubDto getClubLocal() {
        return clubLocal;
    }

    public void setClubLocal(ClubDto clubLocal) {
        this.clubLocal = clubLocal;
    }

    public ClubDto getClubVisitante() {
        return clubVisitante;
    }

    public void setClubVisitante(ClubDto clubVisitante) {
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
    
    public PartidoDto(Long id, ClubDto local, ClubDto visitante, Date fechaPartido,
            Integer golesLocal, Integer golesVisitante){
        setId(id);
        setClubLocal(clubLocal);
        setClubVisitante(clubVisitante);
        setFechaPartido(fechaPartido);
        setGolesLocal(golesLocal);
        setGolesVisitante(golesVisitante);
    }
    
}
