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

public abstract class AbstractService {

    public abstract InterestManagerInterface getInterestManagerProcessor() throws IOException, JAXBException;

    public abstract AmountManagerInterface getAmountsProcessor() throws IOException, JAXBException;

    public abstract AmountAccount getBalanceByAccount(Integer accountNumber) throws IOException, JsonProviderException, JAXBException;

    public abstract void addInterestRate(InterestRateDto interestRate) throws IOException, JsonProviderException, JAXBException;

    public abstract Collection<OutputSummaryAmountDto> filterAmountsByWeeks(Integer accountNumber, LocalDate startDateFormatted, LocalDate endDateFormatted) throws IOException, JAXBException;

    public abstract void addAccount(BankAccountDto savingsAccount) throws IOException, JsonProviderException, JAXBException;

    public abstract Collection<OutputSummaryAmountDto> filterAmountsByMonths(Integer accountNumber, LocalDate startDateFormatted, LocalDate endDateFormatted) throws IOException, JAXBException;

    public abstract AmountAccount addAmount(Integer accountNumber, double amount, LocalDate operationDateFormatted) throws IOException, JAXBException;

    public abstract AmountAccount removeAmount(Integer accountNumber, double amount, LocalDate operationDateFormatted) throws IOException, JAXBException;

    protected AmountHistoryDto createHistory(double amount, Integer accountNumber, LocalDate operationDateFormatted, OperationType operationType, double currentBalance) {
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
        return amountHistory;
    }
}
