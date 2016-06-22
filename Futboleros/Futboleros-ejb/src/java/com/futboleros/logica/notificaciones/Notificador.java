package com.futboleros.logica.notificaciones;

import com.futboleros.logica.club.ClubBean;
import com.futboleros.logica.club.ClubDto;
import com.futboleros.logica.partido.PartidoBean;
import com.futboleros.logica.partido.PartidoDto;
import com.futboleros.logica.usuario.UsuarioBean;
import com.futboleros.logica.usuario.UsuarioDto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", 
            propertyValue = "jms/ColaNotificaciones"),
    @ActivationConfigProperty(propertyName = "destinationType", 
            propertyValue = "javax.jms.Queue")
    })
public class Notificador implements MessageListener {
    
  private static final Logger logger = LogManager.getLogger(Notificador.class);
  
  @EJB
  PartidoBean pb;
  
  @EJB
  ClubBean cb;

  @EJB
  UsuarioBean ub;
  
  @Resource
  private MessageDrivenContext mdc;
    
  public Notificador() {}
    
  @Override
  public void onMessage(Message message) {
    String mensaje;
    PartidoDto partidoDto;
    ClubDto clubVisitante;
    ClubDto clubLocal;
    try {
      mensaje = message.getBody(String.class);
      logger.info("Se recibio el mensaje: " + mensaje);
    } catch (JMSException ex) {
      logger.error("error al recibir mensaje");
      return;
    }
    Long idPartido;
    if (mensaje != null) {
      idPartido = Long.parseLong(mensaje);
    } else {
      logger.error("No se pudo procesar el mensaje");
      return;
    }
    try {
      partidoDto = pb.obtenerPartidoPorId(idPartido);
    } catch (Exception ex) {
      logger.error("No se encontro partido con id: " + idPartido);
      return;
    }
    try {
      clubVisitante = cb.obtenerClubPorId(partidoDto.getClubVisitante());
    } catch (Exception ex) {
      logger.error("No se encontro club con id: " + partidoDto.getClubVisitante());
      return;
    }
    try {
      clubLocal = cb.obtenerClubPorId(partidoDto.getClubLocal());
    } catch (Exception ex) {
      logger.error("No se encontro club con id: " + partidoDto.getClubLocal());
      return;
    }
    try {
      List<UsuarioDto> usuariosLocales = ub.obtenerSeguidoresDeUnClub(clubLocal.getId());
      for (UsuarioDto d : usuariosLocales) {
        logger.info(d.getEmail());
      }
      List<UsuarioDto> usuariosVisitantes = ub.obtenerSeguidoresDeUnClub(clubVisitante.getId());
      for (UsuarioDto d : usuariosVisitantes) {
        logger.info(d.getEmail());
      }
    } catch (Exception ex ) {
      logger.error("error no especificado " + ex.getMessage());
    }
  }
    
}
