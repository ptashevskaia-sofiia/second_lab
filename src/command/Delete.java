package command;

import dao.Database;
import dao.RowCondition;

public class Delete implements Query{
    
    private final String tableName;
    private final RowCondition condition;

    public Delete(String tableName, RowCondition condition) {
        this.tableName=tableName;
        this.condition=condition;
    }

    @Override
    public Object execute(Database db) throws Exception {
        int count = db.getDao(tableName).delete(condition);
        return count+" строк удалено.";
    }
}
