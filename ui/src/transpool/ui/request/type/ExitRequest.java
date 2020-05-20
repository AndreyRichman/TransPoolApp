package transpool.ui.request.type;

import transpool.ui.request.enums.RequestType;
import transpool.ui.interfaces.UserRequest;

public class ExitRequest implements UserRequest {

    @Override
    public RequestType getRequestType() {

        return RequestType.EXIT;
    }
}
