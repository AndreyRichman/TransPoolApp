package enums;

import exception.NotSupportedRideRepeatTimeException;

import java.util.function.Function;

public enum RepeatType {
    ONE_TIME {
        @Override
        int getDevider() {
            return 0;
        }

        @Override
        public Function<Integer, Boolean> getDayMatchComparator(int rideDay) {
            return trempRequestDay -> rideDay == trempRequestDay;
        }
    },
    DAILY {
        @Override
        int getDevider() {
            return 1;
        }
    },
    BIDAIILY {
        @Override
        int getDevider() {
            return 2;
        }
    },
    WEEKLY {
        @Override
        int getDevider() {
            return 7;
        }
    },
    MONTHLY {
        @Override
        int getDevider() {
            return 30;
        }
    };

     public Function<Integer, Boolean> getDayMatchComparator(int rideDay){
        return trempRequestDay -> (trempRequestDay - rideDay) % getDevider() == 0;
    }

    abstract int getDevider();

    public static RepeatType getRepeatTypeFromString(String repeatType) throws NotSupportedRideRepeatTimeException {

        RepeatType type;
        switch(repeatType){
            case "OneTime" : {
                type = RepeatType.ONE_TIME;
                break;
            }
            case "Daily" : {
                type = RepeatType.DAILY;
                break;
            }
            case "BiDaily" : {
                type = RepeatType.BIDAIILY;
                break;
            }
            case "Weekly" : {
                type = RepeatType.WEEKLY;
                break;
            }
            case "Monthly" : {
                type = RepeatType.MONTHLY;
                break;
            }
            default: {
                throw new NotSupportedRideRepeatTimeException(repeatType);
            }
        }
        return type;
    }
}
