package transpool.ui.request.type;

import transpool.ui.request.enums.RequestType;
import transpool.ui.interfaces.UserRequest;

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

}
