package classes;

import enums.Recurrences;

public class Schedule {
    private int hour;
    private int min;
    private Integer day;
    private Recurrences recurrences;

    public Schedule(int hour, Integer day, String rec ){
        this.hour = hour;
        this.day = day;
    }

    public void setRecurrences(String rec) {

        switch(rec){
            case "OneTime" : {this.recurrences = Recurrences.ONE_TIME; break;}
            case "Daily" : {this.recurrences = Recurrences.DAILY; break;}
            case "BiDaily " : {this.recurrences = Recurrences.BIDAIILY; break;}
            case "Weekly " : {this.recurrences = Recurrences.WEEKLY; break;}
            case "Monthly " : {this.recurrences = Recurrences.MONTHLY; break;}

        }
    }
    public int getMin() {
        return min;
    }

    public int getHour() {
        return hour;
    }

    public Integer getDay() {
        return day;
    }
}
