import dao.Database;
import entity.*;
import storage.FileStorage;
import console.ConsoleApp;
import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        try {
            //Инициализация хранилища и базы
            FileStorage storage = new FileStorage("data");
            Database db = new Database(storage);

            // Создаем схему для members
            TableSchema membersSchema = new TableSchema(Arrays.asList(
                    new Column("id", DataType.INT, true, null),
                    new Column("name", DataType.STRING, false, null),
                    new Column("weight", DataType.DOUBLE, false, null)
            ));

            //Создаем схему для visits (вторая таблица для отчета)
            TableSchema visitsSchema = new TableSchema(Arrays.asList(
                    new Column("id", DataType.INT, true, null),
                    new Column("member_id", DataType.INT, false, "members"),
                    new Column("duration", DataType.INT, false, null)
            ));

            // Регистрируем таблицы
            db.registerTable("members", membersSchema);
            db.registerTable("visits", visitsSchema);

            //Запускаем консоль
            ConsoleApp app = new ConsoleApp(db);
            app.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}