package main.window.main.task;

import javafx.concurrent.Task;
import transpool.logic.traffic.item.RideForTremp;
import transpool.logic.traffic.item.TrempRequest;

public class assainTrempTask extends Task<Boolean> {

    private TrempRequest trempRequest;
    private RideForTremp rideForTremp;


    @Override
    protected Boolean call() throws Exception {

        if(trempRequest != null && rideForTremp != null){
            rideForTremp.assignTrempRequest(trempRequest);
            trempRequest.assignRides(rideForTremp);
        }

        return Boolean.TRUE;
    }

    public assainTrempTask(TrempRequest trempRequest, RideForTremp rideForTremp){
        this.trempRequest = trempRequest;
        this.rideForTremp = rideForTremp;

    }
}
