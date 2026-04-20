package activity13;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MultiThreadedListApp {

    List<String> data = new CopyOnWriteArrayList<>();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        MultiThreadedListApp app = new MultiThreadedListApp();

        //load data
        System.out.println("=== MAIN THREAD ===");
        app.loadDataFromJson();

        // thread 1
        System.out.println("\n=== FILE WRITER THREAD ===");
        Thread saver = new Thread(() -> {
            app.saveDataToJson();
        });

        // Show Menu
        app.showMenu();

        // Start the thread
        saver.start();
    }

    private void loadDataFromJson() {
        try {
            Object obj = new JSONParser().parse(new FileReader("data.json"));
            JSONArray jsonArray = (JSONArray) obj;
            for(Object item : jsonArray){
                data.add(item.toString());
            }
            System.out.println("Data Loaded Successfully!");
        } catch (Exception e) {
            System.out.println("No existing file found, starting with empty list.");
        }
    }

    private void showMenu() {
        System.out.println("\n===== MENU =====");
        System.out.println("User choices:");
        System.out.println("1. View Grades");
        System.out.println("2. Search Grades");
        System.out.println("3. Enter Grades");
        System.out.println("4. Edit Grades");
        System.out.println("================\n");
    }

    private void saveDataToJson() {
        JSONArray jsonArray = new JSONArray();

        for (String item : data) {
            JSONObject obj = new JSONObject();
            obj.put("info", item);
            jsonArray.add(obj);
        }

        try {
            FileWriter file = new FileWriter("data.json");
            file.write(jsonArray.toJSONString());
            file.flush();
            file.close();
            System.out.println("Data Saved to JSON File Successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

