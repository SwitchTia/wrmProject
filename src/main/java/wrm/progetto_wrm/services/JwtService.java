package wrm.progetto_wrm.services;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {
    
    private static final String SECRET_KEY = "357538782F4125442A472D4B6150645367566B59703373367639792442264528";

    public String extractEmailfromToken (String jwt) {
        return extractClaim (jwt, Claims::getSubject);
    }
    public String extractEmailFromRequest (HttpServletRequest servletRequest){
        final String authHeader = servletRequest.getHeader("Authorization");
        final String jwt = authHeader.substring(7);
        return extractEmailfromToken(jwt);
    }

    //extract username from token
    public String extractUsername(String token) {
        return extractClaim (token, Claims::getSubject);
    }

    //extract a single claim
    public <T> T extractClaim (String token, Function <Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //generate a token only using user detalis
    public String generateToken (UserDetails userDetails){
        return generateToken(new HashMap<> (), userDetails);
    }

    //generating a token
    public String generateToken (Map <String, Object> extraClaism, UserDetails userDetails){
        return Jwts
        .builder()
        .setClaims(extraClaism)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 5000 * 60 * 48))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
    }

    //validate a token, so that token really belongs to the user
    public boolean isTokenValid (String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim (token, Claims::getExpiration);
    }
   
    //extracting all the claims
    private Claims extractAllClaims (String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSignInKey() {
        byte [] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
}
