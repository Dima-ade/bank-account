package ro.adela.bank.interfaces;

import java.time.LocalDate;

public interface AmountAccount {

    double getBalance();

    String getAccountHolderName();

    LocalDate getStartDate();
}
