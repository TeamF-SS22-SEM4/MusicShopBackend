import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Application {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        // To test if db connection works
        EntityManagerFactory fact = Persistence.createEntityManagerFactory("at.fhv.ss22.ea.f");
        EntityManager entityManager = fact.createEntityManager();
    }
}
