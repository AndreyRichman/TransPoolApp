package requests.classes;

import requests.enums.RequestType;
import requests.interfaces.UserRequest;

public class LoadXMLRequest implements UserRequest {

    private String fileDirectory = "...";

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

}
