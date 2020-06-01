package transpool.logic.traffic.item;

import transpool.logic.user.User;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class RideForTremp {

    private List<SubRide> subRides;

    public RideForTremp(List<SubRide> subRides) {
        this.subRides = subRides;
    }

    public void assignTrempRequest(TrempRequest trempRequest){
        User userToAssign = trempRequest.getUser();
        int dayOfTremp = trempRequest.getDay();
        this.getSubRides()
                .forEach(
                        subRide -> subRide.applyTrempistToAllPartsOfRide(userToAssign, dayOfTremp));
    }

    public List<SubRide> getSubRides() {
        return subRides;
    }

    public LocalDateTime getDepartDateTime(){
        return this.subRides.get(0).getDepartDateTime();
    }

    public LocalDateTime getArriveDateTime(){
        return this.subRides.get(this.subRides.size() - 1).getArrivalDateTime();
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
