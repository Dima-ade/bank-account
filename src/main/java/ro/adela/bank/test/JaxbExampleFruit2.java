package ro.adela.bank.test;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class JaxbExampleFruit2 {

    public static void main(String[] args) {

        JAXBContext jaxbContext = null;
        try {

            // Normal JAXB RI
            jaxbContext = JAXBContext.newInstance(Fruit.class);

            File file = new File("fruit.xml");

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Fruit o = (Fruit) jaxbUnmarshaller.unmarshal(file);

            System.out.println(o);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}
