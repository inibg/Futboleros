
package com.futboleros.usuario;

import com.futboleros.dto.SesionDto;
import com.futboleros.dto.UsuarioDto;
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
public class SesionBean {

    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private UsuarioBean usuarioBean;
    
    private Sesion toEntity(SesionDto dto){
        Usuario usuario= usuarioBean.toEntity(dto.getUsuarioDto());
        Sesion nuevo = new Sesion(dto.getId(), dto.getToken(), usuario,
        dto.getFechaInicio());
        return nuevo;
    }
    
    private SesionDto toDto(Sesion ent){
        UsuarioDto usuarioDto= usuarioBean.toDto(ent.getUsuario());
        SesionDto nuevo = new SesionDto(ent.getId(), ent.getToken(),
                    usuarioDto, ent.getFechaInicio());
        return nuevo;
    }
    
    public void iniciarSesion(SesionDto sesion) throws Exception{
        try{
            Sesion nuevaSesion = toEntity(sesion);
            em.persist(nuevaSesion);
        }catch(Exception e){
            throw e;
        }
    }
    
    public boolean sesionValida(String sesionToken){
        try{
            Sesion buscado = em.createNamedQuery("ObtenerSesionPorToken",
                 Sesion.class).setParameter("sesionToken", sesionToken).getSingleResult();
            if (buscado != null){
                return true;
            }
        }catch(Exception e){
            return false;
        }
        return false;                   
    }
    
  
    
}
