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
    
    public Long agregarUsuario(UsuarioDto usuario) throws Exception{
        try{
            Usuario nuevoUsuario = toEntity(usuario);
            em.persist(nuevoUsuario);
            return nuevoUsuario.getId();
        }catch(Exception e){
            throw e;
        }
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
    
    public UsuarioDto obtenerUsuarioPorId(Long id){
        Usuario buscado = em.find(Usuario.class, id);
        return toDto(buscado);
    }
    
    public UsuarioDto obtenerUsuarioPorNombre(String nombre){
        Usuario buscado = em.createNamedQuery("obtenerUsuarioPorNombre",
                Usuario.class).setParameter("nombreUsuario", nombre).getSingleResult();
        return toDto(buscado);
    }
}
