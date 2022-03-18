package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

public class HibernateProductRepository implements ProductRepository {

    @PersistenceContext
    private EntityManager em;

    public HibernateProductRepository() {
        this.em = EntityManagerSupplier.getEntityManager();
    }

    @Override
    public void add(Product product) {
        em.getTransaction().begin(); //TODO automate transaction management or at least move it to application layer
        em.persist(product);
        em.getTransaction().commit();
    }

    @Override
    public Optional<Product> productById(ProductId productId) {
        TypedQuery<Product> query = em.createQuery(
                "select p from Product p where p.id = :product_id",
                Product.class);
        query.setParameter("product_id", productId);
        //TODO For some reason this doesn't work, when removing where clause and debugging, result list contains
        // the product with the searched id, cant figure out why it doesn't give the right product. HELP pls
        return query.getResultStream().findFirst();
    }

    @Override
    public List<Product> fullTextSearch(String searchString) {
        //TODO implement
        return List.of();
    }

}
