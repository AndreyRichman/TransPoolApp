package enums;

public enum Schedule {
    SINGLE_TIME,
    EVERY_DAY,
    EVERY_2DAY,
    EVERY_7DAY,
    EVERY_MONTH;

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
