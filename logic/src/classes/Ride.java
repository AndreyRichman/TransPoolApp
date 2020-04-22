package classes;

import enums.Schedule;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class Ride {

    User rider;
    Map<Station, ArrayList<Station>> LinkedStation;
    LinkedList<PartOfRide> path;
    Schedule schedule;

    public Ride()
    {
        rider = new User();
        path  = new LinkedList<PartOfRide>();
        LinkedStation = new HashMap<Station, ArrayList<Station>>();
    }

    public void addNewPath(PartOfRide newPath)
    {
        LinkedStation.putIfAbsent(newPath.getRoad().getStartStation(), new ArrayList<Station>());
        LinkedStation.get(newPath.getRoad().getStartStation()).add(newPath.getRoad().getEndStation());
        path.add(newPath);
    }

    public void addTrempist(Trempist trempist, Road specificRoad , SubRide subRide )
    {
        for(PartOfRide partRide : path)
            if(partRide.getRoad().equals(specificRoad))
            {
                partRide.addTrempist(trempist);
                subRide.addNewPartialRide(partRide,rider);
            }
    }

    AtomicBoolean checkSubPath(Station Start, Station End)
    {
        AtomicBoolean flag = new AtomicBoolean(false);
        LinkedStation.forEach((k,v) -> {
            if(k.equals(Start))
                if(v.contains(End))
                   flag.set(true);
        });
        return flag;
    }

    public void updateLinkedStaions()
    {
        for(PartOfRide currPath : path)
            for(Map.Entry<Station, ArrayList<Station>> currStation : LinkedStation.entrySet())
                if(currStation.getValue().equals(currPath.getRoad().getStartStation()))
                    currStation.getValue().add((currPath.getRoad().getEndStation()));
    }

    public LinkedList<PartOfRide> getRidePath()
    {
        return path;
    }

    public int rideCost()
    {
        return 0;
    }

    public int getTimeArriveByStation(Station station)
    {
        return 0;
    }

    public int getTimeDepartureByStation(Station station)
    {
        return 0;
    }















}
