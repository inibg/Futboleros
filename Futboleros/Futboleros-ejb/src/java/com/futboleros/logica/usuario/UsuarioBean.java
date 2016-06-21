package com.futboleros.logica.usuario;

import com.futboleros.persistencia.usuario.Usuario;
import com.futboleros.persistencia.club.Club;
import com.futboleros.logica.club.ClubBean;
import com.futboleros.logica.club.ClubDto;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class UsuarioBean {

  @PersistenceContext
  private EntityManager em;
    
  @EJB
  private ClubBean cb;

  public Usuario toEntity(UsuarioDto dto) {
    Usuario nuevo = new Usuario(dto.getId(), dto.getNombreCompleto(),
            dto.getNombreUsuario(), dto.getRol(), dto.getEmail());
    return nuevo;
  }
    
  public UsuarioDto toDto(Usuario ent) {
    List<ClubDto> clubesDto = convertirClubes(ent.getClubesSeguidos());
    UsuarioDto nuevo = new UsuarioDto(ent.getId(), ent.getNombreCompleto(),
                ent.getNombreUsuario(), ent.getRol(), ent.getEmail(), clubesDto);
    return nuevo;
  }
    
  private List<ClubDto> convertirClubes(List<Club> clubes) {
    List<ClubDto> convertidos = new ArrayList();
    for (Club club : clubes) {
      convertidos.add(cb.toDto(club));
    }
    return convertidos;
  }
    
  public Long agregarUsuario(UsuarioDto usuario) throws Exception {
    try {
      Usuario nuevoUsuario = toEntity(usuario);
      em.persist(nuevoUsuario);
      return nuevoUsuario.getId();
    } catch (Exception ex) {
      throw ex;
    }
  }
    
  @SuppressWarnings("unchecked")
  public List<UsuarioDto> obtenerTodosLosUsuarios() {
    List<UsuarioDto> usuarios = null;
    try {
      usuarios  = (List<UsuarioDto>) em.createNamedQuery("obtenerTodosLosUsuarios").getResultList();
    } catch (Exception ex) {
      System.out.println("Excepcion al obtener todos los usuarios " + ex.getMessage());
    }
    return usuarios;
  }
    
  public UsuarioDto obtenerUsuarioPorId(Long id) {
    Usuario buscado = em.find(Usuario.class, id);
    return toDto(buscado);
  }
    
  public UsuarioDto obtenerUsuarioPorNombre(String nombre) throws Exception {
    try {
      Usuario buscado = em.createNamedQuery("obtenerUsuarioPorNombre",
            Usuario.class).setParameter("nombreUsuario", nombre).getSingleResult();
      return toDto(buscado);
    } catch (Exception ex) {
      throw ex;
    }
  }
    
  public void convertirUsuarioAdmin(String nombre) throws Exception {
    try {
      em.createNamedQuery("hacerUsuarioAdmin").setParameter("nombreUsuario",
            nombre).setParameter("rol", Rol.ADMINISTRADOR).executeUpdate();
    } catch (Exception ex) {
      throw ex;
    }
  }
    
  public List<UsuarioDto> obtenerSeguidoresDeUnClub(Long idClub) throws Exception {
    List<Usuario> usuarios;
    List<UsuarioDto> retorno = new ArrayList();
    try {
      ClubDto cd = cb.obtenerClubPorId(idClub); 
      Club ce = cb.toEntity(cd);
      usuarios = (List<Usuario>) em.createNamedQuery("obtenerUsuariosPorClubSeguido")
              .setParameter("club", ce).getResultList();
      for (Usuario u : usuarios) {
        retorno.add(toDto(u));
      }
    } catch (Exception ex) {
      throw ex;
    }
    return retorno;
  }
    
  public void seguirClub(Long usuarioId, ClubDto clubDto) {
    Usuario buscado = em.find(Usuario.class, usuarioId);
    Club club = cb.toEntity(clubDto);
    if (!buscado.getClubesSeguidos().contains(club)) {
      buscado.getClubesSeguidos().add(club);
      em.persist(buscado);
    }
  }
  
}
