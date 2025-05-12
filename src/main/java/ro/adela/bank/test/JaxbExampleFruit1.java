package ro.adela.bank.test;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import java.io.File;

public class JaxbExampleFruit1 {

    public static void main(String[] args) {

        JAXBContext jaxbContext = null;
        try {

            // Normal JAXB RI
            jaxbContext = JAXBContext.newInstance(Fruit.class);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Fruit o = new Fruit();
            o.setId(1);
            o.setName("Banana");
            o.setPrice("9.99");

            // output to a xml file
            jaxbMarshaller.marshal(o, new File("fruit.xml"));

            // output to console
            // jaxbMarshaller.marshal(o, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }

}