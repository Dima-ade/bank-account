package ro.adela.bank.repository;

import ro.adela.bank.processor.AmountManagerProcessor;
import ro.adela.bank.processor.InterestManagerProcessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JsonService extends AbstractService{
    public JsonService(File file) {
        super(file);
    }

    @Override
    protected void readAccounts() throws IOException {
        if (this.accountsJsonData == null) {
            if (file.exists()) {
                this.accountsJsonData = this.objectMapper.readValue(this.file, AccountsJsonData.class);
            } else {
                this.accountsJsonData = new AccountsJsonData();
                this.accountsJsonData.setAccounts(new ArrayList<>());
                this.accountsJsonData.setAmounts(new ArrayList<>());
                this.accountsJsonData.setInterests(new ArrayList<>());
            }
            this.interestManagerProcessor = new InterestManagerProcessor(this.accountsJsonData.getInterests());
            this.amountsProcessor = new AmountManagerProcessor(this.accountsJsonData.getAmounts());
        }
    }

    @Override
    protected void writeAccounts() throws IOException {
        objectMapper.writeValue(this.file, this.accountsJsonData);
    }
}
