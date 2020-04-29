package requests.classes;

import requests.enums.RequestType;
import requests.interfaces.UserRequest;

public class TryMatchTrempToRideRequest implements UserRequest {

    int trempRequestID;
    int rideID;

    @Override
    public RequestType getRequestType() {

        return RequestType.MATCH_TREMP_TO_RIDE;
    }

    public void setRideID(int rideID) {
        this.rideID = rideID;
    }

    public void setTrempRequestID(int trempRequestID) {
        this.trempRequestID = trempRequestID;
    }

    public int getRideID() {
        return rideID;
    }

    public int getTrempRequestID() {
        return trempRequestID;
    }
}
