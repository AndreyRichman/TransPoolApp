package classes;

import enums.Schedule;
import java.util.*;


public class Ride {

    User rider;
    LinkedList<PartOfRide> path;
    Schedule schedule;

    public Ride()
    {
        rider = new User();
        path  = new LinkedList<PartOfRide>();
    }

    public void addNewPath(PartOfRide newPath)
    {
        path.add(newPath);
    }

    public void addTrempist(Trempist trempist, Road specificRoad, SubRide subRide )
    {
        for(PartOfRide partRide : path)
            if(partRide.getRoad().equals(specificRoad))
            {
                partRide.addTrempist(trempist);
                subRide.addNewparRide(partRide,rider);
            }
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
