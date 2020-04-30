package classes;

import enums.TrempPartType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TrempistsManager {

    private List<Trempist> allTrempists;
    private List<Trempist> justJoinedTrempists;
    private List<Trempist> leavingTrempists;

    public TrempistsManager() {
        allTrempists = new LinkedList<>();
        justJoinedTrempists = new LinkedList<>();
        leavingTrempists = new LinkedList<>();
    }

    public void addTrempist(Trempist trempistToAdd){
        allTrempists.add(trempistToAdd);

        if (trempistToAdd.fromPartType == TrempPartType.FIRST)
            justJoinedTrempists.add(trempistToAdd);

        if (trempistToAdd.toPartType == TrempPartType.LAST)
            leavingTrempists.add(trempistToAdd);
    }

    public List<Trempist> getAllTrempists() {
        return allTrempists;
    }

    public List<Trempist> getJustJoinedTrempists() {
        return justJoinedTrempists;
    }

    public List<Trempist> getLeavingTrempists() {
        return leavingTrempists;
    }
}
