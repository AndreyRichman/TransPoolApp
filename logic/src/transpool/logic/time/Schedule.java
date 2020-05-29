package transpool.logic.time;

import enums.RepeatType;
import exception.NotSupportedRideRepeatTimeException;

public class Schedule {
    private int hour;
    private int min;
    private Integer day;
    private RepeatType repeatType;

    public Schedule(int hour, Integer day, String repeatType ) throws NotSupportedRideRepeatTimeException {
        this.hour = hour;
        this.day = day;

        setRepeatTypeFromString(repeatType);
    }

    public void setRepeatType(RepeatType repeatType) {
        this.repeatType = repeatType;
    }

    public void setRepeatTypeFromString(String repeatType) throws NotSupportedRideRepeatTimeException {

        switch(repeatType){
            case "OneTime" : {
                setRepeatType(RepeatType.ONE_TIME);
                break;
            }
            case "Daily" : {
                setRepeatType(RepeatType.DAILY);
                break;
            }
            case "BiDaily" : {
                setRepeatType(RepeatType.BIDAIILY);
                break;
            }
            case "Weekly" : {
                setRepeatType(RepeatType.WEEKLY);
                break;
            }
            case "Monthly" : {
                setRepeatType(RepeatType.MONTHLY);
                break;
            }
            default: {
                throw new NotSupportedRideRepeatTimeException(repeatType);
            }

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
