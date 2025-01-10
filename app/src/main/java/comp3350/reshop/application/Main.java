package comp3350.reshop.application;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Main {
    // Very important to update this string to match the db script file name
    private static final String DB_INITIAL_NAME = "RESHOP";
    private static String dbName=DB_INITIAL_NAME;

    private Main() {} // prevent instantiation

    public static void setDBPathName(final String name) {
        try {
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        dbName = name;
    }

    public static String getDBPathName() {
        return dbName;
    }

    /**
     * Prepares the path and file names to copy asset files from the source code into a device
     * directory. This should only be called once by the main starting activity.
     * @param context the context calling the copy
     */
    public static void copyDatabaseToDevice(Context context) {
        final String DB_PATH = "db";

        String[] assetNames;
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = context.getAssets();

        try {

            assetNames = assetManager.list(DB_PATH);
            for (int i = 0; i < assetNames.length; i++) {
                assetNames[i] = DB_PATH + "/" + assetNames[i];
            }

            copyAssetsToDirectory(context, assetNames, dataDirectory);

            // This prevents double concatenation if the main activity is somehow called twice
            if (Main.getDBPathName().equals(DB_INITIAL_NAME)) {
                Main.setDBPathName(dataDirectory.toString() + "/" + Main.getDBPathName());
            } else {
                Log.e("Reloaded main activity", Main.getDBPathName());
            }

        } catch (final IOException ioe) {
            System.out.println("Unable to access application data: " + ioe.getMessage());
        }

    }

    /**
     * Copies a list of source code files to a target director. This is where the database script
     * file is actually copied to the data directory in the device.
     * @param context the context cll
     * @param assets a list of asset path names to be copied
     * @param directory the target directory to copy
     * @throws IOException if an errors occurs while copying the file contents
     */
    private static void copyAssetsToDirectory(Context context, String[] assets, File directory) throws  IOException {
        AssetManager assetManager = context.getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];

            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (!outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }

    }

}
