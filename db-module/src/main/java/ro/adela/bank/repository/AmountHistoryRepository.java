package ro.adela.bank.repository;

import dto.AmountHistoryDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AmountHistoryRepository extends Repository<AmountHistoryDto, Integer> {

    public AmountHistoryRepository(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public Optional<AmountHistoryDto> save(AmountHistoryDto emp) {
        Objects.requireNonNull(emp, "AmountHistory must not be null");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            if (emp.getId() != null) {
                em.merge(emp);
            } else {
              em.persist(emp);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e; // or display error message
        } finally {
            em.close();
        }
        return Optional.of(emp);
    }

    @Override
    public Optional<AmountHistoryDto> findById(Integer key) {
        EntityManager em = emf.createEntityManager();
//		em.getTransaction().begin();

        AmountHistoryDto emp = em.find(AmountHistoryDto.class, key);
//		em.getTransaction().commit();
        return Optional.ofNullable(emp);
    }

    public List<AmountHistoryDto> findAll() {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                "SELECT e FROM AmountHistoryDto e ORDER BY e.date DESC")
                .getResultList();
    }

    public List<AmountHistoryDto> findByPage(int pageNumber, int pageSize) {
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("From AmountHistoryDto");
        query.setFirstResult((pageNumber-1) * pageSize);
        query.setMaxResults(pageSize);
        List <AmountHistoryDto> amountsHistoryList = query.getResultList();

        return amountsHistoryList;
    }

    @Override
    public void delete(AmountHistoryDto emp) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            em.remove(em.contains(emp) ? emp : em.merge(emp));

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e; // or display error message
        } finally {
            em.close();
        }
    }
}