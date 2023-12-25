package at.fhtw.mtcg_app.persistence;

public abstract class Token {
    private static final String ADMIN_TOKEN_PREFIX = "Bearer ";

    public static String getAdminTokenPrefix() {
        return ADMIN_TOKEN_PREFIX;
    }

}
