package transpool.logic.time;

import enums.RepeatType;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;

public class Schedule {
    //private int hour;
    //private int min;
    //private int startDay;
    //private int endDay;
    private RepeatType repeatType;
    //private LocalTime startTime;
    //private LocalTime endTime;

    LocalDateTime minDateTime = LocalDateTime.of(1970, 1, 1, 0, 0);
    LocalDateTime startDateTime;
    LocalDateTime endDateTime;

    public Schedule(int hour, int startDay, RepeatType repeatType){
        this.repeatType = repeatType;
        initStartAndEndDateTimes(startDay, hour);
    }

    public Schedule(LocalTime startTime, int day, RepeatType repeatType){
        this.repeatType = repeatType;
        initStartAndEndDateTimes(day, startTime.getHour());
    }

    //TODO: add validation for hour & day
    private void initStartAndEndDateTimes(int day, int hour){
        this.startDateTime = this.minDateTime.plusDays(day).plusHours(hour);
        this.endDateTime = this.startDateTime;
    }

//    public void setStartTimeAndDay(LocalTime startTime, int day){
//        this.startDateTime = minDateTime.plusDays(day).plusHours(startTime.getHour());
////        this.startTime = startTime;
////        this.startDay = day;
//    }

    public void addHoursFromStart(int hoursToAdd){
        this.endDateTime = this.endDateTime.plusHours(hoursToAdd);
    }

    public void addMintuesFromStart(int minutesDuration){

        LocalDateTime expectedEndTime = this.getStartDateTime().plusMinutes(minutesDuration);// startTime.plusMinutes(duration);

        int minutesAtEnd = expectedEndTime.getMinute();
        int sheerit = minutesAtEnd % 5;
        int minutesToAdd = sheerit > 2 ? 5 - sheerit: -sheerit;

        this.endDateTime = expectedEndTime.plusMinutes(minutesToAdd);
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
        this.endDateTime = startDateTime;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public int getStartDay(){
        return (int)Duration.between(this.startDateTime, this.minDateTime).abs().toDays();
    }
    public int getEndDay() {
        return (int)Duration.between(this.endDateTime, this.minDateTime).toDays();
    }

    public LocalTime getStartTime() {
        return this.startDateTime.toLocalTime();
    }

    public LocalTime getEndTime() {
        return this.endDateTime.toLocalTime();
    }

    public RepeatType getRepeatType() {
        return repeatType;
    }

        public void setRepeatType(RepeatType repeatType) {
        this.repeatType = repeatType;
    }

    public boolean startDaysRangeMatchesDateTime(LocalDateTime dateTimeToCheck, int maxMinutesDiff){
        int dayToCheck = (int) Duration.between(minDateTime, dateTimeToCheck).abs().toDays();
        return daysRangeContainsDay(this.getStartDay(), dayToCheck)
                && startDateTimeIsNearDateTime(this.getStartDateTime(), dateTimeToCheck, maxMinutesDiff);
    }

    public boolean endDaysRangeMatchesDateTime(LocalDateTime dateTimeToCheck, int maxMinutesDiff){
        int dayToCheck = (int) Duration.between(minDateTime, dateTimeToCheck).abs().toDays();
        return daysRangeContainsDay(this.getEndDay(), dayToCheck)
                && startDateTimeIsNearDateTime(this.getEndDateTime(), dateTimeToCheck, maxMinutesDiff);
    }

    private boolean daysRangeContainsDay(int thisDay, int dayToCheck){
        return thisDay <= dayToCheck &&
                repeatType.getDayMatchComparator(thisDay).apply(dayToCheck);
    }

    private boolean startDateTimeIsNearDateTime(LocalDateTime thisDateTime, LocalDateTime dateTimeToCheck, int minutesDiffLimit){
        return Duration.between(thisDateTime, dateTimeToCheck).abs().toMinutes() <= minutesDiffLimit;
    }

    public Schedule createClone(){
        return new Schedule(this.getStartTime(), this.getStartDay(), this.getRepeatType());
    }

    public Schedule createCloneWithNewStartDateTime(LocalDateTime newDateTime){
        Schedule newSchedule = createClone();
        newSchedule.setStartDateTime(newDateTime);
        newSchedule.setEndDateTime(newDateTime);

        return newSchedule;
    }
}
