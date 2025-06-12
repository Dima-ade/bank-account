package ro.adela.bank.service;

import ro.adela.bank.AmountHistoryDto;
import ro.adela.bank.BankAccountDto;
import ro.adela.bank.InterestRateDto;
import enums.OperationType;
import interfaces.AmountAccount;
import interfaces.AmountManagerInterface;
import interfaces.InterestManagerInterface;
import jakarta.xml.bind.JAXBException;
import processor.AmountManagerProcessor;
import processor.InterestManagerProcessor;
import processor.SavingsAccountProcessor;
import ro.adela.bank.exceptions.JsonProviderException;
import ro.adela.bank.repository.AmountHistoryRepository;
import ro.adela.bank.repository.BankAccountRepository;
import ro.adela.bank.repository.InterestRateRepository;
import ro.adela.bank.repository.AbstractRepository;

import jakarta.persistence.EntityManagerFactory;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class DatabaseService extends AbstractService {

    private final EntityManagerFactory emf;

    public DatabaseService(EntityManagerFactory emf) {
        this.emf = emf;

    }

    public DatabaseService() {
        emf = AbstractRepository.createEntityManagerFactory();

    }

    @Override
    public void addAccount(BankAccountDto savingsAccount) {

        if (savingsAccount == null) {
            throw new IllegalArgumentException("The savingsAccount is null");
        }

        BankAccountRepository repository = new BankAccountRepository(emf);
            // Create person
        repository.save(savingsAccount);
    }

    @Override
    public AmountAccount addAmount(Integer accountNumber, double amount, LocalDate operationDateFormatted) {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }
        if (operationDateFormatted == null) {
            throw new IllegalArgumentException("The operationDateFormatted is null");
        }

            // Start database
            BankAccountRepository bankAccountRepository = new BankAccountRepository(emf);
            BankAccountDto account = bankAccountRepository.findById(accountNumber).get();
            if (account != null) {
                SavingsAccountProcessor savingsAccountProcessor = new SavingsAccountProcessor(account);
                savingsAccountProcessor.deposit(amount);
                bankAccountRepository.save(savingsAccountProcessor.getSavingsAccountDto());
                double currentBalance = savingsAccountProcessor.getSavingsAccountDto().getBalance();
                AmountHistoryRepository amountHistoryRepository = new AmountHistoryRepository(emf);
                AmountHistoryDto amountHistory = createHistory(amount, accountNumber, operationDateFormatted, OperationType.DEPOSIT, currentBalance);
                amountHistoryRepository.save(amountHistory);
            } else {
                throw new IllegalArgumentException(String.format("The account %d does not exist", accountNumber));
            }
        return account;
    }

    @Override
    public AmountAccount removeAmount(Integer accountNumber, double amount, LocalDate operationDateFormatted) throws IOException, JAXBException {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }
        if (operationDateFormatted == null) {
            throw new IllegalArgumentException("The operationDateFormatted is null");
        }
        // Start database
            BankAccountRepository bankAccountRepository = new BankAccountRepository(emf);
            BankAccountDto account = bankAccountRepository.findById(accountNumber).get();
            if (account != null) {
                SavingsAccountProcessor savingsAccountProcessor = new SavingsAccountProcessor(account);
                savingsAccountProcessor.withdraw(amount);
                bankAccountRepository.save(savingsAccountProcessor.getSavingsAccountDto());
                double currentBalance = savingsAccountProcessor.getSavingsAccountDto().getBalance();
                AmountHistoryRepository amountHistoryRepository = new AmountHistoryRepository(emf);
                AmountHistoryDto amountHistory = createHistory(amount, accountNumber, operationDateFormatted, OperationType.WITHDRAW, currentBalance);
                amountHistoryRepository.save(amountHistory);
            } else {
                throw new IllegalArgumentException(String.format("The account %d does not exist", accountNumber));
            }
        return account;
    }

    @Override
    public void addInterestRate(InterestRateDto interestRate) {
        if (interestRate == null) {
            throw new IllegalArgumentException("The interestRate is null");
        }

        InterestRateRepository repository = new InterestRateRepository(emf);
        // Create person
        repository.save(interestRate);
    }

    private List<InterestRateDto> getInterests() {
        List<InterestRateDto> interestRates = null;

        InterestRateRepository repository = new InterestRateRepository(emf);
        interestRates = repository.findAll();

        return interestRates;
    }

    @Override
    public InterestManagerInterface getInterestManagerProcessor() throws IOException, JAXBException {
        return new InterestManagerProcessor(getInterests());
    }

    @Override
    public final List<AmountHistoryDto> getAmounts() {
        List<AmountHistoryDto> amounts = null;

        AmountHistoryRepository repository = new AmountHistoryRepository(emf);
        amounts = repository.findAll();

        return amounts;
    }

    @Override
    public AmountManagerInterface getAmountsProcessor() throws IOException, JAXBException {
        return new AmountManagerProcessor(getAmounts());
    }

    @Override
    public AmountAccount getBalanceByAccount(Integer accountNumber) throws IOException, JsonProviderException, JAXBException {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }

        // Start database
            BankAccountRepository bankAccountRepository = new BankAccountRepository(emf);
            BankAccountDto account = bankAccountRepository.findById(accountNumber).get();
            if (account == null) {
                throw new IllegalArgumentException(String.format("The account %d does not exist", accountNumber));
            }
        return account;
    }

    @Override
    protected int readTotalCountForAmounts() {
        AmountHistoryRepository repository = new AmountHistoryRepository(emf);

        int totalCount =  repository.totalCount();

        return totalCount;
    }

    @Override
    public List<AmountHistoryDto> getAmounts(int pageIndex, int pageSize) {
        List<AmountHistoryDto> amounts = null;

        AmountHistoryRepository repository = new AmountHistoryRepository(emf);
        amounts = repository.findByPage(pageIndex + 1, pageSize);

        return amounts;
    }

    @Override
    public List<InterestRateDto> getInterestByPage(Integer pageNumber, Integer pageSize) {
        List<InterestRateDto> interests = null;

        InterestRateRepository repository = new InterestRateRepository(emf);
        interests = repository.findByPage(pageNumber, pageSize);

        return interests;
    }
}
