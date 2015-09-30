package eu.disi.unitn.swip.util;

import eu.disi.unitn.swip.datasource.Database;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class Utility {

    public static final String HOST = "host";
    public static final String USER_NAME = "user_name";
    public static final String PASSWORD = "password";
    public static final String IMPORT_SCRIPT = "import_script";
    public static final int NO_DATABASE_FOUND = -2;
    public static final int NO_TABLE_FOUND = -1;
    public static final int DATABASE_CONNECTION_COMPLETED = 1;
    public static final int MYSQL_DRIVER_ERROR = -3;
    public static Properties properties;

    public static Set<String> stringToSet(String str) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < str.length(); i++) {
            set.add(String.valueOf(str.charAt(i)));
        }
        return set;
    }


    public static void write(String content, String writeTo, boolean append) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(writeTo, append)));
            writer.write(content + "\n");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void verifyAndSetupDatabaseInstallation() {
        int status = Database.verifyDatabaseExistence();
        if (status < 0) {

            try {
                if (status == MYSQL_DRIVER_ERROR) {
                    return;
                } else if (status == NO_DATABASE_FOUND) {
                    Database.createDatabase();
                }
                Database.connect();
                Database.createTable();
                List<String> insertStatements = readInsertStatements();
                insertStatements.stream().forEach(sql -> {
                    try {
                        Database.insert(sql);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println("[Info]: Populating the table completed successfully!");
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<String> readInsertStatements() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(properties.getProperty(IMPORT_SCRIPT))));
        String line;
        List<String> insertStatements = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            insertStatements.add(line);
        }
        reader.close();
        return insertStatements;
    }

    public static void initProperties() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("./init.txt")));
        String line;
        properties = new Properties();
        while ((line = reader.readLine()) != null) {
            String[] props = line.split("\t");
            if (props[0].equals(HOST))
                properties.put(HOST, "jdbc:mysql://" + props[1] + "/bigdata_profiling");
            else if (props[0].equals(USER_NAME))
                properties.put(USER_NAME, props[1]);
            else if (props[0].equals(PASSWORD))
                properties.put(PASSWORD, props[1]);
            else if (props[0].equals(IMPORT_SCRIPT))
                properties.put(IMPORT_SCRIPT, props[1]);
        }
        reader.close();
    }

    public static Properties getProperties() {
        return properties;
    }
}
