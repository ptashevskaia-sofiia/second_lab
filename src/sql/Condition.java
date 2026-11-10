package sql;

import dao.RowCondition;
import entity.Row;

public class Condition implements RowCondition{

    private final String column;
    private final String operator;
    private final Object value;

    public Condition(String column, String operator, Object value){
        this.column=column;
        this.operator=operator;
        this.value=value;
    }

    @Override 
    public boolean test(Row row){ 
        Object actual=row.get(column);
        if (actual==null) return false;

        int cmp=compare(actual, value);

        switch(operator){
            case "=": return cmp==0;
            case "<>": return cmp!=0;
            case ">":  return cmp>0;
            case "<":  return cmp<0;
            case ">=": return cmp>=0;
            case "<=": return cmp<=0;
            default: throw new IllegalStateException("Неизвестный оператор: " + operator);
        }
    }

    private int compare(Object a, Object b){
        if (a instanceof Number && b instanceof Number) {
            double valA=((Number) a).doubleValue();
            double valB=((Number) b).doubleValue();
            return Double.compare(valA, valB);
        }

        if (a instanceof Double && b instanceof Double) {
            return ((Double) a).compareTo((Double) b);
        }
        if (a instanceof String && b instanceof String) {
            return ((String) a).compareTo((String) b);
        }
        if (a instanceof java.time.LocalDate && b instanceof java.time.LocalDate) {
            return ((java.time.LocalDate) a).compareTo((java.time.LocalDate) b);
        }
        if (a instanceof Boolean && b instanceof Boolean) {
            return ((Boolean) a).compareTo((Boolean) b);
        }
        throw new IllegalStateException("Несравнимые значения: " + a + " и " + b);
    }
}


