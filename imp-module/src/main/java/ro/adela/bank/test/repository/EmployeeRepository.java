package ro.adela.bank.test.repository;

import ro.adela.bank.test.entity.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class EmployeeRepository extends Repository<Employee, Integer> {

    @Override
    public Optional<Employee> save(Employee emp) {
        Objects.requireNonNull(emp, "Person must not be null");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            if (emp.getEmpid() != null) {
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

    public List<Employee> findAll() {
        EntityManager em = emf.createEntityManager();
        return em.createQuery(
                "SELECT e FROM Employee e")
                .getResultList();
    }

    public List<Employee> findByPage(int pageNumber, int pageSize) {
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("From Employee");
        query.setFirstResult((pageNumber-1) * pageSize);
        query.setMaxResults(pageSize);
        List <Employee> employeeList = query.getResultList();

        return employeeList;
    }

    public long totalCount() {
        EntityManager em = emf.createEntityManager();
        long count = 0;
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();
            count = (long) em.createQuery("select count(e.empid) from Employee e").getSingleResult();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e; // or display error message
        } finally {
            em.close();
        }
        return count;
    }

    @Override
    public Optional<Employee> findById(Integer key) {
        EntityManager em = emf.createEntityManager();
//		em.getTransaction().begin();

        Employee emp = em.find(Employee.class, key);
//		em.getTransaction().commit();
        return Optional.ofNullable(emp);
    }

    @Override
    public void delete(Employee emp) {
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