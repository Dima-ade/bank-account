package ro.adela.bank.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ro.adela.bank.AmountHistoryDto;

@Repository
public interface JpaAmountHistoryRepository extends PagingAndSortingRepository<AmountHistoryDto, Integer> {

    @Query("SELECT count(e) FROM AmountHistoryDto e")
    int totalCount();

    Page<AmountHistoryDto> findAll(Pageable pageable);

}
