package gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;



public class RunningProgramsFetcher {

    public List<String[]> getRunningPrograms() {
        List<String[]> programs = new ArrayList<>();

        try {
            // Execute a system command to get the list of windows
            Process process = Runtime.getRuntime().exec("gdbus call --session --dest org.gnome.Shell --object-path /org/gnome/Shell/Extensions/Windows --method org.gnome.Shell.Extensions.Windows.List");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            process.waitFor();

            // Parse the JSON output
            String jsonString = output.toString();
            jsonString = jsonString.substring(jsonString.indexOf('['), jsonString.lastIndexOf(']') + 1);
            JsonArray windows = JsonParser.parseString(jsonString).getAsJsonArray();

            // Process each window's details
            for (JsonElement windowElement : windows) {
                JsonObject window = windowElement.getAsJsonObject();
                String classValue = window.get("wm_class").getAsString();
                programs.add(new String[] {
                        String.valueOf(window.get("id").getAsLong()),
                        classValue,
                        String.valueOf(window.get("pid").getAsInt()),
                        String.valueOf(window.get("focus").getAsBoolean())
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Sort the programs list by class name (alphabetically)
        Collections.sort(programs, new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return o1[1].compareToIgnoreCase(o2[1]); // Compare based on class name
            }
        });

        return programs;
    }
}
