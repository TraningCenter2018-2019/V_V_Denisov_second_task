package core.parser.dom;

import core.parser.Parser;
import core.parser.TypeReference;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class DOMParser implements Parser {
    private DOMValue root;

    @Override
    public <T> T parseString(String toParse, Class<T> type)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        T obj = (T) type.getConstructors()[0].newInstance();
        return obj;
    }

    @Override
    public <T> T parseString(String toParse, TypeReference<T> ref) throws Exception {
        return null;
    }

    DOMValue parseString(String toParse) {
        //throw new NotImplementedException();
        DOMObject domObject = new DOMObject();
        domObject.setProperties(new ArrayList<DOMProperty>() {
            {
                this.add(new DOMProperty("key", new DOMValue("value")));
            }
        });
        return new DOMValue(domObject);
    }
}
