package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Table implements Serializable, Iterable<Row> {
    
    private static final long serialVersionUID=1L;

    private final String name;
    private final TableSchema schema;
    private final List<Row> rows=new ArrayList<>();

    public Table(String name, TableSchema schema){
        this.name=name;
        this.schema=schema;
    }

    public String getName(){return name;}
    public TableSchema getSchema(){return schema;}
    public List<Row> getRows(){return rows;}

    public boolean hasPrimaryKey(Object pkValue){
        Column pk=schema.getPrimaryKey();
        if (pk==null) return false;

        for (Row r: rows){
            if (Objects.equals(r.get(pk.getName()), pkValue)){
                    return true;
            }
        }
        return false;
    }

    @Override 
    public Iterator<Row> iterator(){
        return rows.iterator();
    }
}
