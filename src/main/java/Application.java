import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.*;

//TODO: Maybe think about moving weather details into a "Weather" class so that I can just update attributes of an instance of the class when I parse/extract
public class Application {

    public static void main(String[] args) {
        Weather weather = new Weather(); // declares and instantiates weather

        // -------------------------------------- UI STUFF STARTS HERE!
        // creates main frame
        JFrame applicationFrame = new JFrame("Weather App");
        applicationFrame.setSize(900, 450);
        applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // -------------------------------------- LOCATION PANEL STUFF
        // creates location selection panel
        JPanel locationPanel = new JPanel();
        locationPanel.setLayout(new GridBagLayout());
        locationPanel.setLayout(new BoxLayout(locationPanel, BoxLayout.Y_AXIS)); // sets locationPanel to a column arrangement
        locationPanel.setPreferredSize(new Dimension(200,400));
        locationPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

        // contents of locationPanel
        JPanel locationSelectSubPanel = new JPanel(); // creates locationSelectSubPanel
        locationSelectSubPanel.setLayout(new FlowLayout (FlowLayout.RIGHT)); // right-orients locationSelectSubPanel
        JTextField zipCodeInput = new JTextField(8); // zip code input field
        //TODO: NEED TO WORK OUT A WAY TO LIMIT ZIP CODE INPUT CHARACTERS TO 5 AND ADD ERROR CHECKING
        JLabel zipCodeLabel = new JLabel("Zip Code:"); // zip code label
        JButton detectLocationButton = new JButton("Detect Location"); // detect location button

        // adds contents to locationSelectSubPanel
        locationSelectSubPanel.add(zipCodeLabel); // adds zipCodeLabel to locationPanel
        locationSelectSubPanel.add(zipCodeInput); // adds zipCodeInput to locationPanel
        locationSelectSubPanel.add(detectLocationButton); // adds detectLocationButton to locationPanel\

        // adds locationSelectSubPanel to locationPanel
        locationPanel.add(locationSelectSubPanel);


        // -------------------------------------- WEATHER PANEL STUFF
        // creates weather display panel
        JPanel weatherPanel = new JPanel();
        weatherPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        // adds panels to frame
        applicationFrame.getContentPane().add(locationPanel, BorderLayout.WEST);
        applicationFrame.getContentPane().add(weatherPanel, BorderLayout.CENTER);

        // toggles frame visiblity
        applicationFrame.setVisible(true);

        // -------------------------------------- BUTTON LOGIC
        detectLocationButton.addActionListener(e -> {
            // gets weather data
            String data = getWeatherData();
            // parses and displays data
            parseAndDisplayData(data, weather, weatherPanel, locationPanel, locationSelectSubPanel);
        });

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

    public static void parseAndDisplayData(String data, Weather weather, JPanel weatherPanel, JPanel locationPanel, JPanel locationSelectSubPanel) {
        // parse
        JSONObject jsonObject = new JSONObject(data);
        JSONObject location = jsonObject.getJSONObject("location"); // location header - use this to pull location data
        JSONObject currentWeather = jsonObject.getJSONObject("current"); // current header - use this to pull current weather data

        // extract details
        weather.setCityName(location.getString("name")); // name of location
        weather.setStateName(location.getString("region")); // name of region/state
        weather.setTemperatureC(currentWeather.getDouble("temp_c")); // temp celsius
        weather.setTemperatureF(currentWeather.getDouble("temp_f")); // temp fahrenheit
        weather.setCondition(currentWeather.getJSONObject("condition").getString("text")); // weather condition
        weather.setWindMPH(currentWeather.getDouble("wind_mph")); // wind MPH
        weather.setWindKPH(currentWeather.getDouble("wind_kph")); // wind KPH
        weather.setWindDirection(currentWeather.getString("wind_dir")); // wind direction
        weather.setPrecipMM(currentWeather.getDouble("precip_mm")); // precipitation (mm)
        weather.setPrecipIN(currentWeather.getDouble("precip_in")); // preciptation (inches)
        weather.setHumidity(currentWeather.getDouble("humidity")); // humidity
        weather.setCloudCoverage(currentWeather.getDouble("cloud")); // cloud coverage
        weather.setFeelsLikeC(currentWeather.getDouble("feelslike_c")); // feels like (c)
        weather.setFeelsLikeF(currentWeather.getDouble("feelslike_f")); // feels like (f)
        weather.setVisionKM(currentWeather.getDouble("vis_km")); // vision (km)
        weather.setVisionMiles(currentWeather.getDouble("vis_miles")); // vision (miles)

        // creates labels for weather data
        JLabel locationLabel = new JLabel("Location: " + weather.getCityName() + ", " + weather.getStateName());
        JLabel temperatureLabel = new JLabel("Temperature: " + weather.getTemperatureF() + "°F");
        JLabel conditionLabel = new JLabel("Condition: " + weather.getCondition());

        // sets label dimensions/alignment
        locationLabel.setPreferredSize((new Dimension(190, 50)));
        locationLabel.setHorizontalAlignment(JLabel.RIGHT);
        temperatureLabel.setPreferredSize(new Dimension(190, 50));
        temperatureLabel.setHorizontalAlignment(JLabel.CENTER);
        conditionLabel.setPreferredSize(new Dimension(190, 50));
        conditionLabel.setHorizontalAlignment(JLabel.CENTER);

        // adds labels to weather panel
        locationSelectSubPanel.add(locationLabel);
        weatherPanel.add(temperatureLabel);
        weatherPanel.add(conditionLabel);

        // refresh the frame
        locationPanel.revalidate();
        locationPanel.repaint();
        weatherPanel.revalidate();
        weatherPanel.repaint();
    }
}


