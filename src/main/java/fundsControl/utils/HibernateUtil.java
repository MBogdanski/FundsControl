package fundsControl.utils;

public class HibernateUtil {
    private static HibernateUtil ourInstance = new HibernateUtil();

    public static HibernateUtil getInstance() {
        return ourInstance;
    }

    private HibernateUtil() {
    }
}
