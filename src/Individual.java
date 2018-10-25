import java.util.ArrayList;
import java.util.Arrays;

public class Individual {
    private int[] route;
    private int[] packingPlan;
    private double[][] distances;
    private int[][] items;
    private Integer[][] groupedItems;
    private ArrayList<double[]> gainOfItems;
    private double maxSpeed;
    private double coefficient;
    private double basicTime;
    private int capacity;
    private int fitnessWage;
    private double fitnessTime;
    private double crowdingDistance;
    private int rank;

    public Individual(int[] route, double[][] distances, int[][] items,
                      Integer[][] groupedItems, double maxSpeed, double coefficient, int capacity) {
        this.route = route;
        this.distances = distances;
        this.groupedItems = groupedItems;
        this.items = items;
        gainOfItems = new ArrayList<>();
        this.maxSpeed = maxSpeed;
        this.coefficient = coefficient;
        basicTime = countTime(0, maxSpeed);
        this.capacity = capacity;
        packingPlan = new int[items.length];

        countGain();
        setPackingPlan();
        countFitnessTime();
        countFitnessWage();
    }

    private void setPackingPlan() {
        int ind = 0;
        int currentWeight = 0;
        while(currentWeight < capacity && ind < gainOfItems.size()) {
            int rowNumber = (int)gainOfItems.get(ind)[0];
            if(items[rowNumber][2] + currentWeight <= capacity) {
                packingPlan[rowNumber] = 1;
                currentWeight += items[rowNumber][2];
            }
            ind++;
        }
    }

    private double countTime(int startIndex, double currentSpeed) {
        int endIndex = route.length - 1;
        return countTime(startIndex, endIndex, currentSpeed);
    }

    private double countTime(int startIndex, int endIndex, double currentSpeed) {
        return countRoad(startIndex, endIndex) / currentSpeed;
    }

    private double countRoad(int startIndex, int endIndex) {
        double completeDistance = 0;
        if(endIndex == route.length - 1) {
            for(int i = startIndex; i < endIndex - 1; ) {
                completeDistance += distances[route[i]][route[++i]];
            }
            completeDistance += distances[route.length - 2][0];
        }
        else {
            for(int i = startIndex; i < endIndex; i++) {
                completeDistance += distances[route[i]][route[i + 1]];
            }
        }

        return completeDistance;
    }

    public double countSpeed(double currentWeight) {
        return maxSpeed - (currentWeight * coefficient);
    }
    //for each item gain is counted as a v - R(t - tb), where v - value of one item, R - renting ratio,
    // t - time of carrying this one item with no more items from the point it was placed to,
    //tb - basic time of travel from the chosen point with empty knapsack
    private void countGain() {
        for(int i = 0; i < items.length; i++) {
            int[] currentRow = items[i];
            gainOfItems.add(new double[] {i, currentRow[1] /
                    (currentRow[2] * countTime(currentRow[3], countSpeed(currentRow[2])))});
        }
        gainOfItems.sort((double[] o1, double[] o2) ->
                o2[1] - o1[1] < 0 ? -1 : o2[1] > 0 ? 1 : 0);
    }


    public void countFitnessWage() {
        int totalWage = 0;
        for(int i = 0; i < packingPlan.length; i++) {
            if(packingPlan[i] == 1) {
                totalWage += items[i][1];
            }
        }
        fitnessWage = totalWage;
    }

    public void countFitnessTime() {
        double weight = 0;
        double time = 0;
        for(int currentPosition = 0; currentPosition < route.length - 1; ) {
            Integer[] currentCity = groupedItems[currentPosition];
            for(int j = 0; j < currentCity.length; j++) {
                if(packingPlan[currentCity[j]] == 1) {
                    weight += items[currentCity[j]][2];
                }
            }
            time += countTime(currentPosition, ++currentPosition, countSpeed(weight));
        }
        fitnessTime = time;
    }

    public int compareTo(Individual o) {
        return (int) Math.signum(Math.signum(fitnessTime - o.fitnessTime) + Math.signum(fitnessWage - o.fitnessWage));
    }

    public int[] getRoute() {
        return route;
    }

    public int getFitnessWage() { return fitnessWage; }

    public double getFitnessTime() { return fitnessTime; }

    public void setCrowdingDistance(double crowdingDistance) {
        this.crowdingDistance = crowdingDistance;
    }

    public double getCrowdingDistance() {
        return crowdingDistance;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRank() {
        return rank;
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
