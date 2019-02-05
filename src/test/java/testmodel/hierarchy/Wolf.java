package testmodel.hierarchy;

public class Wolf extends Mammal {
    double speed;
    int numberOfRabbitsKilled;

    public Wolf(double speed, int numberOfRabbitsKilled, float weight) {
        this.speed = speed;
        this.numberOfRabbitsKilled = numberOfRabbitsKilled;
        this.weight = weight;
    }

    public Wolf() {
    }
}
