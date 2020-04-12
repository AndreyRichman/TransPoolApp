package requests.classes;

import requests.enums.RequestType;
import requests.interfaces.UserRequest;

public class NewRideRequest implements UserRequest {

    //TODO: add all relevant fields for new RIDE
    @Override
    public RequestType getRequestType() {

        return RequestType.NEW_RIDE;
    }
}
