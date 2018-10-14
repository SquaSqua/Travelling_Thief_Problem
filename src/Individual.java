public class Individual {
    private double[] route;
    private int[] packingPlan;

    public Individual(double[] route) {
        this.route = route;
    }

    public double[] getRoute() {
        return route;
    }

    public String toString() {
        String result = "[";
        for(int i = 0; i < route.length - 1; i++) {
            result += route[i] + ", ";
        }
        result += route[route.length - 1] + "]";
        return result;
    }
}
