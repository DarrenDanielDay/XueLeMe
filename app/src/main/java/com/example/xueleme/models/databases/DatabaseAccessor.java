package com.example.xueleme.models.databases;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.xueleme.models.DateParser;
import com.example.xueleme.models.JSONParser;
import com.example.xueleme.models.ReflectiveJSONModel;
import com.example.xueleme.models.locals.ChatMessage;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DatabaseAccessor<T> {
    public final Class<T> entityClass;
    public final String tableName;
    public final String[] columns;
    public final Field[] entityFields;

    public DatabaseAccessor(Class<T> entityClass) {
        this.entityClass = entityClass;
        DatabaseEntity databaseEntity = entityClass.getAnnotation(DatabaseEntity.class);
        assert databaseEntity != null;
        tableName = "".equals(databaseEntity.tableName()) ? entityClass.getSimpleName() : databaseEntity.tableName();
        List<String> columnNames = new ArrayList<>();
        List<Field> fields = new ArrayList<>();
        for (Field field : entityClass.getFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                columnNames.add("".equals(column.columnName()) ? field.getName() : column.columnName());
                fields.add(field);
            }
        }
        columns = new String[columnNames.size()];
        entityFields = new Field[fields.size()];
        for (int i = 0; i < columns.length; i++) {
            columns[i] = columnNames.get(i);
            entityFields[i] = fields.get(i);
        }

    }

    public static final Map<Class<?>, String> dbTypeOf = new HashMap<Class<?>, String>() {
        {
            put(String.class, "TEXT");
            put(Integer.class, "INTEGER");
            put(Double.class, "REAL");
        }
    };

    private String columnDescription(int i) {
        Field field = entityFields[i];
        String typeString = dbTypeOf.get(field.getType());
        String constrains = "";
        PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
        if (primaryKey != null && primaryKey.isInteger() && primaryKey.autoIncrement()) {
            constrains = " PRIMARY KEY AUTOINCREMENT";
        } else if (primaryKey != null) {
            constrains = "PRIMARY KEY";
        }
        return String.format("%s %s%s", columns[i], typeString != null ? typeString : dbTypeOf.get(String.class), constrains);
    }

    public String createSQL() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE ").append(tableName).append(" (\n");
        for (int i = 0; i < columns.length - 1; i++) {
            stringBuilder.append(columnDescription(i));
            stringBuilder.append(",\n");
        }
        stringBuilder.append(columnDescription(columns.length - 1));
        stringBuilder.append("\n)");
        String sql = stringBuilder.toString();
//        Log.d("createSQL of " + entityClass.getName(), sql);
        return sql;
    }

    public void doCreate(SQLiteDatabase database) {
        database.execSQL(createSQL());
    }

    public List<T> fetchAll(Cursor cursor) {
        List<T> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            try {
                T item = entityClass.newInstance();
                for (int i = 0; i < columns.length; i++) {
                    try {
                        int index = cursor.getColumnIndex(columns[i]);
                        Field field = entityFields[i];
                        Class<?> fieldClass = field.getType();
                        if (fieldClass.equals(Integer.class)) {
                            field.set(item, cursor.getInt(index));
                        } else if (fieldClass.equals(Double.class)) {
                            field.set(item, cursor.getDouble(index));
                        } else if (fieldClass.equals(String.class)) {
                            field.set(item, cursor.getString(index));
                        } else if (fieldClass.equals(Date.class)) {
                            field.set(item, DateParser.format.parse(cursor.getString(index)));
                        } else if (fieldClass.isEnum()) {
                            Object[] enumArray = fieldClass.getEnumConstants();
                            field.set(item, enumArray[cursor.getInt(index)]);
                        } else {
                            Gson gson = new Gson();
                            Map<String, Object> jsonMap = gson.fromJson(cursor.getString(index), Map.class);
                            JSONParser<?> parser = getParser(fieldClass);
                            if (parser == null) {
                                continue;
                            }
                            field.set(item, parser.parse(jsonMap));
                        }
                    } catch (IllegalArgumentException ignored) {

                    }
                }
                items.add(item);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return items;
    }

    public void insertOne(T item, SQLiteDatabase database) {
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < columns.length; i++) {
            Field field = entityFields[i];
            String column = columns[i];
            Object value = null;
            try {
                value = field.get(item);
            } catch (IllegalAccessException e) {
                Log.e("convert to database column type", "无法访问column " + column);
            }
            if (value == null) {
                continue;
            }
            if (value instanceof Integer) {
                contentValues.put(column, (Integer) value);
            } else if (value instanceof String) {
                contentValues.put(column, (String) value);
            } else if (value instanceof Double) {
                contentValues.put(column, (Double) value);
            } else if (value instanceof Date) {
                contentValues.put(column, DateParser.format.format((Date) value));
            } else if (value.getClass().isEnum()) {
                Object[] enumArray = value.getClass().getEnumConstants();
                for (int j = 0; j < enumArray.length; j++) {
                    if (enumArray[j].equals(value)) {
                        contentValues.put(column, j);
                        break;
                    }
                }
            } else {
                JSONParser<T> parser = (JSONParser<T>) getParser(value.getClass());
                if (parser == null) {
                    Log.e("convert to database column type","无法转换类型" + value.getClass().getName() + "为数据库存储类型");
                    continue;
                }
                contentValues.put(column, parser.serialize(item).toString());
            }

        }
        database.insert(tableName, null, contentValues);
    }

    public void insertAll(Collection<T> items, SQLiteDatabase database) {
        for (T item : items) {
            insertOne(item, database);
        }
    }

    private static <TFieldType> JSONParser<TFieldType> getParser(Class<TFieldType> fieldTypeClass) {
        if (ReflectiveJSONModel.class.isAssignableFrom(fieldTypeClass)) {
            try {
                return (JSONParser<TFieldType>) fieldTypeClass.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                Log.e("Database JSON Parser", "找不到类 " + fieldTypeClass.getName() + " 的格式化方案");
                return null;
            }
        } else {
            return null;
        }
    }
}
