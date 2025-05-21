package ro.adela.bank.service;

import jakarta.xml.bind.JAXBException;
import ro.adela.bank.dto.*;
import ro.adela.bank.enums.OperationType;
import ro.adela.bank.exceptions.JsonProviderException;
import ro.adela.bank.interfaces.AmountAccount;
import ro.adela.bank.interfaces.AmountManagerInterface;
import ro.adela.bank.interfaces.InterestManagerInterface;
import ro.adela.bank.processor.AmountManagerProcessor;
import ro.adela.bank.processor.InterestManagerProcessor;
import ro.adela.bank.processor.SavingsAccountProcessor;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public abstract class AbstractFileService extends AbstractService {

    protected File file;
    protected BankDataDto bankDataDto;
    protected InterestManagerProcessor interestManagerProcessor;
    protected AmountManagerProcessor amountsProcessor;

    protected AbstractFileService(File file) {
        this.file = file;
    }

    protected AbstractFileService() {
    }

    protected abstract void processBankData() throws IOException, JAXBException;

    public void readAccounts() throws IOException, JAXBException {
        if (this.bankDataDto == null) {
            if (file.exists()) {
                processBankData();
            } else {
                this.bankDataDto = new BankDataDto();
                this.bankDataDto.setAccounts(new ArrayList<>());
                this.bankDataDto.setAmounts(new ArrayList<>());
                this.bankDataDto.setInterests(new ArrayList<>());
            }
            this.interestManagerProcessor = new InterestManagerProcessor(this.bankDataDto.getInterests());
            this.amountsProcessor = new AmountManagerProcessor(this.bankDataDto.getAmounts());
        }
    }

    public BankDataDto getBankData() {
        return bankDataDto;
    }

    @Override
    public final void addAccount(BankAccountDto savingsAccount) throws IOException, JsonProviderException, JAXBException {
            if (savingsAccount == null) {
                throw new IllegalArgumentException("The savingsAccount is null");
            }
            readAccounts();
            boolean exists = false;
            for (BankAccountDto account : this.bankDataDto.getAccounts()) {
                if (account.getAccountNumber().equals(savingsAccount.getAccountNumber())) {
                    exists = true;
                }
            }
            if (!exists) {
                this.bankDataDto.getAccounts().add(savingsAccount);
                // write JSON to a File
                writeAccounts();
                System.out.println("File saved to: " + file.getAbsolutePath());
            } else {
                throw new JsonProviderException(String.format("The account number %s is duplicate", savingsAccount.getAccountNumber()));
            }
    }

    protected abstract void writeAccounts() throws IOException, JAXBException;

    @Override
    public final AmountAccount addAmount(Integer accountNumber, double amount, LocalDate operationDateFormatted) throws IOException, JAXBException {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }
        if (operationDateFormatted == null) {
            throw new IllegalArgumentException("The operationDateFormatted is null");
        }
        readAccounts();
        if (this.bankDataDto.getAccounts().size() > 0) {
            for (BankAccountDto account : this.bankDataDto.getAccounts()) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    SavingsAccountProcessor savingsAccountProcessor = new SavingsAccountProcessor(account);
                    savingsAccountProcessor.deposit(amount);
                    double currentBalance = savingsAccountProcessor.getSavingsAccountDto().getBalance();
                    AmountHistoryDto amountHistory = createHistory(amount, accountNumber, operationDateFormatted, OperationType.DEPOSIT, currentBalance);
                    this.bankDataDto.getAmounts().add(amountHistory);
                    // write JSON to a File
                    writeAccounts();
                    this.amountsProcessor.sortAmounts();
                    return account;
                }
            }
        }
        return null;
    }

    @Override
    public final AmountAccount removeAmount(Integer accountNumber, double amount, LocalDate operationDateFormatted) throws IOException, JAXBException {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }
        if (operationDateFormatted == null) {
            throw new IllegalArgumentException("The operationDateFormatted is null");
        }
        readAccounts();
        if (this.bankDataDto.getAccounts().size() > 0) {
            for (BankAccountDto account : this.bankDataDto.getAccounts()) {
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

    @Override
    public final Collection<OutputSummaryAmountDto> filterAmountsByMonths(Integer accountNumber, LocalDate startDateFormatted, LocalDate endDateFormatted) throws IOException, JAXBException {
        if (startDateFormatted == null) {
            throw new IllegalArgumentException("The startDateFormatted is null");
        }
        if (endDateFormatted == null) {
            throw new IllegalArgumentException("The endDateFormatted is null");
        }
        readAccounts();
        Map<String, OutputSummaryAmountDto> result = new HashMap<>();
        for (AmountHistoryDto accountAmount : this.bankDataDto.getAmounts()) {
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

    @Override
    public final Collection<OutputSummaryAmountDto> filterAmountsByWeeks(Integer accountNumber, LocalDate startDateFormatted, LocalDate endDateFormatted) throws IOException, JAXBException {
        if (startDateFormatted == null) {
            throw new IllegalArgumentException("The startDateFormatted is null");
        }
        if (endDateFormatted == null) {
            throw new IllegalArgumentException("The endDateFormatted is null");
        }
        readAccounts();
        Map<String, OutputSummaryAmountDto> result = new HashMap<>();
        for (AmountHistoryDto accountAmount : this.bankDataDto.getAmounts()) {
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

    @Override
    public final void addInterestRate(InterestRateDto interestRate) throws IOException, JsonProviderException, JAXBException {
        if (interestRate == null) {
            throw new IllegalArgumentException("The interestRate is null");
        }
        readAccounts();
        boolean exists = false;
        if (this.bankDataDto.getInterests() == null) {
            this.bankDataDto.setInterests(new ArrayList<>());
        }
        if (this.bankDataDto.getInterests().size() > 0) {
            for (InterestRateDto interestDto : this.bankDataDto.getInterests()) {
                if (interestDto.getActivationDate().compareTo(interestRate.getActivationDate()) == 0) {
                    exists = true;
                }
            }
        }
        if (!exists) {
            this.bankDataDto.getInterests().add(interestRate);
            // write JSON to a File
            writeAccounts();
            System.out.println("File saved to: " + file.getAbsolutePath());

            this.interestManagerProcessor.sortInterests();
        } else {
            throw new JsonProviderException(String.format("The interest rate %s with the date %d is duplicate", interestRate.getInterestRate(), interestRate.getActivationDate()));
        }
    }

    @Override
    public final InterestManagerInterface getInterestManagerProcessor() throws IOException, JAXBException {
        readAccounts();
        return this.interestManagerProcessor;
    }

    @Override
    public final AmountManagerInterface getAmountsProcessor() throws IOException, JAXBException {
        readAccounts();
        return this.amountsProcessor;
    }

    @Override
    public final AmountAccount getBalanceByAccount(Integer accountNumber) throws IOException, JsonProviderException, JAXBException {
        if (accountNumber == null) {
            throw new IllegalArgumentException("The accountNumber is null");
        }
        readAccounts();
        for (BankAccountDto account : this.bankDataDto.getAccounts()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        throw new JsonProviderException(String.format("The account number %s does not exist", accountNumber));
    }
}
