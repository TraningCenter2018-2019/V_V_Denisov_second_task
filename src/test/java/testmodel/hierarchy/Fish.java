package testmodel.hierarchy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "fishType")
@JsonSubTypes({
        @JsonSubTypes.Type(name = "guppy", value = Guppy.class),
        @JsonSubTypes.Type(name = "angel", value = Angelfish.class)
})
public abstract class Fish {
    protected String color;
}
