/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.club;

import com.futboleros.dto.ClubDto;
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
public class ClubBean {

    @PersistenceContext
    private EntityManager em;
    
    private Club toEntity(ClubDto dto){
        Club nuevo = new Club(dto.getId(), dto.getNombre());
        return nuevo;
    }
    
    private ClubDto toDto(Club ent){
        ClubDto nuevo = new ClubDto(ent.getId(), ent.getNombre());
        return nuevo;
    }
    
    public void agregarClub(ClubDto club){
        Club nuevoClub = toEntity(club);
        em.persist(nuevoClub);
    }
    
}
