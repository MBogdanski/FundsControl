package fundsControl.utils;

import fundsControl.models.Transactions;
import fundsControl.models.TransactionsCategories;
import fundsControl.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final Configuration cfg = new Configuration()
            .addAnnotatedClass(User.class)
            .addAnnotatedClass(Transactions.class)
            .addAnnotatedClass(TransactionsCategories.class)
            .configure("hibernate.cfg.xml");
    private static final StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(cfg.getProperties()).build();
    private static final SessionFactory sf = cfg.configure().buildSessionFactory(serviceRegistry);

    private static HibernateUtil ourInstance = new HibernateUtil();

    public static HibernateUtil getInstance() {
        return ourInstance;
    }

    private HibernateUtil() {
    }

    public static Session openSession() {
        return sf.openSession();
    }
}
