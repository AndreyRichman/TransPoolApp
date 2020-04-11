package requests.classes;

import requests.enums.RequestType;
import requests.interfaces.UserRequest;

public class LoadXMLRequest implements UserRequest {

    public String fileDirectory = "C://Documents....";

    @Override
    public RequestType getRequestType() {

        return RequestType.LOAD_XML_FILE;
    }

    public LoadXMLRequest(String directoryToFile){

        fileDirectory = directoryToFile;
    }

    public String getFileDirectory()
    {

        return fileDirectory;
    }
}
