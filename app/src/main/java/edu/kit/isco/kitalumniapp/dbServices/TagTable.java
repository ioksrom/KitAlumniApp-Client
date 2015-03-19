package edu.kit.isco.kitalumniapp.dbServices;

/**
 * Created by Andre on 02.02.2015.
 *
 * Representation of the database table containing tags
 */
public class TagTable {

    public static final String TABLE_NAME = "tag";
    //Column names
    public static final String ID = "id";
    public static final String NAME = "name";

    /**
    * SQL query for creating the table
    * @return  SQL query
    */
    public static String createSQL() {
        return "CREATE TABLE tag("
                    + ID + " INTEGER PRIMARY KEY, "
                    + NAME + " TEXT"
                + ");";
    }

    /**
     * SQL query for dropping the table
     * @return SQL query
     */
    public static String dropSQL() {
        return "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    }
}
