package ro.adela.bank.repository;

import ro.adela.bank.BankDataDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import java.util.Objects;
import java.util.Optional;

public class BankDataRepository extends AbstractRepository<BankDataDto, Integer> {

    public BankDataRepository(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public Optional<BankDataDto> save(BankDataDto emp) {
        Objects.requireNonNull(emp, "BankData must not be null");

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
    public Optional<BankDataDto> findById(Integer key) {
        EntityManager em = emf.createEntityManager();
//		em.getTransaction().begin();

        BankDataDto emp = em.find(BankDataDto.class, key);
//		em.getTransaction().commit();
        return Optional.ofNullable(emp);
    }

    @Override
    public void delete(BankDataDto emp) {
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