package core.parser;

import core.parser.dom.DOMParser;
import core.parser.sax.SAXParser;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import testmodel.*;
import testmodel.hierarchy.Cat;
import testmodel.hierarchy.Mammal;
import testmodel.hierarchy.Wolf;
import testutil.ResourceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Tests for both parser implementations.
 * 0 - DOMParser
 * 1 - SAXParser
 */
@RunWith(Parameterized.class)
public class ParserTest {
    /**
     * Parser for testing.
     */
    public Parser parser;

    /**
     * Tests use a specific parser implementation.
     *
     * @param parser parser implementation
     */
    public ParserTest(Parser parser) {
        try {
            this.parser = parser.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parser must deserialize object with one field.
     *
     * @throws IOException error reading test resources.
     */
    @Test
    public void parserMustDeserializeObjectWithOneField() throws IOException {
        //arrange
        String json = ResourceManager.getResourceString("testmodel/onefieldobject.json");
        OneFieldObject solution = new OneFieldObject();
        solution.setStringField("stringValue");

        //act
        OneFieldObject oneFieldObject = null;
        try {
            oneFieldObject = parser.parseString(json, OneFieldObject.class);
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //assert
        Assertions.assertNotNull(oneFieldObject,
                "Deserialized object must not be null");
        Assertions.assertEquals(solution.getStringField(), oneFieldObject.getStringField(),
                "Field is deserialized wrong way");
    }

    /**
     * The parser must deserialize an object with multiple fields.
     *
     * @throws IOException error reading test resources.
     */
    @Test
    public void parserMustDeserializeObjectWithMultipleFields() throws IOException {
        //arrange
        String json = ResourceManager.getResourceString("testmodel/multiplefieldsobject.json");
        MultipleFieldsObject solution = new MultipleFieldsObject();
        solution.setStringField1("stringValue1");
        solution.setStringField2("stringValue2");

        //act
        MultipleFieldsObject multipleFieldsObject = null;
        try {
            multipleFieldsObject = parser.parseString(json, MultipleFieldsObject.class);
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //assert
        Assertions.assertNotNull(multipleFieldsObject,
                "Deserialized object must not be null");
        Assertions.assertEquals(solution.getStringField1(), multipleFieldsObject.getStringField1(),
                "Field1 is deserialized wrong way");
        Assertions.assertEquals(solution.getStringField2(), multipleFieldsObject.getStringField2(),
                "Field2 is deserialized wrong way");
    }

    /**
     * The parser should deserialize an object with objects as values.
     *
     * @throws IOException error reading test resources.
     */
    @Test
    public void parserMustDeserializeObjectWithObjectField() throws IOException {
        //arrange
        String json = ResourceManager.getResourceString("testmodel/objectfieldobject.json");
        ObjectFieldObject solution = new ObjectFieldObject();
        solution.setStringField("stringValue");
        ObjectFieldObject.InnerClass innerClass = new ObjectFieldObject.InnerClass();
        innerClass.setInnerStringField("innerStringValue");
        solution.setInnerClassField(innerClass);

        //act
        ObjectFieldObject objectFieldObject = null;
        try {
            objectFieldObject = parser.parseString(json, ObjectFieldObject.class);
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //assert
        Assertions.assertNotNull(objectFieldObject,
                "Deserialized object must not be null");
        Assertions.assertEquals(solution.getStringField(), objectFieldObject.getStringField(),
                "Field is deserialized wrong way");
        Assertions.assertEquals(solution.getInnerClassField(), objectFieldObject.getInnerClassField(),
                "Object field is deserialized wrong way");
    }

    /**
     * The parser should serialize the object with the list as a value.
     *
     * @throws IOException error reading test resources.
     */
    @Test
    public void parserMustDeserializeObjectWithListField() throws IOException {
        //arrange
        String json = ResourceManager.getResourceString("testmodel/listfieldsobject.json");
        ListFieldObject solution = new ListFieldObject();
        solution.setStringList(new ArrayList<>());

        //act
        ListFieldObject listFieldObject = null;
        try {
            listFieldObject = parser.parseString(json, ListFieldObject.class);
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //assert
        Assertions.assertNotNull(listFieldObject,
                "Deserialized object must not be null");
        Assertions.assertEquals(solution.getStringList().size(), listFieldObject.getStringList().size(),
                "Sizes of lists are not equal");
        Assertions.assertEquals(solution.getStringList(), listFieldObject.getStringList(),
                "Lists are not equal");
    }

    /**
     * The parser must deserialize the list of objects.
     *
     * @throws IOException error reading test resources.
     */
    @Test
    public void parserMustDeserializeListOfObjects() throws IOException {
        //arrange
        String json = ResourceManager.getResourceString("testmodel/listofobjects.json");
        List<OneFieldObject> solution = new ArrayList<>();
        solution.add(new OneFieldObject("value1"));
        solution.add(new OneFieldObject("value2"));

        //act
        List<OneFieldObject> listFieldObject = null;
        try {
            listFieldObject = parser.parseString(json, new TypeReference<List<OneFieldObject>>() {
            });
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //assert
        Assertions.assertNotNull(listFieldObject,
                "Deserialized object must not be null");
        Assertions.assertEquals(solution.size(), listFieldObject.size(),
                "Sizes are not equal");
        Assertions.assertEquals(solution, listFieldObject,
                "Lists are not equal");
    }

    /**
     * Parser must deserialize object with dynamic type.
     *
     * @throws IOException error reading test resources.
     */
    @Test
    public void parserMustDeserializeGenericTypeObjects() throws IOException {
        //arrange
        String json = ResourceManager.getResourceString("testmodel/generictypeobject.json");
        GenericTypeObject<OneFieldObject> generic =
                new GenericTypeObject<>(new OneFieldObject("stringValue"));

        //act
        GenericTypeObject<OneFieldObject> genericResult = null;
        try {
            genericResult = parser.parseString(json, new TypeReference<GenericTypeObject<OneFieldObject>>() {
            });
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //assert
        Assertions.assertNotNull(genericResult,
                "Deserialized object must not be null");
        Assertions.assertEquals(generic.getValue(), genericResult.getValue(),
                "Values are ot equal");
    }

    /**
     * Parser must deserialize object with some dynamic types.
     *
     * @throws IOException error reading test resources.
     */
    @Test
    public void parserMustDeserializeMultiGenericTypeObjects() throws IOException {
        //arrange
        String json = ResourceManager.getResourceString("testmodel/multigenerictypeobject.json");
        MultiGenericTypeObject<OneFieldObject, ListFieldObject> generic =
                new MultiGenericTypeObject<>(new OneFieldObject("stringValue"),
                        new ListFieldObject(new ArrayList<>()));

        //act
        MultiGenericTypeObject<OneFieldObject, ListFieldObject> genericResult = null;
        try {
            genericResult = parser.parseString(json,
                    new TypeReference<MultiGenericTypeObject<OneFieldObject, ListFieldObject>>() {
                    });
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //assert
        Assertions.assertNotNull(genericResult,
                "Deserialized object must not be null");
        Assertions.assertEquals(generic.getValue(), genericResult.getValue(),
                "Values are not equal");
        Assertions.assertEquals(generic.getValue2(), genericResult.getValue2(),
                "Values are not equal");
    }


    /**
     * Parser must deserialize object with some dynamic types.
     *
     * @throws IOException error reading test resources.
     */
//    @Test fixme enable
    public void parserMustDeserializeInheritedObjects() throws IOException {
        //arrange
        String json = ResourceManager.getResourceString("testmodel/hierarchy/hierarchy.json");
        List<Mammal> result = new ArrayList<>(1);
        result.add(new Wolf(12345.12345, 123, 12.3f));
        result.add(new Cat("Tom", "kitten", 1.3f));

        //act
        List<Mammal> parsingResult = null;
        try {
            parsingResult = parser.parseString(json,
                    new TypeReference<List<Mammal>>() {
                    });
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //assert
        Assertions.assertNotNull(parsingResult,
                "Deserialized object must not be null");
        Assertions.assertEquals(result.size(), parsingResult.size(),
                "Lists must have equal size");
        Assertions.assertEquals(result.get(0).getClass(), parsingResult.get(0).getClass(),
                "Values are not equal");
        //todo: some asserts here
    }

    /**
     * The method returns parser implementations.
     *
     * @return parser implementations
     */
    @Parameterized.Parameters
    public static Collection<Object[]> instancesToTest() {
        return Arrays.asList(
                new Object[]{new DOMParser()},
                new Object[]{new SAXParser()}
        );
    }
}
