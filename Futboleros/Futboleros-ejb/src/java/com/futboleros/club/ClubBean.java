/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.club;

import com.futboleros.dto.ClubDto;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;


/**
 *
 * @author inibg
 */
@Stateless
@LocalBean
public class ClubBean {

    @PersistenceContext
    private EntityManager em;
    
    //private static final Logger logger = LogManager.getLogger();
    
    protected Club toEntity(ClubDto dto){
        Club nuevo = new Club(dto.getId(), dto.getNombre());
        return nuevo;
    }
    
    protected ClubDto toDto(Club ent){
        ClubDto nuevo = new ClubDto(ent.getId(), ent.getNombre());
        return nuevo;
    }
    
    public Long agregarClub(ClubDto club){
     //   logger.info("intentando guardar el club " + club.getNombre());
        Club nuevoClub = toEntity(club);
        em.persist(nuevoClub);
        return nuevoClub.getId();
    }
    
    public ClubDto obtenerClubPorNombre(String nombre){
        Club buscado = em.createNamedQuery("obtenerClubPorNombre",
                Club.class).setParameter("nombreclub", nombre).getSingleResult();
        return toDto(buscado);
    }
    
    public ClubDto obtenerClubPorId(Long id){
        Club buscado = em.find(Club.class, id);
        return toDto(buscado);
    }
    
    @SuppressWarnings("unchecked")
    public List<ClubDto> obtenerTodosLosClubes(){
        List<ClubDto> clubes = null;
        try{
           clubes  = (List<ClubDto>) em.createNamedQuery("obtenerTodosLosClubes").getResultList();
        }catch(Exception e){
            System.out.println("Excepcion al obtenr todos los clubes " + e.getMessage());
        }
        return clubes;
    }
    
}
