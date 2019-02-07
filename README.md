# JSON-deserializer

### Getting started

To start working with the deserializer, you need to create an object of the class **`ObjectMapper`** as follows:
```
ObjectMapper mapper = new ObjectMapper(new DOMParser());
```
After that, you can use the `mapper` object to deserialize json into objects of the passed types. You can do this in two ways.
The first way is to use the method `readObjectFromJSON(String json, Class<T> clazz)`:
```
Model model = mapper.readObjectFromJSON(jsonString, Model.class);
```
This method allows you to create a simple object from the json-string that describes it. This method is not suitable for cases when json describes lists of objects and for cases when an object has generic types.
For such cases there is a second way - use the method `readObjectFromJSON(String json, TypeReference<T> ref)`:
```
//Example of creating a list:
List<Model> model = mapper.readObjectFromJSON(jsonString, new TypeReference<List<Model>>(){});

//An example of creating an object using generic types:
GenericModel<Model> genericModel = mapper.readObjectFromJSON(jsonString, new TypeReference<GenericModel<Model>>(){});
```
Pay attention to the creation of an object **`TypeReference`**:
```
new TypeReference<GenericModel<Model>>() {}
```
**The curly brackets at the end are necessary** for the program to be able to determine the type of object being created.

### Principle of operation
The deserializer is based on a parser (a subroutine that reads a text file and parses it into components), which builds a DOM tree based on the transmitted json string. When reading large json, it can consume a lot of memory.

### Restrictions
The project currently does not support working with json, which contain numeric or `null` values as variable values.
Also, the deserializer will not be able to deserialize an object that uses an object that has its own set of generic types as a generic type. An example of an object that **will not** be deserialized: `Model<Model<Object>>`
