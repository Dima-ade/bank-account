package ro.adela.bank.repository;

import ro.adela.bank.dto.BankAccountDto;
import ro.adela.bank.dto.BankDataDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BankDataRepository extends Repository<BankDataDto, Integer> {

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