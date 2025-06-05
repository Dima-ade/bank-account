package readobject;

import java.time.LocalDate;

public class GetTotalInterestRateToDateReadObject {

    private Integer accountNumber;

    private LocalDate toDate;

    //Default constructor
    public GetTotalInterestRateToDateReadObject() {
    }

    public LocalDate geTtoDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public LocalDate getToDate() {
        return toDate;
    }
}
