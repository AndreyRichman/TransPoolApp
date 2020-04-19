package classes;

import enums.Schedule;

import java.util.*;
import java.util.stream.Collectors;

public class Ride {

    public class SubRide {

//        private Ride originalRide;
        private Station start;
        private Station end;
        List<PartOfRide> relevantPartsOfRide;

        public SubRide(Station start, Station end) {
//            this.originalRide = originalRide;
            this.start = start;
            this.end = end;
            addRelevantPartsOfRide(start, end);
        }

        private void addRelevantPartsOfRide(Station start, Station end){
            //TODO: change to new implementation
            int indexOfStart = allStations.indexOf(start);
            int indexOfEnd = allStations.indexOf(end);

            List<Station> subStations = allStations.subList(indexOfStart, indexOfEnd);  //excluding last one
            relevantPartsOfRide = subStations.stream().map((s) -> station2Part.get(s)).collect(Collectors.toList());

//            relevantPartsOfRide = parts.subList(indexOfStart, indexOfEnd);
//            PartOfRide current =  parts.stream()
//                    .filter((p) -> p.getRoad().getStartStation() == start)
//                    .findFirst()
//                    .get();
//            relevantPartsOfRide.add(current);
//
//            parts.stream().

        }
    }

    private static int unique_id = 6000;
    private final int id;
    private final User rideOwner;
//    private List<PartOfRide> parts;
//    private List<Station> allStations;
    private HashMap<Station, PartOfRide> station2Part;
    private Schedule schedule;
    private int carCapacity;

    private Ride(User rideOwner, HashMap<Station, PartOfRide> station2Part, int carCapacity) {
        this.id = unique_id++;
        this.rideOwner=rideOwner;
        this.carCapacity = carCapacity;
//        this.parts = partsOfRide;
        this.schedule = Schedule.SINGLE_TIME;
        this.station2Part = station2Part;

//        initStationsList(partsOfRide);
    }

    public Ride createRideFromRoads(User rideOwner, List<Road> roads, int capacity){
        HashMap<Station, PartOfRide> station2Part = new LinkedHashMap<>();
        roads.forEach((road -> station2Part.put(road.getStartStation(), new PartOfRide(road, capacity))));
//        List<PartOfRide> partsOfRide = roads.stream().map((r) -> new PartOfRide(r, capacity)).collect(Collectors.toList());

        return new Ride(rideOwner, station2Part, capacity);
    }
//    public Ride createRideFromRoads(User rideOwner, List<Road> roads, int capacity){
//        List<PartOfRide> partsOfRide = roads.stream().map((r) -> new PartOfRide(r, capacity)).collect(Collectors.toList());
//
//        return new Ride(rideOwner, partsOfRide);
//    }


//    private void initStationsList(List<PartOfRide> partsOfRide){
//        allStations = new ArrayList<>();
//        allStations.add(partsOfRide.get(0).getRoad().getStartStation());
//        allStations.addAll(partsOfRide.stream()
//                .map(PartOfRide::getRoad)
//                .map(Road::getEndStation)
//                .collect(Collectors.toList())
//        );
//
//        ////
//        station2Part = new LinkedHashMap<>();
//        partsOfRide.forEach((p) -> station2Part.put(p.getRoad().getStartStation(), p));
//    }

    public void addTremp(Tremp tremp, Station startStation, Station endStation){
        SubRide subRide = new SubRide(startStation, endStation);
      
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

    public List<PartOfRide> getParts() {return parts;}
}
