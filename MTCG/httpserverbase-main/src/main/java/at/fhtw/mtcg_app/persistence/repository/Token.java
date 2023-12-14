package at.fhtw.mtcg_app.persistence.repository;

public class Token {
    private static final String ADMIN_TOKEN_PREFIX = "Bearer ";

    public Token() {
    }

    public static String getAdminTokenPrefix() {
        return ADMIN_TOKEN_PREFIX;
    }

}
