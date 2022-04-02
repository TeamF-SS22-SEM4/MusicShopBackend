package at.fhv.ss22.ea.f.musicshop.backend.infrastructure;

import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.Sale;
import at.fhv.ss22.ea.f.musicshop.backend.domain.model.sale.SaleId;
import at.fhv.ss22.ea.f.musicshop.backend.domain.repository.SaleRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class HibernateSaleRepository implements SaleRepository {
    private static final String INITIAL_SALE_NUMBER = "R000001";

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

    // TODO: Remove
    @Deprecated
    @Override
    public String nextSaleNumber() {
        TypedQuery<String> query = em.createQuery(
                "select s.invoiceNumber from Sale s where s.invoiceNumber <> 'TODD' order by s.invoiceNumber desc ",
                String.class
        );

        UnaryOperator<String> increasingSaleNumber = old -> {
            int oldNumber = 1;
            try {
                oldNumber = Integer.parseInt(old.substring(1)); //remove the R-character in sale number
            } catch (NumberFormatException e) {
                // ignore for now, will sometimes come here because dummy data may contain "T0DO" as invoiceNumber
            }
            return "R" + String.format("%06d", oldNumber+1);
        };
        return query.getResultStream().findFirst().map(increasingSaleNumber).orElse(INITIAL_SALE_NUMBER);
    }
}
