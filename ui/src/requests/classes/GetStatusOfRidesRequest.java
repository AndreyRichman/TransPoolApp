package requests.classes;

import requests.enums.RequestType;
import requests.interfaces.UserRequest;

public class GetStatusOfRidesRequest implements UserRequest {

    @Override
    public RequestType getRequestType() {

        return RequestType.GET_STATUS_OF_RIDES;
    }
}
