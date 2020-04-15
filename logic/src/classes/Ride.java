package classes;

import enums.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Ride {

    private static int unique_id = 6000;
    private final int id;
    private final User rideOwner;
    private List<PartOfRide> parts;
    private List<Station> allStations;
    private Schedule schedule;

    public Ride(User rideOwner, List<PartOfRide> partsOfRide) {
        this.id = unique_id++;
        this.rideOwner=rideOwner;
        this.parts = partsOfRide;
        this.schedule = Schedule.SINGLE_TIME;

        initStationsList(partsOfRide);
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public int getID(){
        return this.id;
    }

    public User getRideOwner() {
        return rideOwner;
    }

    private void initStationsList(List<PartOfRide> partsOfRide){
        //using Set
//        this.allStations = new LinkedHashSet<>(partsOfRide.size());
//        partsOfRide.forEach((s) -> {
//            allStations.add(s.getRoad().getStartStation());
//            allStations.add(s.getRoad().getEndStation());
//        });

        //Using Array
        allStations = new ArrayList<>();
        allStations.add(partsOfRide.get(0).getRoad().getStartStation());
        allStations.addAll(partsOfRide.stream()
                .map(PartOfRide::getRoad)
                .map(Road::getEndStation)
                .collect(Collectors.toList())
        );
    }



    public List<PartOfRide> getParts() {return parts;
    }
}
