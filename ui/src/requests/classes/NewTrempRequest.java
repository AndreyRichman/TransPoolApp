package requests.classes;

import requests.enums.RequestType;
import requests.interfaces.UserRequest;

public class NewTrempRequest implements UserRequest {

    private String name;
    private String from;
    private String to;
    private String deptTime;

    public String name() {
        return name;
    }

    public void name(String name) {
        this.name = name;
    }

    public String from() {
        return from;
    }

    public void from(String from) {
        this.from = from;
    }

    public String to() {
        return to;
    }

    public void to(String to) {
        this.to = to;
    }

    public String deptTime() {
        return deptTime;
    }

    public void deptTime(String deptTime) {
        this.deptTime = deptTime;
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.NEW_TREMP;
    }
}
