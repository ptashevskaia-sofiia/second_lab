package entity;

public enum DataType {
    INT, DOUBLE, STRING, DATE, BOOLEAN;
    
    public Object parse(String raw){
        switch(this){
            case INT: return Integer.parseInt(raw);
            case DOUBLE: return Double.parseDouble(raw);
            case STRING: return raw;
            case DATE: return java.time.LocalDate.parse(raw);
            case BOOLEAN: return Boolean.parseBoolean(raw);
            default: throw new IllegalStateException("Неизвестный тип: " + this);
        }
    }
} 