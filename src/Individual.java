import java.util.ArrayList;

public class Individual {
    private int[] route;
    private int[] packingPlan;
    double[][] distances;
    int[][] items;
    ArrayList<double[]> gainOfItems;
    double maxSpeed;
    double coefficient;
    double rentingRatio;
    double basicTime;
    int capacity;

    public Individual(int[] route, double[][] distances, int[][] items, double maxSpeed, double coefficient, double rentingRatio, int capacity) {
        this.route = route;
        this.distances = distances;
        this.items = items;
        gainOfItems = new ArrayList<>();
        this.maxSpeed = maxSpeed;
        this.coefficient = coefficient;
        this.rentingRatio = rentingRatio;
        basicTime = countTime(0, maxSpeed);
        this.capacity = capacity;

        countGain();
    }


    private double countTime(int startIndex, double currentSpeed) {
        return countRoad(startIndex) / currentSpeed;
    }

    private double countRoad(int startIndex) {
        double completeDistance = 0;
        for(int i = startIndex; i < route.length - 1; i++) {
            completeDistance += distances[route[i]][route[i + 1]];
        }
        return completeDistance;
    }

    public double countSpeed(double currentWeight) {
        return maxSpeed - (currentWeight * coefficient);
    }
    //As an indirect gain of an item I take a value of an item divided by the total time I would have to carry it
    //to the and of a route if it would be my only taken item.
    //A total time of carrying one item it a total remaining distance divided by velocity of a thief with only this item.
    //Velocity is counted as maxSpeed - (maxSpeed - minSpeed) / capacity) * Wc ***Wc is a current wage of a knapsack***
    private void countGain() {
        for(int i = 0; i < items.length; i++) {
            int ind = -1;//index of a city of an item 'i' in a route
            for(int j = 0; j < route.length; j++) {//finding index of city number
                if(route[j] + 1 == items[i][3]) {
                    ind = j;
                    break;
                }
            }
            double currentSpeed = countSpeed(items[i][2]);
            gainOfItems.add(new double[] {i, (((items[i][1] / countTime(ind, currentSpeed) -
                    - rentingRatio * (countTime(ind, currentSpeed) - countTime(ind, maxSpeed)))
                    / capacity))});
        }
        gainOfItems.sort((double[] o1, double[] o2) ->
                o2[1] - o1[1] < 0 ? -1 : o2[1] > 0 ? 1 : 0);
//        for(int i = 0; i < gainOfItems.size(); i++) {
//            System.out.println(gainOfItems.get(i)[1] + ", ");
//        }
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
