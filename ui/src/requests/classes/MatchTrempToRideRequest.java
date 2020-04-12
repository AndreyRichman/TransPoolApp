package requests.classes;

import requests.enums.RequestType;
import requests.interfaces.UserRequest;

public class MatchTrempToRideRequest implements UserRequest {

    @Override
    public RequestType getRequestType() {

        return RequestType.MATCH_TREMP_TO_RIDE;
    }
}
