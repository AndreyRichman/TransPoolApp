package classes;

import exception.InvalidFileTypeException;
import exception.NoFileFoundInPathException;
import jaxb.schema.generated.TransPool;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class XMLHandler {
    String path;
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.schema.generated";

    public XMLHandler(String XMLpath) {
        path = XMLpath;
    }

    public TransPool LoadXML() throws JAXBException, InvalidFileTypeException, NoFileFoundInPathException {

        File file = new File((path));

        if(!file.exists())
            throw new NoFileFoundInPathException();

        if (!getFileType(path).equalsIgnoreCase(".xml"))
            throw new InvalidFileTypeException(getFileType(path));



        return deserializeFrom(file);
    }


    private static TransPool deserializeFrom(File file) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (TransPool) u.unmarshal(file);
    }

    public String getFileType(String path) {
        String type;
        // Basic check on the type using the filename
        if (path.lastIndexOf('.') != -1) {
            type = path.substring(path.lastIndexOf('.'));
        } else {
            type = null;
        }
        return type;
    }
}