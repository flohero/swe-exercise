package swe4.server.config;

public class ServiceConfig {
    public static String userServiceName() {
        return "UserService";
    }

    public static String teamServiceName() {
        return "TeamService";
    }

    public static String gameServiceName() {
        return "GameService";
    }

    public static String betServiceName() {
        return "BetService";
    }

    public static int port() {
        return 1099;
    }
}
