package classes;

import enums.TrempPartType;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.OptionalDouble;

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
        int indexOfPartsFrom = originalRide.getAllStations().indexOf(start);
        int indexOfPartsTo = originalRide.getAllStations().indexOf(end);

        return originalRide.getPartsOfRide().subList(indexOfPartsFrom, indexOfPartsTo);
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

    public LocalTime getDepartTime(){
        return this.selectedPartsOfRide.get(0).getStartTime();
    }
    public LocalTime getArrivalTime(){
        return this.selectedPartsOfRide.get(this.selectedPartsOfRide.size() - 1).getEndTime();
    }

    public double getAverageFuelUsage(){
        OptionalDouble average = this.selectedPartsOfRide.stream().mapToDouble(PartOfRide::getFuelUsage).average();
        return average.isPresent() ? average.getAsDouble(): 0;
    }


}
