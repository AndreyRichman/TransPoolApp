package transpool.logic.traffic;

import enums.DesiredTimeType;
import enums.RepeatType;
import exception.RideNotExistsException;
import exception.TrempRequestNotExist;
import transpool.logic.map.structure.Station;
import transpool.logic.time.RequestSchedule;
import transpool.logic.time.Schedule;
import transpool.logic.traffic.item.RideForTremp;
import transpool.logic.traffic.item.SubRide;
import transpool.logic.traffic.item.Ride;
import transpool.logic.traffic.item.TrempRequest;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class TrafficManager {


    List<Ride> allRides;
    List<TrempRequest> allTrempRequests;

    Map<Integer, Ride> mapIdToRide;
    Map<Integer, TrempRequest> mapIdToTrempReuqest;

    public TrafficManager() {

        this.allRides = new ArrayList<>();
        this.allTrempRequests = new ArrayList<>();
        this.mapIdToRide = new HashMap<>();
        this.mapIdToTrempReuqest = new HashMap<>();
    }

    public List<Ride> getAllRides() {
        return allRides;
    }

    public Ride getRideByID(int idOfRide) throws RideNotExistsException {
        if (!this.mapIdToRide.containsKey(idOfRide)){
            throw new RideNotExistsException(idOfRide);
        }

        return this.mapIdToRide.get(idOfRide);
    }

    public void addRide(Ride ride){
        this.allRides.add(ride);
        this.mapIdToRide.put(ride.getID(), ride);
    }

    public List<TrempRequest> getAllTrempRequests() {
        return allTrempRequests;
    }

    public void addTrempRequest(TrempRequest trempRequest){
        this.allTrempRequests.add(trempRequest);
        this.mapIdToTrempReuqest.put(trempRequest.getID(), trempRequest);
    }

    public TrempRequest getTrempRequestById(int idOfTrempRequest) throws TrempRequestNotExist{
        if (!this.mapIdToTrempReuqest.containsKey(idOfTrempRequest))
            throw new TrempRequestNotExist(idOfTrempRequest);

        return this.mapIdToTrempReuqest.get(idOfTrempRequest);
    }

//    private List<RideForTremp> getRideOptionsWithNoConnections(Station from, Station to, int onDay){
//        private List<RideForTremp> getRideOptionsWithNoConnections(TrempRequest trempRequest){
//        Station from = trempRequest.getStartStation();
//        Station to = trempRequest.getEndStation();
//        int onDay = trempRequest.getDesiredDay();
//        LocalTime time = trempRequest.getDesiredTime();
//        int maxMinutesDiff = trempRequest.getMaxTimeDiff();
//        DesiredTimeType timeType = trempRequest.getDesiredTimeType();
//
//        List<RideForTremp> noConnectionOptions = new LinkedList<>();
//        this.allRides.stream()
//                .filter(ride -> ride.getSchedule().scheduleIsRelevantForTimeAndDay(time, onDay, timeType ,maxMinutesDiff))
//                .filter(ride -> ride.containsValidRoute(from, to, onDay)) //TODO compare day by schedule
//                .forEach(ride -> {
//                    ArrayList<SubRide> subRidesOption = new ArrayList<>();
//                    subRidesOption.add(ride.getSubRide(from, to, onDay));
//                    noConnectionOptions.add(new RideForTremp(subRidesOption));
//                });
//
//        return noConnectionOptions;
//    }


    private List<RideForTremp> getRideOptionsWithNoConnections(TrempRequest trempRequest){
        Station from = trempRequest.getStartStation();
        Station to = trempRequest.getEndStation();
//        int onDay = trempRequest.getDesiredDay();
//        LocalTime time = trempRequest.getDesiredTime();
        //int maxMinutesDiff = trempRequest.getMaxTimeDiff();
        DesiredTimeType timeType = trempRequest.getDesiredTimeType();

        RequestSchedule requestSchedule = trempRequest.getDesiredSchedule();

//        List<RideForTremp> noConnectionOptions = new LinkedList<>();

        return requestSchedule.getDesiredTimeType() == DesiredTimeType.DEPART ?
                getRideOptionsWithNoConnectionsDepartingOnDay(from, to, requestSchedule) :
                getRideOptionsWithNoConnectionsArrivingOnDay(from, to, requestSchedule);


//        this.allRides.stream()
//                .filter(ride -> ride.getSchedule().scheduleIsRelevantForTimeAndDay(time, onDay, timeType ,maxMinutesDiff))
//                .filter(ride -> ride.containsValidRoute(from, to, onDay)) //TODO compare day by schedule
//                .forEach(ride -> {
//                    ArrayList<SubRide> subRidesOption = new ArrayList<>();
//                    subRidesOption.add(ride.getSubRide(from, to, onDay));
//                    noConnectionOptions.add(new RideForTremp(subRidesOption));
//                });
//
//        return noConnectionOptions;
    }



    private List<RideForTremp> getRideOptionsWithNoConnectionsDepartingOnDay(Station from, Station to, RequestSchedule departSchedule){

        int onDay = departSchedule.getStartDay();

        List<RideForTremp> noConnectionOptions = new LinkedList<>();
        this.allRides.stream()
                .filter(ride -> ride.getSchedule().scheduleIsRelevantForRequestSchedule(departSchedule))
                .filter(ride -> ride.containsValidRouteDepartingOn(onDay, from, to))
                .forEach(ride -> {
                    ArrayList<SubRide> subRidesOption = new ArrayList<>();
                    subRidesOption.add(ride.getSubRide(from, to, onDay));
                    noConnectionOptions.add(new RideForTremp(subRidesOption));
                });

        return noConnectionOptions;
    }

    private List<RideForTremp> getRideOptionsWithNoConnectionsArrivingOnDay(Station from, Station to, RequestSchedule arriveSchedule){
        int onDay = arriveSchedule.getEndDay();

        List<RideForTremp> noConnectionOptions = new LinkedList<>();
        this.allRides.stream()
                .filter(ride -> ride.getSchedule().scheduleIsRelevantForRequestSchedule(arriveSchedule))
                .filter(ride -> ride.containsValidRouteArrivingOn(onDay, from, to))
                .forEach(ride -> {
                    ArrayList<SubRide> subRidesOption = new ArrayList<>();
                    subRidesOption.add(ride.getSubRide(from, to, ride.getSchedule().getStartDay()));
                    noConnectionOptions.add(new RideForTremp(subRidesOption));
                });

        return noConnectionOptions;
    }

//    private List<RideForTremp> getRideOptionsWithConnections(int maxConnections, Station from, Station to, int onDay){
    private List<RideForTremp> getRideOptionsWithConnections(TrempRequest trempRequest){
        Station from = trempRequest.getStartStation();
        Station to = trempRequest.getEndStation();
        int maxNumberOfConnections = trempRequest.getMaxNumberOfConnections();
        int onDay = trempRequest.getDesiredDay();

        List<RideForTremp> rideOptions = new LinkedList<>();
        List<Ride> relevantRides = this.allRides.stream()
                .filter((ride)-> !ride.containsValidRoute(from, to, onDay)).collect(Collectors.toList());

        //TODO: write algorithm to groups of rides and filter by max connections

        return rideOptions;
    }
//    public List<RideForTremp> getRideOptions(int maxConnections, Station from, Station to, int onDay){
    public List<RideForTremp> getRideOptions(TrempRequest trempRequest){
        Station from = trempRequest.getStartStation();
        Station to = trempRequest.getEndStation();
        int maxNumberOfConnections = trempRequest.getMaxNumberOfConnections();
//        int dayOfTrempRequest = trempRequest.getDesiredDay();

        List<RideForTremp> allRideOptions = new ArrayList<>();


        allRideOptions.addAll(getRideOptionsWithNoConnections(trempRequest));
        allRideOptions.addAll(getRideOptionsWithConnections(trempRequest));
//
//        allRideOptions.addAll(getRideOptionsWithNoConnections(from, to, onDay));
//        allRideOptions.addAll(getRideOptionsWithConnections(maxConnections, from, to, onDay));

        return allRideOptions;
    }

    public List<RideForTremp> getAllPossibleTrempsForTrempRequest(TrempRequest trempRequest){


        //TODO Andrey instead of sending the day of leave or arrive - make sure to send object with both
//        List<RideForTremp> relevantByRouteOptions = getRideOptions(maxNumberOfConnections, start, end, dayOfTrempRequest);
                List<RideForTremp> relevantByRouteOptions = getRideOptions(trempRequest);
        relevantByRouteOptions.sort(Comparator.comparingInt(RideForTremp::getNumOfParts));


//        Function<RideForTremp, LocalDateTime> rideCorrectTimeGetter = trempRequest.getDesiredTimeType() == DesiredTimeType.DEPART ?
//                RideForTremp::getDepartDateTime :  RideForTremp::getArriveDateTime;

//        return relevantByRouteOptions.stream()
//                .filter(rideForTremp -> rideCorrectTimeGetter.apply(rideForTremp).equals(trempRequest.getDesiredTime()))
//                .collect(Collectors.toList());
        return relevantByRouteOptions;
    }
}
