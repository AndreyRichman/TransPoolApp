package transpool.logic.traffic.item;

import transpool.logic.time.Schedule;
import transpool.logic.user.Driver;
import transpool.logic.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RideForTremp {

    private List<SubRide> subRides;
    Schedule schedule;

    private Map<User, Driver.Rank> allRanks;

    public RideForTremp(List<SubRide> subRides) {
        this.subRides = subRides;
        this.schedule = initScheduleAccordingToRides(subRides);
        this.allRanks = new HashMap<>();
    }

    private Schedule initScheduleAccordingToRides(List<SubRide> subRides){
        SubRide first = subRides.get(0);
        SubRide last = subRides.get(subRides.size() - 1);

        return new Schedule(first.getSchedule(), last.getSchedule());
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void assignTrempRequest(TrempRequest trempRequest){
        User userToAssign = trempRequest.getUser();

        getSubRides()
                .forEach(
                        subRide -> {
                            subRide.applyTrempistToAllPartsOfRide(userToAssign, subRide.getSchedule().getStartDay());
                        });
    }

    public List<SubRide> getSubRides() {
        return subRides;
    }

    public double getAverageFuelUsage(){
        return this.subRides.stream().mapToDouble(SubRide::getAverageFuelUsage).average().orElse(0);
    }

    public double getTotalDistance(){
        return this.subRides.stream().mapToDouble(SubRide::getTotalDistance).sum();
    }

    public double getTotalCost(){
        return this.subRides.stream().mapToDouble(SubRide::getTotalCost).sum();
    }

    public int getNumOfConnections(){
        return this.subRides.size() - 1;
    }

    public int getNumOfParts(){
        return this.subRides.size();
    }

//    public boolean notAllSubRidesWereRanked(){
//        return this.subRides.stream().anyMatch(SubRide::hasNotRankedUsers);
//    }

//    public boolean wasNotRankedByUser(User user){
//
//    }

    public boolean isRankedByUser(User user){
        return this.allRanks.containsKey(user);
    }

    public void addRank(User user, Driver.Rank rank){
        this.subRides.forEach(subRide -> subRide.getOriginalRide().getRideOwner().addRank(rank));
        this.allRanks.put(user, rank);
    }
}
