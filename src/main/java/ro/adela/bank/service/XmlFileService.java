package ro.adela.bank.service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import ro.adela.bank.dto.BankDataDto;

import java.io.File;
import java.io.IOException;

public class XmlFileService extends AbstractFileService {

    private JAXBContext jaxbContext;

    public XmlFileService(File file) throws JAXBException {
        super(file);

        this.jaxbContext = JAXBContext.newInstance(BankDataDto.class);
    }

    @Override
    protected void processBankData() throws IOException, JAXBException {
        Unmarshaller jaxbUnmarshaller = this.jaxbContext.createUnmarshaller();

        this.bankDataDto = (BankDataDto) jaxbUnmarshaller.unmarshal(file);
    }

    @Override
    protected void writeAccounts() throws IOException, JAXBException {
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // output pretty printed
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(this.bankDataDto, this.file);
    }
}
