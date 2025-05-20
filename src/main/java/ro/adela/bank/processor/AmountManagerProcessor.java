package ro.adela.bank.processor;

import ro.adela.bank.interfaces.AmountManagerInterface;
import ro.adela.bank.interfaces.InterestManagerInterface;
import ro.adela.bank.dto.AmountHistoryDto;
import ro.adela.bank.dto.TotalInterestByDayDto;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AmountManagerProcessor implements AmountManagerInterface {

    private final List<AmountHistoryDto> amounts;

    private java.time.temporal.ChronoUnit DAYS;

    public AmountManagerProcessor(List<AmountHistoryDto> amounts) {
        this.amounts = amounts;
    }

    public void sortAmounts() {
        Comparator<AmountHistoryDto> comparator = new Comparator<AmountHistoryDto>() {
            @Override
            public int compare(AmountHistoryDto o1, AmountHistoryDto o2) {
                LocalDate date1 = o1.getDate();
                LocalDate date2 = o2.getDate();
                if(date1 == null) {
                    throw new NullPointerException("Null date1 argument");
                }
                if(date2 == null) {
                    throw new NullPointerException("Null date2 argument");
                }
                int result = date1.compareTo(date2);
                return -result;
            }
        };
        Collections.sort(amounts, comparator);
    }

    @Override
    public double getBalanceByDateAndAccount(LocalDate date, Integer accountNumber) {
        if (date == null) {
            throw new IllegalArgumentException("The date argument is null");
        }
        if (accountNumber == null) {
            throw new IllegalArgumentException("The account number argument is null");
        }
        int maxSize = this.amounts.size();
        for (int i = 0; i < maxSize; i++) {
            LocalDate amountDate = this.amounts.get(i).getDate();
            Integer amountAccount = this.amounts.get(i).getAccountNumber();
            if (accountNumber.equals(amountAccount) && date.compareTo(amountDate) >= 0) {
                return this.amounts.get(i).getCurrentBalance();
            }
        }

        return 0.0;
    }

    public static TotalInterestByDayDto computeTotalInterestBetweenTwoDates(LocalDate startDate, LocalDate endDate, Integer accountNumber,
                                                                            InterestManagerInterface interestManagerProcessor, AmountManagerInterface amountManagerProcessor) {
        if(startDate == null) {
            throw new IllegalArgumentException("Null startDate argument");
        }
        if(endDate == null) {
            throw new IllegalArgumentException("Null endDate argument");
        }
        if (accountNumber == null) {
            throw new IllegalArgumentException("The account number argument is null");
        }
        if (interestManagerProcessor == null) {
            throw new IllegalArgumentException("The interestManagerProcessor argument is null");
        }
        if (amountManagerProcessor == null) {
            throw new IllegalArgumentException("The amountManagerProcessor argument is null");
        }
        TotalInterestByDayDto totalInterestByDayDto = new TotalInterestByDayDto(startDate, endDate, 0.0, accountNumber);
        if (startDate.compareTo(endDate) > 0) {
            totalInterestByDayDto.setTotalInterest(0.0);
        } else {
            double totalInterestSum = 0.0;
            List<LocalDate> dates = startDate.datesUntil(endDate).collect(Collectors.toList());
            for (LocalDate date : dates) {
                double interest = interestManagerProcessor.getInterestByDate(date);
                double accountBalance = amountManagerProcessor.getBalanceByDateAndAccount(date, accountNumber);
                totalInterestSum += ((interest / 100.0)  * accountBalance) / 365;
            }
            totalInterestByDayDto.setTotalInterest(totalInterestSum);
        }
        return totalInterestByDayDto;
    }
}
