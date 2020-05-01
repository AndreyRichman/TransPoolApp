package classes;

import enums.TrempPartType;

import java.time.LocalTime;

import java.util.*;
import java.util.stream.Collectors;

public class Ride {

    public class SubRide {

        private final Ride originalRide;
        private final Station startStation;
        private final Station endStation;
        List<PartOfRide> selectedPartsOfRide;

        public SubRide(Ride originalRide, Station startStation, Station endStation) {
            this.originalRide = originalRide;
            this.startStation = startStation;
            this.endStation = endStation;
            this.selectedPartsOfRide = getRelevantPartsOfRide(startStation, endStation);
        }

        public Ride getOriginalRide() {
            return originalRide;
        }

        private List<PartOfRide> getRelevantPartsOfRide(Station start, Station end){
            int indexOfPartsFrom = allStations.indexOf(start);
            int indexOfPartsTo = allStations.indexOf(end);

            return partsOfRide.subList(indexOfPartsFrom, indexOfPartsTo);
        }

        public void applyTrempistToAllPartsOfRide(User user) {
            selectedPartsOfRide.forEach( partOfRide -> {
                TrempPartType fromStation = TrempPartType.MIDDLE, toStation = TrempPartType.MIDDLE;

                if (partOfRide == selectedPartsOfRide.get(0))
                    fromStation = TrempPartType.FIRST;

                if (partOfRide == selectedPartsOfRide.get(selectedPartsOfRide.size() - 1))
                    toStation = TrempPartType.LAST;

                partOfRide.addTrempist(new Trempist(user, fromStation, toStation));
            });
        }

        public Station getStartStation() {
            return startStation;
        }

        public Station getEndStation() {
            return endStation;
        }

        public double getTotalCost(){
            return this.selectedPartsOfRide.stream()
                    .mapToDouble(PartOfRide::getLengthOfRoad)
                    .map( length -> length * originalRide.getPricePerKilometer())
                    .sum();
        }

        public List<Station> getAllStations(){
            LinkedHashSet<Station> allStations = new LinkedHashSet<>();
            this.selectedPartsOfRide.forEach(partOfRide -> {
                allStations.add(partOfRide.getRoad().getStartStation());
                allStations.add(partOfRide.getRoad().getEndStation());
            });

            return new ArrayList<>(allStations);
        }

        public LocalTime getArrivalTime(){
            return this.selectedPartsOfRide.get(this.selectedPartsOfRide.size() - 1).getEndTime();
        }

        public double getAverageFuelUsage(){
            OptionalDouble average = this.selectedPartsOfRide.stream().mapToDouble(PartOfRide::getFuelUsage).average();
            return average.isPresent() ? average.getAsDouble(): 0;
        }
    }

    private static int unique_id = 6000;
    private final int id;
    private final User rideOwner;
    private int pricePerKilometer = 0;
    private LocalTime startTime = LocalTime.MIN;

    private List<PartOfRide> partsOfRide;
    private List<Station> allStations;
    private LinkedHashMap<Station, PartOfRide> mapFromStationToRoad;
    private Schedule schedule;
    private int carCapacity;

    private Ride(User rideOwner, List<Road> allRoads, int carCapacity) {
        this.id = unique_id++;
        this.rideOwner = rideOwner;
        this.carCapacity = carCapacity;
        initDataStructures(allRoads, carCapacity);
    }

    private void initDataStructures(List<Road> roads, int carCapacity){
        this.allStations = new ArrayList<>();
        this.partsOfRide = new ArrayList<>();
        this.mapFromStationToRoad = new LinkedHashMap<>();

        this.allStations.add(roads.get(0).getStartStation());
        roads.forEach( road -> {
            this.allStations.add(road.getEndStation());
            PartOfRide partOfRide = new PartOfRide(road, carCapacity);
            this.partsOfRide.add(partOfRide);
            this.mapFromStationToRoad.put(road.getStartStation(), partOfRide);
        });

    }

    public List<PartOfRide> getPartOfRide()
    {
        return this.partsOfRide;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        updateTimesOfAllPartsOfRide(startTime);
    }

    private void updateTimesOfAllPartsOfRide(LocalTime start){

        for (PartOfRide part : this.partsOfRide){
            part.setStartTime(start);
            start = part.getEndTime();
        }
    }

    public static Ride createRideFromRoads(User rideOwner, List<Road> roads, int capacity){

        return new Ride(rideOwner, roads, capacity);
    }

    public double getTotalTimeOfRide(){
        return this.partsOfRide
                .stream()
                .mapToDouble(PartOfRide::getPeriodInMinutes)
                .sum();
    }

    public double getTotalCostOfRide(){
        return this.partsOfRide
                .stream()
                .mapToDouble(PartOfRide::getLengthOfRoad)
                .map((length) -> length * this.pricePerKilometer)
                .sum();
    }

    public double getAverageFuelUsage(){
        return this.partsOfRide.size() == 0 ? 0 : this.partsOfRide
                .stream()
                .mapToDouble(PartOfRide::getFuelUsage)
                .average()
                .getAsDouble();

    }

    public SubRide getSubRide(Station fromStation, Station toStation){
        return new SubRide(this, fromStation, toStation);
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
        List<Station> subRoute = new ArrayList<>();

//        for(Map.Entry<Station, PartOfRide> station2Part: this.mapFromStationToRoad.entrySet()){
//            subRoute.add(station2Part.getKey());
//
//            if (station2Part.getValue().canAddTrempist()
//                    && station2Part.getValue() == partOfRides.get(partOfRides.size() - 1)){
//                subRoute.add(station2Part.getValue().getRoad().getEndStation());
//                routesWithFreeSpace.add(subRoute);
//            }
//            else{
//                if(subRoute.size() > 1)
//                    routesWithFreeSpace.add(subRoute);
//                subRoute = new ArrayList<>();
//            }
//
//        }

        for(PartOfRide part: partsOfRide){
            subRoute.add(part.getRoad().getStartStation());
            if (part.canAddTrempist() && part == partsOfRide.get(partsOfRide.size() - 1)) {
                subRoute.add(part.getRoad().getEndStation());
                routesWithFreeSpace.add(subRoute);
            }
            else{
                if (subRoute.size() > 1)
                    routesWithFreeSpace.add(subRoute);
                subRoute = new ArrayList<>();
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

    public void setSchedule(int hour, Integer day, String rec) {
        this.schedule = new Schedule(hour, day, rec);
        this.setStartTime(LocalTime.MIN.plusHours(hour));
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

    public List<PartOfRide> getPartsOfRide() {return partsOfRide;}

    public List<Station> getAllStations(){
        return this.allStations;
    }

    public int getPricePerKilometer() {
        return pricePerKilometer;
    }
}
