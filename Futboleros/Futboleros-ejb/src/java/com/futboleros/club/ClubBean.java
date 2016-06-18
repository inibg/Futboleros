/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.club;

import java.util.List;
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
public class ClubBean {

    @PersistenceContext
    private EntityManager em;
    
    private static final Logger logger = LogManager.getLogger(ClubBean.class);
    
    public Club toEntity(ClubDto dto){
        Club nuevo = new Club(dto.getId(), dto.getNombre());
        return nuevo;
    }
    
    public ClubDto toDto(Club ent){
        ClubDto nuevo = new ClubDto(ent.getId(), ent.getNombre());
        return nuevo;
    }
    
    public Long agregarClub(ClubDto club){
        logger.info("intentando guardar el club " + club.getNombre());
        Club nuevoClub = toEntity(club);
        em.persist(nuevoClub);
        return nuevoClub.getId();
    }
    
    public ClubDto obtenerClubPorNombre(String nombre){
        Club buscado = em.createNamedQuery("obtenerClubPorNombre",
                Club.class).setParameter("nombreclub", nombre).getSingleResult();
        if (buscado==null){
            return null;
        }else{
            return toDto(buscado);
        }
    }
    
    public ClubDto obtenerClubPorId(Long id){
        Club buscado = em.find(Club.class, id);
        if (buscado==null){
            return null;
        }else{
            return toDto(buscado);
        }
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
    
    public Boolean eliminarClub(Long idclub){
       logger.info("intentando eliminar el club " + idclub);
        ClubDto club= this.obtenerClubPorId(idclub);
        if (club != null) {
            em.createNamedQuery("eliminarClub",
            Club.class).setParameter("idclub", idclub).executeUpdate();
            return true;
        }else{
             return false;
        }
    }
    public void modificarNombreClub(Long idclub,String nombreClub){
       logger.info("intentando modificar el club " + idclub);
       em.createNamedQuery("modificarNombreClub",
            Club.class).setParameter("idclub", idclub)
               .setParameter("nombreClub",nombreClub).executeUpdate(); 
        
    }
    
}
