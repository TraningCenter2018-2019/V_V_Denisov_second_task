package core.parser.dom;

import core.parser.Parser;

import java.util.ArrayList;

public class DOMParser<T> implements Parser<T> {
    private DOMValue root;

    @Override
    public T parseString(String toParse, Class<T> type) {
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
