package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TableSchema implements Serializable {

    private static final long serialVersionUID=1L;

    private final List<Column> columns;

    public TableSchema(List<Column> columns){
        this.columns=new ArrayList<>(columns);
    }

    public List<Column> getColumns(){return columns;}

    public Column getPrimaryKey(){
        for (Column c: columns){
            if (c.isPrimaryKey()) return c;
        }
        return null;
    }

    public Column byName(String name){
        for (Column c: columns){
            if (c.getName(). equalsIgnoreCase(name)) return c;
        }
        return null;
    }
}
