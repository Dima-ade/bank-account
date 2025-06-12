package ro.adela.bank.repository;

import ro.adela.bank.InterestRateDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class InterestRateRepository extends AbstractRepository<InterestRateDto, Integer> {

    public InterestRateRepository(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public Optional<InterestRateDto> save(InterestRateDto emp) {
        Objects.requireNonNull(emp, "InterestRate must not be null");

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
    public Optional<InterestRateDto> findById(Integer key) {
        EntityManager em = emf.createEntityManager();
//		em.getTransaction().begin();

        InterestRateDto emp = em.find(InterestRateDto.class, key);
//		em.getTransaction().commit();
        return Optional.ofNullable(emp);
    }

    public List<InterestRateDto> findAll() {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                "SELECT e FROM InterestRateDto e ORDER BY e.activationDate DESC")
                .getResultList();
    }

    public List<InterestRateDto> findByPage(int pageNumber, int pageSize) {
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("From InterestRateDto");
        query.setFirstResult((pageNumber-1) * pageSize);
        query.setMaxResults(pageSize);
        List <InterestRateDto> interestRatesList = query.getResultList();

        return interestRatesList;
    }

    @Override
    public void delete(InterestRateDto emp) {
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