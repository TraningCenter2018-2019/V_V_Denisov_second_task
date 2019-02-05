package testmodel.hierarchy;

public class Cat extends Mammal {
    String name;
    String breed;

    public Cat(String name, String breed, float weight) {
        this.name = name;
        this.breed = breed;
        this.weight = weight;
    }

    public Cat() {
    }
}
