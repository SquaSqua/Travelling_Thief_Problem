import java.util.Comparator;

public class CrowdingDistanceComparator  implements Comparator<Individual> {
    @Override
    public int compare(Individual o1, Individual o2) {
        return Double.compare(o1.getCrowdingDistance(), o2.getCrowdingDistance());
    }
}
