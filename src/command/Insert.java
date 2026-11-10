package command;

import dao.Database;
import dao.DaoException;
import dao.TableDao;
import entity.*;


public class Insert implements Query {

    private final String tableName;
    private final String[] columns;
    private final String[] rawValues;

    public Insert(String tableName, String[] columns, String[] rawValues){
        this.tableName=tableName;
        this.columns=columns;
        this.rawValues=rawValues;
    }

    @Override
    public Object execute(Database db) throws Exception {
        TableDao dao=db.getDao(tableName);
        //getDao?
        Table table=db.getTable(tableName);
        TableSchema schema=table.getSchema();

        Row newRow=new Row();

        if (columns==null){
            if (rawValues.length!=schema.getColumns().size()){
                throw new DaoException("Ошибка: Неверное количество значений. Ожидалось " + schema.getColumns().size());
            }
            for(int i=0; i<rawValues.length; ++i){
                Column col=schema.getColumns().get(i);
                Object parsedVal=col.getType().parse(cleanString(rawValues[i]));
                newRow.put(col.getName(), parsedVal);
            }
        }else{
            for (int i=0; i<columns.length; ++i){
                Column col=schema.byName(columns[i]);
                if(col==null) throw new DaoException("Ошибка: Колонка не найдена: " + columns[i]);
                Object parsedVal=col.getType().parse(cleanString(rawValues[i]));
                newRow.put(col.getName(), parsedVal);
            }
        }
        dao.insert(newRow);
        return "1 строка добавлена.";
    }

    private String cleanString(String s){
        s=s.trim();
        if (s.startsWith("'") && s.endsWith("'")){
            return s.substring(1,s.length()-1);
        }
        return s;
    }
}
