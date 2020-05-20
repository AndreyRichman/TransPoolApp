package transpool.ui.request.type;

import transpool.ui.request.enums.RequestType;
import transpool.ui.interfaces.UserRequest;

import java.time.LocalTime;
import java.util.List;

public class NewRideRequest implements UserRequest {

    private String userName;
    private int carCapacity;
    private int pricePerKilometer = 0;
    private LocalTime startTime = LocalTime.MIN;
    private List<String> stations;

    @Override
    public RequestType getRequestType() {

        return RequestType.NEW_RIDE;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCarCapacity(int carCapacity) {
        this.carCapacity = carCapacity;
    }

    public void setPricePerKilometer(int pricePerKilometer) {
        this.pricePerKilometer = pricePerKilometer;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setStations(List<String> stations) {
        this.stations = stations;
    }

    public String getUserName() {
        return userName;
    }

    public int getCarCapacity() {
        return carCapacity;
    }

    public int getPricePerKilometer() {
        return pricePerKilometer;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public List<String> getStations() {
        return stations;
    }
}
