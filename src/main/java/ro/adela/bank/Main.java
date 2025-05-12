package ro.adela.bank;

import ro.adela.bank.dto.InterestRateDto;
import ro.adela.bank.dto.OutputSummaryAmountDto;
import ro.adela.bank.dto.SavingsAccountDto;
import ro.adela.bank.dto.TotalInterestByDayDto;
import ro.adela.bank.exceptions.JsonProviderException;
import ro.adela.bank.interfaces.AmountAccount;
import ro.adela.bank.processor.AmountManagerProcessor;
import ro.adela.bank.processor.InterestManagerProcessor;
import ro.adela.bank.readobject.*;
import ro.adela.bank.repository.JsonRepository;
import ro.adela.bank.utils.CsvFileWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Scanner;

public class Main {

    private JsonRepository repository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private void run() throws IOException, JsonProviderException {
        File file  = new File("account.json");
        this.repository = new JsonRepository(file);

        // Java 17 multiline string
        String inputMessage = """
                Please enter number from 1 - 10 or write 'exit' to stop the program.
                1 create account
                2 add money
                3 withdraw money
                4 filter inputs and outputs by months
                5 filter inputs and outputs by months and account
                6 filter inputs and outputs by weeks
                7 filter inputs and outputs by weeks and account
                8 add interest rate to interest rates list
                9 get interest rate by date
                10 get sum interests for an account between creatin date and an introduced date
                11 exit
                """;
        System.out.println(inputMessage);
        // BufferedReader is created in try-with-resources. It will be closed automatically
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            do {
                // Read every time user input in the beginning
                line = reader.readLine();
                // line will be checked. If it is different from 1,2,3 or "exit" it will print
                // "Invalid input. Please try again"
                // Switch in Java 17 with Lambda expressions
                String command = switch (line) {
                    case "1" -> createAccountOption();
                    case "2" -> addMoneyOption();
                    case "3" -> removeMoneyOption();
                    case "4" -> filterAmountsByMonthsOption();
                    case "5" -> filterAmountsByMonthsAndAccountOption();
                    case "6" -> filterAmountsByWeeksOption();
                    case "7" -> filterAmountsByWeeksAndAccountOption();
                    case "8" -> addInterestRateOption();
                    case "9" -> getInterestRateByDateOption();
                    case "10" -> getTotalInterestRateToDateOption();
                    case "11" -> "exit";
                    default -> "Invalid input. Valid input 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 or 'exit' Please try again";
                };
                System.out.println("The command is: " + command);

                // do again the cycle if the user input is not "exit"
            } while (!"11".equals(line));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Printing final message
        System.out.println("The program exit! Goodbye!");
    }

    private String createAccountOption() throws IOException, JsonProviderException {
        CreateAccountReadObject createAccountReadObject;
        do {
            System.out.println("Enter the details for create account!!!");
            createAccountReadObject = readFromKeyboardCreateAccount();
        }
        while (createAccountReadObject == null);

        SavingsAccountDto accountDto = new SavingsAccountDto(createAccountReadObject.getAccountNumber(), createAccountReadObject.getAccountHolderName(), createAccountReadObject.getBirtDate(), createAccountReadObject.getStartDate());
        System.out.println("Savings account balance: " + accountDto.getBalance()); // Check balance

        this.repository.addAccount(accountDto);

        return "1";
    }

    private CreateAccountReadObject readFromKeyboardCreateAccount() {
        // Using Scanner for Getting Input from User
        Scanner s = new Scanner(System.in);
        CreateAccountReadObject createAccountReadObject = new CreateAccountReadObject();
        try{
            System.out.println("Enter the account number");
            Integer accountNumber = s.nextInt();
            System.out.println("The account number you entered is " + accountNumber);

            System.out.println("Enter the account holder name");
            String accountHolderName = s.next();
            System.out.println("The account holder name you entered is " + accountHolderName);

            System.out.println("Enter the birth date");
            String birthDate = s.next();
            System.out.println("The birth date you entered is " + birthDate);
            LocalDate birthDateFormatted = LocalDate.parse(birthDate, formatter);

            System.out.println("Enter the account start date");
            String startDate = s.next();
            System.out.println("The account start date you entered is " + startDate);
            LocalDate startDateFormatted = LocalDate.parse(startDate, formatter);

            createAccountReadObject.setAccountNumber(accountNumber);
            createAccountReadObject.setAccountHolderName(accountHolderName);
            createAccountReadObject.setBirtDate(birthDateFormatted);
            createAccountReadObject.setStartDate(startDateFormatted);
        } catch(Exception e) {
            createAccountReadObject = null;
        }
        return createAccountReadObject;
    }

    private String addMoneyOption() throws IOException {
       AddRemoveMoneyReadObject addMoneyReadObject;
        do {
            System.out.println("Enter the details for add money!!!");
            addMoneyReadObject = readFromKeyboardAddRemoveMoney();
        }
        while (addMoneyReadObject == null);

        AmountAccount result = this.repository.addAmount(addMoneyReadObject.getAccountNumber(), addMoneyReadObject.getAmount(), addMoneyReadObject.getOperationDate());
        if (result == null) {
            System.out.println(String.format("The account %s cannot be found", addMoneyReadObject.getAccountNumber()));
        } else {
            System.out.println(String.format("The balance of the account for %s is %f", result.getAccountHolderName(), result.getBalance()));
        }

        return "2";
    }

    private AddRemoveMoneyReadObject readFromKeyboardAddRemoveMoney() {
        // Using Scanner for Getting Input from User
        Scanner s = new Scanner(System.in);
        AddRemoveMoneyReadObject addMoneyReadObject = new AddRemoveMoneyReadObject();
        try{
            System.out.println("Enter the account number");
            Integer accountNumber = s.nextInt();
            System.out.println("The account number you entered is " + accountNumber);

            System.out.println("Enter the amount");
            double amount = s.nextDouble();
            System.out.println("The amount you entered is " + amount);

            System.out.println("Enter the the date of the operation");
            String operationDate = s.next();
            System.out.println("The operation date you entered is " + operationDate);
            LocalDate operationDateFormatted = LocalDate.parse(operationDate, formatter);

            addMoneyReadObject.setAccountNumber(accountNumber);
            addMoneyReadObject.setAmount(amount);
            addMoneyReadObject.setOperationDate(operationDateFormatted);
        } catch(Exception e) {
            addMoneyReadObject = null;
        }
        return addMoneyReadObject;
    }

    private String removeMoneyOption() throws IOException {
        AddRemoveMoneyReadObject removeMoneyReadObject;
        do {
            System.out.println("Enter the details for remove money!!!");
            removeMoneyReadObject = readFromKeyboardAddRemoveMoney();
        }
        while (removeMoneyReadObject == null);

        AmountAccount result = this.repository.removeAmount(removeMoneyReadObject.getAccountNumber(), removeMoneyReadObject.getAmount(), removeMoneyReadObject.getOperationDate());
        if (result == null) {
            System.out.println(String.format("The account %s cannot be found", removeMoneyReadObject.getAccountNumber()));
        } else {
            System.out.println(String.format("The balance of the account for %s is %f", result.getAccountHolderName(), result.getBalance()));
        }

        return "3";
    }

    private String filterAmountsByMonthsOption() throws IOException {
        FilterAmountsReadObject filterAmountsReadObject;
        do {
            System.out.println("Enter the details for filter amounts by months!!!");
            filterAmountsReadObject = readFromKeyboardFilterAmounts();
        }
        while (filterAmountsReadObject == null);

        Collection<OutputSummaryAmountDto> outputSummaryAmountDtos = this.repository.filterAmountsByMonths(null, filterAmountsReadObject.getStartDate(), filterAmountsReadObject.getEndDate());
        CsvFileWriter.writeCsvFile(true, "output-summary-by-months.csv", outputSummaryAmountDtos);

        return "4";
    }

    private FilterAmountsReadObject readFromKeyboardFilterAmounts() {
        // Using Scanner for Getting Input from User
        Scanner s = new Scanner(System.in);
        FilterAmountsReadObject filterAmountsReadObject = new FilterAmountsReadObject();
        try{
            System.out.println("Enter the start date for the filter amounts");
            String startDate = s.next();
            System.out.println("The start date for the filter amounts is " + startDate);
            LocalDate startDateFormatted = LocalDate.parse(startDate, formatter);

            System.out.println("Enter the end date for the filter amounts");
            String endDate = s.next();
            System.out.println("The end date for the filter amounts is " + endDate);
            LocalDate endDateFormatted = LocalDate.parse(endDate, formatter);

            filterAmountsReadObject.setAccountNumber(null);
            filterAmountsReadObject.setStartDate(startDateFormatted);
            filterAmountsReadObject.setEndDate(endDateFormatted);
        } catch(Exception e) {
            filterAmountsReadObject = null;
        }
        return filterAmountsReadObject;
    }

    private String filterAmountsByMonthsAndAccountOption() throws IOException {
        FilterAmountsReadObject filterAmountsReadObject;
        do {
            System.out.println("Enter the details for filter amounts by months and account!!!");
            filterAmountsReadObject = readFromKeyboardFilterAmountsByAccount();
        }
        while (filterAmountsReadObject == null);

        Collection<OutputSummaryAmountDto> outputSummaryAmountDtos = this.repository.filterAmountsByMonths(filterAmountsReadObject.getAccountNumber(), filterAmountsReadObject.getStartDate(), filterAmountsReadObject.getEndDate());
        CsvFileWriter.writeCsvFile(false, "output-summary-by-months-and-account.csv", outputSummaryAmountDtos);

        return "5";
    }

    private FilterAmountsReadObject readFromKeyboardFilterAmountsByAccount() {
        // Using Scanner for Getting Input from User
        Scanner s = new Scanner(System.in);
        FilterAmountsReadObject filterAmountsReadObject = new FilterAmountsReadObject();
        try{
            System.out.println("Enter the account number");
            Integer accountNumber = s.nextInt();
            System.out.println("The account number you entered is " + accountNumber);

            System.out.println("Enter the start date for the filter amounts");
            String startDate = s.next();
            System.out.println("The start date for the filter amounts is " + startDate);
            LocalDate startDateFormatted = LocalDate.parse(startDate, formatter);

            System.out.println("Enter the end date for the filter amounts");
            String endDate = s.next();
            System.out.println("The end date for the filter amounts is " + endDate);
            LocalDate endDateFormatted = LocalDate.parse(endDate, formatter);

            filterAmountsReadObject.setAccountNumber(accountNumber);
            filterAmountsReadObject.setStartDate(startDateFormatted);
            filterAmountsReadObject.setEndDate(endDateFormatted);
        } catch(Exception e) {
            filterAmountsReadObject = null;
        }
        return filterAmountsReadObject;
    }

    private String filterAmountsByWeeksOption() throws IOException {
        FilterAmountsReadObject filterAmountsReadObject;
        do {
            System.out.println("Enter the details for filter amounts by weeks!!!");
            filterAmountsReadObject = readFromKeyboardFilterAmounts();
        }
        while (filterAmountsReadObject == null);

        Collection<OutputSummaryAmountDto> outputSummaryAmountDtos = this.repository.filterAmountsByWeeks(null, filterAmountsReadObject.getStartDate(), filterAmountsReadObject.getEndDate());
        CsvFileWriter.writeCsvFile(true, "output-summary-by-weeks.csv", outputSummaryAmountDtos);

        return "6";
    }

    private String filterAmountsByWeeksAndAccountOption() throws IOException {
        FilterAmountsReadObject filterAmountsReadObject;
        do {
            System.out.println("Enter the details for filter amounts by weeks and account!!!");
            filterAmountsReadObject = readFromKeyboardFilterAmountsByAccount();
        }
        while (filterAmountsReadObject == null);

        Collection<OutputSummaryAmountDto> outputSummaryAmountDtos = this.repository.filterAmountsByWeeks(filterAmountsReadObject.getAccountNumber(), filterAmountsReadObject.getStartDate(), filterAmountsReadObject.getEndDate());
        CsvFileWriter.writeCsvFile(false, "output-summary-by-weeks-and-account.csv", outputSummaryAmountDtos);

        return "7";
    }

    private String addInterestRateOption() throws IOException, JsonProviderException {
        AddInterestRateReadObject interestRateReadObject;
        do {
            System.out.println("Enter the details for adding interest rate!!!");
            interestRateReadObject = readFromKeyboardAddInterestRate();
        }
        while (interestRateReadObject == null);

        InterestRateDto interestRateDto = new InterestRateDto(interestRateReadObject.getInterestRate(), interestRateReadObject.getActivationDate());

        this.repository.addInterestRate(interestRateDto);

        return "8";
    }

    private AddInterestRateReadObject readFromKeyboardAddInterestRate() {
        // Using Scanner for Getting Input from User
        Scanner s = new Scanner(System.in);
        AddInterestRateReadObject interestRateReadObject = new AddInterestRateReadObject();
        try{
            System.out.println("Enter the value for the interest rate");
            Double interestRate = s.nextDouble();
            System.out.println("The value for the interest rate you entered is " + interestRate);

            System.out.println("Enter the activation date for the interest rate");
            String activationDate = s.next();
            System.out.println("The activation date for the interest rate is " + activationDate);
            LocalDate activationDateFormatted = LocalDate.parse(activationDate, formatter);

            interestRateReadObject.setInterestRate(interestRate);
            interestRateReadObject.setActivationDate(activationDateFormatted);
        } catch(Exception e) {
            interestRateReadObject = null;
        }
        return interestRateReadObject;
    }

    private String getInterestRateByDateOption() throws IOException, JsonProviderException {
        GetInterestRateByDateReadObject interestRateByDateReadObject;
        do {
            System.out.println("Enter the details for getting interest rate!!!");
            interestRateByDateReadObject = readFromKeyboardGetInterestRateByDate();
        }
        while (interestRateByDateReadObject == null);

        double interestRate = this.repository.getInterestManagerProcessor().getInterestByDate(interestRateByDateReadObject.getProvidedDate());
        System.out.println("The interest rate for the provided date " + interestRateByDateReadObject.getProvidedDate() + " is: " + interestRate);

        return "9";
    }

    private GetInterestRateByDateReadObject readFromKeyboardGetInterestRateByDate() {
        // Using Scanner for Getting Input from User
        Scanner s = new Scanner(System.in);
        GetInterestRateByDateReadObject interestRateByDateReadObject = new GetInterestRateByDateReadObject();
        try{
            System.out.println("Enter the date for which you want to obtain the interest rate");
            String preparationDate = s.next();
            System.out.println("The date for which you want to obtain the interest rate is " + preparationDate);
            LocalDate preparationDateFormatted = LocalDate.parse(preparationDate, formatter);

            interestRateByDateReadObject.setProvidedDate(preparationDateFormatted);
        } catch(Exception e) {
            interestRateByDateReadObject = null;
        }
        return interestRateByDateReadObject;
    }

    private String getTotalInterestRateToDateOption() throws IOException, JsonProviderException {
        GetTotalInterestRateToDateReadObject getTotalInterestRateToDateReadObject;
        do {
            System.out.println("Enter the details for getting total interest rate for an account from creation till a date!!!");
            getTotalInterestRateToDateReadObject = readFromKeyboardGetTotalInterestRateToDate();
        }
        while (getTotalInterestRateToDateReadObject == null);

        AmountAccount amountAccount = this.repository.getBalanceByAccount(getTotalInterestRateToDateReadObject.getAccountNumber());
        LocalDate startDate = amountAccount.getStartDate();
        double balance = amountAccount.getBalance();
        LocalDate endDate = getTotalInterestRateToDateReadObject.getToDate();
        Integer accountNumber = getTotalInterestRateToDateReadObject.getAccountNumber();
        System.out.println(String.format("The creation date is %s", startDate.toString()));
        System.out.println(String.format("The balance is %s", balance));

        InterestManagerProcessor interestManagerProcessor = this.repository.getInterestManagerProcessor();
        AmountManagerProcessor amountManagerProcessor = this.repository.getAmountsProcessor();
        TotalInterestByDayDto totalInterest = AmountManagerProcessor.computeTotalInterestBetweenTwoDates(startDate, endDate, accountNumber, interestManagerProcessor, amountManagerProcessor);
        System.out.println(String.format("The total interest for account number %d beteween %s and %s is %f", totalInterest.getAccountNumber(), totalInterest.getStarDate().toString(), totalInterest.getEndDate().toString(), totalInterest.getTotalInterest()));

        return "10";
    }

    private GetTotalInterestRateToDateReadObject readFromKeyboardGetTotalInterestRateToDate() {
        // Using Scanner for Getting Input from User
        Scanner s = new Scanner(System.in);
        GetTotalInterestRateToDateReadObject getTotalInterestRateToDateReadObject = new GetTotalInterestRateToDateReadObject();
        try{
            System.out.println("Enter the account number");
            Integer accountNumber = s.nextInt();
            System.out.println("The account number you entered is " + accountNumber);

            System.out.println("Enter the to date");
            String toDate = s.next();
            System.out.println("The to date you entered is " + toDate);
            LocalDate toDateFormatted = LocalDate.parse(toDate, formatter);

            getTotalInterestRateToDateReadObject.setAccountNumber(accountNumber);
            getTotalInterestRateToDateReadObject.setToDate(toDateFormatted);
        } catch(Exception e) {
            getTotalInterestRateToDateReadObject = null;
        }
        return getTotalInterestRateToDateReadObject;
    }


    public static void main(String[] args) throws IOException {

        Main m = new Main();
        try {
            m.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
