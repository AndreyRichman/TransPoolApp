package transpool.ui.request.type;

import transpool.ui.request.enums.RequestType;
import transpool.ui.interfaces.UserRequest;

public class GetStatusOfRidesRequest implements UserRequest {

    @Override
    public RequestType getRequestType() {

        return RequestType.GET_STATUS_OF_RIDES;
    }
}
