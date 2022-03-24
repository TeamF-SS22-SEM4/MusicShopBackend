package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import io.github.cdimascio.dotenv.Dotenv;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

class EntityManagerSupplier {
    private static EntityManager ENTITY_MANAGER;

    public static EntityManager getEntityManager() {
        if (ENTITY_MANAGER == null) {
            System.setProperty("hibernate.search.default.indexBase", "data-IGNORE/hibernate/index/default");
            //setting index location for hibernate-lucene here, because didn't work when setting the property in persistence.xml

            Dotenv dotenv = Dotenv.load();
            Map<String, Object> configOverrides = new HashMap<>();

            configOverrides.put("javax.persistence.jdbc.user", dotenv.get("POSTGRES_USER"));
            configOverrides.put("javax.persistence.jdbc.password", dotenv.get("POSTGRES_PASSWORD"));

            System.out.println(dotenv.get("POSTGRES_USER"));
            System.out.println(dotenv.get("POSTGRES_PASSWORD"));

            EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("MusicShopBackend", configOverrides);
            ENTITY_MANAGER = emFactory.createEntityManager();
        }

        return ENTITY_MANAGER;
    }
}
