package main.java.com.TransPool.ui.request.type;

import main.java.com.TransPool.ui.request.enums.RequestType;
import main.java.com.TransPool.ui.interfaces.UserRequest;

public class ExitRequest implements UserRequest {

    @Override
    public RequestType getRequestType() {

        return RequestType.EXIT;
    }
}
