package constants;

import java.io.FileInputStream;
import java.util.Properties;

public class ServerConstants {

    public static short VERSION = 90;
    public static String[] WORLD_NAMES = {"Scania", "Bera", "Broa", "Windia", "Khaini", "Bellocan", "Mardia", "Kradia", "Yellonde", "Demethos", "Galicia", "El Nido", "Zenith", "Arcenia", "Kastia", "Judis", "Plana", "Kalluna", "Stius", "Croa", "Medere"};
    ;
	// Login Configuration
    public static final int CHANNEL_LOAD = 100;//Players per channel
    public static final long RANKING_INTERVAL = 60 * 60 * 1000;//60 minutes, 3600000
    public static final boolean ENABLE_PIC = true;
    //Event Configuration
    public static final boolean PERFECT_PITCH = false;
    // IP Configuration
    public static String HOST = "127.0.0.1";
    //Database Configuration
    public static String DB_URL = "jdbc:mysql://localhost:3306/thor";
    public static String DB_USER = "root";
    public static String DB_PASS = "";
    //Other Configuration
    public static boolean JAVA_8 = true;
    public static boolean SHUTDOWNHOOK = true;
    //Gameplay Configurations
    public static final boolean USE_MTS = false;
    public static final boolean USE_FAMILY_SYSTEM = false;
    public static final boolean USE_DUEY = false;
    public static final boolean USE_ITEM_SORT = false;
    public static final boolean USE_PARTY_SEARCH = false;
    //Rates
    public static final int EXP_RATE = 4;
    public static final int MESO_RATE = 2;
    public static final int DROP_RATE = 2;
    public static final int BOSS_DROP_RATE = 2;
    public static final int PARTY_EXPERIENCE_MOD = 1; // change for event stuff
	public static final double PQ_BONUS_EXP_MOD = 0.5;
	
	public static final long EVENT_END_TIMESTAMP = 1428897600000L;
}

