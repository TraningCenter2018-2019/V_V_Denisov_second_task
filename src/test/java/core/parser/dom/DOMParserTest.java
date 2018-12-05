package core.parser.dom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import testutil.ResourceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class DOMParserTest {

    @Test
    void parserMustReadOnlyOneProperty() throws IOException {
        //arrange
        DOMParser domParser = new DOMParser();
        String json = ResourceManager.getResourceString("onefield.json");
        DOMObject rightNode = new DOMObject();
        rightNode.setProperties(new ArrayList<DOMProperty>() {
            {
                this.add(new DOMProperty("key", new DOMValue("value")));
            }
        });

        //act
        DOMObject node = null;
        try {
            node = domParser.parseString(json).getObjectValue();
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //node = rightNode;

        //assert
        Assertions.assertNotNull(node,
                "Узел не должен быть null");
        List<DOMProperty> properties = node.getProperties();
        Assertions.assertNotNull(properties,
                "Узел должен иметь список свойств");
        Assertions.assertEquals(1, properties.size(),
                "Должно быть считано ровно одно свойство");
        DOMProperty property = properties.get(0);
        Assertions.assertNotNull(property.getKey(),
                "Ключ поля не был считан");
        Assertions.assertEquals("key", property.getKey(),
                "Ключ поля был считан не верно");
        DOMValue value = property.getValue();
        Assertions.assertNotNull(value,
                "Значение поля не было считано");
        Assertions.assertEquals("value", value.getStringValue(),
                "Значение поля было считано не верно");
        Assertions.assertNull(value.getListValue(),
                "Значение поля было считано как список, а должно быть считано как строка");
        Assertions.assertNull(value.getObjectValue(),
                "Значение поля было считано как объект, а должно быть считано как строка");
    }

    @Test
    void parserMustReadMultipleProperties() throws IOException {
        //arrange
        DOMParser domParser = new DOMParser();
        String json = ResourceManager.getResourceString("twofields.json");
        DOMProperty firstProperty = new DOMProperty("key", new DOMValue("value"));
        DOMProperty secondProperty = new DOMProperty("second_key", new DOMValue("second_value"));
        List<DOMProperty> nodeList = new ArrayList<DOMProperty>() {
            {
                this.add(firstProperty);
                this.add(secondProperty);
            }
        };
        DOMObject rightNode = new DOMObject(nodeList);

        //act
        DOMObject node = null;
        try {
            node = domParser.parseString(json).getObjectValue();
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //node = rightNode;

        //assert
        Assertions.assertNotNull(node,
                "Узел не должен быть null");
        List<DOMProperty> properties = node.getProperties();
        Assertions.assertNotNull(properties,
                "Узел должен иметь список свойств");
        Assertions.assertEquals(2, properties.size(),
                "Должно быть считано два свойства");
        Assertions.assertEquals(rightNode.getProperties(), properties,
                "Свойства считаны некорректно");
    }

    @Test
    void parserMustReadListAsValue() throws IOException {
        //arrange
        DOMParser domParser = new DOMParser();
        String json = ResourceManager.getResourceString("listfield.json");
        DOMValue domValue = new DOMValue(new ArrayList<DOMValue>() {
            {
                new DOMValue("first");
                new DOMValue("second");
                new DOMValue("third");
                new DOMValue(new ArrayList<DOMValue>() {
                    {
                        new DOMValue("inner1");
                        new DOMValue("inner2");
                    }
                });
            }
        });

        //act
        DOMValue node = null;
        try {
            node = domParser.parseString(json);
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //node = domValue;

        //assert
        Assertions.assertNotNull(node,
                "Список не должен быть null");
        List<DOMValue> values = node.getListValue();
        Assertions.assertNotNull(values,
                "Список должен иметь список значений");
        Assertions.assertEquals(domValue.getListValue().size(), values.size(),
                "В списке должно быть 4 значения");
        for (int i = 0; i < domValue.getListValue().size(); i++) {
            Assertions.assertEquals(domValue.getListValue().get(i), values.get(i),
                    String.format("Значение под номером %d считано неверно", i));
        }
        Assertions.assertEquals(domValue, node,
                "Свойства считаны некорректно");
    }

    @Test
    void parserMustReadStringAsValue() throws IOException {
        //arrange
        DOMParser domParser = new DOMParser();
        String json = ResourceManager.getResourceString("listfield.json");
        DOMValue domValue = new DOMValue(json);

        //act
        DOMValue node = null;
        try {
            node = domParser.parseString(json);
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //node = domValue;

        //assert
        Assertions.assertNotNull(node,
                "Значение не должно быть null");
        String value = node.getStringValue();
        Assertions.assertNotNull(value,
                "Значение не было считано");
        Assertions.assertEquals(json, value,
                "Строка была считана некорректно");
    }

    @Test
    void parserMustReadObjectAsValue() throws IOException {
        //arrange
        DOMParser domParser = new DOMParser();
        String json = ResourceManager.getResourceString("objectasvalue.json");
        DOMValue domValue = new DOMValue(new DOMObject(new ArrayList<DOMProperty>(){
            {
                new DOMProperty("string_key", new DOMValue("string_value"));
                new DOMProperty("object_key", new DOMValue(new DOMObject(new ArrayList<DOMProperty>(){
                    {
                        new DOMProperty("inner_string_key", new DOMValue("inner_value"));
                        new DOMProperty("inner_list_key", new DOMValue(new ArrayList<DOMValue>(){
                            {
                                new DOMValue(new DOMObject(new ArrayList<DOMProperty>(){
                                    {
                                        new DOMProperty("key1", new DOMValue("val1"));
                                        new DOMProperty("key2", new DOMValue("val2"));
                                    }
                                }));
                            }
                        }));
                    }
                })));
            }
        }));

        //act
        DOMValue node = null;
        try {
            node = domParser.parseString(json);
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //node = domValue;

        //assert
        Assertions.assertNotNull(node,
                "Значение не должно быть null");
        Assertions.assertEquals(domValue.getObjectValue(), node.getObjectValue(),
                "JSON был считан некорректно");
    }
}
