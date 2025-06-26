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

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public abstract class AbstractDatabaseService extends AbstractService {

    // TODO Adela: move emf to child at the end
    protected final EntityManagerFactory emf;

    public AbstractDatabaseService(EntityManagerFactory emf) {
        this.emf = emf;

    }

    public AbstractDatabaseService() {
        emf = AbstractRepository.createEntityManagerFactory();

    }

    protected abstract void saveAccount(BankAccountDto savingsAccount);

    @Override
    public void addAccount(BankAccountDto savingsAccount) {

        if (savingsAccount == null) {
            throw new IllegalArgumentException("The savingsAccount is null");
        }

        // Create person
        saveAccount(savingsAccount);
    }

    protected abstract BankAccountDto findAccountByNumber(Integer accountNumber);


    protected abstract void saveAmountHistory(AmountHistoryDto emp);


        @Override
    public AmountAccount addAmount(Integer accountNumber, double amount, LocalDate operationDateFormatted) {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }
        if (operationDateFormatted == null) {
            throw new IllegalArgumentException("The operationDateFormatted is null");
        }

            // Start database
            BankAccountDto account = findAccountByNumber(accountNumber);
            if (account != null) {
                SavingsAccountProcessor savingsAccountProcessor = new SavingsAccountProcessor(account);
                savingsAccountProcessor.deposit(amount);

                saveAccount(savingsAccountProcessor.getSavingsAccountDto());

                double currentBalance = savingsAccountProcessor.getSavingsAccountDto().getBalance();
                AmountHistoryDto amountHistory = createHistory(amount, accountNumber, operationDateFormatted, OperationType.DEPOSIT, currentBalance);
                saveAmountHistory(amountHistory);
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
            BankAccountDto account = findAccountByNumber(accountNumber);
            if (account != null) {
                SavingsAccountProcessor savingsAccountProcessor = new SavingsAccountProcessor(account);
                savingsAccountProcessor.withdraw(amount);

                saveAccount(savingsAccountProcessor.getSavingsAccountDto());

                double currentBalance = savingsAccountProcessor.getSavingsAccountDto().getBalance();

                AmountHistoryDto amountHistory = createHistory(amount, accountNumber, operationDateFormatted, OperationType.WITHDRAW, currentBalance);
                saveAmountHistory(amountHistory);
            } else {
                throw new IllegalArgumentException(String.format("The account %d does not exist", accountNumber));
            }
        return account;
    }

    protected abstract void saveInterestRate(InterestRateDto interestRate);

    @Override
    public void addInterestRate(InterestRateDto interestRate) {
        if (interestRate == null) {
            throw new IllegalArgumentException("The interestRate is null");
        }

        saveInterestRate(interestRate);
    }

    protected abstract List<InterestRateDto> findAllInterestRate();

    private List<InterestRateDto> getInterests() {
        List<InterestRateDto> interestRates = findAllInterestRate();

        return interestRates;
    }

    @Override
    public InterestManagerInterface getInterestManagerProcessor() throws IOException, JAXBException {
        return new InterestManagerProcessor(getInterests());
    }

    protected abstract List<AmountHistoryDto> findAllAmounts();

    @Override
    public final List<AmountHistoryDto> getAmounts() {
        return findAllAmounts();
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
            BankAccountDto account = findAccountByNumber(accountNumber);
            if (account == null) {
                throw new IllegalArgumentException(String.format("The account %d does not exist", accountNumber));
            }
        return account;
    }

    protected abstract Integer amountTotalCount();

    @Override
    protected int readTotalCountForAmounts() {
        int totalCount =  amountTotalCount();

        return totalCount;
    }

    protected abstract List<AmountHistoryDto> getAmountsByPage(int pageIndex, int pageSize);

    @Override
    public List<AmountHistoryDto> getAmounts(int pageIndex, int pageSize) {
        List<AmountHistoryDto> amounts = getAmountsByPage(pageIndex + 1, pageSize);

        return amounts;
    }

    protected abstract List<InterestRateDto> getInterestByPageInRepository(Integer pageNumber, Integer pageSize);
    @Override
    public List<InterestRateDto> getInterestByPage(Integer pageNumber, Integer pageSize) {
        List<InterestRateDto> interests = getInterestByPageInRepository(pageNumber, pageSize);

        return interests;
    }
}
