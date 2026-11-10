package entity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class Row implements Serializable {
    
    private static final long serialVersionUID=1L;

    private final Map<String, Object> values=new LinkedHashMap<>();

    public Object get(String column){return values.get(column);}
    public void put(String column, Object value){values.put(column, value);}
    public boolean has(String column){return values.containsKey(column);}
    public Map<String, Object> asMap() {return values;}
}
