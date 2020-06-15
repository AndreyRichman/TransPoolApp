package exception;

public class CapacityException extends  Exception {

    String mgs;

    public CapacityException(String msg){
        this.mgs = msg;
    }

    public void setMgs(String mgs) {
        this.mgs = mgs;
    }

    public String getMgs() {
        return mgs;
    }


}
