package command;

import dao.Database;

public interface Query {
    Object execute(Database db) throws Exception;
}
