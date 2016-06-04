package com.futboleros.servicios.rest;

import com.futboleros.club.ClubBean;
import com.futboleros.dto.ClubDto;
import com.futboleros.dto.UsuarioDto;
import com.futboleros.usuario.Rol;
import com.futboleros.usuario.SesionBean;
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
    @EJB
    private SesionBean sb;

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
    
    @GET
    @Path("/Clubes/Nombre/{nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerClub(@PathParam("nombre") String nombre){
        logger.info("Invocado el servicio /Clubes/Nombre/{nombre}");
        ClubDto clubBuscado = cb.obtenerClubPorNombre(nombre);
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
    @Path("/Usuarios/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerUsuario(@PathParam("id") Long id){
        logger.info("Invocado el servicio /Usuarios/{id}");
        UsuarioDto usuarioBuscado = ub.obtenerUsuarioPorId(id);
        Gson gson = new Gson();
        String jsonRespuesta = gson.toJson(usuarioBuscado);
        logger.info("La respuesta generada fue: " + jsonRespuesta);
        return Response.ok(jsonRespuesta).build();
    }
    
    @GET
    @Path("/Usuarios/NombreUsuario/{Nombre}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerUsuario(@PathParam("Nombre") String nombre){
        logger.info("Invocado el servicio /Usuarios/NombreUsuario/{Nombre}");
        try{
            UsuarioDto usuarioBuscado = ub.obtenerUsuarioPorNombre(nombre);
            Gson gson = new Gson();
            String jsonRespuesta = gson.toJson(usuarioBuscado);
            logger.info("La respuesta generada fue: " + jsonRespuesta);
            return Response.ok(jsonRespuesta).build();
        }catch(Exception e){
            //Usuario no encontrado
            return Response.ok("{}").build();
        }
        
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
        String nombreCompleto = parse(confirmacionJson, "NombreCompleto");
        String nombreUsuario  = parse(confirmacionJson, "NombreUsuario");
        String email          = parse(confirmacionJson, "Email");
        String token = parse(confirmacionJson, "Token");
        String tokenSecret = parse(confirmacionJson, "TokenSecret");
        String pinAcceso = parse(confirmacionJson, "Pin");

        if (token.isEmpty() || pinAcceso.isEmpty() || tokenSecret.isEmpty()){
            logger.error("El json recibio no es correcto");
            MensajeResponse mr = new MensajeResponse(false, "El json recibido no es correcto");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }
            
        
        TwitterAuthentication ta = new TwitterAuthentication();
        String accessToken = "";
        try{
           accessToken = ta.obtenerAcceso(pinAcceso, token, tokenSecret);
        }catch(Exception ex){
            logger.error(ex.getMessage());
            MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al confirmar el usuario");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }
        UsuarioDto nuevoUsuario = new UsuarioDto(0L, nombreCompleto, nombreUsuario, Rol.CLIENTE, email);
        try{
            Long id = ub.agregarUsuario(nuevoUsuario);
            String mensajeRespuesta = "\"mensaje\":\"Usuario creado con id: " + id.toString() + "\n";
            mensajeRespuesta = mensajeRespuesta.concat(", su accessToken es: ");
            mensajeRespuesta = mensajeRespuesta.concat(accessToken + "\"}");
            MensajeResponse mr = new MensajeResponse(true, mensajeRespuesta);
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }catch(Exception ex){
            logger.error(ex.getMessage());
            MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al confirmar el usuario");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }
    }
//    
//    @POST
//    @Path("/Usuarios/IniciarSesion")
//    @Consumes(MediaType.APPLICATION_JSON)
//    public Response iniciarSesion(String ){
//        
//    }
// </editor-fold>
    
    public String parse(String json, String elemento)  {
        JsonParser  parser = new JsonParser();
        JsonObject rootObj = parser.parse(json).getAsJsonObject();
        JsonElement element  = rootObj.get(elemento);
        return element.getAsString();
    }
}
