package it.polito.ai.lab03.utils;

/**
 * Per login e ottenere i tokens, cazzi e mazzi, chiedere a:
 * 1) [/oauth/token],methods=[GET] --> per ottenere i tuoi token date le tue credenziali
 * 2) [/oauth/token],methods=[POST] --> per farti fabbricare nuovo token + refresh token date le credenziali
 */
public class Constants {

    // Endpoints url
    public static final String SECURED_PATTERN = "/secured/**"; // Solo per user, get e post positions

    // Roles
    public static final String ROLE_USER = "ROLE_USER";

    // Db informations
    public static final String DATABASE_URI = "mongodb://localhost:27017";
    public static final String DATABASE_NAME = "db";

    // Configuration
    public static final String CLIENT_SECRET = "$2a$04$u7AkEd1xISJiIMLbi0BKIeRRpkViEu6Hk0nxBe.LpMrsySFWb/IkG";
    public static final String SIMMETRIC_KEY = "keysegretissima";
    public static final String CLIENT_ID = "client";

    // Costanti per posizioni e calcolo con formula di Haversine
    static final double validValueLowerBoundLongitude = -180;
    static final double validValueUpperBoundLongitude = 180;
    static final double validValueLowerBoundLatitude = -90;
    static final double validValueUpperBoundLatitude = 90;
    static final long minTimestamp = 1000000000;
    static final double MAX_SPEED = 100;

    //Il prezzo dell'acquisto di una singola position per ora lo metterei costante
    public static final double priceSingleArchive = 1.00;
    public static final double percentageToUser = 0.70;
}
