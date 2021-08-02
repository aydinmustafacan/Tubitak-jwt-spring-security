package com.aydinmustafa.dynamicauthproject.security.jwt;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import com.aydinmustafa.dynamicauthproject.security.services.*;

import static com.auth0.jwt.algorithms.Algorithm.HMAC256;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;


    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);


    /**
     * @param request     http request
     * @param response    http response
     * @param filterChain indicates the order of the filter in the chain in  the chain of filters b/w the client and server
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);// here jwt is authorization token that we passed into header after Bearer flag
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //System.out.println("user details is ");
                //System.out.println(userDetails.getUsername());
                //System.out.println(userDetails.getPassword());


                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                //UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) getUsernamePasswordAuthenticationTokenObject(request);
                //setDetails funtion comes from AbstractAuthenticationToken class. We set the details of authentication object
                // with WebAuthenticationDetails built by the current request (WebAuthDetailsSOuce.buildDetails() will return
                // WebAuthDetails object) It sets remoteAddress and sessionId in the WebAuthDetails object.
                // So details part in the authentication object is set to WebAuthDetails object filled with request's contents
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                System.out.println("authentication object principal equals to");
                System.out.println(authentication.getPrincipal());
                System.out.println("authentication object credentials equals to");
                System.out.println(authentication.getCredentials());
                System.out.println("authentication object details equals to");
                System.out.println(authentication.getDetails());

                // SecContextHolder.getContext() will give a SecContextImpl then it will have Authentication field coming from
                // Athentication interface which UsernamePasswordAuthenticationToken class implements. So we set authentication
                // with request's fields to securityContextImpl's authentication field.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        //it sets necessary filter parameter while taking care of place of the filter in the filter chain, doFilter() comes
        // from OncePerRequestFilter
        filterChain.doFilter(request, response);
    }

    /**
     * @param request describes the http request
     * @return returns the authentication Token that will be passed into the header as Authorization: Bearer token$$$$$$$
     * Bearer part is cut by the taking the substring from 7th index
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        System.out.println("here is the header token " + headerAuth);
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);

        }

        return null;
    }

    /**
     * @param permissions permissions that the user has taken from payload of json web token
     * @param roles       roles that the user have taken from payload of json web token
     * @return list of authorities that use have as a collection of GrantedAuthority objects
     */
//    private List<GrantedAuthority> getListOfAuthorities(Claim permissions, Claim roles) {
//        final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        final List<String> allPermissions = new ArrayList<>();
//        List<String> listOfPermissions = permissions.asList(String.class);
//        List<String> listOfRoles = roles.asList(String.class);
//        for (String permission : listOfPermissions)
//            allPermissions.add(permission);
//        for (String role : listOfRoles)
//            allPermissions.add(role);
//
//        for (String privilege : allPermissions)
//            grantedAuthorities.add(new SimpleGrantedAuthority(privilege));
//        return grantedAuthorities;
//    }


}


//    /**
//     *
//     * @param request http post request to fetch information of username and password
//     * @return UsernamePasswordAuthenticationToken object with the name authentication
//     * @throws TokenExpiredException
//     */
//    private Authentication getUsernamePasswordAuthenticationTokenObject(HttpServletRequest request) throws TokenExpiredException {
//        /** we set roles when we pass the argument called userDetails.setAuthorities()
//         * UsernamePasswordAuthenticationToken implements Authorization interface
//         * JwtProperties.HEADER_STRING=Authorization header of the http request will be searched for Bearer and if found
//         * .replace() function replaces it with nothing effectively deletes it
//         * Instead one can use req.getHeader("Authorization").substring(7) as well. I might as well change it according
//         * to best design principles. Substring better since replace might replace the Bearer in the middle so it
//         * will search for extra
//         */
//        String httpHeaderToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX,"");
//        try {
//            //it is inside of the auth0
//            DecodedJWT decodedJWT = JWT.require(HMAC256(JwtProperties.SECRET.getBytes()))
//                    .build()
//                    .verify(httpHeaderToken);
//
//            String currUsersUsername = decodedJWT.getSubject();
//            //Claim authorities = decodedJWT.getClaim(JwtProperties.CLAIM_AUTHORITIES);
//            Claim permissions = decodedJWT.getClaim(JwtProperties.CLAIM_PERMISSIONS);
//            Claim roles = decodedJWT.getClaim(JwtProperties.CLAIM_ROLES);
//            final List<GrantedAuthority> grantedAuths = getListOfAuthorities(permissions, roles);
//            if (currUsersUsername != null) {
//                //
//                return new UsernamePasswordAuthenticationToken(currUsersUsername,
//                        null, grantedAuths);
//            }
//            return null;
//        }catch (TokenExpiredException e){
//            //TODO:TokenExpire error can be returned
//            throw e;
//        }
//    }
//}

