package ro.adela.bank.service;

import ro.adela.bank.AmountHistoryDto;
import ro.adela.bank.BankAccountDto;
import ro.adela.bank.InterestRateDto;
import ro.adela.bank.OutputSummaryAmountDto;
import enums.OperationType;
import interfaces.AmountAccount;
import interfaces.AmountManagerInterface;
import interfaces.InterestManagerInterface;
import jakarta.xml.bind.JAXBException;
import ro.adela.bank.exceptions.JsonProviderException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractService {

    public abstract InterestManagerInterface getInterestManagerProcessor() throws IOException, JAXBException;

    public abstract AmountManagerInterface getAmountsProcessor() throws IOException, JAXBException;

    public abstract AmountAccount getBalanceByAccount(Integer accountNumber) throws IOException, JsonProviderException, JAXBException;

    public abstract List<InterestRateDto> getInterestByPage(Integer pageNumber, Integer pageSize);

    public abstract void addInterestRate(InterestRateDto interestRate) throws IOException, JsonProviderException, JAXBException;

    public abstract void addAccount(BankAccountDto savingsAccount) throws IOException, JsonProviderException, JAXBException;

    public abstract AmountAccount addAmount(Integer accountNumber, double amount, LocalDate operationDateFormatted) throws IOException, JAXBException;

    public abstract AmountAccount removeAmount(Integer accountNumber, double amount, LocalDate operationDateFormatted) throws IOException, JAXBException;

    public abstract List<AmountHistoryDto> getAmounts() throws JAXBException, IOException;

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

    private static final int pageSize = 2;

    protected abstract int readTotalCountForAmounts() throws JAXBException, IOException;

    public List<AmountHistoryDto> getAmounts(int pageIndex, int pageSize) throws JAXBException, IOException {
        return null;
    }

    public final Collection<OutputSummaryAmountDto> filterAmountsByMonths(Integer accountNumber, LocalDate startDateFormatted, LocalDate endDateFormatted) throws JAXBException, IOException {
        if (startDateFormatted == null) {
            throw new IllegalArgumentException("The startDateFormatted is null");
        }
        if (endDateFormatted == null) {
            throw new IllegalArgumentException("The endDateFormatted is null");
        }
        int totalRecords = readTotalCountForAmounts();
        int pageNumber = totalRecords / pageSize;
        if (totalRecords % pageSize != 0) {
            pageNumber++;
        }
        Map<String, OutputSummaryAmountDto> result = new HashMap<>();
        for (int i = 0; i < pageNumber; i++) {
            int pageIndex = i * pageSize;
            List<AmountHistoryDto> amounts = getAmounts(pageIndex, pageSize);
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
        }

        return result.values();
    }

        public Collection<OutputSummaryAmountDto> filterAmountsByWeeks(Integer accountNumber, LocalDate startDateFormatted, LocalDate endDateFormatted) throws IOException, JAXBException {
            if (startDateFormatted == null) {
                throw new IllegalArgumentException("The startDateFormatted is null");
            }
            if (endDateFormatted == null) {
                throw new IllegalArgumentException("The endDateFormatted is null");
            }

            int totalRecords = readTotalCountForAmounts();
            int pageNumber = totalRecords / pageSize;
            if (totalRecords % pageSize != 0) {
                pageNumber++;
            }

            Map<String, OutputSummaryAmountDto> result = new HashMap<>();
            for (int i = 0; i < pageNumber; i++) {
                int pageIndex = i * pageSize;
                List<AmountHistoryDto> amounts = getAmounts(pageIndex, pageSize);
                for (AmountHistoryDto accountAmount : amounts) {
                    if (accountNumber == null || accountNumber.equals(accountAmount.getAccountNumber())) {
                        LocalDate date = accountAmount.getDate();
                        if (date.compareTo(startDateFormatted) >= 0 && date.compareTo(endDateFormatted) <= 0) {
                            int dayOfMonth = date.getDayOfMonth();
                            int dayOfWeek = date.getDayOfWeek().getValue();
                            int calculatedDay = dayOfMonth + (7 - dayOfWeek);

                            LocalDate firstDayOfMonth = date.minusDays(dayOfMonth - 1);
                            LocalDate lastDayOfMonth = firstDayOfMonth.plusMonths(1).minusDays(1);
                            int lastDayOfMontInt = lastDayOfMonth.getDayOfMonth();
                            int day = calculatedDay <= lastDayOfMontInt ? calculatedDay : lastDayOfMontInt;
                            int month = date.getMonthValue();
                            int year = date.getYear();
                            LocalDate endDate = LocalDate.of(year, month, day);

                            StringBuilder key = new StringBuilder();
                            key.append(date.getMonthValue())
                                    .append("-")
                                    .append(date.getYear());
                            OutputSummaryAmountDto summaryAmount = result.get(key.toString());
                            if (summaryAmount == null) {
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
            }

            return result.values();
        }
}
