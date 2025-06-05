package readobject;

import java.time.LocalDate;

public class GetInterestRateByDateReadObject {

    private LocalDate providedDate;

    //Default constructor
    public GetInterestRateByDateReadObject() {
    }

    public LocalDate getProvidedDate() {
        return providedDate;
    }

    public void setProvidedDate(LocalDate providedDate) {
        this.providedDate = providedDate;
    }
}
