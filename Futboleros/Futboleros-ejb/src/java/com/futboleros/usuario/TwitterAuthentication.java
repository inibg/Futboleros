package com.futboleros.usuario;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author inibg
 */
public class TwitterAuthentication {
    
    private static final Logger logger = LogManager.getLogger(TwitterAuthentication.class);
 
    private String authUrl;
    private final String _APIKEY = "e6jUHpukx8vA88NTh4bgvJCXl";
    private final String _APISECRET = "V5xQlaeRym4TEI9GLH7pa1aCD3i260yA06rzytVdjVlRSO46d7";
    private OAuth1RequestToken requestToken ;
    private String oauthVerifier;
    
    
    public String getAuthUrl() {
        return authUrl;
    }
    
    public List<String>  autenticar() throws Exception{
        logger.info("=== iniciando autenticacion ===");
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(_APIKEY)
                .apiSecret(_APISECRET)
                .build(TwitterApi.instance());
        logger.info("Obteniendo el token de pedido...");
        try{
            requestToken = service.getRequestToken();
            logger.info("Se obtuvo el token!");
            authUrl = service.getAuthorizationUrl(requestToken);
            logger.info("La url de autorizacion es: " + authUrl);
        }catch(Exception e){
            throw  e;
        }
        List<String> respuesta = new ArrayList<>();
        respuesta.add(authUrl);
        respuesta.add(requestToken.getToken());
        respuesta.add(requestToken.getTokenSecret());
        return respuesta;
    }
    
    public void obtenerAcceso(String verifier, String request, String requestSecret) throws Exception{
        logger.info("Obteniendo token de acceso!");
        oauthVerifier = verifier;
        final OAuth10aService service = new ServiceBuilder()
                .apiKey(_APIKEY)
                .apiSecret(_APISECRET)
                .build(TwitterApi.instance());
        try{
            OAuth1RequestToken oldRequest = new OAuth1RequestToken(request, requestSecret);
            OAuth1AccessToken  accessToken = service.getAccessToken(oldRequest, oauthVerifier);
            logger.info("Se obtuvo el token de acceso: " + accessToken.getToken());
            logger.info("La respuesta raw es: " + accessToken.getRawResponse());
        }catch(Exception e){
            throw e;
        }
    }
    
    public TwitterAuthentication() {
        
    }

    
}
