package interfaces;

import java.time.LocalDate;

public interface AmountManagerInterface {

    double getBalanceByDateAndAccount(LocalDate date, Integer accountNumber);
}
