package exception;

public class PricePerKilometerException extends  Exception{
    String mgs;

    public PricePerKilometerException(String msg){
        this.mgs = msg;
    }

    public void setMgs(String mgs) {
        this.mgs = mgs;
    }

    public String getMgs() {
        return mgs;
    }
}
