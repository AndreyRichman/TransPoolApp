package main.java.com.TransPool.ui.request.type;

import main.java.com.TransPool.ui.request.enums.RequestType;
import main.java.com.TransPool.ui.interfaces.UserRequest;

public class GetStatusOfRidesRequest implements UserRequest {

    @Override
    public RequestType getRequestType() {

        return RequestType.GET_STATUS_OF_RIDES;
    }
}
