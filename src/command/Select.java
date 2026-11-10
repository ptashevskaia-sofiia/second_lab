package command;

import dao.Database;
import dao.RowCondition;
import entity.Row;
import entity.Table;
import java.util.ArrayList;
import java.util.List;

public class Select implements Query{
    
    private final String tableName;
    private final String[] columns; 
    private final RowCondition condition;

    public Select(String tableName, String[] columns, RowCondition condition) {
        this.tableName=tableName;
        this.columns=columns;
        this.condition=condition;
    }

    @Override
    public Object execute(Database db) throws Exception{
        Table table=db.getTable(tableName);
        List<Row> result=new ArrayList<>();
        
        for (Row r: table){
            if (condition==null || condition.test(r)) {
                result.add(r);
            }
        }

        return result;
    }

    public String[] getColumns() {
        return columns;
    }

    public String getTableName(){
        return tableName;
    }
}
