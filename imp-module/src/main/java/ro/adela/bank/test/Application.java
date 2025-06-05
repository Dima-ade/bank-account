package ro.adela.bank.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.adela.bank.test.entity.Employee;
import ro.adela.bank.test.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        EmployeeRepository repository = null;
        try {
            // Start database

            //Employee emp = new Employee();
            //emp.setName("Elon");

            repository = new EmployeeRepository();
            // Create person
            //repository.save(emp);

            // Hibernate generates id of 1
            Optional<Employee> p = repository.findById(1);

            p.ifPresent(elon -> {
                log.info("Employee from database: {}", elon);
            });
            // Update person record
            repository.save(p.get());

            p = Optional.empty();

            // Read updated record
            p = repository.findById(1);
            p.ifPresent(consumer -> {
                log.info("Employee updated: {}", consumer);
            });

            // Delete person
            //repository.delete(p.get());

            p = repository.findById(1);

            log.info("Does employee exist: {}", p.isPresent());
            System.out.println("Does employee exist: " + p.isPresent());

            log.info("Find all employees");
            int pageNumber = 1;
            int pageSize = 3;
            List<Employee> empByPage = repository.findByPage(pageNumber, pageSize);
            long count = repository.totalCount();
            log.info("Found");
        } catch (Exception e) {
            log.error("Error occurred in initialization: " + e.getMessage());
            e.printStackTrace();
        } finally {
            log.info("Closing Entity Manager Factory");
            if (repository != null)
                repository.close();
            log.info("Entity Manager Factory closed ");
            log.info("Server shutting down");
            log.info("Shutdown complete");
        }
    }
}
