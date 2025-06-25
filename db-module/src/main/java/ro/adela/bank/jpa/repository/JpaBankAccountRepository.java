package ro.adela.bank.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.adela.bank.BankAccountDto;

@Repository
public interface JpaBankAccountRepository extends JpaRepository<BankAccountDto, Integer> {
}

