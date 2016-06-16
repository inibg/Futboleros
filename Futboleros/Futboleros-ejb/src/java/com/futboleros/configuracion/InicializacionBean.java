/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.configuracion;

import com.futboleros.club.ClubBean;
import com.futboleros.dto.ClubDto;
import com.futboleros.dto.ParametroDto;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author inibg
 */
@Singleton
@LocalBean
@Startup
public class InicializacionBean {

    @PersistenceContext
    private EntityManager em;
    
    @EJB
    ClubBean clubBean;
    
    @EJB
    ParametroBean parametroBean;
    
    private static final Logger logger = LogManager.getLogger(InicializacionBean.class);
    
    @PostConstruct
    public void inicializarDb(){
        logger.info("EjecutaInicializar");
        cargarClubes();
     //   cargarParametros();
    }
    
    
    private void cargarClubes(){
   
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
    
    public InicializacionBean(){
    }
    
    private void cargarParametros(){
        ParametroDto nuevoPar = new ParametroDto(0L, "Version","1.1", false);
        parametroBean.agregarParametro(nuevoPar);
    }
}
