import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.*;



// TODO: Need to figure out how to import org.json.*. Might have to manually download the JAR file and add to my classpath.
public class Application {

    public static void main(String[] args) {
        // builds UI
        buildUI();
        // gets weather data
        getWeatherData();
        // parses and displays data


    }

    public static void buildUI(){
        // creates main frame
        JFrame applicationFrame = new JFrame("Weather App");
        applicationFrame.setSize(1600, 900);
        applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // creates location selection panel
        JPanel locationPanel = new JPanel();
        locationPanel.setPreferredSize(new Dimension(300,850));
        locationPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        // creates weather display panel
        JPanel weatherPanel = new JPanel();
        weatherPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // adds panels to frame
        applicationFrame.getContentPane().add(locationPanel, BorderLayout.WEST);
        applicationFrame.getContentPane().add(weatherPanel, BorderLayout.CENTER);

        // toggles frame visiblity
        applicationFrame.setVisible(true);
    }


    public static String getLocation() {
        // location detection logic here


        // debugging w/ a static location
        String debugLocation = "Chicago";
        return debugLocation;
    }

    public static String getWeatherData() {
        try {
            String weatherAPIKey = "8bb46e6e7ee445d3875222324240702"; // key
            String location = getLocation(); // location

            // creates a URL object from the API URL
            URL url = new URL("http://api.weatherapi.com/v1/current.json?key=" + weatherAPIKey + "&q=" + location + "&aqi=no");

            // creates a buffered reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));

            // reads data
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            return sb.toString();
        } catch (MalformedURLException e) {
            System.out.println("The URL is not formatted correctly.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred while reading from the URL.");
            e.printStackTrace();
        }

        return null;
    }


    public static void parseAndDisplayData(String data) {
        // parse

        // display
    }
}


