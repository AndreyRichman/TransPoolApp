package transpool.logic.traffic.item;

import enums.RepeatType;
import enums.TrempPartType;
import transpool.logic.time.Schedule;
import transpool.logic.user.Trempist;
import transpool.logic.user.User;
import transpool.logic.map.structure.Station;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.OptionalDouble;

public class SubRide {

    private final Ride originalRide;
    private final Station startStation;
    private final Station endStation;
    List<PartOfRide> selectedPartsOfRide;
    private Schedule schedule;

    public SubRide(Ride originalRide, Station startStation, Station endStation, int onDay) {
        this.originalRide = originalRide;
        this.startStation = startStation;
        this.endStation = endStation;
        this.selectedPartsOfRide = getRelevantPartsOfRide(startStation, endStation);
        this.schedule = initOneTimeScheduleAccordingToParts(onDay);
    }

    private Schedule initOneTimeScheduleAccordingToParts(int onDay){
        PartOfRide firstPart = selectedPartsOfRide.get(0);
        PartOfRide lastPart = selectedPartsOfRide.get(selectedPartsOfRide.size() - 1);

        Schedule schedule = new Schedule(firstPart.getSchedule().getStartTime(), onDay, RepeatType.ONE_TIME); //firstPart.getSchedule().createClone();
        schedule.setEndDateTime(lastPart.getSchedule().getEndDateTime());

        return schedule;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Ride getOriginalRide() {
        return originalRide;
    }

    private List<PartOfRide> getRelevantPartsOfRide(Station start, Station end){
        int indexOfPartsFrom = originalRide.getAllStations().indexOf(start);
        int indexOfPartsTo = originalRide.getAllStations().indexOf(end);

        return originalRide.getPartsOfRide().subList(indexOfPartsFrom, indexOfPartsTo);
    }

    public void applyTrempistToAllPartsOfRide(User user, int onDay) {
        selectedPartsOfRide.forEach( partOfRide -> {
            TrempPartType fromStation = TrempPartType.MIDDLE, toStation = TrempPartType.MIDDLE;

            if (partOfRide == selectedPartsOfRide.get(0))
                fromStation = TrempPartType.FIRST;

            if (partOfRide == selectedPartsOfRide.get(selectedPartsOfRide.size() - 1))
                toStation = TrempPartType.LAST;

            partOfRide.addTrempist(new Trempist(user, fromStation, toStation), onDay);
        });
    }

    public Station getStartStation() {
        return startStation;
    }

    public Station getEndStation() {
        return endStation;
    }

    public double getTotalCost(){
        return getTotalDistance() * originalRide.getPricePerKilometer();
    }

    public double getTotalDistance(){
        return this.selectedPartsOfRide.stream()
                .mapToDouble(PartOfRide::getLengthOfRoad)
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

    public double getAverageFuelUsage(){
        OptionalDouble average = this.selectedPartsOfRide.stream().mapToDouble(PartOfRide::getFuelUsage).average();
        return average.isPresent() ? average.getAsDouble(): 0;
    }


}
