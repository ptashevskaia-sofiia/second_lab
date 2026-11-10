package console;

import command.Query;
import command.Select;
import dao.Database;
import entity.Row;
import entity.Table;
import sql.Parser;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp{

    private final Database db;
    private final Parser parser;

    public ConsoleApp(Database db) {
        this.db = db;
        this.parser = new Parser();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Простая СУБД (Фитнес-клуб)");
        System.out.println("Введите SQL-запрос или 'exit' для выхода.");
        
        while (true){
            System.out.print("sql> ");
            String line=scanner.nextLine().trim();
            
            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Выход");
                break;
            }
            if (line.isEmpty()) continue;

            try {
                Query query=parser.parse(line);
                Object result=query.execute(db);

                if (query instanceof Select){
                    Select select = (Select) query;
                    printTable((List<Row>) result, select.getColumns(), select.getTableName());
                } else{
                    System.out.println("-> "+result);
                }

            }catch (Exception e) {
                System.out.println("ОШИБКА: "+e.getMessage());
            }
        }
    }

    private void printTable(List<Row> rows, String[] selectedCols, String tableName) throws Exception {
        if (rows.isEmpty()){
            System.out.println("(Пустой результат)");
            return;
        }

        Table table=db.getTable(tableName);
        String[] cols=(selectedCols!=null) ? selectedCols : 
                table.getSchema().getColumns().stream().map(c -> c.getName()).toArray(String[]::new);

        for (String col: cols) {
            System.out.printf("%-15s", col);
        }
        System.out.println();
        
        for (int i=0; i<cols.length; ++i) {
            System.out.print("---------------");
        }
        System.out.println();

        for (Row row: rows) {
            for (String col : cols) {
                Object val=row.get(col);
                System.out.printf("%-15s", val!=null ? val.toString() : "null");
            }
            System.out.println();
        }
        System.out.println("("+rows.size()+" строк)");
    }
}