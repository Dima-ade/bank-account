package ro.adela.bank.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.adela.bank.AmountHistoryDto;
import ro.adela.bank.repository.AbstractRepository;

@Repository
public interface JpaAmountHistoryRepository extends JpaRepository<AmountHistoryDto, Integer> {
}
