package com.futboleros.logica.partido;

import com.futboleros.persistencia.partido.Partido;
import com.futboleros.logica.club.ClubBean;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class PartidoBean {

  @PersistenceContext
  private EntityManager em;
  private static final Logger logger = LogManager.getLogger(PartidoBean.class);
    
  @EJB
  private ClubBean clubBean;
    
  protected Partido toEntity(PartidoDto dto) {
    Partido ent = new Partido(dto.getId(), dto.getClubLocal(), dto.getClubVisitante(),
            dto.getFechaPartido(), dto.getGolesLocal(), dto.getGolesVisitante());
    return ent; 
  }
    
  protected PartidoDto toDto(Partido ent) {
    PartidoDto dto = new PartidoDto(ent.getId(), ent.getClubLocal(), ent.getClubVisitante(),
            ent.getFechaPartido(), ent.getGolesLocal(), ent.getGolesVisitante());
    return dto;
  }
    
  public Long agregarPartido(PartidoDto dto) {
    Partido nuevo = toEntity(dto);
    em.persist(nuevo);
    return nuevo.getId();
  }
    
  public PartidoDto obtenerPartidoPorId(Long id) {
    Partido buscado = em.find(Partido.class, id);
    if (buscado == null) {
      return null;
    } else {
      return toDto(buscado);
    }
  }

  public Long actualizarResultado(PartidoDto actPardidoDto) {
    logger.info("intentando actualizar el resultado del partido" + actPardidoDto.getId());
    if (actPardidoDto.getId() != 0) {
      Partido actPartido = this.toEntity(actPardidoDto);
      em.merge(actPartido);
    }
    return actPardidoDto.getId();
  }
}