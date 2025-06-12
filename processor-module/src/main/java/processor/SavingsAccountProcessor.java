package processor;

import ro.adela.bank.BankAccountDto;

public class SavingsAccountProcessor extends BankAccountProcessor {

    // Attribute for interest rate
    private BankAccountDto savingsAccountDto;

    // Constructor to initialize SavingsAccount object
    public SavingsAccountProcessor(BankAccountDto savingsAccountDto) {
        super(savingsAccountDto);
        this.savingsAccountDto = savingsAccountDto;
    }

    public BankAccountDto getSavingsAccountDto() {
        return savingsAccountDto;
    }
}
