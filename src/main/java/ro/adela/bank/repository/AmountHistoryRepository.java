package ro.adela.bank.repository;

import ro.adela.bank.dto.AmountHistoryDto;
import ro.adela.bank.dto.BankAccountDto;
import ro.adela.bank.test.entity.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AmountHistoryRepository extends Repository<AmountHistoryDto, Integer> {

    @Override
    public Optional<AmountHistoryDto> save(AmountHistoryDto emp) {
        Objects.requireNonNull(emp, "AmountHistory must not be null");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            if (emp.getAccountNumber() != null) {
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
                "SELECT e FROM AmountHistoryDto e")
                .getResultList();
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