package ro.adela.bank.service;

import enums.OperationType;
import interfaces.AmountAccount;
import interfaces.AmountManagerInterface;
import interfaces.InterestManagerInterface;
import jakarta.persistence.EntityManagerFactory;
import jakarta.xml.bind.JAXBException;
import processor.AmountManagerProcessor;
import processor.InterestManagerProcessor;
import processor.SavingsAccountProcessor;
import ro.adela.bank.AmountHistoryDto;
import ro.adela.bank.BankAccountDto;
import ro.adela.bank.InterestRateDto;
import ro.adela.bank.exceptions.JsonProviderException;
import ro.adela.bank.repository.AbstractRepository;
import ro.adela.bank.repository.AmountHistoryRepository;
import ro.adela.bank.repository.BankAccountRepository;
import ro.adela.bank.repository.InterestRateRepository;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class JpaDatabaseService extends AbstractDatabaseService {

    private final PersistenceManager persistenceManager;

    public JpaDatabaseService(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;

    }

}
