package com.futboleros.servicios.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.futboleros.club.ClubBean;
import com.futboleros.club.ClubDto;
import com.futboleros.partido.PartidoBean;
import com.futboleros.partido.PartidoDto;
import com.futboleros.usuario.Rol;
import com.futboleros.usuario.SesionBean;
import com.futboleros.usuario.SesionDto;
import com.futboleros.usuario.TwitterAuthentication;
import com.futboleros.usuario.UsuarioBean;
import com.futboleros.usuario.UsuarioDto;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;


@Path("Servicios")
@RequestScoped
public class Servicios {
  private static final Logger logger = LogManager.getLogger(Servicios.class);
    
  @Context
  private UriInfo context;
  
  @EJB
  private ClubBean cb;
  
  @EJB
  private UsuarioBean ub;

  @EJB
  private PartidoBean pb;
  
  @EJB
  private SesionBean sb;

  @Resource(lookup = "jms/ColaNotificaciones")
  private Queue queue;

  @Resource(lookup = "jms/ColaFactory")
  private  QueueConnectionFactory connectionFactory;

  // <editor-fold defaultstate="collapsed" desc=" Clubes ">    
  
  @POST
  @Path("/Clubes")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenerClubes(String requestJson) {
    logger.info("Invocado el servicio /Clubes");
    logger.info("con este json: " + requestJson);
    Gson gson = new Gson();
    String accessToken = parse(requestJson, "AccessToken");
    try {
      if (!validarToken(accessToken)) {
        MensajeResponse mr = new MensajeResponse(false, "Sesion invalida");
        return Response.ok(gson.toJson(mr)).build();
      }
    } catch (Exception ex) {
      MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al validar la sesion");
      return Response.ok(gson.toJson(mr)).build();
    }
    List<ClubDto> clubes = cb.obtenerTodosLosClubes();
    String jsonRespuesta = gson.toJson(clubes);
    logger.info("La respuesta generada fue: " + jsonRespuesta);
    return Response.ok(jsonRespuesta).build();
  }
    
  @POST
  @Path("/Clubes/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenerClub(@PathParam("id") Long id, String requestJson) {
    logger.info("Invocado el servicio /Clubes/{id}");
    Gson gson = new Gson();
    String accessToken = parse(requestJson, "AccessToken");
    try {
      if (!validarToken(accessToken)) {
        MensajeResponse mr = new MensajeResponse(false, "Sesion invalida");
        return Response.ok(gson.toJson(mr)).build();
      }
    } catch (Exception ex) {
      MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al validar la sesion");
      return Response.ok(gson.toJson(mr)).build();
    }
    ClubDto clubBuscado = null;
    String jsonRespuesta;
    MensajeResponse mr = null;
    try {
      clubBuscado = cb.obtenerClubPorId(id);            
      if (clubBuscado == null) {
        mr = new MensajeResponse(false, "Club no encontrado");
      }
    } catch (Exception ex) {
      mr = new MensajeResponse(false, "Club no encontrado");
    }
    if (mr != null && clubBuscado == null) {
      jsonRespuesta = gson.toJson(mr);
    } else {
      jsonRespuesta = gson.toJson(clubBuscado);
    }
    logger.info("La respuesta generada fue: " + jsonRespuesta);
    return Response.ok(jsonRespuesta).build();
  }
    
  @POST
  @Path("/Clubes/Nombre/{nombre}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenerClub(@PathParam("nombre") String nombre, String requestJson) {
    logger.info("Invocado el servicio /Clubes/Nombre/{nombre}");
    Gson gson = new Gson();
    String accessToken = parse(requestJson, "AccessToken");
    try {
      if (!validarToken(accessToken)) {
        MensajeResponse mr = new MensajeResponse(false, "Sesion invalida");
        return Response.ok(gson.toJson(mr)).build();
      }
    } catch (Exception ex) {
      MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al validar la sesion");
      return Response.ok(gson.toJson(mr)).build();
    }   
    String jsonRespuesta;
    MensajeResponse mr = null;
    ClubDto clubBuscado = null;
    try {
      clubBuscado = cb.obtenerClubPorNombre(nombre);
    } catch (Exception ex) {
      mr = new MensajeResponse(false, "Club no encontrado");
    }
    if (mr != null && clubBuscado == null) {
      jsonRespuesta = gson.toJson(mr);
    } else {
      jsonRespuesta = gson.toJson(clubBuscado);
    }
    logger.info("La respuesta generada fue: " + jsonRespuesta);
    return Response.ok(jsonRespuesta).build();
  }
    
  @POST
  @Path("/Clubes/nuevoClub")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response nuevoClub(String nuevoClubJson) {
    logger.info("Invocado el servicio /Clubes/nuevo");
    logger.info("Con este Json: " + nuevoClubJson);
    String token = parse(nuevoClubJson, "AccessToken");
    MensajeResponse validaAdmin = validarUsuarioAdmin(token);
    if (validaAdmin.getExito() == false) {
      Gson gson = new Gson();
      return Response.ok(gson.toJson(validaAdmin)).build();
    }
    Gson gson = new Gson();
    ClubDto nuevoClub;
    try {
      nuevoClub = gson.fromJson(nuevoClubJson, ClubDto.class);
    } catch (Exception ex) {
      logger.error("Ocurrio un error al convertir el Json en un ClubDto " + ex.getMessage());
      return Response.ok("{\"exito\":0, \"mensaje\":\"El Json recibido no es correcto\"}").build();
    }
    try {
      if (cb.obtenerClubPorNombre(nuevoClub.getNombre()) == null) {
        Long id = cb.agregarClub(nuevoClub);
        logger.info("Se ha creado un club con id: " + id );
        return Response.ok("{\"exito\":1, \"mensaje\":\"Se ha"
                + " creado un club con id: " + id.toString() + "\"}").build();
      } else {
        logger.error("El club ya existe ");
        return Response.ok("{\"exito\":0, \"mensaje\":\"El club ya existe \"}").build();
      }
    } catch (Exception ex) {
      logger.error("Ocurrio un error al grabar el club " + ex.getMessage());
      return Response.ok("{\"exito\":0, \"mensaje\":\"El club no pudo ser agregado\"}").build();
    }
  }
    
  @POST
  @Path("/Clubes/Eliminar/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response eliminarClub(@PathParam("id") Long id, String clubJson) {
    logger.info("Invocado el servicio /Clubes/Eliminar");
    logger.info("Con este Json: " + id);
    String token = parse(clubJson, "AccessToken");
    MensajeResponse validaAdmin = validarUsuarioAdmin(token);
    if (validaAdmin.getExito() == false) {
      Gson gson = new Gson();
      return Response.ok(gson.toJson(validaAdmin)).build();
    }
    try {
      boolean elimino = cb.eliminarClub(id);
      if (elimino) {
        logger.info("Se ha eliminado el club con id: " + id );
        return Response.ok("{\"exito\":1, \"mensaje\":\"Se ha eliminado"
                + " el club con id: " + id + "\"}").build();
      } else {
        logger.info("No existe el club: " + id );
        return Response.ok("{\"exito\":0, \"mensaje\":\"El club " + id + "no existe\"}").build();
      }
    } catch (Exception ex) {
      logger.error("Ocurrio un error al eliminar el club " + ex.getMessage());
      return Response.ok("{\"exito\":0, \"mensaje\":\"El "
            + "club no pudo ser eliminado\"}").build();
    }
  }
    
  @POST
  @Path("/Clubes/CambiarNombre/{nombreOriginal}/{nombreNuevo}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response modificarNombreClub(@PathParam("nombreOriginal") String nombreOri,
          @PathParam("nombreNuevo") String nombreNue, String clubJson) {
    logger.info("Invocado el servicio /Clubes/CambiarNombre/{nombreOriginal}/{nombreNuevo}");
    logger.info("Con el siguiente json: " + clubJson);
    String token = parse(clubJson, "AccessToken");
    MensajeResponse validaAdmin = validarUsuarioAdmin(token);
    if (validaAdmin.getExito() == false) {
      Gson gson = new Gson();
      return Response.ok(gson.toJson(validaAdmin)).build();
    }
    ClubDto clubBuscado = cb.obtenerClubPorNombre(nombreOri);
    if (clubBuscado != null) {
      if (!nombreNue.equalsIgnoreCase("")) {
        this.cb.modificarNombreClub(clubBuscado.getId(), nombreNue);
        return Response.ok("Se modificó el nombre del club: " + nombreOri 
                + " por " + nombreNue).build();
      } else {
        logger.info("El nombre del club no puede ser vacio");
        return Response.ok("El nombre del club no puede ser vacio").build();
      }
    } else {
      logger.info("No existe el club que quiere modificar");
      return Response.ok("No existe el club que quiere modificar").build();
    }
  }

  //</editor-fold>
    
  // <editor-fold defaultstate="collapsed" desc=" Usuarios ">
  
  @POST
  @Path("/Usuarios")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenerUsuarios(String requestJson) {
    logger.info("Invocado el servicio /Usuarios");
    logger.info("con este json: " + requestJson);
    Gson gson = new Gson();
    String accessToken = parse(requestJson, "AccessToken");
    try {
      if (!validarToken(accessToken)) {
        MensajeResponse mr = new MensajeResponse(false, "Sesion invalida");
        return Response.ok(gson.toJson(mr)).build();
      }
    } catch (Exception ex) {
      MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al validar la sesion");
      return Response.ok(gson.toJson(mr)).build();
    }   
    List<UsuarioDto> usuarios  = ub.obtenerTodosLosUsuarios();
    String jsonRespuesta = gson.toJson(usuarios);
    logger.info("La respuesta generada fue: " + jsonRespuesta);
    return Response.ok(jsonRespuesta).build();
  }
    
  @POST
  @Path("/Usuarios/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenerUsuario(String json, @PathParam("id") Long id) {
    logger.info("Invocado el servicio /Usuarios/{id}");
    String token = parse(json, "AccessToken");
    if (token.isEmpty()) {
      logger.warn("La solicitud fue realizada sin AccessToken");
      MensajeResponse mr = new MensajeResponse(false, "Debe ingresar su AccessToken");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    } else {
      if (!validarToken(token)) {
        logger.warn("El AccessToken proporcionado no es válido");
        MensajeResponse mr = new MensajeResponse(false, "AccessToken no valido,"
                + " intente iniciar sesión nuevamente");
        Gson gson = new Gson();
        return Response.ok(gson.toJson(mr)).build();
      }
    }
    UsuarioDto usuarioBuscado = ub.obtenerUsuarioPorId(id);
    Gson gson = new Gson();
    String jsonRespuesta = gson.toJson(usuarioBuscado);
    logger.info("La respuesta generada fue: " + jsonRespuesta);
    return Response.ok(jsonRespuesta).build();
  }
    
  @POST
  @Path("/Usuarios/NombreUsuario/{Nombre}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response obtenerUsuario(String json, @PathParam("Nombre") String nombre) {
    logger.info("Invocado el servicio /Usuarios/NombreUsuario/{Nombre}");
    String token = parse(json, "AccessToken");
    if (token.isEmpty()) {
      logger.warn("La solicitud fue realizada sin AccessToken");
      MensajeResponse mr = new MensajeResponse(false, "Debe ingresar su AccessToken");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    } else {
      if (!validarToken(token)) {
        logger.warn("El AccessToken proporcionado no es válido");
        MensajeResponse mr = new MensajeResponse(false, "AccessToken no valido,"
                + " intente iniciar sesión nuevamente");
        Gson gson = new Gson();
        return Response.ok(gson.toJson(mr)).build();
      }
    }
    try {
      UsuarioDto usuarioBuscado = ub.obtenerUsuarioPorNombre(nombre);
      Gson gson = new Gson();
      String jsonRespuesta = gson.toJson(usuarioBuscado);
      logger.info("La respuesta generada fue: " + jsonRespuesta);
      return Response.ok(jsonRespuesta).build();
    } catch (Exception ex) {
      return Response.ok("{}").build();
    }
        
  }
    
  @GET
  @Path("/Usuarios/SolicitudTwitter")
  @Produces(MediaType.TEXT_PLAIN)
  public Response loginTwitter() {
    logger.info("Invocado el servicio /Usuarios/CrearUsuario");
    TwitterAuthentication ta = new TwitterAuthentication();
    List<String> autenticacion = null;
    try {
      autenticacion = ta.autenticar();
    } catch (Exception ex) {
      logger.error(ex.getMessage());
    }
    if (autenticacion == null || autenticacion.size() < 3) {
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
  public Response confirmarUsuario(String confirmacionJson) {
    logger.info("Invocado el servicio /Usuarios/ConfirmarUsuario");
    logger.info("con este json: " + confirmacionJson);
    String token = parse(confirmacionJson, "Token");
    String tokenSecret = parse(confirmacionJson, "TokenSecret");
    String pinAcceso = parse(confirmacionJson, "Pin");
    if (token.isEmpty() || pinAcceso.isEmpty() || tokenSecret.isEmpty()) {
      logger.error("El json recibio no es correcto");
      MensajeResponse mr = new MensajeResponse(false, "El json recibido no es correcto");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    }
    TwitterAuthentication ta = new TwitterAuthentication();
    String accessToken = null;
    String userName = null;
    try {
      List<String> datosTwitter = ta.obtenerAcceso(pinAcceso, token, tokenSecret);
      if (datosTwitter != null && datosTwitter.size() == 2) {
        userName    = datosTwitter.get(0);
        accessToken = datosTwitter.get(1);
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      MensajeResponse mr = new MensajeResponse(false, "Ocurrio"
              + " un problema al confirmar el usuario");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    }
    if (userName == null || userName.isEmpty() || accessToken == null || accessToken.isEmpty()) {
      logger.error("El accessToken o el userName están vacios");
      MensajeResponse mr = new MensajeResponse(false, "Ocurrio"
              + " un problema al confirmar el usuario");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    }
    String nombreCompleto = parse(confirmacionJson, "NombreCompleto");
    String email          = parse(confirmacionJson, "Email");
    UsuarioDto nuevoUsuario = new UsuarioDto(0L, nombreCompleto, 
            userName, Rol.CLIENTE, email, new ArrayList<ClubDto>());
    try {
      Long id = ub.agregarUsuario(nuevoUsuario);
      if (id > 0) {
        nuevoUsuario.setId(id);
        Date fechaInicio = new Date();
        SesionDto sesion = new SesionDto(0L, accessToken, nuevoUsuario, fechaInicio);
        sb.iniciarSesion(sesion);
      }
      String mensajeRespuesta = "Usuario creado con id: " + id.toString();
      mensajeRespuesta = mensajeRespuesta.concat(", su accessToken es: ");
      mensajeRespuesta = mensajeRespuesta.concat(accessToken );
      MensajeResponse mr = new MensajeResponse(true, mensajeRespuesta);
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      MensajeResponse mr = new MensajeResponse(false, "Ocurrio"
              + " un problema al confirmar el usuario");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    }
  }
    
  @POST
  @Path("/Usuarios/CerrarSesion")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response cerrarSesion(String json) {
    logger.info("Invocado el servicio /Usuarios/CerrarSesion");
    String token = parse(json, "AccessToken");
    Gson gson = new Gson();
    if (token.isEmpty()) {
      logger.warn("La solicitud fue realizada sin AccessToken");
      MensajeResponse mr = new MensajeResponse(false, "Debe ingresar su AccessToken");
      return Response.ok(gson.toJson(mr)).build();
    } else {
      if (!validarToken(token)) {
        logger.warn("El AccessToken proporcionado no es válido");
        MensajeResponse mr = new MensajeResponse(false, "AccessToken no valido, "
                + "intente iniciar sesión nuevamente");
        return Response.ok(gson.toJson(mr)).build();
      }
    }
    MensajeResponse mr;
    if (sb.terminarSesion(token)) {
      mr = new MensajeResponse(true, "Sesion terminada");
    } else {
      mr = new MensajeResponse(true, "Ocurrio un problema al cerrar la sesion");
    }
    return Response.ok(gson.toJson(mr)).build();
  }
    
  @POST
  @Path("/Usuarios/IniciarSesion")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response inicioSesion(String inicioJson) {
    UsuarioDto usuario;
    logger.info("Invocado el servicio /Usuarios/IniciarSesion");
    logger.info("con este json: " + inicioJson);
    String token = parse(inicioJson, "Token");
    String tokenSecret = parse(inicioJson, "TokenSecret");
    String pinAcceso = parse(inicioJson, "Pin");
    if (token.isEmpty() || pinAcceso.isEmpty() || tokenSecret.isEmpty()) {
      logger.error("El json recibio no es correcto");
      MensajeResponse mr = new MensajeResponse(false, "El json recibido no es correcto");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    }
    TwitterAuthentication ta = new TwitterAuthentication();
    String accessToken = null;
    String userName = null;
    try {
      List<String> datosTwitter = ta.obtenerAcceso(pinAcceso, token, tokenSecret);
      if (datosTwitter != null && datosTwitter.size() == 2) {
        userName = datosTwitter.get(0);
        accessToken = datosTwitter.get(1);
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      MensajeResponse mr = new MensajeResponse(false, "Ocurrio"
              + " un problema al iniciar sesion");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    }
    if (userName == null || userName.isEmpty() || accessToken == null || accessToken.isEmpty()) {
      logger.error("El accessToken o el userName están vacios");
      MensajeResponse mr = new MensajeResponse(false, "Ocurrio"
              + " un problema al confirmar el usuario");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    }
    try {
      usuario = ub.obtenerUsuarioPorNombre(userName);
    } catch (Exception ex) {
      logger.error("usuario no encontrado");
      MensajeResponse mr = new MensajeResponse(false, "usuario no encontrado");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    }
    try {
      SesionDto ses = sb.obtenerSesionPorUsuario(usuario);
      if (ses != null) {
        sb.terminarSesion(ses.getToken());
      }
    } catch (Exception e) {
      logger.info("Sesion no encontrada");
    }
    Date fechaInicio = new Date();
    SesionDto sesion = new SesionDto(0L, accessToken, usuario, fechaInicio);
    try {
      sb.iniciarSesion(sesion);
      String mensajeRespuesta = "Su accessToken es: ";
      mensajeRespuesta = mensajeRespuesta.concat(accessToken );
      MensajeResponse mr = new MensajeResponse(true, mensajeRespuesta);
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    } catch (Exception ex) {
      logger.error(ex.getMessage());
      MensajeResponse mr = new MensajeResponse(false, "Ocurrio"
              + " un problema al iniciar la sesion");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    }
  }
    
  @POST
  @Path("/Usuarios/HacerAdmin")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response convierteAdmin(String requestJson) {
    logger.info("Invocado el servicio /Usuarios/IniciarSesion");
    logger.info("con este json: " + requestJson);
    String token = parse(requestJson, "AccessToken");
    MensajeResponse validaAdmin = validarUsuarioAdmin(token);
    if (validaAdmin.getExito() == false) {
      Gson gson = new Gson();
      return Response.ok(gson.toJson(validaAdmin)).build();
    }
    String idUsuarioNuevoAdministrador = parse(requestJson, "UsuarioId");
    if (idUsuarioNuevoAdministrador.isEmpty()) {
      logger.error("El json recibido no es correcto");
      MensajeResponse mr = new MensajeResponse(false, "El json recibido no es correcto");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    }
    Long idUsuario;
    try {
      idUsuario =  Long.parseLong(idUsuarioNuevoAdministrador);
    } catch (Exception ex) {
      logger.error("El json recibido no es correcto");
      MensajeResponse mr = new MensajeResponse(false, "El json recibido no es correcto");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    }
    UsuarioDto usuNuevoAdmin;
    try {
      usuNuevoAdmin = ub.obtenerUsuarioPorId(idUsuario);
    } catch (Exception ex) {
      logger.error("No se encontro un usuario con id " + idUsuario);
      MensajeResponse mr = new MensajeResponse(false, "No se encontro un"
              + " usuario con id " + idUsuario);
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    }
    try {
      ub.convertirUsuarioAdmin(usuNuevoAdmin.getNombreUsuario());
    } catch (Exception ex) {
      logger.error("Ocurrio un problema al procesar su pedido");
      MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al procesar su pedido");
      Gson gson = new Gson();
      return Response.ok(gson.toJson(mr)).build();
    }
    logger.error("Operacion realizada exitosamente");
    MensajeResponse mr = new MensajeResponse(true, "Operacion realizada exitosamente");
    Gson gson = new Gson();
    return Response.ok(gson.toJson(mr)).build();
  }
    
  @POST
  @Path("/Usuarios/SeguirClub/{idClub}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response seguirClub(String requestJson, @PathParam("idClub") Long idClub) {
    logger.info("Invocado el servicio /Usuarios/SeguirClub/" + idClub.toString());
    logger.info("con este json: " + requestJson);
    Gson gson = new Gson();
    String accessToken = parse(requestJson, "AccessToken");
    try {
      if (!validarToken(accessToken)) {
        MensajeResponse mr = new MensajeResponse(false, "Sesion invalida");
        return Response.ok(gson.toJson(mr)).build();
      }
    } catch (Exception ex) {
      MensajeResponse mr = new MensajeResponse(false, "Ocurrio"
              + " un problema al validar la sesion");
      return Response.ok(gson.toJson(mr)).build();
    }
    ClubDto clubSeguir = cb.obtenerClubPorId(idClub);
    if (clubSeguir == null) {
      MensajeResponse mr = new MensajeResponse(false, "No se encontró el club a seguir");
      return Response.ok(gson.toJson(mr)).build();
    }
    SesionDto sesion = sb.obtenerSesionPorToken(accessToken);
    try {
      ub.seguirClub(sesion.getUsuarioDto().getId(), clubSeguir); 
      MensajeResponse mr = new MensajeResponse(true, "Se agregó correctamente");
      return Response.ok(gson.toJson(mr)).build();
    } catch (Exception ex) {
      MensajeResponse mr = new MensajeResponse(false, "Ocurrió un"
              + " problema al agregar el club al usuario");
      return Response.ok(gson.toJson(mr)).build();
    }
  }
  
  // </editor-fold>
    
  // <editor-fold defaultstate="collapsed" desc=" Partidos ">
  
  @POST
  @Path("/Partido/nuevoPartido")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response nuevoPartido(String nuevoPartidoJson) {
    logger.info("Invocado el servicio /Partido/nuevo");
    logger.info("Con este Json: " + nuevoPartidoJson);
    String token = parse(nuevoPartidoJson, "AccessToken");
    MensajeResponse validaAdmin = validarUsuarioAdmin(token);
    if (validaAdmin.getExito() == false) {
      Gson gson = new Gson();
      return Response.ok(gson.toJson(validaAdmin)).build();
    }
    Gson gson = new Gson();
    PartidoDto nuevoPartido;
    try {
      nuevoPartido = gson.fromJson(nuevoPartidoJson, PartidoDto.class);
    } catch (Exception ex) {
      logger.error("Ocurrio un error al convertir el Json en un PartidoDto " 
            + ex.getMessage());
      return Response.ok("{\"exito\":0, \"mensaje\":\"El Json recibido no es correcto\"}").build();
    }
    try {   
      Long id = this.pb.agregarPartido(nuevoPartido);
      if (id != 0) {        
        logger.info("Se ha creado un partido con id : " + id );
        return Response.ok("{\"exito\":1, \"mensaje\":\"Se ha creado"
                + " un partido con id: " + id + "\"}").build();
      } else {
        return Response.ok("{\"exito\":1, \"mensaje\":\"NULO: \"}").build();
      }
    } catch (Exception ex) {
      logger.error("Ocurrio un error al grabar el partido " 
        + ex.getMessage());
      return Response.ok("{\"exito\":0, \"mensaje\":\"El partido no pudo ser agregado\"}").build();
    }
  }
    
  @POST
  @Path("/Partido/actualizarResultado")
  @Produces(MediaType.APPLICATION_JSON)
  public Response actualizarResultadoPartido(String JsonPartido) {     
    logger.info("Invocado el servicio /Partido/actualizarResultado");
    logger.info("Con este Json: " + JsonPartido);
    String token = parse(JsonPartido, "AccessToken");
    MensajeResponse validaAdmin = validarUsuarioAdmin(token);
    if (validaAdmin.getExito() == false) {
      Gson gson = new Gson();
      return Response.ok(gson.toJson(validaAdmin)).build();
    }
    Gson gson = new Gson();
    PartidoDto actPartido;
    try {
      actPartido = gson.fromJson(JsonPartido, PartidoDto.class);
    } catch (Exception ex) {
      logger.error("Ocurrio un error al convertir el Json en un PartidoDto " 
          + ex.getMessage());
      return Response.ok("{\"exito\":0, \"mensaje\":\"El Json recibido no es correcto\"}").build();
    }
    try {
      Long id = this.pb.actualizarResultado(actPartido);
      if (id != 0) {
        enviarMensaje(id.toString());
        logger.info("Se actualizó el resultado del partido : " + id );
        return Response.ok("{\"exito\":1, \"mensaje\":\"Se actualizó"
                + " el resultado del partido: " + id + "\"}").build();
      } else {
        return Response.ok("{\"exito\":1, \"mensaje\":\"NULO: \"}").build();
      }
    } catch (Exception ex) {
      logger.error("Ocurrio un error al actualizar el resultado del partido " 
            + ex.getMessage());
      return Response.ok("{\"exito\":0, \"mensaje\":\"El partido no pudo"
              + " ser actualizado\"}").build();
    }
  }

  //</editor-fold>
    
  public void enviarMensaje(String numeroPartido) {
    JMSContext contextJms = connectionFactory.createContext();
    contextJms.createProducer().send(queue, numeroPartido);
  }
    
  public String parse(String json, String elemento)  {
    JsonParser  parser = new JsonParser();
    JsonObject rootObj = parser.parse(json).getAsJsonObject();
    JsonElement element  = rootObj.get(elemento);
    if(element == null){
        return "";
    } else {
      return element.getAsString();
    }
  }
  
  public boolean validarToken(String accessToken) {
    return sb.sesionValida(accessToken);
  }
    
  public MensajeResponse validarUsuarioAdmin(String accessToken) {
    if (accessToken.isEmpty()) {
      logger.warn("La solicitud fue realizada sin AccessToken");
      MensajeResponse mr = new MensajeResponse(false, "Debe ingresar su AccessToken");
      return mr;
    } else {
      if (!validarToken(accessToken)) {
        logger.warn("El AccessToken proporcionado no es válido");
        MensajeResponse mr = new MensajeResponse(false, "AccessToken no valido, "
                + "intente iniciar sesión nuevamente");
        return mr;
      }
    }
    SesionDto sdto = sb.obtenerSesionPorToken(accessToken);
    if (sdto != null) {
      UsuarioDto udto = sdto.getUsuarioDto();
      if (udto == null) {
        logger.error("Ocurrio un problema al obtener el usuario de la sesion");
        MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al validar su usuario");
        return mr;
      } else {
        if (udto.getRol() != Rol.ADMINISTRADOR) {
          logger.error("Debe ser administrador para procesar la operacion");
          MensajeResponse mr = new MensajeResponse(false, "Debe ser "
                  + "administrador para realizar la operacion");
          return mr;
        }
      }
    } else {
      logger.error("Ocurrio un problema al recuperar la sesion");
      MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al recuperar la sesion");
      return mr;
    }
    MensajeResponse mr = new MensajeResponse(true, "");
    return mr;
  }
}
