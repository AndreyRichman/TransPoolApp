package classes;

import enums.Schedule;
import enums.TrempPartType;

import java.util.*;
import java.util.stream.Collectors;

public class Ride {

    public class SubRide {

        private Station start;
        private Station end;
        List<PartOfRide> selectedPartsOfRide;

        public SubRide(Station start, Station end) {
            this.start = start;
            this.end = end;
            this.selectedPartsOfRide = getRelevantPartsOfRide(start, end);
        }

        private List<PartOfRide> getRelevantPartsOfRide(Station start, Station end){
            int indexOfStart = allStations.indexOf(start);
            int indexOfEnd = allStations.indexOf(end);

            List<Station> subStations = allStations.subList(indexOfStart, indexOfEnd);  //excluding last one

            return subStations.stream().map((s) -> mapOfStationsReachedByPartOfRoad.get(s)).collect(Collectors.toList());
        }

        private void applyTrempistToAllPartsOfRide(User user) {
            selectedPartsOfRide.forEach( partOfRide -> {
                TrempPartType partType = partOfRide == selectedPartsOfRide.get(0) ?
                        TrempPartType.FIRST :
                        partOfRide == selectedPartsOfRide.get(selectedPartsOfRide.size() - 1) ?
                                TrempPartType.LAST : TrempPartType.MIDDLE;

                partOfRide.addTrempist(new Trempist(user, partType));
            });
        }
    }

    private static int unique_id = 6000;
    private final int id;
    private final User rideOwner;
    private List<PartOfRide> partOfRides;
    private List<Station> allStations;
    private LinkedHashMap<Station, PartOfRide> mapOfStationsReachedByPartOfRoad;
    private Schedule schedule;
    private int carCapacity;

    private Ride(User rideOwner, List<Road> allRoads, int carCapacity) {
        this.id = unique_id++;
        this.rideOwner = rideOwner;
        this.carCapacity = carCapacity;
        this.schedule = Schedule.SINGLE_TIME;

        initDataStructures(allRoads, carCapacity);
    }

    private void initDataStructures(List<Road> roads, int carCapacity){
        this.allStations = new ArrayList<>();
        this.partOfRides = new ArrayList<>();
        this.mapOfStationsReachedByPartOfRoad = new LinkedHashMap<>();

        this.allStations.add(roads.get(0).getStartStation());
        roads.forEach( road -> {
            this.allStations.add(road.getEndStation());
            PartOfRide partOfRide = new PartOfRide(road, carCapacity);
            this.partOfRides.add(partOfRide);
            this.mapOfStationsReachedByPartOfRoad.put(road.getEndStation(), partOfRide);
        });

    }

    public Ride createRideFromRoads(User rideOwner, List<Road> roads, int capacity){

        return new Ride(rideOwner, roads, capacity);
    }

    public void assignTrempRequestToCurrentRide(TrempRequest trempRequest, Station joinFromStation, Station joinUntilStation) {
        SubRide subRide = new SubRide(joinFromStation, joinUntilStation);
        subRide.applyTrempistToAllPartsOfRide(trempRequest.getUser());
        trempRequest.addSubRide(subRide);
    }

    public boolean containsRoute(Station from, Station to){
       List<Station> reachableStation = this.allStations.stream()
               .filter(this::canReachStationByCapacity)
               .collect(Collectors.toList());

       return reachableStation.contains(from)
                && this.allStations.contains(to)
                && this.allStations.indexOf(to) >= this.allStations.indexOf(from);
    }

    private boolean canReachStationByCapacity(Station station){
        return this.mapOfStationsReachedByPartOfRoad.get(station).canAddTrempist();
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

    public List<PartOfRide> getPartOfRides() {return partOfRides;}
}
