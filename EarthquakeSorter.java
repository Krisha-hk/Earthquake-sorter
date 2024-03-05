// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2023T3, Assignment 2
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;

/**
 * EarthquakeSorter
 * Sorts data about a collection of 4335 NZ earthquakes from May 2016 to May 2017
 * Each line of the file "earthquake-data.txt" has a description of one earthquake:
 *   ID time longitude latitude magnitude depth region
 * Data is from http://quakesearch.geonet.org.nz/
 *  Note the earthquakes' ID have been modified to suit this assignment.
 *  Note bigearthquake-data.txt has just the 421 earthquakes of magnitude 4.0 and above
 *   which may be useful for testing, since it is not as big as the full file.
 *   
 */

public class EarthquakeSorter{

    private List<Earthquake> earthquakes = new ArrayList<Earthquake>();
    private String region;

    /**
     * Load data from the specified data file into the earthquakes field:
     */
    public void loadData(String filename){
        try {
            /*# YOUR CODE HERE */
            List<String>allLines = Files.readAllLines(Path.of("bigearthquake-data.txt")); // Read all lines from the file and store them in a list
            for(String equake : allLines) { // Iterate through each line in the list
                // Create a scanner to parse the earthquake data in the current line
                Scanner scan = new Scanner(equake); 

                String id = scan.next();
                String dat = scan.next();
                String tim = scan.next();
                double lon = scan.nextDouble();
                double lat = scan.nextDouble();
                double mag = scan.nextDouble();
                double dep = scan.nextDouble();
                String reigon = scan.next();
                Earthquake quake = new Earthquake (id, dat, tim, lon, lat, mag, dep, reigon);// Create an Earthquake object using the data
                earthquakes.add(quake); // Add the Earthquake object to the list of earthquakes
            }
            UI.printf("Loaded %d earthquakes into list\n", this.earthquakes.size());
            UI.println("----------------------------");
        } catch(IOException e){UI.println("File reading failed");}
    }

    // Initialize instance variables
    /**
     * Sorts the earthquakes by ID
     */
    public void sortByID(){
        UI.clearText();
        UI.println("Earthquakes sorted by ID");
        /*# YOUR CODE HERE */
        Collections.sort(earthquakes); //Sorting the earthquakes list 
        for (Earthquake e : this.earthquakes){
            UI.println(e);
        }

        UI.println("------------------------");
    }

    /**
     * Sorts the earthquakes by magnitude, largest first
     */
    public void sortByMagnitude(){
        UI.clearText();
        UI.println("Earthquakes sorted by magnitude (largest first)");
        /*# YOUR CODE HERE */
        
        Comparator<Earthquake> magnitudeComparator = new Comparator<Earthquake>() {  // Creating a comparator to sort by magnitude in reverse order
                @Override
                public int compare(Earthquake e1, Earthquake e2) {
                    return Double.compare(e2.getMagnitude(), e1.getMagnitude()); //Comparing in descending order e2 to e1
                }
            };

        for (Earthquake e : this.earthquakes){
            UI.println(e);
        }
        UI.println("------------------------");
    }

    /**
     * Sorts the list of earthquakes according to the date and time that they occurred.
     */
    public void sortByTime(){
        UI.clearText();
        UI.println("Earthquakes sorted by time");
        /*# YOUR CODE HERE */
        Comparator<Earthquake> timeComparator = new Comparator<Earthquake>(){ // Creating a comparator to sort by time
                @Override
                public int compare(Earthquake e1, Earthquake e2) {
                    String dateTime1 = e1.getDate() + "" + e1.getTime(); // Combine date and time for comparison
                    String dateTime2 = e2.getDate() + "" + e2.getTime();

                    return dateTime1.compareTo(dateTime2); // Compare the combined date and time strings from above

                }
            };
        Collections.sort(earthquakes, timeComparator); // Sort the list of earthquakes using the timeComparator
        for (Earthquake e : this.earthquakes){
            UI.println(e);
        }
        UI.println("------------------------");
    }

    /**
     * Sorts the list of earthquakes according to region. If two earthquakes have the same
     *   region, they should be sorted by magnitude (highest first) and then depth (more shallow first)
     */
    public void sortByRegion(){
        UI.clearText();
        UI.println("Earthquakes sorted by region, then by magnitude and depth");
        /*# YOUR CODE HERE */
        Comparator<Earthquake> regComparator = new Comparator<Earthquake>(){
                @Override
                public int compare(Earthquake r1, Earthquake r2) {
                    int regComparison = r1.getRegion().compareTo(r2.getRegion());
                    if (regComparison != 0) {
                        return regComparison; // Different regions, sort by region
                    } else {
                        int magnitudeComparison = Double.compare(r2.getMagnitude(), r1.getMagnitude()); // Same region, compare by highest magnitude first
                        if (magnitudeComparison != 0) {
                            return magnitudeComparison; // Different magnitudes, sort by magnitude
                        } else {
                            return Double.compare(r1.getDepth(), r2.getDepth()); // Same magnitude, compare by depth (more shallow first)
                        }
                    }
                }
            };
        Collections.sort(earthquakes, regComparator);
        for (Earthquake e : this.earthquakes){
            UI.println(e);
        }
        UI.println("------------------------");
    }

    /**
     * Sorts the earthquakes by proximity to a specified location
     */
    public void sortByProximity(double longitude, double latitude){
        UI.clearText();
        UI.println("Earthquakes sorted by proximity");
        UI.println("Longitude: " + longitude + " Latitude: " + latitude );
        /*# YOUR CODE HERE */
        Comparator<Earthquake> proximityComparator = new Comparator<Earthquake>() { // Create a comparator to sort by proximity
                @Override
                public int compare(Earthquake e1, Earthquake e2) {
                    double distance1 = e1.distanceTo(longitude, latitude); // Compare earthquakes based on their distance to the input 
                    double distance2 = e2.distanceTo(longitude, latitude);

                    
                    return Double.compare(distance1, distance2); // Sort by distance - closest earthquakes first
                }
            };
        Collections.sort(earthquakes, proximityComparator);         // Using comparator to sort the earthquakes list

        for (Earthquake e : this.earthquakes) {
            double distance = e.distanceTo(longitude, latitude);
            UI.println(e + " - Distance: " + distance + " km"); // Print the sorted earthquakes with their distance to the location
        }
        UI.println("------------------------");
    }

    /**
     * Add the buttons
     */
    public void setupGUI(){
        UI.initialise();
        UI.addButton("Load", this::loadData);
        UI.addButton("sort by ID",  this::sortByID);
        UI.addButton("sort by Magnitude",  this::sortByMagnitude);
        UI.addButton("sort by Time",  this::sortByTime);
        UI.addButton("sort by Region", this::sortByRegion);
        UI.addButton("sort by Proximity", this::sortByProximity);
        UI.addButton("Quit", UI::quit);
        UI.setWindowSize(900,400);
        UI.setDivider(1.0);  //text pane only 
    }

    public static void main(String[] arguments){
        EarthquakeSorter obj = new EarthquakeSorter();
        obj.setupGUI();

        //WRITE HERE WHICH PARTS OF THE ASSIGNMENT YOU HAVE COMPLETED
        // so the markers know what to look for.
        UI.println("""
        
         I have done this assignment; up to challenge   :)
      
         --------------------
         """);

    }   

    public void loadData(){
        this.loadData(UIFileChooser.open("Choose data file"));
    }

    public void sortByProximity(){
        UI.clearText();
        this.sortByProximity(UI.askDouble("Give longitude: "), UI.askDouble("Give latitude: "));
    }

}
