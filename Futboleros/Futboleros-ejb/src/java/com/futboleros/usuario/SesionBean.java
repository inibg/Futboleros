package com.futboleros.usuario;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class SesionBean {

  @PersistenceContext
  private EntityManager em;

  @EJB
  private UsuarioBean usuarioBean;

  private Sesion toEntity(SesionDto dto) {
    Usuario usuario = usuarioBean.toEntity(dto.getUsuarioDto());
    Sesion nuevo = new Sesion(dto.getId(), dto.getToken(), usuario, dto.getFechaInicio());
    return nuevo;
  }

  private SesionDto toDto(Sesion ent) {
    UsuarioDto usuarioDto = usuarioBean.toDto(ent.getUsuario());
    SesionDto nuevo = new SesionDto(ent.getId(), ent.getToken(),
                usuarioDto, ent.getFechaInicio());
    return nuevo;
  }
    
  public void iniciarSesion(SesionDto sesion) throws Exception {
    try {
      Sesion nuevaSesion = toEntity(sesion);
      em.persist(nuevaSesion);
    } catch (Exception ex) {
      throw ex;
    }
  }
    
  public boolean sesionValida(String sesionToken) {
    try {
      Sesion buscado = em.createNamedQuery("ObtenerSesionPorToken",
           Sesion.class).setParameter("sesionToken", sesionToken).getSingleResult();
      if (buscado != null) {
        return true;
      }
    } catch (Exception ex) {
      return false;
    }
    return false;                   
  }
    
  public SesionDto obtenerSesionPorToken(String sesionToken) {
    try {
      Sesion buscado = em.createNamedQuery("ObtenerSesionPorToken",
           Sesion.class).setParameter("sesionToken", sesionToken).getSingleResult();
      return toDto(buscado);
    } catch (Exception ex) {
      return null;
    }
  }
    
  public boolean terminarSesion(String sesionToken) {
    try {
      Sesion buscado = em.createNamedQuery("ObtenerSesionPorToken",
           Sesion.class).setParameter("sesionToken", sesionToken).getSingleResult();
      if (buscado != null) {
        em.createNamedQuery("TerminarSesionPorId").setParameter("sesionId",
                buscado.getId()).executeUpdate();
        return true;
      } else {
        return false;
      }
    } catch (Exception ex) {
      return false;
    }
  }
    
  public SesionDto obtenerSesionPorUsuario(UsuarioDto usuario) throws Exception {
    Usuario ue = usuarioBean.toEntity(usuario);
    try {
      Sesion buscado = em.createNamedQuery("ObtenerSesionPorUsuario",
            Sesion.class).setParameter("usuario", ue).getSingleResult();
      return toDto(buscado);
    } catch (Exception ex) {
      throw ex;
    }
  }
    
}
