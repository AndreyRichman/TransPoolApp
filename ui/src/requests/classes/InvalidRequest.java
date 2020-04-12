package requests.classes;

import requests.enums.RequestType;
import requests.interfaces.UserRequest;

public class InvalidRequest implements UserRequest {

    @Override
    public RequestType getRequestType() {

        return RequestType.INVALID;
    }
}
