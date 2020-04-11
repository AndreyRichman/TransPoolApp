package requests.classes;

import requests.enums.RequestType;
import requests.interfaces.UserRequest;

public class ExitRequest implements UserRequest {

    @Override
    public RequestType getRequestType() {

        return RequestType.EXIT;
    }
}
