package processor;

import ro.adela.bank.InterestRateDto;
import interfaces.InterestManagerInterface;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InterestManagerProcessor implements InterestManagerInterface {
    private final List<InterestRateDto> interestRates;

    private java.time.temporal.ChronoUnit DAYS;

    public InterestManagerProcessor(List<InterestRateDto> interestRates) {
        this.interestRates = interestRates;
    }

    public void sortInterests() {
        Comparator<InterestRateDto> comparator = new Comparator<InterestRateDto>() {
            @Override
            public int compare(InterestRateDto o1, InterestRateDto o2) {
                LocalDate date1 = o1.getActivationDate();
                LocalDate date2 = o2.getActivationDate();
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
        Collections.sort(interestRates, comparator);
    }

    @Override
    public double getInterestByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("The date argument is null");
        }
        int maxSize = this.interestRates.size();
        for (int i = 0; i < maxSize; i++) {
            LocalDate interestRateDate = this.interestRates.get(i).getActivationDate();
            if (date.compareTo(interestRateDate) >= 0) {
                return this.interestRates.get(i).getInterestRate();
            }
        }
        return 0.0;
    }
}
