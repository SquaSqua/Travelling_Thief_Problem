import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Individual {
    private int[] route;
    private int[] packingPlan;
    private double fitness;

    Individual(int[] route) {
        this.route = route;
        packingPlan = null;
    }

    Individual(int dimension) {
        int[] route = new int[dimension + 1];
        ArrayList<Integer> routeList = new ArrayList<>();
        for (int i = 0; i < dimension; i++) {
            routeList.add(i);
        }
        Collections.shuffle(routeList);
        for (int i = 0; i < dimension; i++) {
            route[i] = routeList.get(i).shortValue();
        }
        route[dimension] = route[0];
        this.route = route;

        PackingPlanGenerator.setPackingPlanAndFitness(this);
    }

    void mutate(double mutProb) {
        for(int i = 0; i < route.length - 2; i++) {
            if(Math.random() < mutProb) {
                int swapIndex = new Random().nextInt(route.length - 1);
                int temp = route[i];
                route[i] = route[swapIndex];
                route[swapIndex] = temp;
            }
            route[route.length - 1] = route[0];
        }
        PackingPlanGenerator.setPackingPlanAndFitness(this);
    }

    int[][] crossover(int[] route2, double crossProb) {
        int[] routeWithoutLastCity, routeWithoutLastCity2;
        int[][] children = new int[2][];
        if(Math.random() < crossProb) {
            routeWithoutLastCity = Arrays.copyOf(route, route.length - 1);
            routeWithoutLastCity2 = Arrays.copyOf(route2, route2.length - 1);
            children = crossingOver(routeWithoutLastCity, routeWithoutLastCity2);
            children[0] = addLastCity(children[0]);
            children[1] = addLastCity(children[1]);
        }
        else {
            children[0] = route;
            children[1] = route2;
        }
        return children;
}

    private int[][] crossingOver(int[] gens1, int[] genes2) {
            int[] child1 = new int[gens1.length];
            int[] child2 = new int[genes2.length];

            fillWithInitialValues(child1);
            fillWithInitialValues(child2);

            int beginningValue = gens1[0];
            int currentInd = 0;

            boolean isSwapTurn = false;
            while (true) {
                assignGens(isSwapTurn, currentInd, gens1, genes2, child1, child2);
                if (gens1[currentInd] == genes2[currentInd]) {
                    isSwapTurn = !isSwapTurn;
                }
                currentInd = findIndexOfaValue(genes2[currentInd], gens1);
                if (genes2[currentInd] == beginningValue) {
                    assignGens(isSwapTurn, currentInd, gens1, genes2, child1, child2);
                    currentInd = findFirstEmpty(child1);
                    if (currentInd == -1) {
                        break;
                    }
                    beginningValue = gens1[currentInd];
                    isSwapTurn = !isSwapTurn;
                }
        }
        return new int[][]{child1, child2};
    }

    private int findFirstEmpty(int[] route) {
        int firstEmpty = -1;
        for (int i = 0; i < route.length; i++) {
            if (route[i] == -1) {
                firstEmpty = i;
                break;
            }
        }
        return firstEmpty;
    }

    private void fillWithInitialValues(int[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = -1;
        }
    }

    private int findIndexOfaValue(int value, int[] route) {
        int index = -1;
        for (int i = 0; i < route.length; i++) {
            if (route[i] == value) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void assignGens(boolean isSwapTurn, int currentInd, int[] route1, int[] route2, int[] child1, int[] child2) {
        if (!isSwapTurn) {
            child1[currentInd] = route1[currentInd];
            child2[currentInd] = route2[currentInd];
        } else {
            child1[currentInd] = route2[currentInd];
            child2[currentInd] = route1[currentInd];
        }
    }

    private int[] addLastCity(int[] child) {
        int[] ch = new int[child.length + 1];
        System.arraycopy(child, 0, ch, 0, child.length);
        ch[ch.length - 1] = ch[0];
        return ch;
    }

    int[] getRoute() {
        return route;
    }

    double getFitness() { return fitness; }

    int[] getPackingPlan() {
        return packingPlan;
    }

    void setPackingPlan(int[] packingPlan) {
        this.packingPlan = packingPlan;
    }

    void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("[");
        for(int i = 0; i < route.length - 1; i++) {
            result.append(route[i]).append(", ");
        }
        result.append(route[route.length - 1]).append("]");
        return result.toString();
    }
}
