package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.customer.CustomerId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.Sale;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SaleRepository;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Local(SaleRepository.class)
@Stateless
public class HibernateSaleRepository implements SaleRepository {

    private EntityManager em;

    public HibernateSaleRepository() {
        this.em = EntityManagerUtil.getEntityManager();
    }

    @Override
    public void add(Sale sale) {
        sale.getSaleItemList().forEach(item -> em.persist(item));
        em.persist(sale);
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

    @Override
    public Optional<Sale> saleByInvoiceNumber(String invoiceNumber) {
        TypedQuery<Sale> query = em.createQuery(
                "select s from Sale s where s.invoiceNumber = :invoiceNumber",
                Sale.class
        );

        query.setParameter("invoiceNumber", invoiceNumber);
        return query.getResultStream().findFirst();
    }

    @Override
    public long amountOfSales() {
        TypedQuery<Long> query = em.createQuery(
                "select count(s) from Sale s",
                Long.class
        );

        return query.getSingleResult();
    }

    @Override
    public List<Sale> salesByCustomerId(CustomerId customerId) {
        TypedQuery<Sale> query = em.createQuery(
                "select s from Sale s where s.customerId = :customerId",
                Sale.class
        );

        query.setParameter("customerId", customerId);
        return query.getResultList();
    }
}
