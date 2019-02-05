package core.parser.dom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import testutil.ResourceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for DOM parser.
 */
class DOMParserTest {

    /**
     * The test checks whether the parser can read json correctly with a single field.
     *
     * @throws IOException error reading test resources.
     */
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

        //assert
        Assertions.assertNotNull(node,
                "Node can't null");
        List<DOMProperty> properties = node.getProperties();
        Assertions.assertNotNull(properties,
                "Node must have property list");
        Assertions.assertEquals(1, properties.size(),
                "Only one property must be read");
        DOMProperty property = properties.get(0);
        Assertions.assertNotNull(property.getKey(),
                "The field key was not read");
        Assertions.assertEquals("key", property.getKey(),
                "The field key was not read correctly");
        DOMValue value = property.getValue();
        Assertions.assertNotNull(value,
                "The field value was not read");
        Assertions.assertEquals("value", value.getStringValue(),
                "The field value was not read correctly");
        Assertions.assertNull(value.getListValue(),
                "The value of the field was read as a list, but should be read as a string");
        Assertions.assertNull(value.getObjectValue(),
                "The value of the field was read as an object, but should be read as a string");
    }

    /**
     * The test checks whether the parser can correctly read json with several fields.
     *
     * @throws IOException error reading test resources.
     */
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

        //assert
        Assertions.assertNotNull(node,
                "Node can't null");
        List<DOMProperty> properties = node.getProperties();
        Assertions.assertNotNull(properties,
                "Node must have a list of properties");
        Assertions.assertEquals(2, properties.size(),
                "Two properties must be read");
        Assertions.assertEquals(rightNode.getProperties(), properties,
                "Properties read incorrectly");
    }

    /**
     * The test checks whether the parser can correctly read json with the list as a field.
     *
     * @throws IOException error reading test resources.
     */
    @Test
    void parserMustReadListAsValue() throws IOException {
        //arrange
        DOMParser domParser = new DOMParser();
        String json = ResourceManager.getResourceString("listfield.json");
        DOMValue domValue = new DOMValue(new ArrayList<DOMValue>() {
            {
                this.add(new DOMValue("first"));
                this.add(new DOMValue("second"));
                this.add(new DOMValue("third"));
                this.add(new DOMValue(new ArrayList<DOMValue>() {
                    {
                        this.add(new DOMValue("inner1"));
                        this.add(new DOMValue("inner2"));
                    }
                }));
            }
        });

        //act
        DOMValue node = null;
        try {
            node = domParser.parseString(json);
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //assert
        Assertions.assertNotNull(node,
                "List must not be null");
        List<DOMValue> values = node.getListValue();
        Assertions.assertNotNull(values,
                "The list must have a list of values");
        Assertions.assertEquals(domValue.getListValue().size(), values.size(),
                "The list should have 4 values");
        for (int i = 0; i < domValue.getListValue().size(); i++) {
            Assertions.assertEquals(domValue.getListValue().get(i), values.get(i),
                    String.format("Value numbered %d read incorrectly", i));
        }
        Assertions.assertEquals(domValue, node,
                "Properties read incorrectly");
    }

    /**
     * The test checks whether the parser can correctly consider
     * json as consisting of one line. (JSON thus considered valid).
     *
     * @throws IOException error reading test resources.
     */
    @Test
    void parserMustReadStringAsValue() throws IOException {
        //arrange
        DOMParser domParser = new DOMParser();
        String json = ResourceManager.getResourceString("string.json");
        DOMValue domValue = new DOMValue(json.substring(1, json.length() - 1));

        //act
        DOMValue node = null;
        try {
            node = domParser.parseString(json);
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //assert
        Assertions.assertNotNull(node,
                "Value should not be null");
        String value = node.getStringValue();
        Assertions.assertNotNull(value,
                "Value has not been read");
        Assertions.assertEquals(domValue.getStringValue(), value,
                "The string was read incorrectly");
    }

    /**
     * The test checks whether the parser can correctly read json with the object as the value.
     *
     * @throws IOException error reading test resources.
     */
    @Test
    void parserMustReadObjectAsValue() throws IOException {
        //arrange
        DOMParser domParser = new DOMParser();
        String json = ResourceManager.getResourceString("objectasvalue.json");
        DOMValue domValue = new DOMValue(new DOMObject(new ArrayList<DOMProperty>() {
            {
                this.add(new DOMProperty("string_key", new DOMValue("value")));
                this.add(new DOMProperty("object_key", new DOMValue(new DOMObject(new ArrayList<DOMProperty>() {
                    {
                        this.add(new DOMProperty("inner_string_key", new DOMValue("inner_value")));
                        this.add(new DOMProperty("inner_list_key", new DOMValue(new ArrayList<DOMValue>() {
                            {
                                this.add(new DOMValue(new DOMObject(new ArrayList<DOMProperty>() {
                                    {
                                        this.add(new DOMProperty("key1", new DOMValue("val1")));
                                        this.add(new DOMProperty("key2", new DOMValue("val2")));
                                    }
                                })));
                            }
                        })));
                    }
                }))));
            }
        }));

        //act
        DOMValue node = null;
        try {
            node = domParser.parseString(json);
        } catch (Exception e) {
            Assertions.fail(e);
        }

        //assert
        Assertions.assertNotNull(node,
                "Value should not be null");
        Assertions.assertEquals(domValue.getObjectValue(), node.getObjectValue(),
                "JSON was read incorrectly");
    }
}
