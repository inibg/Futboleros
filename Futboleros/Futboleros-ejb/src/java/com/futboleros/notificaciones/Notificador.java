/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.notificaciones;

import java.util.logging.Level;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author inibg
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/ColaNotificaciones"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class Notificador implements MessageListener {
    
    private static final Logger logger = LogManager.getLogger(Notificador.class);
    
    @Resource
    private MessageDrivenContext mdc;
    
    public Notificador() {
    }
    
    @Override
    public void onMessage(Message message) {
        try {
            logger.info("Se recibio el mensaje: " + message.getBody(String.class));
        } catch (JMSException ex) {
            logger.error("error al recibir mensaje");
        }
    }
    
}
