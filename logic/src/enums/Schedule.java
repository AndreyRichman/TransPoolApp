package enums;

public enum Schedule {
    NONE,
    EVERYDAY,
    EVERY2DAY,
    EVERY7DAY,
    EVERYMONTH;

    private int day;
    private int hour;
    private int minutes;

    public void setDay(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getHour() {
        return hour;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes() {
        return minutes;
    }
}
