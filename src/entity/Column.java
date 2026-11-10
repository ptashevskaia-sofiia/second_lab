package entity;

import java.io.Serializable;

public class Column implements Serializable{
    
    private static final long serialVersionUID=1L;

    private final String name;
    private final DataType type;
    private final boolean primaryKey;
    private final String foreignKeyTable;

    public Column(String name, DataType type, boolean primaryKey, String foreignKeyTable) {
        this.name=name;
        this.type=type;
        this.primaryKey=primaryKey;
        this.foreignKeyTable=foreignKeyTable;
    }

    public String getName(){return name;}
    public DataType getType() {return type;}
    public boolean isPrimaryKey(){return primaryKey;}
    public String getForeignKeyTable(){return foreignKeyTable;}
    public boolean isForeignKey(){return foreignKeyTable!=null;}
}
