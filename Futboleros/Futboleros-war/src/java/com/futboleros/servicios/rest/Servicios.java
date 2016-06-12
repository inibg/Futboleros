package com.futboleros.servicios.rest;

import com.futboleros.club.ClubBean;
import com.futboleros.partido.Partido;
import com.futboleros.partido.PartidoBean;
import com.futboleros.dto.ClubDto;
import com.futboleros.dto.PartidoDto;
import com.futboleros.dto.SesionDto;
import com.futboleros.dto.UsuarioDto;
import com.futboleros.usuario.Rol;
import com.futboleros.usuario.SesionBean;
import com.futboleros.usuario.TwitterAuthentication;
import com.futboleros.usuario.UsuarioBean;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
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
    
    @POST
    @Path("/Clubes/Eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    //eliminar club
    public Response eliminarClub(@PathParam("id") Long id){
        logger.info("Invocado el servicio /Clubes/Eliminar");
        logger.info("Con este Json: " + id);
        try
        {
            boolean elimino = cb.eliminarClub(id);
            if (elimino){
                logger.info("Se ha eliminado el club con id: " + id );
                return Response.ok("{\"exito\":1, \"mensaje\":\"Se ha eliminado el club con id: " + id + "\"}").build();
            }else{
                logger.info("No existe el club: " + id );
                return Response.ok("{\"exito\":0, \"mensaje\":\"El club " + id + "no existe\"}").build();
            }
        }catch(Exception e){
            logger.error("Ocurrio un error al eliminar el club " 
            + e.getMessage());
            return Response.ok("{\"exito\":0, \"mensaje\":\"El club no pudo ser eliminado\"}").build();
        }
    }
    

    @POST
    @Path("/Clubes/CambiarNombre/{nombreOriginal}/{nombreNuevo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response modificarNombreClub(@PathParam("nombreOriginal") String nombreOri,@PathParam("nombreNuevo") String nombreNue){
        logger.info("Invocado el servicio /Clubes/CambiarNombre/{nombreOriginal}/{nombreNuevo}");
        ClubDto clubBuscado = cb.obtenerClubPorNombre(nombreOri);
        
        if(clubBuscado != null){
            /*modifico el nombre del club*/
            if (!nombreNue.equalsIgnoreCase("")){
                this.cb.modificarNombreClub(clubBuscado.getId(), nombreNue);
                return Response.ok("Se modificó el nombre del club: "+nombreOri +" por " + nombreNue).build();
            }else{
                logger.info("El nombre del club no puede ser vacio");
                return Response.ok("El nombre del club no puede ser vacio").build();
            }
            
        }else{
            logger.info("No existe pelotilla");
            return Response.ok("No existe pelotilla").build();
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
    
    @POST
    @Path("/Usuarios/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerUsuario(String json, @PathParam("id") Long id){
        logger.info("Invocado el servicio /Usuarios/{id}");
        String token = parse(json, "AccessToken");
        if (token.isEmpty()){
            logger.warn("La solicitud fue realizada sin AccessToken");
            MensajeResponse mr = new MensajeResponse(false, "Debe ingresar su AccessToken");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }else{
           if (!validarToken(token))
           {
                logger.warn("El AccessToken proporcionado no es válido");
                MensajeResponse mr = new MensajeResponse(false, "AccessToken no valido, intente iniciar sesión nuevamente");
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
    public Response obtenerUsuario(String json, @PathParam("Nombre") String nombre){
        logger.info("Invocado el servicio /Usuarios/NombreUsuario/{Nombre}");
        String token = parse(json, "AccessToken");
        if (token.isEmpty()){
            logger.warn("La solicitud fue realizada sin AccessToken");
            MensajeResponse mr = new MensajeResponse(false, "Debe ingresar su AccessToken");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }else{
           if (!validarToken(token))
           {
                logger.warn("El AccessToken proporcionado no es válido");
                MensajeResponse mr = new MensajeResponse(false, "AccessToken no valido, intente iniciar sesión nuevamente");
                Gson gson = new Gson();
                return Response.ok(gson.toJson(mr)).build();
           }
        }
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
    @Path("/Usuarios/SolicitudTwitter")
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
        String accessToken = null, userName = null;
        try{
            List<String> datosTwitter = ta.obtenerAcceso(pinAcceso, token, tokenSecret);
            if (datosTwitter != null && datosTwitter.size() == 2){
                userName    = datosTwitter.get(0);
                accessToken = datosTwitter.get(1);
            }
        }catch(Exception ex){
            logger.error(ex.getMessage());
            MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al confirmar el usuario");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }
        if (userName == null || userName.isEmpty() || accessToken == null || accessToken.isEmpty()){
            logger.error("El accessToken o el userName están vacios");
            MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al confirmar el usuario");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }
        UsuarioDto nuevoUsuario = new UsuarioDto(0L, nombreCompleto, userName, Rol.CLIENTE, email, new ArrayList<ClubDto>());
        try{
            Long id = ub.agregarUsuario(nuevoUsuario);
            if (id > 0){
                nuevoUsuario.setId(id);
                //Si se creo el usuario le inicio una sesion
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
        }catch(Exception ex){
            logger.error(ex.getMessage());
            MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al confirmar el usuario");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }
    }
    
    @POST
    @Path("/Usuarios/CerrarSesion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cerrarSesion(String json){
        logger.info("Invocado el servicio /Usuarios/CerrarSesion");
        String token = parse(json, "AccessToken");
        Gson gson = new Gson();
        if (token.isEmpty()){
            logger.warn("La solicitud fue realizada sin AccessToken");
            MensajeResponse mr = new MensajeResponse(false, "Debe ingresar su AccessToken");
            return Response.ok(gson.toJson(mr)).build();
        }else{
           if (!validarToken(token))
           {
                logger.warn("El AccessToken proporcionado no es válido");
                MensajeResponse mr = new MensajeResponse(false, "AccessToken no valido, intente iniciar sesión nuevamente");
                return Response.ok(gson.toJson(mr)).build();
           }
        }
        MensajeResponse mr;
        if (sb.terminarSesion(token)){
            mr = new MensajeResponse(true, "Sesion terminada");
        }else
        {
            mr = new MensajeResponse(true, "Ocurrio un problema al cerrar la sesion");
        }
        return Response.ok(gson.toJson(mr)).build();
    }
    
    
    @POST
    @Path("/Usuarios/IniciarSesion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response inicioSesion(String inicioJson){
        UsuarioDto usuario;
        logger.info("Invocado el servicio /Usuarios/IniciarSesion");
        logger.info("con este json: " + inicioJson);
        String token = parse(inicioJson, "Token");
        String tokenSecret = parse(inicioJson, "TokenSecret");
        String pinAcceso = parse(inicioJson, "Pin");
        if (token.isEmpty() || pinAcceso.isEmpty() || tokenSecret.isEmpty()){
            logger.error("El json recibio no es correcto");
            MensajeResponse mr = new MensajeResponse(false, "El json recibido no es correcto");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }
        TwitterAuthentication ta = new TwitterAuthentication();
        String accessToken = null, userName = null;
        try{
            List<String> datosTwitter = ta.obtenerAcceso(pinAcceso, token, tokenSecret);
            if (datosTwitter != null && datosTwitter.size() == 2){
                userName    = datosTwitter.get(0);
                accessToken = datosTwitter.get(1);
            }
        }catch(Exception ex){
            logger.error(ex.getMessage());
            MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al iniciar sesion");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }
        if (userName == null || userName.isEmpty() || accessToken == null || accessToken.isEmpty()){
            logger.error("El accessToken o el userName están vacios");
            MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al confirmar el usuario");
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
        //Si el usuario ya tiene una sesion, la cierro 
        try{
            SesionDto ses = sb.obtenerSesionPorUsuario(usuario);
            if (ses != null){
                sb.terminarSesion(ses.getToken());
            }
        }catch(Exception e){
            logger.info("Sesion no encontrada");
        }
        Date fechaInicio = new Date();
        SesionDto sesion = new SesionDto(0L, accessToken, usuario, fechaInicio);
        try{
            sb.iniciarSesion(sesion);
            String mensajeRespuesta = "Su accessToken es: ";
            mensajeRespuesta = mensajeRespuesta.concat(accessToken );
            MensajeResponse mr = new MensajeResponse(true, mensajeRespuesta);
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }catch(Exception e){
            logger.error(e.getMessage());
            MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al iniciar la sesion");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }
    }
    
    @POST
    @Path("/Usuarios/HacerAdmin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response convierteAdmin(String requestJson){
        logger.info("Invocado el servicio /Usuarios/IniciarSesion");
        logger.info("con este json: " + requestJson);
        String token = parse(requestJson, "AccessToken");
        if (token.isEmpty()){
            logger.warn("La solicitud fue realizada sin AccessToken");
            MensajeResponse mr = new MensajeResponse(false, "Debe ingresar su AccessToken");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }else{
           if (!validarToken(token))
           {
                logger.warn("El AccessToken proporcionado no es válido");
                MensajeResponse mr = new MensajeResponse(false, "AccessToken no valido, intente iniciar sesión nuevamente");
                Gson gson = new Gson();
                return Response.ok(gson.toJson(mr)).build();
           }
        }
        SesionDto sdto = sb.obtenerSesionPorToken(token);
        if (sdto != null){
            UsuarioDto udto = sdto.getUsuarioDto();
            if (udto == null){
                logger.error("Ocurrio un problema al obtener el usuario de la sesion");
                MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al validar su usuario");
                Gson gson = new Gson();
                return Response.ok(gson.toJson(mr)).build();
            }else{
                if (udto.getRol() != Rol.ADMINISTRADOR){
                    logger.error("Debe ser administrador para procesar la operacion");
                    MensajeResponse mr = new MensajeResponse(false, "Debe ser administrador para realizar la operacion");
                    Gson gson = new Gson();
                    return Response.ok(gson.toJson(mr)).build();
                }
            }
        }else{
            logger.error("Ocurrio un problema al recuperar la sesion");
            MensajeResponse mr = new MensajeResponse(false, "Ocurrio un problema al recuperar la sesion");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }
        String idUsuarioNuevoAdministrador = parse(requestJson, "UsuarioId");
        if (idUsuarioNuevoAdministrador.isEmpty()){
            logger.error("El json recibido no es correcto");
            MensajeResponse mr = new MensajeResponse(false, "El json recibido no es correcto");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }
        Long idUsuario;
        try{
            idUsuario =  Long.parseLong(idUsuarioNuevoAdministrador);
        }catch(Exception e){
            logger.error("El json recibido no es correcto");
            MensajeResponse mr = new MensajeResponse(false, "El json recibido no es correcto");
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }
        UsuarioDto uNuevoAdmin;
        try{
            uNuevoAdmin = ub.obtenerUsuarioPorId(idUsuario);
        }catch(Exception e){
            logger.error("No se encontro un usuario con id " + idUsuario);
            MensajeResponse mr = new MensajeResponse(false, "No se encontro un usuario con id " + idUsuario);
            Gson gson = new Gson();
            return Response.ok(gson.toJson(mr)).build();
        }
        try{
            ub.convertirUsuarioAdmin(uNuevoAdmin.getNombreUsuario());
        }catch(Exception e){
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
    
// </editor-fold>
    
// <editor-fold defaultstate="collapsed" desc=" Partidos ">
    @POST
    @Path("/Partido/nuevo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response nuevoPartido(String nuevoPartidoJson){
        logger.info("Invocado el servicio /Partido/nuevo");
        logger.info("Con este Json: " + nuevoPartidoJson);
        Gson gson = new Gson();
        PartidoDto nuevoPartido;
        
        try
        {
            nuevoPartido = gson.fromJson(nuevoPartidoJson, PartidoDto.class);
    
        }catch(Exception e){
            logger.error("Ocurrio un error al convertir el Json en un PartidoDto " 
            + e.getMessage());
            return Response.ok("{\"exito\":0, \"mensaje\":\"El Json recibido no es correcto\"}").build();
        }
        try
        {
           
            Long id=this.pb.agregarPartido(nuevoPartido);
            
            if(id!=0){        
            logger.info("Se ha creado un partido con id club local : " + id );
            return Response.ok("{\"exito\":1, \"mensaje\":\"Se ha creado un partido con id: " + id + "\"}").build();
            }else{
                return Response.ok("{\"exito\":1, \"mensaje\":\"NULO: \"}").build();
                
            }
        }catch(Exception e){
            logger.error("Ocurrio un error al grabar el partido " 
            + e.getMessage());
            return Response.ok("{\"exito\":0, \"mensaje\":\"El partido no pudo ser agregado\"}").build();
        }
    }
    
    @POST
    @Path("/Partido/actualizarResultado/{idPartido}/{golesLocal}/{golesVisitante}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarResultadoPartido(@PathParam("idPartido") Long idPartido,@PathParam("golesLocal") Integer golesLocal,@PathParam("golesVisiante") Integer golesVisitante){     
        PartidoDto PartidoAct = pb.obtenerPartidoPorId(idPartido);
        
        if(PartidoAct != null){
            //modifico el resultado del partido
            this.pb.ActualizarResultado(idPartido,golesLocal,golesVisitante);
          
             return Response.ok("Se actualizó el resultado del partido correctamente").build();
        }else{
            logger.info("No hay ningún partido con ese identificador");
           // return Response.ok(jsonRespuesta).build();
            return Response.ok("No hay ningún partido con ese identificador").build();
        }
        
        
        //Gson gson = new Gson();
        //String jsonRespuesta = gson.toJson(clubBuscado);
        //logger.info("La respuesta generada fue: " + jsonRespuesta);
        //return Response.ok("a").build();
    }

    //</editor-fold>
    
    @GET
    @Path("/pruebajms")
    public void pruebamensaje(){
        try{
            JMSContext contextJms = connectionFactory.createContext();
            contextJms.createProducer().send(queue, "te mando el mensaje piola");
        }catch(Exception e){
                
        }
    }
    
    public String parse(String json, String elemento)  {
        JsonParser  parser = new JsonParser();
        JsonObject rootObj = parser.parse(json).getAsJsonObject();
        JsonElement element  = rootObj.get(elemento);
        if (element != null)
            return element.getAsString();
        else
            return "";
    }
    
    public boolean validarToken(String accessToken){
        return sb.sesionValida(accessToken);
    }
}
