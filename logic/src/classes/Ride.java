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
            int indexOfPartsFrom = allStations.indexOf(start);
            int indexOfPartsTo = allStations.indexOf(end);

            return partOfRides.subList(indexOfPartsFrom, indexOfPartsTo);
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
    private int pricePerKilometer = 0;

    private List<PartOfRide> partOfRides;
    private List<Station> allStations;
    private LinkedHashMap<Station, PartOfRide> mapFromStationToRoad;
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
        this.mapFromStationToRoad = new LinkedHashMap<>();

        this.allStations.add(roads.get(0).getStartStation());
        roads.forEach( road -> {
            this.allStations.add(road.getEndStation());
            PartOfRide partOfRide = new PartOfRide(road, carCapacity);
            this.partOfRides.add(partOfRide);
            this.mapFromStationToRoad.put(road.getStartStation(), partOfRide);
        });

    }

    public Ride createRideFromRoads(User rideOwner, List<Road> roads, int capacity){

        return new Ride(rideOwner, roads, capacity);
    }

    public double getTotalTimeOfRide(){
        return this.partOfRides
                .stream()
                .mapToDouble(PartOfRide::getPeriodInMinutes)
                .sum();
    }

    public double getTotalCostOfRide(){
        return this.partOfRides
                .stream()
                .mapToDouble(PartOfRide::getLengthOfRoad)
                .map((length) -> length * this.pricePerKilometer)
                .sum();
    }

    public double getAverageFuelUsage(){
        return this.partOfRides.size() == 0 ? 0 : this.partOfRides
                .stream()
                .mapToDouble(PartOfRide::getFuelUsage)
                .average()
                .getAsDouble();

    }

    public void assignTrempRequestToCurrentRide(TrempRequest trempRequest, Station joinFromStation, Station joinUntilStation) {
        SubRide subRide = new SubRide(joinFromStation, joinUntilStation);
        subRide.applyTrempistToAllPartsOfRide(trempRequest.getUser());
        trempRequest.addSubRide(subRide);
    }

    public boolean containsValidRoute(Station from, Station to){    //valid = have empty space in all parts
        boolean containsRoute = false;

        if (rideContainsStations(from, to)){
            boolean hasSpaceInRoad = true;

            while(from != to && hasSpaceInRoad){
                PartOfRide partRoad = mapFromStationToRoad.get(from);
                hasSpaceInRoad = partRoad.canAddTrempist();
                to = partRoad.getRoad().getEndStation();
            }
            if (from == to){
                containsRoute = true;
            }
        }

        return containsRoute;
    }

    public List<List<Station>> getListsOfAllStationsStillRelevantForTremps() {
        List<List<Station>> routesWithFreeSpace = new ArrayList<>();
        List<Station> route = new ArrayList<>();

        for(Map.Entry<Station, PartOfRide> station2Part: this.mapFromStationToRoad.entrySet()){
            Station station = station2Part.getKey();
            PartOfRide partOfRide = station2Part.getValue();

            if (partOfRide.canAddTrempist()){
                route.add(station);
            }
            else{
                route.add(station);
                routesWithFreeSpace.add(route);
                route = new ArrayList<>();
            }
        }

        return routesWithFreeSpace;
    }

    private boolean rideContainsStations(Station a, Station b){
        return allStations.contains(a)
                && allStations.contains(b)
                && allStations.indexOf(a) < allStations.indexOf(b);
    }

    private boolean hasFreeSpaceFromStation(Station station){
        return this.mapFromStationToRoad.get(station).canAddTrempist();
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setPricePerKilometer(int pricePerKilometer) {
        this.pricePerKilometer = pricePerKilometer;
    }

    public int getID(){
        return this.id;
    }

    public User getRideOwner() {
        return rideOwner;
    }

    public List<PartOfRide> getPartOfRides() {return partOfRides;}
}
