package requests.classes;

import requests.enums.RequestType;
import requests.interfaces.UserRequest;

public class GetPossibleRidesForTrempRequest implements UserRequest {
    int trempRequestId;
    int maxNumberOfConnections = 0;

    @Override
    public RequestType getRequestType() {
        return RequestType.GET_POSSIBLE_RIDES_FOR_TREMP_REQUEST;
    }

    public void setTrempRequestId(int trempRequestId) {
        this.trempRequestId = trempRequestId;
    }

    public void setMaxNumberOfConnections(int maxNumberOfConnections) {
        this.maxNumberOfConnections = maxNumberOfConnections;
    }

    public int getTrempRequestId() {
        return trempRequestId;
    }

    public int getMaxNumberOfConnections() {
        return maxNumberOfConnections;
    }
}
