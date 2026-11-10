package dao;

import entity.Table;
import entity.TableSchema;
import storage.FileStorage;
import java.util.LinkedHashMap;
import java.util.Map;

public class Database {
    
    private final Map<String, Table> tables=new LinkedHashMap<>();
    private final FileStorage storage;

    public Database(FileStorage storage){
        this.storage=storage;
    }

    public void registerTable(String name, TableSchema defaultSchema) throws Exception{
        Table loaded=storage.load(name);
        if(loaded!=null){
            tables.put(name, loaded);
            System.out.println("[DB] Таблица '" + name + "' загружена с диска (" + loaded.getRows().size() + " строк)");
        } else{
            Table fresh=new Table(name, defaultSchema);
            tables.put(name, fresh);
            storage.save(fresh);
            System.out.println("[DB] Таблица '" + name + "' создана заново");
        }
    }

    public TableDao getDao(String tableName){
        Table t=tables.get(tableName);
        if (t==null) throw new IllegalArgumentException("Таблица не найдена: " + tableName);
        return new TableDao(t, storage);
    }

    public Table getTable(String tableName){
        Table t=tables.get(tableName);
        if (t==null) throw new IllegalArgumentException("Таблица не найдена: " + tableName);
        return t;
    }
}
