package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HibernateProductRepository implements ProductRepository {

    private EntityManager em;
    private FullTextEntityManager fullTextEM;

    public HibernateProductRepository() {
        this.em = EntityManagerSupplier.getEntityManager();
        this.fullTextEM = Search.getFullTextEntityManager(this.em);
        try {
            this.fullTextEM.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                "select p from Product p where p.productId = :product_id",
                Product.class);
        query.setParameter("product_id", productId);
        return query.getResultStream().findFirst();
    }

    @Override
    public List<Product> fullTextSearch(String searchString) {
        List<String> keywords = Arrays.stream(searchString.split(" "))
                .map(String::toLowerCase)//by default hibernate-lucene indexes/analyzes the searchable fields as lowerCase
                .collect(Collectors.toList());
        QueryBuilder queryBuilder = this.fullTextEM.getSearchFactory().buildQueryBuilder().forEntity(Product.class).get();

        //this variable and loop are needed to allow for a dynamic number of keywords
        BooleanJunction buildedQuery =  queryBuilder.bool();
        for (String keyword : keywords) {
            buildedQuery = buildedQuery.should(
                queryBuilder.keyword()
                        .wildcard()
                .onField("name").boostedTo(2)
                .andField("label")
                .andField("songs.title").boostedTo(2)
                .matching("*" + keyword + "*")
                .createQuery()
            );
        }
        org.apache.lucene.search.Query query = buildedQuery.createQuery();
        FullTextQuery jpaQuery = fullTextEM.createFullTextQuery(query, Product.class);

        //notes on searching by artistName:
        // since artist is a separate entity to product and lucene-search doesn't support joins, need to add  results
        // from artist name in separate query, but also unsure how to search product by artistName since
        // artist and product entities arent related, "as seen by hibernate"
        // still unsure how to sort the total resulting list

        return jpaQuery.getResultList();
    }
}
