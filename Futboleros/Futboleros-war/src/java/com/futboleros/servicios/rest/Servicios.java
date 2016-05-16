/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.servicios.rest;

import com.futboleros.club.ClubBean;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author inibg
 */

@Path("Servicios")
@RequestScoped
public class Servicios {
    
    @Context
    private UriInfo context;
    @EJB
    private ClubBean cb;
    
    @GET
    @Path("/sayHello")
    @Produces(MediaType.TEXT_PLAIN)
    public String HelloWorld(){
        return "Hola Mundo";
    }
    
}
