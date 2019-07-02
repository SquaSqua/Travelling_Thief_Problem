import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

class Evolution {

    private int popSize;
    private int numOfGeners;
    private double crossProb;
    private double mutProb;
    private int tournamentSize;

    private double[] generationData, minData, avgData, maxData;

    private ArrayList<Individual> population = new ArrayList<>();

    private double maxOfAll = -Double.MAX_VALUE;

    Evolution(int popSize, int numOfGeners, int tournamentSize, double crossProb, double mutProb) {
        this.popSize = popSize;
        this.numOfGeners = numOfGeners;
        this.tournamentSize = tournamentSize;
        this.crossProb = crossProb;
        this.mutProb = mutProb;

        this.generationData = new double[numOfGeners];
        this.minData = new double[numOfGeners];
        this.avgData = new double[numOfGeners];
        this.maxData = new double[numOfGeners];
    }

    void run(String name) {
        initialize();
        try {
            PrintWriter out = new PrintWriter(name);
//            out.println("nr populacji, najgorszy osobnik, najlepszy osobnik, sredni osobnik");
            for (int i = 0; i < numOfGeners; i++) {
                out.println(evolve(i));
            }
            System.out.println("Najlepszy w ewolucji: " + maxOfAll);
            out.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Nie da się utworzyć pliku!");
        }

        XYChart chart = getChart();
        new SwingWrapper<>(chart).displayChart();
    }

    private XYChart getChart() {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(1000).height(600).title(getClass().getSimpleName()).xAxisTitle("Numer generacji").yAxisTitle("Zarobek (fitness)").build();


        //Series
        chart.addSeries("max", generationData, maxData);
        chart.addSeries("avg", generationData, avgData);
        chart.addSeries("min", generationData, minData);

        return chart;
    }

    private void initialize() {
        for (int i = 0; i < popSize; i++) {
            population.add(new Individual(DataFromFile.getDimension()));
        }
    }

    private String evolve(int generCounter) {
        ArrayList<Individual> nextGeneration = new ArrayList<>();
        for(int i = 0; i < popSize; i++) {// - 1
            int[][] children = tournament().crossover(tournament().getRoute(), crossProb);
            Individual child1 = new Individual(children[0]);
            child1.mutate(mutProb);
            nextGeneration.add(child1);
            if(nextGeneration.size() < popSize) {
                Individual child2 = new Individual(children[1]);
                child2.mutate(mutProb);
                nextGeneration.add(child2);
                i++;
            }
        }
        population = nextGeneration;
        return statistics(generCounter);
    }

    private Individual tournament() {

        double bestFitness = -Double.MAX_VALUE;
        int indexOfBest = -1;
        for(int i = 0; i < tournamentSize; i++) {
            int indexOfNew = new Random().nextInt(popSize);
            Individual current = population.get(indexOfNew);
            double fitness = current.getFitness();
            if(fitness > bestFitness) {
                bestFitness = fitness;
                indexOfBest = indexOfNew;
            }
        }
        return population.get(indexOfBest);
    }

    Individual roulette() {
        double sumedFitensses = 0;
        for(Individual i : population) {
            sumedFitensses += i.getFitness();
        }
        Random random = new Random();
        double drawn = (-1) * random.nextInt((int)(Math.floor(Math.abs(sumedFitensses)))) + random.nextDouble();
        double partialSum = 0;
        int i = 0;
        for( ; partialSum < drawn; i++) {
            partialSum += population.get(i).getFitness();
        }
        return population.get(i);
    }

    private String statistics(int pop_number) {
        double minFitness = Double.MAX_VALUE;
        double maxFitness = -Double.MAX_VALUE;
        double avgDuration = 0.0D;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);
        df.setMinimumFractionDigits(3);
        df.setGroupingUsed(false);

        for(int i = 0; i < popSize; i++) {
            Individual ind = population.get(i);
            double fitness = ind.getFitness();
            if (fitness < minFitness) {
                minFitness = fitness;
            }

            if (fitness > maxFitness) {
                maxFitness = fitness;
            }

            avgDuration += fitness;
        }

        if (this.maxOfAll < maxFitness) {
            this.maxOfAll = maxFitness;
        }

        generationData[pop_number] = pop_number;
        minData[pop_number] = minFitness;
        avgData[pop_number] = avgDuration / (double)popSize;
        maxData[pop_number] = maxFitness;

        return pop_number + ", " + df.format(minFitness) + "," + df.format(avgDuration / (double)popSize) + "," + df.format(maxFitness);
    }
}
