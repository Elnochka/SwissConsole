

public class ListString {
    private String longest;
    private String shortest;
    private int length;
    private double average;

    public String getLongest() {
        return longest;
    }

    public void setLongest(String longest) {
        this.longest = longest;
    }

    public String getShortest() {
        return shortest;
    }

    public void setShortest(String shortest) {
        this.shortest = shortest;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return "ListString{" +
                "longest='" + longest + '\'' +
                ", shortest='" + shortest + '\'' +
                ", length=" + length +
                ", average=" + average +
                '}';
    }

    public ListString() {
    }

    public ListString(String longest, String shortest, int length, double average) {

        this.longest = longest;
        this.shortest = shortest;
        this.length = length;
        this.average = average;
    }

}
