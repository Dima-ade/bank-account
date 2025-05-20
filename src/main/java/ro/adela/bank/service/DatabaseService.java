package ro.adela.bank.service;

import jakarta.xml.bind.JAXBException;
import ro.adela.bank.repository.BankDataRepository;

import java.io.IOException;

public class DatabaseService extends AbstractFileService {

    BankDataRepository repository;

    public DatabaseService() throws JAXBException {
        super();

        repository = new BankDataRepository();
    }

    @Override
    protected void processBankData() throws IOException, JAXBException {
        //this.bankDataDto = this.repository.findAll();
    }

    @Override
    protected void writeAccounts() throws IOException, JAXBException {
    }
}
