package com.futboleros.soporte;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.model.OAuth1RequestToken;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author inibg
 */
public class Login {
    
    private static final Logger logger = LogManager.getLogger(Login.class);
 
    public Login() {
         final OAuth10aService service = new ServiceBuilder()
                .apiKey("e6jUHpukx8vA88NTh4bgvJCXl")
                .apiSecret("V5xQlaeRym4TEI9GLH7pa1aCD3i260yA06rzytVdjVlRSO46d7")
                .build(TwitterApi.instance());
        final Scanner in = new Scanner(System.in);
        logger.info("=== Twitter's OAuth Workflow ===");
        logger.info("Fetching the Request Token...");
        final OAuth1RequestToken requestToken = service.getRequestToken();
        logger.info("Got the Request Token!");
        logger.info("The request token is: " + requestToken.getToken());
        
        
        logger.info("Now go and authorize Futboleros2016 here:");
        String authUrl = service.getAuthorizationUrl(requestToken);
        logger.info("La url de autorizacion es: " + authUrl);
        
    }
}
