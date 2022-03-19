package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.Sale;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SaleRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Optional;

public class HibernateSaleRepository implements SaleRepository {
    private EntityManager em;

    public HibernateSaleRepository() {
        this.em = EntityManagerSupplier.getEntityManager();
    }

    @Override
    public void add(Sale sale) {
        em.getTransaction().begin();
        sale.getSaleItemList().forEach(item -> em.persist(item));
        em.persist(sale);
        em.getTransaction().commit();
    }

    @Override
    public Optional<Sale> saleById(SaleId saleId) {
        TypedQuery<Sale> query = em.createQuery(
                "select s from Sale s where s.saleId = :sale_id",
                Sale.class
        );
        query.setParameter("sale_id", saleId);
        return query.getResultStream().findFirst();
    }
}
