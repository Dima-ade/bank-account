package ro.adela.bank.utils;

import dto.OutputSummaryAmountDto;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

/**
 * @author ashraf
 *
 */
public class CsvFileWriter {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADER_ACCOUNT = "start date,end date,in,out, account number";
    private static final String FILE_HEADER_ACCOUNTS = "start date,end date,in,out";

    public static void writeCsvFile(boolean accounts, String fileName, Collection<OutputSummaryAmountDto> outputSummaryAmountDtos) {
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException(String.format("The %s is blank", fileName));
        }
        if (outputSummaryAmountDtos == null) {
            throw new IllegalArgumentException("The outputSummaryAmountDtos argument is null");
        }
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            if(accounts) {
                fileWriter.append(FILE_HEADER_ACCOUNTS.toString());
            } else {
                fileWriter.append(FILE_HEADER_ACCOUNT.toString());
            }

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            //Write a new output object list to the CSV file
            for (OutputSummaryAmountDto output : outputSummaryAmountDtos) {
                fileWriter.append(output.getStartDate().toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(output.getEndDate().toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(output.getIn().toString());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(output.getOut().toString());
                if (!accounts) {
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(output.getAccountNumber().toString());
                }
                fileWriter.append(NEW_LINE_SEPARATOR);
            }

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }
}