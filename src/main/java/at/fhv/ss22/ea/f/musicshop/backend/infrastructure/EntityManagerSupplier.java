package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

class EntityManagerSupplier {
    private static EntityManager ENTITY_MANAGER;

    public static EntityManager getEntityManager() {
        if (ENTITY_MANAGER == null) {
            EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("MusicShopBackend");
            ENTITY_MANAGER = emFactory.createEntityManager();
        }

        return ENTITY_MANAGER;
    }
}
