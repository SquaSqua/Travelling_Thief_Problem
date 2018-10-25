import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Evolution {

    private int dimension;
    private int capacity;
    private double minSpeed;
    private double maxSpeed;
    private double[][] cities;
    private double[][] distances;
    private int[][] items;
    private Integer[][] groupedItems;

    private int popSize;
    private int numOfGeners;
    private double crossProb;
    private double mutProb;
    private int tournamentSize;

    private double coefficient;

    private ArrayList<Individual> population = new ArrayList<>();
    private ArrayList<ArrayList<Individual>> paretoFronts = new ArrayList<>();

    public double maxOfAll = Double.MIN_VALUE;

    public Evolution(String definitionFile, int popSize, int numOfGeners, int tournamentSize, double crossProb, double mutProb) {
        Loader loader = new Loader(definitionFile);
        loader.readFile();
        dimension = loader.getDimension();
        capacity = loader.getCapacity();
        minSpeed = loader.getMinSpeed();
        maxSpeed = loader.getMaxSpeed();
        cities = loader.getCities();
        items = loader.getItems();
        distances = new double[dimension][dimension];
        groupedItems = new Integer[dimension][];
        createDistancesArray();
        createGroupedItemsArray();

        coefficient = (maxSpeed - minSpeed) / capacity;

        this.popSize = popSize;
        this.numOfGeners = numOfGeners;
        this.tournamentSize = tournamentSize;
        this.crossProb = crossProb;
        this.mutProb = mutProb;
    }

    public void createDistancesArray() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                double distance;
                if (i == j) {
                    distance = 0;
                } else {
                    distance = Math.sqrt(Math.abs(cities[i][1] - cities[j][1])
                            + Math.abs(cities[i][2] - cities[j][2]));
                }
                distances[i][j] = distance;
                distances[j][i] = distance;//redundant
                //mozna zmienic tablicę na niesymetryczna, zeby zajmowala mniej miejsca
            }
        }
    }

    public void createGroupedItemsArray() {
        ArrayList<Integer>[] groupedItemsList = new ArrayList[dimension];
        for(int i = 0; i < dimension; i++) {
            groupedItemsList[i] = new ArrayList<>();
        }
        for(int i = 0; i < items.length; i++) {
            groupedItemsList[items[i][3] - 1].add(items[i][0] - 1);
        }
        for(int i = 0; i < dimension; i++) {
            groupedItems[i] = new Integer[groupedItemsList[i].size()];
            for (int j = 0; j < groupedItemsList[i].size(); j++) {
                groupedItems[i][j] = groupedItemsList[i].get(j);
            }
        }
    }

    public void initialize() {
        for (int i = 0; i < popSize; i++) {
            population.add(generateRandomInd());
        }
    }

    public Individual generateRandomInd() {
        int[] route = new int[dimension + 1];
        ArrayList<Integer> routeList = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            routeList.add(i);
        }
        Collections.shuffle(routeList);
        for (int i = 0; i < dimension; i++) {
            route[i] = routeList.get(i);
        }
        route[dimension] = route[0];
        Individual ind = new Individual(route, distances, items, groupedItems, maxSpeed, coefficient, capacity);

        return ind;
    }


    public String evolve() {
        String measures = "", archivePrint = "";
        StringBuilder sBMeasures = new StringBuilder(measures);
        StringBuilder sBArchivePrint = new StringBuilder(archivePrint);

        initialize();
        ArrayList<Individual> offspring;
        ArrayList<Individual> nextGeneration;
        for(int generation = 1; generation < numOfGeners; generation++) {
            paretoFronts = frontGenerator(population);
            assignRank();
            crowdingDistanceSetter();
            offspring = matingPool();
            population.addAll(offspring);
            paretoFronts = frontGenerator(population);
            assignRank();
            crowdingDistanceSetter();
            nextGeneration = new ArrayList<>();
            int i = 0;
            while(nextGeneration.size() < popSize) {
                if(paretoFronts.get(i).size() < popSize - nextGeneration.size()) {
                    nextGeneration.addAll(paretoFronts.get(i));
                    i++;
                }
                else {
                    sortFront(i);
                    while(nextGeneration.size() < popSize) {
                        nextGeneration.add(paretoFronts.get(i).get(0));
                        paretoFronts.get(i).remove(paretoFronts.get(i).get(0));
                    }
                }
            }
            population = nextGeneration;
        }
//        sBMeasures.append(ED_measure(paretoFronts.get(0)) + ", " + PFS_measure() + ", " + HV_measure());
        sBMeasures.append("\n");
//        sBArchivePrint.append(printPF(archive, sBArchivePrint));
//        System.out.println(sBArchivePrint.toString());
        sBMeasures.append(sBArchivePrint.toString());
        measures = sBMeasures.toString();

        return measures;
    }

    public ArrayList<Individual> matingPool() {
        ArrayList<Individual> offspring = new ArrayList<>();
        for(int i = 0; i < popSize; i++) {
            Individual parent1 = tournament();
            Individual parent2 = tournament();
            int[][] children = crossingOver(parent1.getRoute(), parent2.getRoute());
            offspring.add(new Individual(mutation(children[0]), distances, items, groupedItems, maxSpeed,
                    coefficient, capacity));
            i++;
            if(i < popSize) {
                offspring.add(new Individual(mutation(children[1]), distances, items, groupedItems, maxSpeed,
                        coefficient, capacity));
            }
        }
        return offspring;
    }

    public Individual tournament() {
        Individual bestIndividual = population.get(0);//just any individual to initialize
        int bestRank = Integer.MAX_VALUE;
        Random rand = new Random();
        for(int i = 0; i < tournamentSize; i++) {
            Individual individual = population.get(rand.nextInt(popSize));
            int rank = individual.getRank();
            if (rank < bestRank) {
                bestRank = rank;
                bestIndividual = individual;
            }
            else if(rank == bestRank) {
                if(bestIndividual.getCrowdingDistance() < individual.getCrowdingDistance()) {
                    bestRank = rank;
                    bestIndividual = individual;
                }
            }
        }
        return bestIndividual;
    }

    public double countDistance(int[] route) {
        double distance = 0;
        for(int i = 0; i < route.length - 2; ) {
            distance += distances[i][++i];
        }
        distance += distances[route.length - 2][0];
        return distance;
    }

    public int[][] crossingOver(int[] parent1, int[] parent2) {
        int[] child1 = new int[parent1.length];
        int[] child2 = new int[parent1.length];
        if(Math.random() < crossProb) {
            int crossPoint = new Random().nextInt(parent1.length);
            for(int i = 0; i < crossPoint; i++) {
                child1[i] = parent1[i];
                child2[i] = parent2[i];
            }
            //rest for parent1
            boolean used = false;
            int from = 0;
            for(int empty = crossPoint; empty < child1.length - 1;) {
                for(int j = 0; j < empty; j++) {
                    if(child1[j] == parent2[from]){
                        used = true;
                        break;
                    }
                }
                if(!used) {
                    child1[empty] = parent2[from];
                    empty++;
                }
                used = false;
                from++;
            }
            child1[parent1.length - 1] = child1[0];

            //rest for parent2
            used = false;
            from = 0;
            for(int empty = crossPoint; empty < child2.length - 1;) {
                for(int j = 0; j < empty; j++) {
                    if(child2[j] == parent1[from]){
                        used = true;
                        break;
                    }
                }
                if(!used) {
                    child2[empty] = parent1[from];
                    empty++;
                }
                used = false;
                from++;
            }
            child2[parent2.length - 1] = child2[0];
        }
        else {
            child1 = parent1;
            child2 = parent2;
        }
        return new int[][] {child1, child2};
    }

    public int[] mutation(int[] route) {
        for(int i = 0; i < route.length - 2; i++) {
            if(Math.random() < mutProb) {
                int swapIndex = new Random().nextInt(route.length - 1);
                int temp = route[i];
                route[i] = route[swapIndex];
                route[swapIndex] = temp;
            }
            route[route.length - 1] = route[0];
        }
        return route;
    }

    public int getNumOfGeners() {
        return numOfGeners;
    }

//    public String statistics(int pop_number) {
//        double minFitness = 2.147483647E9D;
//        double maxFitness = 0.0D;
//        double avgDuration = 0.0D;
//
//
//        for(int i = 0; i < popSize; i++) {
//            Individual ind = population.get(i);
//            double fitness = ind.getFitness();
//            if (fitness < minFitness) {
//                minFitness = fitness;
//            }
//
//            if (fitness > maxFitness) {
//                maxFitness = fitness;
//            }
//
//            avgDuration += fitness;
//        }
//
//        if (this.maxOfAll < maxFitness) {
//            this.maxOfAll = maxFitness;
//        }
//
//        return pop_number + ", " + minFitness + "," + maxFitness + "," + avgDuration / (double)popSize;
//    }

    public ArrayList<ArrayList<Individual>> frontGenerator(ArrayList<Individual> group) {

        ArrayList<ArrayList<Individual>> fronts = new ArrayList<>();
//        paretoFronts.clear();
        fronts.add(new ArrayList<>());
        for(int i = 0; i < group.size(); i++) {
            for (int j = 0; j < fronts.size(); j++) {
                ArrayList<Individual> currentFront = fronts.get(j);
                if (currentFront.size() == 0) {
                    currentFront.add(group.get(i));
                    break;
                } else {
                    for (int k = 0; k < currentFront.size(); k++) {
                        int compared = group.get(i).compareTo(currentFront.get(k));
                        if ((compared == 0) && (k == currentFront.size() - 1)) {
                            currentFront.add(group.get(i));
                            if(i < group.size() - 1) {
                                i++;
                                j = -1;
                            }else {
                                j = fronts.size();
                            }
                            break;
                        } else if (compared == -1) {
                            //zamiana miejsc
                            ArrayList<Individual> betterFront = new ArrayList<>();
                            betterFront.add(group.get(i));
                            for(int z = 0; z < k; ) {
                                betterFront.add(currentFront.get(z));
                                currentFront.remove(z);
                                k--;
                            }
                            for(int z = 1; z < currentFront.size(); z++) {
                                if(group.get(i).compareTo(currentFront.get(z)) == 0) {
                                    betterFront.add(currentFront.get(z));
                                    currentFront.remove(z);
                                    z--;
                                }
                            }
                            fronts.add(j, betterFront);
                            if(i < group.size() - 1) {
                                i++;
                                j = -1;
                            }else {
                                j = fronts.size();
                            }
                            break;
                        } else if (compared == 1) {
                            //nowy front
                            if (fronts.size() < j + 2) {
                                fronts.add(new ArrayList<>());
                                fronts.get(j + 1).add(group.get(i));
                                if(i < group.size() - 1) {
                                    i++;
                                    j = -1;
                                }else {
                                    j = fronts.size();
                                }
                                break;
                            }
                            else {
                                break;
                            }
                        }
                    }
                }
            }
        }
//        crowdingDistanceSetter();
//
//        for(ArrayList<Individual> a : paretoFronts) {
//            System.out.println("*************************");
//            for(Individual i : a) {
//                i.toString();
//                System.out.println("CrowdingDistance: " + i.getCrowdingDistance());
//            }
//        }
//        return dataGenerator();
        return fronts;
    }

    public void assignRank() {
        for(int i = 0; i < paretoFronts.size(); i++) {
            for(int j = 0; j < paretoFronts.get(i).size(); j++) {
                paretoFronts.get(i).get(j).setRank(i);
            }
        }
    }

    public void sortFront(int i) {
        Collections.sort(paretoFronts.get(i), new CrowdingDistanceComparator());
    }

    public void objectiveSorting() {
        for(int i = 0; i < paretoFronts.size(); i++) {
            Collections.sort(paretoFronts.get(i), new ObjectiveFrontComparator());
        }
    }

    public void crowdingDistanceSetter() {
        objectiveSorting();
        for(int i = 0; i < paretoFronts.size(); i++) {
            paretoFronts.get(i).get(0).setCrowdingDistance(Double.POSITIVE_INFINITY);
            paretoFronts.get(i).get(paretoFronts.get(i).size() - 1).setCrowdingDistance(Double.POSITIVE_INFINITY);

            for(int j = 1; j < paretoFronts.get(i).size() - 1; j++) {
                Individual currentInd = paretoFronts.get(i).get(j);
                double a = Math.abs(paretoFronts.get(i).get(j + 1).getFitnessTime()
                        - paretoFronts.get(i).get(j - 1).getFitnessTime());
                double b = Math.abs(paretoFronts.get(i).get(0).getFitnessWage()
                        - paretoFronts.get(i).get(paretoFronts.get(i).size() - 1).getFitnessWage());
                currentInd.setCrowdingDistance(a * b);
            }
        }
    }
}
