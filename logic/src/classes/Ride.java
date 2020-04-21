package classes;

import com.sun.xml.internal.ws.wsdl.writer.document.Part;
import enums.Schedule;

import java.util.*;
import java.util.stream.Collectors;

public class Ride {

    User rider;
    Map<Road, PartOfRide> path;
    Schedule schedule;

    public Ride()
    {
        rider = new User();
        path = new HashMap<Road, PartOfRide>();
    }

    public void addNewPath(PartOfRide newPath)
    {
        path.put((newPath.getRoad()),newPath);
    }

    public void addTrempist(Trempist trempist, Road specificRoad )
    {
        path.get(specificRoad).addTrempist(trempist);
    }

    public ArrayList<PartOfRide> getRidePath()
    {
        return null;
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
