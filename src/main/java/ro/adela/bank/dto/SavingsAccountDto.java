package ro.adela.bank.dto;

import java.time.LocalDate;

public class SavingsAccountDto extends BankAccountDto {

    // Default constructor
    public SavingsAccountDto() {
        super();

    }

    // Constructor to initialize SavingsAccount object
    public SavingsAccountDto(Integer accountNumber, String accountHolderName, LocalDate birthDate, LocalDate startDate) {
        super(accountNumber, accountHolderName, birthDate, startDate); // Call the constructor of the superclass
    }

}
