package processor;

import dto.BankAccountDto;

public class BankAccountProcessor {
    // Attribute for account number
    private final BankAccountDto bankAccountDto;

    // Constructor to initialize BankAccount object
    public BankAccountProcessor(BankAccountDto bankAccountDto) {
        this.bankAccountDto = bankAccountDto;
    }

    // Method to deposit money into the account
    public void deposit(double amount) {
        if (amount > 0) {
            double balance = this.bankAccountDto.getBalance();
            this.bankAccountDto.setBalance(balance + amount);
            System.out.println("Deposited: " + amount + ". New balance: " + this.bankAccountDto.getBalance());
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    // Method to withdraw money from the account
    public void withdraw(double amount) {
        double balance = this.bankAccountDto.getBalance();
        if (amount > 0 && amount <= balance) {
            this.bankAccountDto.setBalance(balance - amount);
            System.out.println("Withdrew: " + amount + ". New balance: " + this.bankAccountDto.getBalance());
        } else {
            System.out.println("Insufficient balance or invalid amount.");
        }
    }

    public BankAccountDto getBankAccountDto() {
        return bankAccountDto;
    }
}
