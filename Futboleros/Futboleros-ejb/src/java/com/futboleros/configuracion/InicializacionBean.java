package com.futboleros.configuracion;

import com.futboleros.club.ClubBean;
import com.futboleros.club.ClubDto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Singleton
@LocalBean
@Startup
public class InicializacionBean {

  @PersistenceContext
  private EntityManager em;
   
  @EJB
  ClubBean clubBean;
        
  private static final Logger logger = LogManager.getLogger(InicializacionBean.class);
    
  @PostConstruct
  public void inicializarDb() {
    logger.info("EjecutaInicializar");
    cargarClubes();
  }
    
    
  private void cargarClubes() {
    ClubDto nuevo = new ClubDto(0L, "Peñarol");
    clubBean.agregarClub(nuevo);
    nuevo = new ClubDto(0L, "Nacional");
    clubBean.agregarClub(nuevo);
    nuevo = new ClubDto(0L, "Danubio");
    clubBean.agregarClub(nuevo);
    nuevo = new ClubDto(0L, "Defensor");
    clubBean.agregarClub(nuevo);
    nuevo = new ClubDto(0L, "Wanderers");
    clubBean.agregarClub(nuevo);
    nuevo = new ClubDto(0L, "Cerro");
    clubBean.agregarClub(nuevo);
    nuevo = new ClubDto(0L, "Plaza Colonia");
    clubBean.agregarClub(nuevo);
    nuevo = new ClubDto(0L, "Rentistas");
    clubBean.agregarClub(nuevo);
    nuevo = new ClubDto(0L, "Fenix");
    clubBean.agregarClub(nuevo);
    nuevo = new ClubDto(0L, "Liverpool");
    clubBean.agregarClub(nuevo);
    nuevo = new ClubDto(0L, "Tanque Sisley");
    clubBean.agregarClub(nuevo);
    nuevo = new ClubDto(0L, "Juventud de Las Piedras");
    clubBean.agregarClub(nuevo);
    nuevo = new ClubDto(0L, "River Plate");
    clubBean.agregarClub(nuevo);
    nuevo = new ClubDto(0L, "Villa Teresa");
    clubBean.agregarClub(nuevo);
  }
    
  public InicializacionBean() {}
    
}
