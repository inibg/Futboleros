/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.club;

import com.futboleros.dto.ClubDto;
import com.futboleros.dto.PartidoDto;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author inibg
 */
@Stateless
@LocalBean
public class PartidoBean {

    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private ClubBean clubBean;
    
    protected Partido toEntity(PartidoDto dto){
        Club local = clubBean.toEntity(dto.getClubLocal());
        Club visitante = clubBean.toEntity(dto.getClubVisitante());
        Partido ent = new Partido(dto.getId(), local, visitante, dto.getFechaPartido(),
            dto.getGolesLocal(), dto.getGolesVisitante());
        return ent;
    }
    
    protected PartidoDto toDto(Partido ent){
        ClubDto local = clubBean.toDto(ent.getClubLocal());
        ClubDto visitante = clubBean.toDto(ent.getClubVisitante());
        PartidoDto dto = new PartidoDto(ent.getId(), local, visitante, ent.getFechaPartido(),
            ent.getGolesLocal(), ent.getGolesVisitante());
        return dto;
    }
    
    public void agregarPartido(PartidoDto dto){
        Partido nuevo = toEntity(dto);
        em.persist(nuevo);
        
    }
}
