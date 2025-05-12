package ro.adela.bank.interfaces;

import java.time.LocalDate;

public interface InterestManagerInterface {

    double getInterestByDate(LocalDate date);
}
