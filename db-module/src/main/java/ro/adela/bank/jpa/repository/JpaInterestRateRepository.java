package ro.adela.bank.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.adela.bank.AmountHistoryDto;
import ro.adela.bank.InterestRateDto;
import ro.adela.bank.repository.AbstractRepository;

import java.util.List;

@Repository
public interface JpaInterestRateRepository extends PagingAndSortingRepository<InterestRateDto, Integer>, JpaRepository<InterestRateDto, Integer> {

    Page<InterestRateDto> findAllById(Integer id, Pageable pageable);
}
