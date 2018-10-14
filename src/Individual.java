public class Individual {
    private int[] route;
    private int[] packingPlan;
    double[][] distances;
    int[][] items;
    double[] costOfItems;
    double maxSpeed;
    double coefficient;

    public Individual(int[] route, double[][] distances, int[][] items, double maxSpeed, double coefficient) {
        this.route = route;
        this.distances = distances;
        this.items = items;
        costOfItems = new double[items.length];
        this.maxSpeed = maxSpeed;
        this.coefficient = coefficient;

        countCosts();
    }


    //As an indirect cost of an item I take a value of an item divided by the total time I would have to carry it
    //to the and of a route if it would be my only taken item.
    //A total time of carrying one item it a total remaining distance divided by velocity of a thief with only this item.
    //Velocity is counted as maxSpeed - capacity * (maxSpeed - minSpeed)/ W *** W is a current wage of a knapsack***
    public void countCosts() {
        for(int i = 0; i < costOfItems.length; i++) {
            costOfItems[i] = ((items[i][1] * maxSpeed) - (coefficient / items[i][2])) / items[i][2];
        }
    }

    public int[] getRoute() {
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
