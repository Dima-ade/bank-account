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
import ro.adela.bank.processor.AmountManagerProcessor;
import ro.adela.bank.processor.InterestManagerProcessor;
import ro.adela.bank.processor.SavingsAccountProcessor;
import ro.adela.bank.repository.AmountHistoryRepository;
import ro.adela.bank.repository.BankAccountRepository;
import ro.adela.bank.repository.InterestRateRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                AmountHistoryDto amountHistory = createHistory(amount, accountNumber, operationDateFormatted, OperationType.WITHDRAW, currentBalance);
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
    public void addInterestRate(InterestRateDto interestRate) {
        if (interestRate == null) {
            throw new IllegalArgumentException("The interestRate is null");
        }

        InterestRateRepository repository = null;
        try {
            // Start database
            repository = new InterestRateRepository();
            // Create person
            repository.save(interestRate);
        } finally {
            if (repository != null)
                repository.close();
        }
    }

    private List<InterestRateDto> getInterests() {
        List<InterestRateDto> interestRates = null;

        InterestRateRepository repository = null;
        try {
            // Start database
            repository = new InterestRateRepository();
            interestRates = repository.findAll();
        } finally {
            if (repository != null)
                repository.close();
        }
        return interestRates;
    }

    @Override
    public InterestManagerInterface getInterestManagerProcessor() throws IOException, JAXBException {
        return new InterestManagerProcessor(getInterests());
    }

    private List<AmountHistoryDto> getAmounts() {
        List<AmountHistoryDto> amounts = null;

        AmountHistoryRepository repository = null;
        try {
            // Start database
            repository = new AmountHistoryRepository();
            amounts = repository.findAll();
        } finally {
            if (repository != null)
                repository.close();
        }
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
        BankAccountRepository bankAccountRepository = null;
        BankAccountDto account = null;
        try {
            // Start database
            bankAccountRepository = new BankAccountRepository();
            account = bankAccountRepository.findById(accountNumber).get();
            if (account == null) {
                throw new IllegalArgumentException(String.format("The account %d does not exist", accountNumber));
            }
        } finally {
            if (bankAccountRepository != null) {
                bankAccountRepository.close();
            }
        }
        return account;
    }

    @Override
    public Collection<OutputSummaryAmountDto> filterAmountsByWeeks(Integer accountNumber, LocalDate startDateFormatted, LocalDate endDateFormatted) throws IOException, JAXBException {
        if (startDateFormatted == null) {
            throw new IllegalArgumentException("The startDateFormatted is null");
        }
        if (endDateFormatted == null) {
            throw new IllegalArgumentException("The endDateFormatted is null");
        }
        List<AmountHistoryDto> amounts = getAmounts();
        Map<String, OutputSummaryAmountDto> result = new HashMap<>();
        for (AmountHistoryDto accountAmount : amounts) {
            if (accountNumber == null || accountNumber.equals(accountAmount.getAccountNumber())) {
                LocalDate date = accountAmount.getDate();
                if (date.compareTo(startDateFormatted) >= 0 && date.compareTo(endDateFormatted) <= 0) {
                    int dayOfMonth = date.getDayOfMonth();
                    int dayOfWeek = date.getDayOfWeek().getValue();
                    int calculatedDay = dayOfMonth + (7 - dayOfWeek);
                    int lastDayOfMonth = date.plusMonths(1).minusDays(1).getDayOfMonth() + 1;
                    LocalDate firstDayOfMonth =  date.minusDays(dayOfMonth - 1);
                    int day = calculatedDay <= lastDayOfMonth ? calculatedDay : lastDayOfMonth;
                    int month = date.getMonthValue();
                    int year = date.getYear();
                    LocalDate endDate = LocalDate.of(year, month, day);

                    StringBuilder key = new StringBuilder();
                    key.append(date.getMonthValue())
                            .append("-")
                            .append(date.getYear());
                    OutputSummaryAmountDto summaryAmount = result.get(key.toString());
                    if(summaryAmount == null) {
                        summaryAmount = new OutputSummaryAmountDto(date, endDate, accountNumber);
                        result.put(key.toString(), summaryAmount);
                    }
                    if (accountAmount.getOperationType().equals(OperationType.DEPOSIT)) {
                        summaryAmount.setIn(summaryAmount.getIn() + accountAmount.getAmount());
                    } else if (accountAmount.getOperationType().equals(OperationType.WITHDRAW)) {
                        summaryAmount.setOut(summaryAmount.getOut() + accountAmount.getAmount());
                    }
                }
            }
        }
        return result.values();
    }

    @Override
    public Collection<OutputSummaryAmountDto> filterAmountsByMonths(Integer accountNumber, LocalDate startDateFormatted, LocalDate endDateFormatted) {
        if (startDateFormatted == null) {
            throw new IllegalArgumentException("The startDateFormatted is null");
        }
        if (endDateFormatted == null) {
            throw new IllegalArgumentException("The endDateFormatted is null");
        }
        List<AmountHistoryDto> amounts = getAmounts();
        Map<String, OutputSummaryAmountDto> result = new HashMap<>();
        for (AmountHistoryDto accountAmount : amounts) {
            if (accountNumber == null || accountNumber.equals(accountAmount.getAccountNumber())) {
                LocalDate date = accountAmount.getDate();

                if (date.compareTo(startDateFormatted) >= 0 && date.compareTo(endDateFormatted) <= 0) {
                    int dayOfMonth = date.getDayOfMonth();
                    LocalDate firstDayOfMonth = date.minusDays(dayOfMonth - 1);
                    int month = date.getMonthValue();
                    int year = date.getYear();
                    LocalDate startDate = LocalDate.of(year, month, firstDayOfMonth.getDayOfMonth());
                    LocalDate endDate = firstDayOfMonth.plusMonths(1).minusDays(1);
                    StringBuilder key = new StringBuilder();
                    key.append(date.getMonthValue())
                            .append("-")
                            .append(date.getYear());
                    OutputSummaryAmountDto summaryAmount = result.get(key.toString());
                    if(summaryAmount == null) {
                        summaryAmount = new OutputSummaryAmountDto(startDate, endDate, accountNumber);
                        result.put(key.toString(), summaryAmount);
                    }
                    if (accountAmount.getOperationType().equals(OperationType.DEPOSIT)) {
                        summaryAmount.setIn(summaryAmount.getIn() + accountAmount.getAmount());
                    } else if (accountAmount.getOperationType().equals(OperationType.WITHDRAW)) {
                        summaryAmount.setOut(summaryAmount.getOut() + accountAmount.getAmount());
                    }
                }
            }
        }
        return result.values();
    }
}
