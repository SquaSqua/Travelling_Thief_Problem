import java.util.ArrayList;
import java.util.Collections;

public class Evolution {

    private int dimension;
    private int numOfItems;
    private int capacity;
    private double minSpeed;
    private double maxSpeed;
    private double rentingRatio;
    private double[][] cities;
    private double[][] distances;
    private int[][] items;

    private int popSize;
    private int numOfGeners;

    private ArrayList<Individual> population = new ArrayList<>();

    public Evolution(String definitionFile, int popSize, int numOfGeners) {
        Loader loader = new Loader(definitionFile);
        loader.readFile();
        dimension = loader.getDimension();
        numOfItems = loader.getNumOfItems();
        capacity = loader.getCapacity();
        minSpeed = loader.getMinSpeed();
        maxSpeed = loader.getMaxSpeed();
        rentingRatio = loader.getRentingRatio();
        cities = loader.getCities();
        items = loader.getItems();
        distances = new double[dimension][dimension];
        createDimensionArray();

        this.popSize = popSize;
        this.numOfGeners = numOfGeners;
        initialize();
    }

    public void createDimensionArray() {
        for(int i = 0; i < dimension; i++) {
            for(int j = 0; j < dimension; j++) {
                double distance;
                if(i == j) {
                    distance = 0;
                } else {
                    distance = Math.sqrt(Math.abs(cities[i][1] - cities[j][1])
                            + Math.abs(cities[i][2] - cities[j][2]));
                }
                distances[i][j] = distance;
                distances[j][i] = distance;//redundant
            }
        }
    }

    public void initialize() {
        for(int i = 0; i < popSize; i++) {
            population.add(generateRandomInd());
        }
    }

    public Individual generateRandomInd() {
        double[] route = new double[dimension + 1];
        ArrayList<Integer> routeList = new ArrayList<>();
        for(int i = 0; i < dimension; i++) {
            routeList.add(i);
        }
        Collections.shuffle(routeList);
        for(int i = 0; i < dimension; i++) {
            route[i] = routeList.get(i);
        }
        route[dimension] = route[0];
        Individual ind =  new Individual(route);
        return ind;
    }

    public void evolve() {

    }
}
