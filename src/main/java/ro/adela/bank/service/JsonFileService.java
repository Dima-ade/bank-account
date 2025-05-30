package ro.adela.bank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.xml.bind.JAXBException;
import ro.adela.bank.dto.BankDataDto;
import ro.adela.bank.dto.InterestRateDto;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonFileService extends AbstractFileService {

    private final ObjectMapper objectMapper;

    public JsonFileService(File file) {
        super(file);
        this.objectMapper = new ObjectMapper();
        // support Java 8 date time apis
        this.objectMapper.registerModule(new JavaTimeModule());
        //configure Object mapper for pretty print
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    }

    @Override
    protected void processBankData() throws IOException, JAXBException {
        this.bankDataDto = this.objectMapper.readValue(this.file, BankDataDto.class);
    }

    @Override
    protected void writeAccounts() throws IOException {
        objectMapper.writeValue(this.file, this.bankDataDto);
    }

    @Override
    public List<InterestRateDto> getInterestByPage(Integer pageNumber, Integer pageSize) {
        return null;
    }
}
