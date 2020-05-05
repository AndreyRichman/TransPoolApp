package main.java.com.TransPool.logic.traffic.item;

import main.java.com.TransPool.logic.map.structure.Road;
import main.java.com.TransPool.logic.map.structure.Station;
import main.java.com.TransPool.logic.time.Schedule;
import main.java.com.TransPool.logic.user.User;

import java.time.LocalTime;

import java.util.*;

public class Ride {



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

    public boolean isTrempsAssignToRide() {
        for(PartOfRide pride: this.getPartOfRide())
            if(!pride.getTrempistsManager().getAllTrempists().isEmpty())
                return true;
        return false;
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
                from = partRoad.getRoad().getEndStation();
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
