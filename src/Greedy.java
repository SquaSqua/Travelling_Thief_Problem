import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class Greedy {

    void run(String name){
        try {
            PrintWriter out = new PrintWriter(name);
            String result = findIndividual();
            System.out.println("Znaleziony osobnik: " + result);
            out.println(result);
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Nie da się utworzyć pliku!");
        }


    }

    private String findIndividual() {

        Individual individual = new Individual(createRoute());
        PackingPlanGenerator.setPackingPlanAndFitness(individual);

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);
        df.setMinimumFractionDigits(3);
        df.setGroupingUsed(false);
        System.out.println(Arrays.toString(individual.getRoute()));
        System.out.println(Arrays.toString(individual.getPackingPlan()));
        return df.format(individual.getFitness());
    }

    private int[] createRoute() {
        int[] road = new int[DataFromFile.getDimension() + 1];
        int indexOfCurrentInRoad = 0;
        Random random = new Random();
        int currentCity = random.nextInt(DataFromFile.getDimension());
        road[indexOfCurrentInRoad] = currentCity;
        indexOfCurrentInRoad++;

        ArrayList<Integer> notVisited = new ArrayList<>();
        fillArrayWithCities(notVisited);

        int indexToRemove = notVisited.indexOf(currentCity);
        notVisited.remove(indexToRemove);

        while(!notVisited.isEmpty()) {
            double minDistance = Double.MAX_VALUE;
            int closestNotVisitedCity = -1;
            for(Integer i : notVisited) {
                if(DataFromFile.getDistances()[currentCity][i] < minDistance) {
                    minDistance = DataFromFile.getDistances()[currentCity][i];
                    closestNotVisitedCity = i;
                }
            }
            currentCity = closestNotVisitedCity;
            indexToRemove = notVisited.indexOf(currentCity);
            notVisited.remove(indexToRemove);
            road[indexOfCurrentInRoad] = currentCity;
            indexOfCurrentInRoad++;
        }
        road[indexOfCurrentInRoad] = road[0];
        return road;
    }

    private void fillArrayWithCities(ArrayList<Integer> notVisited) {
        for(int i = 0; i < DataFromFile.getDimension(); i++) {
            notVisited.add(i);
        }
    }
}
