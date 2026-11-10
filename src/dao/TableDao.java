package dao;

import entity.Column;
import entity.Row;
import entity.Table;
import storage.FileStorage;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class TableDao implements BaseDao{
    
    private final Table table;
    private final FileStorage storage;

    public TableDao(Table table, FileStorage storage){
        this.table=table;
        this.storage=storage;
    }

    //вставка(insert в SQL)
    @Override 
    public void insert(Row row) throws DaoException{
        Column pk=table.getSchema().getPrimaryKey();

        if(pk!=null){
            Object pkValue=row.get(pk.getName());
            if (table.hasPrimaryKey(pkValue)){
                throw new DaoException("Ошибка: Ключ " + pkValue + " уже существует!");
            }
        }

        table.getRows().add(row);
        persist();
    }

    //аналог Select
    @Override 
    public List<Row> findAll(){
        return table.getRows();
    }

    //Обновление(update в SQL)
    @Override 
    public int update(RowCondition condition, Map<String, Object> newValues) throws DaoException{
        int count=0;
        for (Row r: table){
            if (condition.test(r)){
                for(Map.Entry<String, Object> entry: newValues.entrySet()){
                    r.put(entry.getKey(), entry.getValue());
                }
                ++count;
            }
        }
        if (count>0){ persist();}
        return count;
    }

    //Удаление строк(delete в SQL)
    @Override 
    public int delete(RowCondition condition) throws DaoException{

        int removed=0;
        java.util.Iterator<Row> iterator = table.getRows().iterator();
        while(iterator.hasNext()){
            Row r=iterator.next();
            if (condition.test(r)){
                iterator.remove();
                ++removed;
            }
        }
        if (removed>0){persist();}
        return removed;
    }

    private void persist() throws DaoException {
        try {storage.save(table);}
        catch (IOException e){
            throw new DaoException("Не удалось сохранить таблицу: " + table.getName(), e);
        }
    }
}
