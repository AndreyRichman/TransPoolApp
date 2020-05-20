package transpool.logic.traffic.item;

import transpool.logic.user.User;

import java.time.LocalTime;
import java.util.List;

public class RideForTremp {

    private List<SubRide> subRides;

    public RideForTremp(List<SubRide> subRides) {
        this.subRides = subRides;
    }

    public void assignTrempRequest(TrempRequest trempRequest){
        User userToAssign = trempRequest.getUser();
        this.getSubRides()
                .forEach(
                        subRide -> subRide.applyTrempistToAllPartsOfRide(userToAssign));
    }

    public List<SubRide> getSubRides() {
        return subRides;
    }

    public LocalTime getDepartTime(){
        return this.subRides.get(0).getDepartTime();
    }

    public LocalTime getArriveTime(){
        return this.subRides.get(this.subRides.size() - 1).getArrivalTime();
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
}
