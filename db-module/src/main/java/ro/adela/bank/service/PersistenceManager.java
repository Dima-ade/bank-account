package ro.adela.bank.service;

import lombok.experimental.FieldNameConstants;
import ro.adela.bank.jpa.repository.JpaAmountHistoryRepository;
import ro.adela.bank.jpa.repository.JpaBankAccountRepository;
import ro.adela.bank.jpa.repository.JpaBankDataRepository;
import ro.adela.bank.jpa.repository.JpaInterestRateRepository;

public class PersistenceManager {

    private final JpaAmountHistoryRepository jpaAmountHistoryRepository;

    private final JpaBankAccountRepository jpaBankAccountRepository;

    private JpaBankDataRepository jpaBankDataRepository;

    private JpaInterestRateRepository jpaInterestRateRepository;

    public PersistenceManager(JpaAmountHistoryRepository jpaAmountHistoryRepository, JpaBankAccountRepository jpaBankAccountRepository, JpaBankDataRepository jpaBankDataRepository, JpaInterestRateRepository jpaInterestRateRepository) {
        this.jpaAmountHistoryRepository = jpaAmountHistoryRepository;
        this.jpaBankAccountRepository = jpaBankAccountRepository;
        this.jpaBankDataRepository = jpaBankDataRepository;
        this.jpaInterestRateRepository = jpaInterestRateRepository;
    }



    public JpaAmountHistoryRepository getJpaAmountHistoryRepository() {
        return jpaAmountHistoryRepository;
    }

    public JpaBankAccountRepository getJpaBankAccountRepository() {
        return jpaBankAccountRepository;
    }

    public JpaBankDataRepository getJpaBankDataRepository() {
        return jpaBankDataRepository;
    }

    public JpaInterestRateRepository getJpaInterestRateRepository() {
        return jpaInterestRateRepository;
    }
}
