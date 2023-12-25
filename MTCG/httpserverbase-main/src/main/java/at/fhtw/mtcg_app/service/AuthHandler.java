package at.fhtw.mtcg_app.service;

import at.fhtw.httpserver.server.Request;
import at.fhtw.mtcg_app.persistence.Token;

public class AuthHandler {
    private static String userToken;
    private static String usernameFromToken;

    static boolean isUserAdmin(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.equals(Token.getAdminTokenPrefix() + "admin-mtcgToken");
    }

    public static boolean checkAuthFromHeader(Request request) {

        try {
            String expectedPrefix = Token.getAdminTokenPrefix();
            if ((request.getHeaderMap().getHeader("Authorization")) != null) {
                userToken = (request.getHeaderMap().getHeader("Authorization"));
                usernameFromToken = userToken.substring(expectedPrefix.length()).split("-")[0];
//TODO:: check token validity also if case: "Bearer -mtcgToken". So with no username
                //System.out.println("valid token format");
                return userToken.startsWith(expectedPrefix);
            }
            //System.out.println("Invalid token format");
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String getUserToken() {
        return userToken;
    }

    public static String getUsernameFromToken() {
        return usernameFromToken;
    }
}
