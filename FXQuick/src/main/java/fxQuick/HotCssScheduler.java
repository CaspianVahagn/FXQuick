package fxQuick;

import javafx.animation.AnimationTimer;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotCssScheduler extends AnimationTimer {
    List<List<String>> StylesheetLists = new ArrayList<>();
    long elapsed = 0;
    Map<String, String> nameMap = new HashMap<>();
    private final String BUILD_LOC = "build/resources/main";
    private final String SOURCE_LOC = "src/main/resources";

    @Override
    public void handle(long now) {
        if (now - elapsed > 1_000_000_000) {
            this.elapsed = now;
            for (List<String> sheets : StylesheetLists) {
                String[] names = sheets.stream().filter(s -> s.contains("res") && !s.contains("https://") && s.endsWith("css")).toArray(String[]::new);
                for (int i = 0; i < names.length; i++) {
                    try {
                        if (nameMap.containsKey(names[i])) {
                            //FileInputStream fis = new FileInputStream(new URL(names[i].replace(BUILD_LOC,SOURCE_LOC)).getFile());
                            BufferedReader reader = new BufferedReader(new FileReader(new URL(names[i].replace(BUILD_LOC,SOURCE_LOC)).getFile()));
                            String currentLine = reader.lines().reduce( (a,b)-> a+b).get();
                            String newhash =currentLine;
                            System.out.println(names[i]);
                            if (!newhash.equals(nameMap.get(names[i].replace(BUILD_LOC,SOURCE_LOC)))) {
                                nameMap.put(names[i].replace(BUILD_LOC,SOURCE_LOC), newhash);
                                sheets.remove(names[i]);
                                sheets.add(names[i].replace(BUILD_LOC,SOURCE_LOC));
                            }
                        } else {
                            nameMap.put(names[i], "");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void addSheetToObserve(List<String> sheets){
        this.StylesheetLists.add(sheets);
    }

}
