package ro.adela.bank.repository;

import ro.adela.bank.BankAccountDto;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BankAccountRepository extends AbstractRepository<BankAccountDto, Integer> {

    public BankAccountRepository(EntityManagerFactory emf) {
        super(emf);
    }

    @Override
    public Optional<BankAccountDto> save(BankAccountDto emp) {
        Objects.requireNonNull(emp, "BankAccount must not be null");

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
    public Optional<BankAccountDto> findById(Integer key) {
        EntityManager em = emf.createEntityManager();
//		em.getTransaction().begin();

        BankAccountDto emp = em.find(BankAccountDto.class, key);
//		em.getTransaction().commit();
        return Optional.ofNullable(emp);
    }

    public List<BankAccountDto> findAll() {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                "SELECT e FROM BankAccountDto e")
                .getResultList();
    }

    public List<BankAccountDto> findByPage(int pageNumber, int pageSize) {
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("From BankAccountDto");
        query.setFirstResult((pageNumber-1) * pageSize);
        query.setMaxResults(pageSize);
        List <BankAccountDto> bankAccountsList = query.getResultList();

        return bankAccountsList;
    }

    @Override
    public void delete(BankAccountDto emp) {
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