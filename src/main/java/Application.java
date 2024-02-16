import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Flow;

import com.formdev.flatlaf.FlatDarculaLaf;
import org.json.*;

//TODO: Maybe think about moving weather details into a "Weather" class so that I can just update attributes of an instance of the class when I parse/extract
public class Application {

    public static void main(String[] args) {
        Weather weather = new Weather(); // declares and instantiates weather

        // ----------------------------------------------------------------------------------- START OF UI SECTION
        // sets look and feel to utilize flatlaf
        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to initialize LaF");
        }
        // sets UI font
        Font uiFont = new Font("SansSerif", Font.BOLD, 14);

        // creates main frame
        JFrame applicationFrame = new JFrame("Weather App");
        applicationFrame.setSize(900, 450);
        applicationFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
        applicationFrame.setResizable(false);
        applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // -------------------------------------- LOCATION PANEL STUFF
        // creates location selection panel
        JPanel locationPanel = new JPanel();
        locationPanel.setLayout(new GridBagLayout());
        locationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // sets locationPanel to right-orient
        locationPanel.setMaximumSize(new Dimension(900,50));
        //locationPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        locationPanel.setBorder(new LineBorder(Color.BLACK, 1)); // border for debugging. remove before final build!!!

        // contents of locationPanel
        JPanel locationSelectSubPanel = new JPanel(); // creates locationSelectSubPanel
        locationSelectSubPanel.setLayout(new FlowLayout (FlowLayout.RIGHT)); // right-orients locationSelectSubPanel
        locationSelectSubPanel.setPreferredSize(new Dimension(850, 50));
        locationSelectSubPanel.setBorder(new LineBorder(Color.BLACK, 1)); // border for debugging. remove before final build!!!
        JTextField searchInput = new JTextField(20); // city/zip code search input field
        String searchInputDefaultText = "Enter City or ZIP Code"; // default searchbox text
        searchInput.setText(searchInputDefaultText); // sets default searchbox text
        // searchInput default text behavior
        searchInput.addFocusListener(new FocusListener(){
            @Override public void focusGained(FocusEvent e) {
                if (searchInput.getText().equals(searchInputDefaultText)){
                    searchInput.setText("");
                }
            }

            @Override public void focusLost(FocusEvent e) {
                if (searchInput.getText().isEmpty()){
                    searchInput.setText(searchInputDefaultText);
                }
            }
        });

        JButton detectLocationButton = new JButton("Detect Location"); // detect location button

        // adds contents to locationSelectSubPanel
        locationSelectSubPanel.add(searchInput); // adds searchInput to locationPanel
        locationSelectSubPanel.add(detectLocationButton); // adds detectLocationButton to locationPanel\

        // adds locationSelectSubPanel to locationPanel
        locationPanel.add(locationSelectSubPanel);


        // -------------------------------------- WEATHER PANEL STUFF
        // creates weather display panel
        JPanel weatherPanel = new JPanel();
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.Y_AXIS));
        //weatherPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        weatherPanel.setBorder(new LineBorder(Color.BLACK, 1)); // border for debugging. remove before final build!!!

        // -------------------------------------- LOCATION INFO PANEL STUFF
        JPanel locationInfoPanel = new JPanel();
        locationInfoPanel.setPreferredSize(new Dimension(900, 50));
        locationInfoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        locationInfoPanel.setBorder(new LineBorder(Color.BLACK, 1)); // border for debugging. remove before final build!!!
        // contents of locationInfoPanel

        // creates labels for weather data
        JLabel locationLabel = new JLabel();
        JLabel temperatureLabel = new JLabel();
        JLabel conditionLabel = new JLabel();

        // sets label dimensions/alignment
        locationLabel.setPreferredSize((new Dimension(190, 50)));
        locationLabel.setHorizontalAlignment(JLabel.RIGHT);
        temperatureLabel.setPreferredSize(new Dimension(190, 50));
        temperatureLabel.setHorizontalAlignment(JLabel.CENTER);
        conditionLabel.setPreferredSize(new Dimension(190, 50));
        conditionLabel.setHorizontalAlignment(JLabel.CENTER);

        // adds labels to weather panel
        locationInfoPanel.add(locationLabel);
        weatherPanel.add(temperatureLabel);
        weatherPanel.add(conditionLabel);

        // adds all panels to frame
        applicationFrame.add(locationPanel, BorderLayout.NORTH);
        applicationFrame.add(locationInfoPanel);
        applicationFrame.add(weatherPanel);

        // toggles frame visiblity
        applicationFrame.setVisible(true);

        // ----------------------------------------------------------------------------------- END OF UI SECTION

        // -------------------------------------- BUTTON LOGIC
        detectLocationButton.addActionListener(e -> {
            // gets weather data
            String data = getWeatherData();
            // parses and displays data
            parseAndDisplayData(data, weather, locationLabel, temperatureLabel, conditionLabel);
        });

        // gets weather data -- FOR DEBUG PURPOSES ONLY. DELETE AFTER UI IS DONE!!!
        String data = getWeatherData();
        // parses and displays data -- FOR DEBUG PURPOSES ONLY. DELETE AFTER UI IS DONE!!!
        parseAndDisplayData(data, weather, locationLabel, temperatureLabel, conditionLabel);

        detectLocationButton.requestFocusInWindow(); // sets focus to button so that search box behavior is not broken
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
        // resolves location from IP address
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

    public static void parseAndDisplayData(String data, Weather weather, JLabel locationLabel, JLabel temperatureLabel, JLabel conditionLabel) {
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
        locationLabel.setText((weather.getCityName() + ", " + weather.getStateName()));
        temperatureLabel.setText(weather.getTemperatureF() + "°F");
        conditionLabel.setText(weather.getCondition());
    }
}


