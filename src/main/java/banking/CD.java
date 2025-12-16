package banking;

public class CD extends Account {

    private int monthsPassed;
    private static final int MATURITY_PERIOD = 12; // 12 months to mature

    public CD(String id, double apr, double initialBalance){
        super(id, apr);
        this.balance = initialBalance;
    }

    public int getMonthsPassed() {
        return monthsPassed;
    }

    public void incrementMonths(int months) {
        this.monthsPassed += months;
    }

    public boolean hasMatured() {
        return monthsPassed >= MATURITY_PERIOD;
    }

    public int getMaturityPeriod() {
        return MATURITY_PERIOD;
    }
}
