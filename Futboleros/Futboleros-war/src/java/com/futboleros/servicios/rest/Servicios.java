/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.futboleros.servicios.rest;

import com.futboleros.club.ClubBean;
import com.futboleros.dto.ClubDto;
import com.futboleros.dto.UsuarioDto;
import com.futboleros.usuario.TwitterAuthentication;
import com.futboleros.usuario.UsuarioBean;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 *
 * @author inibg
 */

@Path("Servicios")
@RequestScoped
public class Servicios {
    
    private static final Logger logger = LogManager.getLogger();
    
    @Context
    private UriInfo context;
    @EJB
    private ClubBean cb;
    @EJB
    private UsuarioBean ub;
    

// <editor-fold defaultstate="collapsed" desc=" Clubes ">    
    @GET
    @Path("/Clubes")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerClubes(){
        logger.info("Invocado el servicio /Clubes");
        List<ClubDto> clubes = cb.obtenerTodosLosClubes();
        Gson gson = new Gson();
        String jsonRespuesta = gson.toJson(clubes);
        logger.info("La respuesta generada fue: " + jsonRespuesta);
        return Response.ok(jsonRespuesta).build();
    }
    
    @GET
    @Path("/Clubes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerClub(@PathParam("id") Long id){
        logger.info("Invocado el servicio /Clubes/{id}");
        ClubDto clubBuscado = cb.obtenerClubPorId(id);
        Gson gson = new Gson();
        String jsonRespuesta = gson.toJson(clubBuscado);
        logger.info("La respuesta generada fue: " + jsonRespuesta);
        return Response.ok(jsonRespuesta).build();
    }
    
    @POST
    @Path("/Clubes/nuevo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response nuevoClub(String nuevoClubJson){
        logger.info("Invocado el servicio /Clubes/nuevo");
        logger.info("Con este Json: " + nuevoClubJson);
        Gson gson = new Gson();
        ClubDto nuevoClub;
        try
        {
            nuevoClub = gson.fromJson(nuevoClubJson, ClubDto.class);
        }catch(Exception e){
            logger.error("Ocurrio un error al convertir el Json en un ClubDto " 
            + e.getMessage());
            return Response.ok("{\"exito\":0, \"mensaje\":\"El Json recibido no es correcto\"}").build();
        }
        try
        {
            Long id = cb.agregarClub(nuevoClub);
            logger.info("Se ha creado un club con id: " + id );
            return Response.ok("{\"exito\":1, \"mensaje\":\"Se ha creado un club con id: " + id + "\"}").build();
        }catch(Exception e){
            logger.error("Ocurrio un error al grabar el club " 
            + e.getMessage());
            return Response.ok("{\"exito\":0, \"mensaje\":\"El club no pudo ser agregado\"}").build();
        }
    }
    //</editor-fold>
    
// <editor-fold defaultstate="collapsed" desc=" Usuarios ">
    @GET
    @Path("/Usuarios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerUsuarios(){
        logger.info("Invocado el servicio /Usuarios");
        List<UsuarioDto> usuarios  = ub.obtenerTodosLosUsuarios();
        Gson gson = new Gson();
        String jsonRespuesta = gson.toJson(usuarios);
        logger.info("La respuesta generada fue: " + jsonRespuesta);
        return Response.ok(jsonRespuesta).build();
    }
    
    @GET
    @Path("/Usuarios/CrearUsuario")
    @Produces(MediaType.TEXT_PLAIN)
    public Response loginTwitter(){
        logger.info("Invocado el servicio /Usuarios/CrearUsuario");
        TwitterAuthentication ta = new TwitterAuthentication();
        List<String> autenticacion = null;
        try {
            autenticacion = ta.autenticar();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        if (autenticacion == null || autenticacion.size() < 3){
            return Response.ok("El usuario no ha podido ser creado").build();
        }
        String respuesta = "Por favor, entre a la " + autenticacion.get(0);
        respuesta = respuesta + " y autorice a la aplicación\n";
        respuesta = respuesta + "Su token es: " + autenticacion.get(1) + "\n";
        respuesta = respuesta + "El token secret es: " + autenticacion.get(2);
        return Response.ok(respuesta).build();
    }
    
    @POST
    @Path("/Usuarios/ConfirmarUsuario")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response confirmarUsuario(String confirmacionJson){
        logger.info("Invocado el servicio /Usuarios/ConfirmarUsuario");
        logger.info("con este json: " + confirmacionJson);
        String token = parse(confirmacionJson, "Token");
        String tokenSecret = parse(confirmacionJson, "TokenSecret");
        String pinAcceso = parse(confirmacionJson, "Pin");
        logger.info("El token es: " + token);
        logger.info("El pin es: " + pinAcceso);
        logger.info("El tokenSecret es: " + tokenSecret);
        if (token.isEmpty() || pinAcceso.isEmpty() || tokenSecret.isEmpty())
            return Response.ok("{\"exito\":0, \"mensaje\":\"El json recibido no es correcto\"}").build();
        
        TwitterAuthentication ta = new TwitterAuthentication();
        try{
            ta.obtenerAcceso(pinAcceso, token, tokenSecret);
        }catch(Exception ex){
            logger.error(ex.getMessage());
            return Response.ok("{\"exito\":0, \"mensaje\":\"Ocurrio un problema al confirmar el usuario\"}").build();
        }
        return Response.ok("{\"exito\":1, \"mensaje\":\"Usuario confirmado\"}").build();
        
    }
// </editor-fold>
    
    public String parse(String json, String elemento)  {
        JsonParser  parser = new JsonParser();
        JsonObject rootObj = parser.parse(json).getAsJsonObject();
        JsonElement element  = rootObj.get(elemento);
        return element.getAsString();
    }
}
