package sql;

import command.*;
import dao.RowCondition;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public Query parse(String sql){
        String trimmed=sql.trim();
        String upper=trimmed.toUpperCase();

        if (upper.startsWith("INSERT")) return parseInsert(trimmed);
        if (upper.startsWith("SELECT")) return parseSelect(trimmed);
        if (upper.startsWith("UPDATE")) return parseUpdate(trimmed);
        if (upper.startsWith("DELETE")) return parseDelete(trimmed);

        throw new IllegalArgumentException("Неизвестная команда: "+sql);
    }

    private Query parseInsert(String sql){
        Pattern pattern=Pattern.compile(
                "INSERT\\s+INTO\\s+(\\w+)\\s*(?:\\(([^)]+)\\))?\\s*VALUES\\s*\\(([^)]+)\\)",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher=pattern.matcher(sql);
        if (!matcher.find()){throw new IllegalArgumentException("Неверный синтаксис INSERT.");}

        String tableName=matcher.group(1);
        String colsStr=matcher.group(2);
        String valsStr=matcher.group(3);

        String[] columns=null;
        if (colsStr!=null){
            columns=splitAndTrim(colsStr);
        }

        String[] values=splitAndTrim(valsStr);

        return new Insert(tableName, columns, values);
    }

    private Query parseSelect(String sql) {
        
        Pattern pattern = Pattern.compile(
                "SELECT\\s+(.+?)\\s+FROM\\s+(\\w+)(?:\\s+WHERE\\s+(.+))?",
                Pattern.CASE_INSENSITIVE
        );
        
        Matcher matcher = pattern.matcher(sql);
        if (!matcher.find()) {throw new IllegalArgumentException("Неверный синтаксис SELECT");}

        String colsStr=matcher.group(1); 
        String tableName=matcher.group(2); 
        String whereStr=matcher.group(3);  

        String[] columns=null;
        if (!colsStr.equals("*")) {
            columns = splitAndTrim(colsStr);
        }

        RowCondition condition = null;
        if (whereStr != null) {
            condition = parseCondition(whereStr);
        }

        return new Select(tableName, columns, condition);
    }


    private Query parseUpdate(String sql){

        Pattern pattern = Pattern.compile(
                "UPDATE\\s+(\\w+)\\s+SET\\s+(.+?)(?:\\s+WHERE\\s+(.+))?$",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(sql);
        if (!matcher.find()) throw new IllegalArgumentException("Неверный синтаксис UPDATE");

        String tableName = matcher.group(1);
        String setStr = matcher.group(2);   
        String whereStr = matcher.group(3);

        Map<String, Object> newValues=new java.util.HashMap<>();
        String[] setParts=setStr.split(",");
        for (String part: setParts) {
            String[] kv=part.split("=");
            if (kv.length!=2) throw new IllegalArgumentException("Ошибка в SET: "+part);
            
            String colName=kv[0].trim();
            String valStr=kv[1].trim();

            if (valStr.startsWith("'") && valStr.endsWith("'")){
                valStr = valStr.substring(1, valStr.length() - 1);
            }

            Object val=parseValue(valStr);
            newValues.put(colName, val);
        }

        RowCondition condition=null;
        if (whereStr!=null) {
            condition=parseCondition(whereStr);
        }

        return new Update(tableName, newValues, condition);
    }

    private Query parseDelete(String sql) {
        
        Pattern pattern = Pattern.compile(
                "DELETE\\s+FROM\\s+(\\w+)(?:\\s+WHERE\\s+(.+))?$",
                Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = pattern.matcher(sql);
        if (!matcher.find()) throw new IllegalArgumentException("Неверный синтаксис DELETE");

        String tableName=matcher.group(1);
        String whereStr=matcher.group(2);

        RowCondition condition=null;
        if (whereStr!=null){
            condition=parseCondition(whereStr);
        }

        return new Delete(tableName, condition);
    }

    private String[] splitAndTrim(String s){
        String[] parts=s.split(",");
        for (int i=0; i<parts.length; ++i) {
            parts[i]=parts[i].trim();
        }
        return parts;
    }

    private RowCondition parseCondition(String where){

        Pattern pattern = Pattern.compile("(\\w+)\\s*(=|<>|>=|<=|>|<)\\s*('[^']*'|[^\\s]+)");
        Matcher matcher = pattern.matcher(where);

        if (!matcher.find()){throw new IllegalArgumentException("Неверное условие WHERE: "+where);}

        String colName=matcher.group(1);
        String op=matcher.group(2);
        String valStr=matcher.group(3);

        if (valStr.startsWith("'") && valStr.endsWith("'")) {
            valStr=valStr.substring(1, valStr.length()-1);
        }

        Object val;
        try {
            val = Integer.parseInt(valStr); 
        } catch (NumberFormatException e1) {
            try {
                val=Double.parseDouble(valStr); 
            } catch (NumberFormatException e2) {
                val=valStr; 
            }
        }

        return new Condition(colName, op, val);
    }

    private Object parseValue(String valStr) {
        try{
            return Integer.parseInt(valStr);
        }catch (NumberFormatException e1){
            try{
                return Double.parseDouble(valStr);
            }catch (NumberFormatException e2) {
                return valStr; 
            }
        }
    }
}

