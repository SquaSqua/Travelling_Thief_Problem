public class Runner {
    public static void main(String[] args) {
        Evolution population = new Evolution("src/definitionFiles/trivial_0.ttp",100 , 100);
        population.evolve();
    }
}
