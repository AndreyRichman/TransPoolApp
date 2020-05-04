package classes;

import enums.TrempPartType;

public class Trempist {

    User user;
    TrempPartType fromPartType;
    TrempPartType toPartType;

    public Trempist(User user, TrempPartType fromPartType, TrempPartType toPartType) {
        this.user = user;
        this.fromPartType = fromPartType;
        this.toPartType = toPartType;
    }

    public TrempPartType getFromPartType() {
        return fromPartType;
    }

    public TrempPartType getToPartType() {
        return toPartType;
    }

    public User getUser() {
        return user;
    }
}
