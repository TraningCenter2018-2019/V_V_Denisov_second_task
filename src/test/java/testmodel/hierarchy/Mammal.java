package testmodel.hierarchy;

import com.fasterxml.jackson.annotation.JsonSubTypes;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "animalInfo")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "wolf", value = Wolf.class),
        @JsonSubTypes.Type(name = "cat", value = Cat.class)
})
public abstract class Mammal {
    protected float weight;
}
