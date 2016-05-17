package com.futboleros.usuario;

import com.futboleros.dto.UsuarioDto;
import java.util.List;
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
                dto.getNombreUsuario(), dto.getRol(),
            dto.getEmail());
        return nuevo;
    }
    
    protected UsuarioDto toDto(Usuario ent){
        UsuarioDto nuevo = new UsuarioDto(ent.getId(), ent.getNombreCompleto(),
                    ent.getNombreUsuario(), ent.getRol(), 
                ent.getEmail());
        return nuevo;
    }
    
    private Long agregarUsuario(UsuarioDto usuario){
        Usuario nuevoUsuario = toEntity(usuario);
        em.persist(nuevoUsuario);
        return nuevoUsuario.getId();
    }
    
    @SuppressWarnings("unchecked")
    public List<UsuarioDto> obtenerTodosLosUsuarios(){
        List<UsuarioDto> usuarios = null;
        try{
           usuarios  = (List<UsuarioDto>) em.createNamedQuery("obtenerTodosLosUsuarios").getResultList();
        }catch(Exception e){
            System.out.println("Excepcion al obtener todos los usuarios " + e.getMessage());
        }
        return usuarios;
    }
}
