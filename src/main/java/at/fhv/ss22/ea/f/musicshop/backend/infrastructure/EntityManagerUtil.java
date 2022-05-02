package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class EntityManagerUtil {
    private static EntityManager ENTITY_MANAGER;

    public static void beginTransaction() {
        getEntityManager().getTransaction().begin();
    }
    public static void rollback() {
        getEntityManager().getTransaction().rollback();
    }
    public static void commit() {
        getEntityManager().getTransaction().commit();
    }

    private EntityManagerUtil() {
    }

    public static EntityManager getEntityManager() {
        if (ENTITY_MANAGER == null) {
            System.setProperty("hibernate.search.default.indexBase", "data-IGNORE/hibernate/index/default");
            //setting index location for hibernate-lucene here, because didn't work when setting the property in persistence.xml

            Map<String, Object> configOverrides = new HashMap<>();
            Map<String, String> envs = System.getenv();

            envs.forEach((k, v) -> {
                if (k.contains("POSTGRES_USER")) {
                    configOverrides.put("javax.persistence.jdbc.user", v);
                }

                if (k.contains("POSTGRES_PASSWORD")) {
                    configOverrides.put("javax.persistence.jdbc.password", v);
                }
            });

            EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("MusicShopBackend", configOverrides);
            ENTITY_MANAGER = emFactory.createEntityManager();
        }

        return ENTITY_MANAGER;
    }
}
