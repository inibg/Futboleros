/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.partido;

import com.futboleros.club.ClubBean;
import com.futboleros.dto.PartidoDto;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author inibg
 */
@Stateless
@LocalBean
public class PartidoBean {

    @PersistenceContext
    private EntityManager em;
    private static final Logger logger = LogManager.getLogger(PartidoBean.class);
    
    @EJB
    private ClubBean clubBean;
    
    protected Partido toEntity(PartidoDto dto){
        Partido ent = new Partido (dto.getId(), dto.getClubLocal(), dto.getClubVisitante(),
                dto.getFechaPartido(), dto.getGolesLocal(), dto.getGolesVisitante());
        return ent; 
    }
    
    protected PartidoDto toDto(Partido ent){
        PartidoDto dto = new PartidoDto(ent.getId(), ent.getClubLocal(), ent.getClubVisitante(),
                ent.getFechaPartido(),
        ent.getGolesLocal(), ent.getGolesVisitante());
        return dto;
    }
    
    public Long agregarPartido(PartidoDto dto){
        Partido nuevo = toEntity(dto);
        em.persist(nuevo);
        return nuevo.getId();
    }
    
    public PartidoDto obtenerPartidoPorId(Long id){
        Partido buscado = em.find(Partido.class, id);
        if (buscado==null){
            return null;
        }else{
            return toDto(buscado);
        }
    }

    public Long ActualizarResultado(Long idPartido, Integer golesLocal,Integer golesVisitante){
        logger.info("intentando actualizar el resultado del partido" + idPartido);
       if (idPartido!=0){
           em.createNamedQuery("ActualizarResultado",Partido.class).setParameter("idPartido",idPartido)
                   .setParameter("golesLocal", golesLocal).setParameter("golesVisitante", golesVisitante).executeUpdate();
       }
        return idPartido;
    }
}