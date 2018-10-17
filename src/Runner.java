public class Runner {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Evolution population = new Evolution("src/definitionFiles/trivial_0.ttp",1 ,100);
        population.evolve();
        System.out.println(System.currentTimeMillis() - start);
    }
}