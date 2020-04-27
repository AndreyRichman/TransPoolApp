package classes;

import jaxb.schema.generated.TransPool;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class XMLHandler {
    String path;
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.generated";

    public XMLHandler(String XMLpath){
        path = XMLpath;
    }

    public TransPool LoadXML() {

        File file = new File((path));
        TransPool transPool = null;

        try {
            transPool = deserializeFrom(file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return transPool;
    }


    private static TransPool deserializeFrom(File file) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (TransPool) u.unmarshal(file);
    }


}
