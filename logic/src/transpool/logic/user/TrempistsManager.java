package transpool.logic.user;

import enums.TrempPartType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TrempistsManager {


    private HashMap<Integer, LinkedList<Trempist>> allTrempists;
    private HashMap<Integer, LinkedList<Trempist>> justJoinedTrempists;
    private HashMap<Integer, LinkedList<Trempist>> leavingTrempists;

    public TrempistsManager() {
        allTrempists = new HashMap<>();
        justJoinedTrempists = new HashMap<>();
        leavingTrempists = new HashMap<>();
    }

    public void addTrempist(Trempist trempistToAdd, int onDay){

        addTrempistToMap(allTrempists, onDay, trempistToAdd);

        if (trempistToAdd.fromPartType == TrempPartType.FIRST)
            addTrempistToMap(justJoinedTrempists, onDay, trempistToAdd);

        if (trempistToAdd.toPartType == TrempPartType.LAST)
            addTrempistToMap(leavingTrempists, onDay, trempistToAdd);
    }

    private void addTrempistToMap(HashMap<Integer, LinkedList<Trempist>> map, int key, Trempist trempistToAdd){
        if (!map.containsKey(key))
            map.put(key, new LinkedList<>());

        map.get(key).add(trempistToAdd);
    }

    public List<Trempist> getAllTrempists(int onDay) {
        if (!allTrempists.containsKey(onDay))
            allTrempists.put(onDay, new LinkedList<>());

        return allTrempists.get(onDay);
    }

    public List<Trempist> getJustJoinedTrempists(int onDay) {
        if (!justJoinedTrempists.containsKey(onDay))
            justJoinedTrempists.put(onDay, new LinkedList<>());

        return justJoinedTrempists.get(onDay);
    }

    public List<Trempist> getLeavingTrempists(int onDay) {
        if (!leavingTrempists.containsKey(onDay))
            leavingTrempists.put(onDay, new LinkedList<>());

        return leavingTrempists.get(onDay);
    }
}
