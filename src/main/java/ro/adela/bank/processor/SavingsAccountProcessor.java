package ro.adela.bank.processor;

import ro.adela.bank.dto.SavingsAccountDto;

public class SavingsAccountProcessor extends BankAccountProcessor {

    // Attribute for interest rate
    private SavingsAccountDto savingsAccountDto;

    // Constructor to initialize SavingsAccount object
    public SavingsAccountProcessor(SavingsAccountDto savingsAccountDto) {
        super(savingsAccountDto);
        this.savingsAccountDto = savingsAccountDto;
    }

    public SavingsAccountDto getSavingsAccountDto() {
        return savingsAccountDto;
    }
}
