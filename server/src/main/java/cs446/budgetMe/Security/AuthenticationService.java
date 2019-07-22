package cs446.budgetMe.Security;

import cs446.budgetMe.Model.User;
import cs446.budgetMe.Service.UserService;
import cs446.budgetMe.Utils.BadResponseException;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthenticationService {
    public AuthenticationService() { }

    private String hashPass(String pass){
        try {
            MessageDigest md = MessageDigest.getInstance( "SHA-256" );
            // Change this to UTF-16 if needed
            md.update( pass.getBytes( StandardCharsets.UTF_8 ) );
            byte[] digest = md.digest();
            String hex = String.format( "%064x", new BigInteger( 1, digest ) );
            return hex;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String generateAuthToken(String email, String password) {
        String strKey = email + password;
        return hashPass(strKey);
    }

}
