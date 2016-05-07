/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.usuario;

import com.futboleros.dto.UsuarioDto;
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
public class UsuarioBean {

    @PersistenceContext
    private EntityManager em;
    
    protected Usuario toEntity(UsuarioDto dto){
        Usuario nuevo = new Usuario(dto.getId(), dto.getNombreCompleto(),
                dto.getNombreUsuario(), dto.getPassword(), dto.getRol(),
            dto.getEmail());
        return nuevo;
    }
    
    protected UsuarioDto toDto(Usuario ent){
        UsuarioDto nuevo = new UsuarioDto(ent.getId(), ent.getNombreCompleto(),
                    ent.getNombreUsuario(), ent.getPassword(), ent.getRol(), 
                ent.getEmail());
        return nuevo;
    }
    
    private void agregarUsuario(UsuarioDto usuario){
        Usuario nuevoUsuario = toEntity(usuario);
        em.persist(nuevoUsuario);
    }
}
