
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BaconsLaw {
    private TableGraph graph = new TableGraph();

    /**
     * Format the Array list into a String which shows relations
     * @param path - Arraylist of the path between 2 actors
     * @param actor1
     * @param actor2
     * @return - a "presentable" string
     */
    private String outPut(ArrayList<String> path , String actor1, String actor2){
        String output = String.format("Path between %s and %s: " ,CapName(actor1) ,CapName(actor2));
        for(int i = path.size() - 1; i >= 0; i--){
            output += CapName(path.get(i));
            if(i != 0){
                output += " --> ";
            }
        }
        return output;
    }
    /**
     * Capitalize the first character for every part in a name
     * @param name - name of an actor
     * @return - proper formatted name
     */
    private String CapName(String name){
        String[] temp = name.split(" ");
        String formattedName = "";
        for(String part : temp){
            formattedName += " " + part.substring(0,1).toUpperCase() + part.substring(1);
        }
        return formattedName.substring(1);
    }

    /**
     * Parse quotes into csv format to Json format
     *
     * @param str - the line parsing
     * @return - Json formatted line
     */
    private String StripDoubleQuote(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (i == 0 || i == str.length() - 1) {
                continue;
            }
            // handle double quotes
            if (c == '\"' && str.charAt(i + 1) == '\"') {
                // handle quad quotes
                if (str.charAt(i + 2) == '\"' && str.charAt(i + 3) == '\"') {
                    sb.append(c);
                }
                continue;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Parse the line into Json format
     * extract all the casts from a movie
     * @param line - line to be parsed
     * @return - parsed line
     * @throws ParseException
     */
    private String[] CastParse(String line) throws ParseException {
        String str = line.split(",", 3)[2];
        //check if there is no cast within a movie
        if (str.contains("[]")) {
            return null;
            //return;
        }
        String Cast_info = str.split("\\[\\{")[1];
        String cast = StripDoubleQuote("\"[{" + Cast_info.substring(0, Cast_info.length() - 2));
        // System.out.println(cast);
        JSONParser jp = new JSONParser();
        JSONArray jArr = (JSONArray) jp.parse(cast); // parse line into an array
        String[] result = new String[jArr.size()];
        for (int i = 0; i < jArr.size(); i++) {
            JSONObject obj = (JSONObject) jArr.get(i);
            result[i] = ((String) obj.get("name")).toLowerCase();

        }
        return result;
    }

    /**
     * Read tmdb_5000_credits.csv file from the given path
     *
     * @param file - tmdb_5000_credits.csv file
     */
    private void readFile(String file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            //int count = 0;
            while ((line = br.readLine()) != null){ //&& count < 5) {
                String[] CastList = CastParse(line);

                if(CastList != null){
                    graph.addEdge(CastList);
                    //System.out.println(Arrays.toString(CastList));
                }
                //count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    /**
     * Read input from the user
     */
    private void readInput() throws Exception{
        try (Scanner scan = new Scanner(System.in)) {
            boolean quit = false;
            while (!quit) {
                // get name of actor 1
                System.out.print("Actor 1 name: ");
                String actor1 = scan.nextLine().trim().toLowerCase();
                while (!graph.isInList(actor1)) {
                    System.out.println("No such actor.");
                    System.out.print("Actor 1 name: ");
                    actor1 = scan.nextLine().trim().toLowerCase();

                }
                // get name of actor 2
                System.out.print("Actor 2 name: ");
                String actor2 = scan.nextLine().trim().toLowerCase();
                while (!graph.isInList(actor2)) {
                    System.out.println("No such actor.");
                    System.out.print("Actor 2 name: ");
                    actor2 = scan.nextLine().trim().toLowerCase();

                }
                ArrayList<String> path = graph.CalculateShortestPath(actor1,actor2);
                //System.out.println(path.toString());
                System.out.println(outPut(path, actor1, actor2));
                System.out.println("Continue or Quit ? (c/q)");
                if (scan.nextLine().trim().toLowerCase().equals("q")) {
                    quit = true;
                    scan.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main function
     * @param args - the path of tmdb_5000_credits.csv
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        BaconsLaw BL = new BaconsLaw();
        BL.readFile(args[0]);
        //BL.readFile("test.csv");
        BL.readInput();
    }
}
