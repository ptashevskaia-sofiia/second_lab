package dao;

import entity.Row;
import java.util.List;
import java.util.Map;

public interface BaseDao {
    
    void insert(Row row) throws DaoException;

    List<Row> findAll();

    int update(RowCondition condition, Map<String, Object> newValues) throws DaoException;

    int delete(RowCondition condition) throws DaoException;
}
