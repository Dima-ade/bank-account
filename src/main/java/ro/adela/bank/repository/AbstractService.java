package ro.adela.bank.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ro.adela.bank.dto.AmountHistoryDto;
import ro.adela.bank.dto.InterestRateDto;
import ro.adela.bank.dto.OutputSummaryAmountDto;
import ro.adela.bank.dto.SavingsAccountDto;
import ro.adela.bank.enums.OperationType;
import ro.adela.bank.exceptions.JsonProviderException;
import ro.adela.bank.interfaces.AmountAccount;
import ro.adela.bank.processor.AmountManagerProcessor;
import ro.adela.bank.processor.InterestManagerProcessor;
import ro.adela.bank.processor.SavingsAccountProcessor;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public abstract class AbstractService {

    protected File file;
    protected AccountsJsonData accountsJsonData;
    protected final ObjectMapper objectMapper;
    protected InterestManagerProcessor interestManagerProcessor;
    protected AmountManagerProcessor amountsProcessor;

    protected AbstractService(File file) {
        this.file = file;
        this.objectMapper = new ObjectMapper();
        // support Java 8 date time apis
        this.objectMapper.registerModule(new JavaTimeModule());
        //configure Object mapper for pretty print
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    protected abstract void readAccounts() throws IOException;

    public AccountsJsonData getAccountsJsonData() {
        return accountsJsonData;
    }

    public void addAccount(SavingsAccountDto savingsAccount) throws IOException, JsonProviderException {
            if (savingsAccount == null) {
                throw new IllegalArgumentException("The savingsAccount is null");
            }
            readAccounts();
            boolean exists = false;
            for (SavingsAccountDto account : this.accountsJsonData.getAccounts()) {
                if (account.getAccountNumber().equals(savingsAccount.getAccountNumber())) {
                    exists = true;
                }
            }
            if (!exists) {
                this.accountsJsonData.getAccounts().add(savingsAccount);
                // write JSON to a File
                writeAccounts();
                System.out.println("File saved to: " + file.getAbsolutePath());
            } else {
                throw new JsonProviderException(String.format("The account number %s is duplicate", savingsAccount.getAccountNumber()));
            }
    }

    protected abstract void writeAccounts() throws IOException;

    private void createHistory(double amount, Integer accountNumber, LocalDate operationDateFormatted, OperationType operationType, double currentBalance) {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }
        if (operationDateFormatted == null) {
            throw new IllegalArgumentException("The operationDateFormatted is null");
        }
        if (operationType == null) {
            throw new IllegalArgumentException("The operationType is null");
        }
        AmountHistoryDto amountHistory = new AmountHistoryDto();
        amountHistory.setAmount(amount);
        amountHistory.setAccountNumber(accountNumber);
        amountHistory.setDate(operationDateFormatted);
        amountHistory.setOperationType(operationType);
        amountHistory.setCurrentBalance(currentBalance);
        this.accountsJsonData.getAmounts().add(amountHistory);
    }

    public AmountAccount addAmount(Integer accountNumber, double amount, LocalDate operationDateFormatted) throws IOException {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }
        if (operationDateFormatted == null) {
            throw new IllegalArgumentException("The operationDateFormatted is null");
        }
        readAccounts();
        if (this.accountsJsonData.getAccounts().size() > 0) {
            for (SavingsAccountDto account : this.accountsJsonData.getAccounts()) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    SavingsAccountProcessor savingsAccountProcessor = new SavingsAccountProcessor(account);
                    savingsAccountProcessor.deposit(amount);
                    double currentBalance = savingsAccountProcessor.getSavingsAccountDto().getBalance();
                    createHistory(amount, accountNumber, operationDateFormatted, OperationType.DEPOSIT, currentBalance);
                    // write JSON to a File
                    writeAccounts();
                    this.amountsProcessor.sortAmounts();
                    return account;
                }
            }
        }
        return null;
    }

    public AmountAccount removeAmount(Integer accountNumber, double amount, LocalDate operationDateFormatted) throws IOException {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }
        if (operationDateFormatted == null) {
            throw new IllegalArgumentException("The operationDateFormatted is null");
        }
        readAccounts();
        if (this.accountsJsonData.getAccounts().size() > 0) {
            for (SavingsAccountDto account : this.accountsJsonData.getAccounts()) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    SavingsAccountProcessor savingsAccountProcessor = new SavingsAccountProcessor(account);
                    savingsAccountProcessor.withdraw(amount);
                    double currentBalance = savingsAccountProcessor.getSavingsAccountDto().getBalance();
                    createHistory(amount, accountNumber, operationDateFormatted, OperationType.WITHDRAW, currentBalance);
                    // write JSON to a File
                    writeAccounts();
                    this.amountsProcessor.sortAmounts();
                    return account;
                }
            }
        }
        return null;
    }

    public Collection<OutputSummaryAmountDto> filterAmountsByMonths(Integer accountNumber, LocalDate startDateFormatted, LocalDate endDateFormatted) throws IOException {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }
        if (startDateFormatted == null) {
            throw new IllegalArgumentException("The startDateFormatted is null");
        }
        if (endDateFormatted == null) {
            throw new IllegalArgumentException("The endDateFormatted is null");
        }
        readAccounts();
        Map<String, OutputSummaryAmountDto> result = new HashMap<>();
        for (AmountHistoryDto accountAmount : this.accountsJsonData.getAmounts()) {
            if (accountNumber == null || accountNumber.equals(accountAmount.getAccountNumber())) {
                LocalDate date = accountAmount.getDate();
                if (date.compareTo(startDateFormatted) >= 0 && date.compareTo(endDateFormatted) <= 0) {
                    int firstDayOfMonth = 1;
                    int month = date.getMonthValue();
                    int year = date.getYear();
                    LocalDate startDate = LocalDate.of(year, month, firstDayOfMonth);
                    LocalDate endDate = date.plusMonths(1).minusDays(1);

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

    public Collection<OutputSummaryAmountDto> filterAmountsByWeeks(Integer accountNumber, LocalDate startDateFormatted, LocalDate endDateFormatted) throws IOException {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }
        if (startDateFormatted == null) {
            throw new IllegalArgumentException("The startDateFormatted is null");
        }
        if (endDateFormatted == null) {
            throw new IllegalArgumentException("The endDateFormatted is null");
        }
        readAccounts();
        Map<String, OutputSummaryAmountDto> result = new HashMap<>();
        for (AmountHistoryDto accountAmount : this.accountsJsonData.getAmounts()) {
            if (accountNumber == null || accountNumber.equals(accountAmount.getAccountNumber())) {
                LocalDate date = accountAmount.getDate();
                if (date.compareTo(startDateFormatted) >= 0 && date.compareTo(endDateFormatted) <= 0) {
                    int dayOfMonth = date.getDayOfMonth();
                    int dayOfWeek = date.getDayOfWeek().getValue();
                    int calculatedDay = dayOfMonth + (7 - dayOfWeek);
                    int lastDayOfMonth = date.plusMonths(1).minusDays(1).getDayOfMonth() + 1;
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

    public void addInterestRate(InterestRateDto interestRate) throws IOException, JsonProviderException {
        if (interestRate == null) {
            throw new IllegalArgumentException("The interestRate is null");
        }
        readAccounts();
        boolean exists = false;
        if (this.accountsJsonData.getInterests() == null) {
            this.accountsJsonData.setInterests(new ArrayList<>());
        }
        if (this.accountsJsonData.getInterests().size() > 0) {
            for (InterestRateDto interestDto : this.accountsJsonData.getInterests()) {
                if (interestDto.getActivationDate().compareTo(interestRate.getActivationDate()) == 0) {
                    exists = true;
                }
            }
        }
        if (!exists) {
            this.accountsJsonData.getInterests().add(interestRate);
            // write JSON to a File
            writeAccounts();
            System.out.println("File saved to: " + file.getAbsolutePath());

            this.interestManagerProcessor.sortInterests();
        } else {
            throw new JsonProviderException(String.format("The interest rate %s with the date %d is duplicate", interestRate.getInterestRate(), interestRate.getActivationDate()));
        }
    }

    public InterestManagerProcessor getInterestManagerProcessor() throws IOException {
        readAccounts();
        return this.interestManagerProcessor;
    }

    public AmountManagerProcessor getAmountsProcessor() throws IOException {
        readAccounts();
        return this.amountsProcessor;
    }

    public AmountAccount getBalanceByAccount(Integer accountNumber) throws IOException, JsonProviderException {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }
        readAccounts();
        for (SavingsAccountDto account : this.accountsJsonData.getAccounts()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        throw new JsonProviderException(String.format("The account number %s does not exist", accountNumber));
    }
}
