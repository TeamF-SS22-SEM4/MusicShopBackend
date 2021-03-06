package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.artist.Artist;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.Product;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.product.ProductId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.ProductRepository;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;

@Local(ProductRepository.class)
@Stateless
public class HibernateProductRepository implements ProductRepository {

    private EntityManager em;

    private FullTextEntityManager fullTextEM;

    public HibernateProductRepository() {
        this.em = EntityManagerUtil.getEntityManager();
        this.fullTextEM = Search.getFullTextEntityManager(this.em);
        try {
            this.fullTextEM.createIndexer().startAndWait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Product product) {
        em.persist(product);
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

        List<Product> productsByArtistName = new LinkedList<>();
        //this variable and loop are needed to allow for a dynamic number of keywords
        BooleanJunction<BooleanJunction> buildedQuery = queryBuilder.bool();
        for (String keyword : keywords) {
            buildedQuery = buildedQuery.should(
                    queryBuilder.keyword()
                            .wildcard()
                            .onField("name").boostedTo(20)
                            .andField("label")
                            .andField("songs.title").boostedTo(5)
                            .matching("*" + keyword + "*")
                            .createQuery()
            );

            // since artist name is searched in separate query, because hibernate-lucene doesn't support joins,
            // we don't have results ordered by relevancy, currently all products found via artist name are added
            // at the end of the result list
            TypedQuery<Artist> artistQuery = em.createQuery("" +
                            "select a from Artist a where lower(a.artistName) like :keyword_pattern",
                    Artist.class);
            artistQuery.setParameter("keyword_pattern", "%" + keyword + "%");

            artistQuery.getResultList().forEach(artist -> {
                TypedQuery<Product> subProductQuery = em.createQuery(
                        "select p from Product p left join fetch p.artistIds l where l.artistId = :artist_id",
                        Product.class
                );
                subProductQuery.setParameter("artist_id", artist.getArtistId().getUUID());
                productsByArtistName.addAll(subProductQuery.getResultList());
            });
        }
        org.apache.lucene.search.Query query = buildedQuery.createQuery();
        FullTextQuery jpaQuery = fullTextEM.createFullTextQuery(query, Product.class);


        Set<Product> allProducts = new HashSet<>();
        allProducts.addAll(jpaQuery.getResultList());
        allProducts.addAll(productsByArtistName);
        return new LinkedList<>(allProducts);
    }
}
