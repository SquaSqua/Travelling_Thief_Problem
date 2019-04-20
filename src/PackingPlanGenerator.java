import java.util.ArrayList;

class PackingPlanGenerator {

    private static double maxSpeed = DataFromFile.getMaxSpeed();
    private static int capacity = DataFromFile.getCapacity();
    private static int dimension = DataFromFile.getDimension();
    private static double[][] distances = DataFromFile.getDistances();
    private static int[][] items = DataFromFile.getItems();
    private static Integer[][] groupedItems = createGroupedItemsArray();
    private static double coefficient = (maxSpeed - DataFromFile.getMinSpeed()) / capacity;

    static void setPackingPlanAndFitness(Individual individual) {
        setPackingPlan(individual);
        individual.setFitness(countFitness(individual));
    }

    private static void setPackingPlan(Individual individual) {
        int[] route = individual.getRoute();
        ArrayList<double[]> gainOfItems = countGain(route);
        int[] packingPlan = new int[items.length];
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
        individual.setPackingPlan(packingPlan);
    }

    private static double countFitness(Individual individual) {
        int[] route = individual.getRoute();
        int[] packingPlan = individual.getPackingPlan();
        double wage = 0;
        double weight = 0;
        double time = 0;
        for(int currentPosition = 0; currentPosition < route.length - 1; ) {
            int currentCityIndex = route[currentPosition];
            Integer[] itemsInCity = groupedItems[currentCityIndex];
            for(int itemInd : itemsInCity) {
                if(packingPlan[itemInd] == 1) {
                    wage += items[itemInd][DataProvider.PROFIT_FROM_ITEM];
                    weight += items[itemInd][DataProvider.WEIGHT_OF_ITEM];
                }
            }
            time += countTime(route, currentCityIndex, route[++currentPosition], countSpeed(weight));
        }

        return wage - (DataFromFile.getRentingRatio() * time);
    }

    private static double countTime(int[] route, int startIndex, double currentSpeed) {
        int endIndex = route.length - 1;
        return countTime(route, startIndex, endIndex, currentSpeed);
    }

    private static double countTime(int[] route, int startIndex, int endIndex, double currentSpeed) {
        return countRoad(route, startIndex, endIndex) / currentSpeed;
    }

//    private double countRoad(int startIndex) {
//        return countRoad(startIndex, route.length - 1);
//    }

    private static double countRoad(int[] route, int startIndex, int endIndex) {
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

    private static double countSpeed(double currentWeight) {
        return maxSpeed - (currentWeight * coefficient);
    }
    //for each item gain is counted as a v - R(t - tb), where v - value of one item, R - renting ratio,
    // t - time of carrying this one item with no more items from the point it was placed to,
    //tb - basic time of travel from the chosen point with empty knapsack
    private static ArrayList<double[]> countGain(int[] route) {
        ArrayList<double[]> gainOfItems = new ArrayList<>();
        for(int itemIndex = 0; itemIndex < items.length; itemIndex++) {
            int cityIndexInRoute = -666;
            for(int j = 0; j < route.length; j++) {
                if(route[j] + 1 == items[itemIndex][DataProvider.CITY_OF_ITEM]) {
                    cityIndexInRoute = j;
                    break;
                }
            }
            gainOfItems.add(new double[] {itemIndex, (((items[itemIndex][DataProvider.PROFIT_FROM_ITEM] -
                    - DataFromFile.getRentingRatio() * (countTime(route, cityIndexInRoute, countSpeed(items[itemIndex][DataProvider.WEIGHT_OF_ITEM]))
                            - countTime(route, cityIndexInRoute, maxSpeed)))
                    / capacity))});
        }
        gainOfItems.sort((double[] o1, double[] o2) ->
                o2[1] - o1[1] < 0 ? -1 : o2[1] > 0 ? 1 : 0);
        return gainOfItems;
    }

    private static Integer[][] createGroupedItemsArray() {
        groupedItems = new Integer[dimension][];
        ArrayList<Integer>[] groupedItemsList = new ArrayList[dimension];
        for(int i = 0; i < dimension; i++) {
            groupedItemsList[i] = new ArrayList<>();
        }
        for (int[] item : items) {
            groupedItemsList[item[DataProvider.CITY_OF_ITEM] - 1].add(item[DataProvider.INDEX_OF_ITEM] - 1);
        }
        for(int i = 0; i < dimension; i++) {
            groupedItems[i] = new Integer[groupedItemsList[i].size()];
            for (int j = 0; j < groupedItemsList[i].size(); j++) {
                groupedItems[i][j] = groupedItemsList[i].get(j);
            }
        }
        return groupedItems;
    }
}
