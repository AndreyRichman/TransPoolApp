package requests.classes;

import requests.enums.RequestType;
import requests.interfaces.UserRequest;

public class LoadXMLRequest implements UserRequest {

    private String fileDirectory = "C://Documents.....";

    @Override
    public RequestType getRequestType() {

        return RequestType.LOAD_XML_FILE;
    }

    public void setFileDirectory(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    public String getFileDirectory()
    {
        return fileDirectory;
    }

    public LoadXMLRequest()
    {
        System.out.println("im in XML constractor");
    }
}
