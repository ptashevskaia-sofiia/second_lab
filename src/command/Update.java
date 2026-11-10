package command;

import dao.Database;
import dao.RowCondition;
import java.util.Map;

public class Update implements Query{
    
    private final String tableName;
    private final Map<String, Object> newValues; 
    private final RowCondition condition;

    public Update(String tableName, Map<String, Object> newValues, RowCondition condition) {
        this.tableName=tableName;
        this.newValues=newValues;
        this.condition=condition;
    }

    @Override
    public Object execute(Database db) throws Exception{
        int count=db.getDao(tableName).update(condition, newValues);
        return count+" строк обновлено.";
    }
}
