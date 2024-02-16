import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.*;
import javax.servlet.http.HttpServletRequest;

public class Application {

    public static void main(String[] args) {
        // -------------------------------------- UI STUFF STARTS HERE!
        // creates main frame
        JFrame applicationFrame = new JFrame("Weather App");
        applicationFrame.setSize(1600, 900);
        applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // -------------------------------------- LOCATION PANEL STUFF
        // creates location selection panel
        JPanel locationPanel = new JPanel();
        locationPanel.setPreferredSize(new Dimension(300,850));
        locationPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        // contents of locationPanel
        JTextField zipCodeInput = new JTextField(20); // zip code input field
        JLabel zipCodeLabel = new JLabel("Zip Code:"); // zip code label
        JButton detectLocationButton = new JButton("Detect Location"); // detect location button

        // adds contents to locationPanel
        locationPanel.add(zipCodeLabel); // adds zipCodeLabel to locationPanel
        locationPanel.add(zipCodeInput); // adds zipCodeInput to locationPanel
        locationPanel.add(detectLocationButton); // adds detectLocationButton to locationPanel


        // -------------------------------------- WEATHER PANEL STUFF
        // creates weather display panel
        JPanel weatherPanel = new JPanel();
        weatherPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // adds panels to frame
        applicationFrame.getContentPane().add(locationPanel, BorderLayout.WEST);
        applicationFrame.getContentPane().add(weatherPanel, BorderLayout.CENTER);

        // toggles frame visiblity
        applicationFrame.setVisible(true);

        // gets weather data
        String data = getWeatherData();

        // parses and displays data
        parseAndDisplayData(data, weatherPanel, locationPanel);
    }



    // METHODS
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
            JOptionPane.showMessageDialog(null,"The URL is not formatted correctly.");
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"An error occurred while reading from the URL.");
            e.printStackTrace();
        }

        return null;
    }

    public static String getLocation() {
        // location detection logic here
        String location = "Undefined Location";
        // geolocates based on IP address

        // converts IP to a location
        try {
            // creates a URL object from the API URL
            URL url = new URL("http://ip-api.com/json/");
            // creates a buffered reader
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            // reads data
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();

            // parse
            JSONObject jsonObject = new JSONObject(sb.toString());
            String city = jsonObject.getString("city");
            String region = jsonObject.getString("region");

            // extract
            location = city + "+" + region;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Unable to get location.");
        }

        // debugging w/ a static location
        //location = "Chicago";
        return location;
    }

    public static void parseAndDisplayData(String data, JPanel weatherPanel, JPanel locationPanel) {
        // parse
        JSONObject jsonObject = new JSONObject(data);
        JSONObject location = jsonObject.getJSONObject("location"); // location header - use this to pull location data
        JSONObject currentWeather = jsonObject.getJSONObject("current"); // current header - use this to pull current weather data

        // extract details
        String cityName = location.getString("name"); // name of location
        String stateName = location.getString("region"); // name of region/state
        double temperatureC = currentWeather.getDouble("temp_c"); // temp celsius
        double temperatureF = currentWeather.getDouble("temp_f"); // temp fahrenheit
        String condition = currentWeather.getJSONObject("condition").getString("text"); // weather condition
        double windMPH = currentWeather.getDouble("wind_mph"); // wind MPH
        double windKPH = currentWeather.getDouble("wind_kph"); // wind KPH
        String windDirection = currentWeather.getString("wind_dir"); // wind direction
        double precipMM = currentWeather.getDouble("precip_mm"); // precipitation (mm)
        double precipIN = currentWeather.getDouble("precip_in"); // preciptation (inches)
        double humidity = currentWeather.getDouble("humidity"); // humidity
        double cloudCoverage = currentWeather.getDouble("cloud"); // cloud coverage
        double feelsLikeC = currentWeather.getDouble("feelslike_c"); // feels like (c)
        double feelsLikeF = currentWeather.getDouble("feelslike_f"); // feels like (f)
        double visionKM = currentWeather.getDouble("vis_km"); // vision (km)
        double visionMiles = currentWeather.getDouble("vis_miles"); // vision (miles)

        // creates labels for weather data
        JLabel locationLabel = new JLabel("Location: " + cityName + ", " + stateName);
        JLabel temperatureLabel = new JLabel("Temperature: " + temperatureF + "°F");
        JLabel conditionLabel = new JLabel("Condition: " + condition);

        // sets label dimensions/alignment
        locationLabel.setPreferredSize((new Dimension(300, 100)));
        locationLabel.setHorizontalAlignment(JLabel.CENTER);
        temperatureLabel.setPreferredSize(new Dimension(300, 100));
        temperatureLabel.setHorizontalAlignment(JLabel.CENTER);
        conditionLabel.setPreferredSize(new Dimension(300, 100));
        conditionLabel.setHorizontalAlignment(JLabel.CENTER);

        // adds labels to weather panel
        locationPanel.add(locationLabel);
        weatherPanel.add(temperatureLabel);
        weatherPanel.add(conditionLabel);

        // refresh the frame
        weatherPanel.revalidate();
        weatherPanel.repaint();
    }
}


