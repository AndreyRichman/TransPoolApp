package transpool.logic.user;

import enums.TrempPartType;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TrempistsManager {


    private HashMap<Integer, LinkedList<Trempist>> allTrempists;
//    private List<Trempist> allTrempists;
    private HashMap<Integer, LinkedList<Trempist>> justJoinedTrempists;
//    private List<Trempist> justJoinedTrempists;
    private HashMap<Integer, LinkedList<Trempist>> leavingTrempists;
//    private List<Trempist> leavingTrempists;

    public TrempistsManager() {
//        allTrempists = new LinkedList<>();
        allTrempists = new HashMap<>();
//        justJoinedTrempists = new LinkedList<>();
        justJoinedTrempists = new HashMap<>();
//        leavingTrempists = new LinkedList<>();
        leavingTrempists = new HashMap<>();
    }

    public void addTrempist(Trempist trempistToAdd, int onDay){

//        allTrempists.add(trempistToAdd);
        addTrempistToMap(allTrempists, onDay, trempistToAdd);
//        allTrempists.get(onDay).add(trempistToAdd);


        if (trempistToAdd.fromPartType == TrempPartType.FIRST)
//            justJoinedTrempists.add(trempistToAdd);
            addTrempistToMap(justJoinedTrempists, onDay, trempistToAdd);

        if (trempistToAdd.toPartType == TrempPartType.LAST)
//            leavingTrempists.add(trempistToAdd);
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
