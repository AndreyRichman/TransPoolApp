package requests.classes;

import requests.enums.RequestType;
import requests.interfaces.UserRequest;

public class ShowUnMatchedTrempReqRequest implements UserRequest {
    @Override
    public RequestType getRequestType() {
        return RequestType.SHOW_UN_MATCHED_TREMP_REQUESTS;
    }
}
