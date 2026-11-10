# Лабораторная работа 2: Разработка упрощённой СУБД на Java

## Предметная область: Фитнес-клуб
Система управляет двумя связанными таблицами: `members` (клиенты клуба) и `visits` (посещения). Связь реализована через внешний ключ (`visits.member_id -> members.id`).

## Архитектура системы
Программа разделена на слои (паттерны DAO, Command):
*   **entity** — модель данных (`Table`, `Row`, `Column`, `TableSchema`, `DataType`). Все классы реализуют `Serializable`.
*   **storage** — сохранение данных на диск (`FileStorage`). Использует Java-сериализацию.
*   **dao** — слой доступа к данным (`BaseDao`, `TableDao`, `Database`). Проверяет уникальность PK и сохраняет изменения.
*   **sql** — парсер SQL-запросов (`Parser`, `Condition`).
*   **command** — реализации команд (`Insert`, `Select`, `Update`, `Delete`).
*   **console** — консольный интерфейс для ввода команд.

### Диаграмма классов

```mermaid
classDiagram
    class App {
        +main(String[] args)
    }
    class ConsoleApp {
        -Database db
        -Parser parser
        +run()
        -printTable()
    }
    class Parser {
        +parse(String sql) Query
    }
    class Query {
        <<interface>>
        +execute(Database db) Object
    }
    class Insert {
        +execute(Database db) Object
    }
    class Select {
        +execute(Database db) Object
    }
    class Update {
        +execute(Database db) Object
    }
    class Delete {
        +execute(Database db) Object
    }
    class Database {
        -Map~String, Table~ tables
        -FileStorage storage
        +registerTable()
        +getTable()
        +getDao()
    }
    class TableDao {
        -Table table
        -FileStorage storage
        +insert()
        +findAll()
        +update()
        +delete()
    }
    class FileStorage {
        -Path dataDir
        +save(Table table)
        +load(String name)
    }
    class Table {
        -String name
        -TableSchema schema
        -List~Row~ rows
    }
    
    App --> ConsoleApp
    ConsoleApp --> Parser
    ConsoleApp --> Database
    Parser --> Query
    Query <|.. Insert
    Query <|.. Select
    Query <|.. Update
    Query <|.. Delete
    Insert --> Database
    Select --> Database
    Update --> Database
    Delete --> Database
    Database --> TableDao
    Database --> FileStorage
    TableDao --> FileStorage
    TableDao --> Table
    FileStorage --> Table
```
## Диаграмма последовательностей
```mermaid
    sequenceDiagram
    actor User as Пользователь
    participant App
    participant ConsoleApp
    participant Parser
    participant Insert as InsertQuery
    participant Database
    participant TableDao
    participant FileStorage

    User->>App: Запускает программу
    App->>ConsoleApp: run()
    ConsoleApp->>User: sql>
    User->>ConsoleApp: "INSERT INTO members VALUES (1, 'Ivanov', 78.5)"
    ConsoleApp->>Parser: parse(sql)
    Parser-->>ConsoleApp: new Insert(...)
    ConsoleApp->>Insert: execute(db)
    Insert->>Database: getDao("members")
    Database-->>Insert: TableDao
    Insert->>TableDao: insert(Row)
    TableDao->>TableDao: Проверка уникальности PK
    TableDao->>FileStorage: save(Table)
    FileStorage-->>TableDao: Успешно сохранено
    TableDao-->>Insert: Строка добавлена
    Insert-->>ConsoleApp: "1 строка добавлена."
    ConsoleApp->>User: Вывод результата
```
### Схема базы данных
**Таблица `members` (Клиенты):**
*   `id` (INT) — первичный ключ (PK).
*   `name` (STRING) — имя.
*   `weight` (DOUBLE) — вес.

**Таблица `visits` (Посещения):**
*   `id` (INT) — первичный ключ (PK).
*   `member_id` (INT) — внешний ключ (FK), ссылается на `members.id`.
*   `duration` (INT) — длительность занятия в минутах.

## Инструкция пользователя (Как запустить)

1. Откройте проект в VS Code (или любой IDE).
2. Запустите файл `src/App.java`.
3. В консоли появится приглашение `sql> `.
4. Введите SQL-подобную команду и нажмите Enter.
5. Для выхода введите `exit`.

### Примеры команд для проверки:
```sql
-- Вставка с указанием всех полей
INSERT INTO members VALUES (1, 'Ivanov', 78.5);

-- Вставка с указанием конкретных полей (остальные будут null)
INSERT INTO members (id, name) VALUES (2, 'Petrova');

-- Выборка всех записей
SELECT * FROM members;

-- Выборка с условием (WHERE)
SELECT name, weight FROM members WHERE weight > 70;

-- Обновление (UPDATE)
UPDATE members SET weight = 80.0 WHERE id = 2;

-- Удаление (DELETE)
DELETE FROM members WHERE name = 'Ivanov';
```

### Выводы
В ходе работы была разработана упрощённая СУБД на Java. Реализована сериализация таблиц в файлы, поддержка пяти типов данных, парсинг SQL-подобного языка (INSERT, SELECT, UPDATE, DELETE) и консольный интерфейс. Применены паттерны проектирования (DAO, Command, Iterator). Ограничения: нет поддержки транзакций, сложных JOIN-запросов и индексов.