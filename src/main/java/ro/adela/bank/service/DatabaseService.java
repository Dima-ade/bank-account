package ro.adela.bank.service;

import jakarta.xml.bind.JAXBException;
import ro.adela.bank.dto.AmountHistoryDto;
import ro.adela.bank.dto.BankAccountDto;
import ro.adela.bank.dto.InterestRateDto;
import ro.adela.bank.dto.OutputSummaryAmountDto;
import ro.adela.bank.enums.OperationType;
import ro.adela.bank.exceptions.JsonProviderException;
import ro.adela.bank.interfaces.AmountAccount;
import ro.adela.bank.interfaces.AmountManagerInterface;
import ro.adela.bank.interfaces.InterestManagerInterface;
import ro.adela.bank.processor.SavingsAccountProcessor;
import ro.adela.bank.repository.AmountHistoryRepository;
import ro.adela.bank.repository.BankAccountRepository;
import ro.adela.bank.repository.BankDataRepository;
import ro.adela.bank.test.entity.Employee;
import ro.adela.bank.test.repository.EmployeeRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public class DatabaseService extends AbstractService {

    @Override
    public void addAccount(BankAccountDto savingsAccount) {

        if (savingsAccount == null) {
            throw new IllegalArgumentException("The savingsAccount is null");
        }

        BankAccountRepository repository = null;
        try {
            // Start database
            repository = new BankAccountRepository();
            // Create person
            repository.save(savingsAccount);
        } finally {
            if (repository != null)
                repository.close();
        }
    }

    @Override
    public AmountAccount addAmount(Integer accountNumber, double amount, LocalDate operationDateFormatted) {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }
        if (operationDateFormatted == null) {
            throw new IllegalArgumentException("The operationDateFormatted is null");
        }
        BankAccountRepository bankAccountRepository = null;
        AmountHistoryRepository amountHistoryRepository = null;
        BankAccountDto account = null;
        try {
            // Start database
            bankAccountRepository = new BankAccountRepository();
            account = bankAccountRepository.findById(accountNumber).get();
            if (account != null) {
                SavingsAccountProcessor savingsAccountProcessor = new SavingsAccountProcessor(account);
                savingsAccountProcessor.deposit(amount);
                bankAccountRepository.save(savingsAccountProcessor.getSavingsAccountDto());
                double currentBalance = savingsAccountProcessor.getSavingsAccountDto().getBalance();
                amountHistoryRepository = new AmountHistoryRepository();
                AmountHistoryDto amountHistory = createHistory(amount, accountNumber, operationDateFormatted, OperationType.DEPOSIT, currentBalance);
                amountHistoryRepository.save(amountHistory);
            } else {
                throw new IllegalArgumentException(String.format("The account %d does not exist", accountNumber));
            }
        } finally {
            if (bankAccountRepository != null) {
                bankAccountRepository.close();
            }
            if (amountHistoryRepository != null) {
                amountHistoryRepository.close();
            }
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
        BankAccountRepository bankAccountRepository = null;
        AmountHistoryRepository amountHistoryRepository = null;
        BankAccountDto account = null;
        try {
            // Start database
            bankAccountRepository = new BankAccountRepository();
            account = bankAccountRepository.findById(accountNumber).get();
            if (account != null) {
                SavingsAccountProcessor savingsAccountProcessor = new SavingsAccountProcessor(account);
                savingsAccountProcessor.withdraw(amount);
                bankAccountRepository.save(savingsAccountProcessor.getSavingsAccountDto());
                double currentBalance = savingsAccountProcessor.getSavingsAccountDto().getBalance();
                amountHistoryRepository = new AmountHistoryRepository();
                AmountHistoryDto amountHistory = createHistory(amount, accountNumber, operationDateFormatted, OperationType.DEPOSIT, currentBalance);
                amountHistoryRepository.save(amountHistory);
            } else {
                throw new IllegalArgumentException(String.format("The account %d does not exist", accountNumber));
            }
        } finally {
            if (bankAccountRepository != null) {
                bankAccountRepository.close();
            }
            if (amountHistoryRepository != null) {
                amountHistoryRepository.close();
            }
        }
        return account;
    }

    @Override
    public InterestManagerInterface getInterestManagerProcessor() throws IOException, JAXBException {
        return null;
    }

    @Override
    public AmountManagerInterface getAmountsProcessor() throws IOException, JAXBException {
        return null;
    }

    @Override
    public AmountAccount getBalanceByAccount(Integer accountNumber) throws IOException, JsonProviderException, JAXBException {
        return null;
    }

    @Override
    public void addInterestRate(InterestRateDto interestRate) throws IOException, JsonProviderException, JAXBException {

    }

    @Override
    public Collection<OutputSummaryAmountDto> filterAmountsByWeeks(Integer accountNumber, LocalDate startDateFormatted, LocalDate endDateFormatted) throws IOException, JAXBException {
        return null;
    }

    @Override
    public Collection<OutputSummaryAmountDto> filterAmountsByMonths(Integer accountNumber, LocalDate startDateFormatted, LocalDate endDateFormatted) throws IOException, JAXBException {
        return null;
    }
}
