import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import com.formdev.flatlaf.FlatDarculaLaf;
import org.json.*;

public class Application {
// TODO: Need to work on getting weatherConditionImage to display
    public static void main(String[] args) {
        Weather weather = new Weather(); // declares and instantiates weather
        int[] cOrF = new int[1]; // holds binary value for determining whether user has selected C or F

        // ----------------------------------------------------------------------------------- START OF UI SECTION
        // sets look and feel to utilize flatlaf
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(null, "Failed to initialize LaF");
            }
            // sets UI font
            Font locationFont = new Font("SansSerif", Font.BOLD, 16);
            Font temperatureFont = new Font("SansSerif", Font.BOLD, 22);
            Font conditionFont = new Font("SansSerif", Font.BOLD, 12);

            // creates main frame
            JFrame applicationFrame = new JFrame("Weather App");
            applicationFrame.setSize(900, 600);
            applicationFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
            applicationFrame.setResizable(false);
            applicationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // -------------------------------------- LOCATION PANEL STUFF
            // creates location selection panel
            JPanel locationPanel = new JPanel(new GridBagLayout());
            locationPanel.setLayout(new FlowLayout(FlowLayout.RIGHT)); // sets locationPanel to right-orient
            locationPanel.setMaximumSize(new Dimension(890,50));
            //locationPanel.setBorder(new LineBorder(Color.BLACK, 1)); // border for debugging. remove before final build!!!

            // contents of locationPanel
            JPanel locationSelectSubPanel = new JPanel(); // creates locationSelectSubPanel
            locationSelectSubPanel.setLayout(new FlowLayout (FlowLayout.RIGHT)); // right-orients locationSelectSubPanel
            locationSelectSubPanel.setPreferredSize(new Dimension(870, 50));
            //locationSelectSubPanel.setBorder(new LineBorder(Color.BLACK, 1)); // border for debugging. remove before final build!!!
            JTextField searchInput = new JTextField(20); // city/zip code search input field
            String searchInputDefaultText = "Enter City Name or Postal Code"; // default searchbox text
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

            // -------------------------------------- LOCATION INFO PANEL STUFF
            JPanel locationInfoPanel = new JPanel();
            locationInfoPanel.setPreferredSize(new Dimension(550, 380));
            locationInfoPanel.setLayout(new BoxLayout(locationInfoPanel, BoxLayout.Y_AXIS));
            //locationInfoPanel.setBorder(new LineBorder(Color.BLACK, 1)); // border for debugging. remove before final build!!!

            // creates labels for weather data
            JLabel locationLabel = new JLabel(); // location indicator
            JLabel temperatureLabel = new JLabel(); // temp indicator
            JLabel conditionLabel = new JLabel(); // condition indicator

            // sets label alignment and font
            locationLabel.setHorizontalAlignment(JLabel.CENTER);
            locationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            locationLabel.setFont(locationFont);
            temperatureLabel.setHorizontalAlignment(JLabel.CENTER);
            temperatureLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            temperatureLabel.setFont(temperatureFont);
            conditionLabel.setHorizontalAlignment(JLabel.CENTER);
            conditionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            conditionLabel.setFont(conditionFont);

            // contents of locationInfoPanel
            JPanel currentWeatherSubPanel = new JPanel() ;
            currentWeatherSubPanel.setLayout(new BoxLayout(currentWeatherSubPanel, BoxLayout.Y_AXIS));
            //currentWeatherSubPanel.setLayout(new BoxLayout(currentWeatherSubPanel, BoxLayout.Y_AXIS));
            currentWeatherSubPanel.setPreferredSize(new Dimension (700, 500));
            //currentWeatherSubPanel.setBorder(new LineBorder(Color.BLACK, 1)); // border for debugging. remove before final build!!!
            currentWeatherSubPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
            ImageIcon weatherConditionImage = null;
            JLabel weatherConditionImageLabel = new JLabel(weatherConditionImage);
            weatherConditionImageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // creates current weather display boxes and adds their child items
            JPanel locationNameDisplayBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1)) {
                @Override public Dimension getMaximumSize(){
                    return getPreferredSize();
                }

            };
            locationNameDisplayBox.setPreferredSize(new Dimension(600,30));
            locationNameDisplayBox.setBorder(new EmptyBorder(0,0,0,0));
            //locationNameDisplayBox.setBorder(new LineBorder(Color.BLACK, 1)); // border for debugging. remove before final build!!!
            locationNameDisplayBox.add(locationLabel);
            JPanel weatherImageDisplayBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 1, 1)) {
                @Override
                public Dimension getMaximumSize() {
                    return getPreferredSize();
                }
            };
            weatherImageDisplayBox.setPreferredSize(new Dimension(250,220));
            //weatherImageDisplayBox.setBorder(new LineBorder(Color.BLACK, 1)); // border for debugging. remove before final build!!!
            weatherImageDisplayBox.add(weatherConditionImageLabel);
            JPanel weatherConditionDisplayBox = new JPanel();
            /*JPanel weatherConditionDisplayBox = new JPanel() {
                @Override
                public Dimension getMaximumSize() {
                    return getPreferredSize();
                }
            };;*/
            //weatherConditionDisplayBox.setBorder(new LineBorder(Color.BLACK, 1)); // border for debugging. remove before final build!!!
            weatherConditionDisplayBox.add(conditionLabel);
            JPanel temperatureDisplayBox = new JPanel() {
                @Override
                public Dimension getMaximumSize() {
                    return getPreferredSize();
                }
            };;
            //temperatureDisplayBox.setBorder(new LineBorder(Color.BLACK, 1)); // border for debugging. remove before final build!!!
            temperatureDisplayBox.add(temperatureLabel);

            // adds objects to currentWeatherSubPanel
            currentWeatherSubPanel.add(locationNameDisplayBox);
            currentWeatherSubPanel.add(temperatureDisplayBox);
            currentWeatherSubPanel.add(weatherImageDisplayBox);
            currentWeatherSubPanel.add(weatherConditionDisplayBox);

            // adds currentWeatherSubPanel to locationInfoPanel
            locationInfoPanel.add(currentWeatherSubPanel);

            // -------------------------------------- FORECAST PANEL STUFF
            // creates weather display panel
            JPanel forecastPanel = new JPanel();
            forecastPanel.setLayout(new BoxLayout(forecastPanel, BoxLayout.Y_AXIS));
            forecastPanel.setPreferredSize(new Dimension(880, 90));
            //forecastPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            //forecastPanel.setBorder(new LineBorder(Color.BLACK, 1)); // border for debugging. remove before final build!!!

            // weather panel contents
            //TODO: Get 5-day forecast panel in place

            // adds all panels to frame
            applicationFrame.add(locationPanel);
            applicationFrame.add(locationInfoPanel);
            applicationFrame.add(forecastPanel);

            // toggles frame visiblity
            applicationFrame.setVisible(true);

            // ----------------------------------------------------------------------------------- END OF UI SECTION
            int temperatureStyle;
            final String[] locationArray = new String[1];
            String location = "";


            // -------------------------------------- BUTTON LOGIC
            // temperature label C/F switcher
            temperatureLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (cOrF[0] <= 0){
                        cOrF[0] = 1;
                    }
                    else {
                        cOrF[0] = 0;
                    }
                }
            });
            temperatureStyle = cOrF[0]; // sets variable for use in other methods

            // detect button
            detectLocationButton.addActionListener(e -> {
                if (searchInput.getText().equals(searchInputDefaultText)) {
                    locationArray[0] = "";
                }
                else {
                    locationArray[0] = searchInput.getText();
                }
                String locationString = locationArray[0];
                // gets weather data
                String data = getWeatherData(locationString);
                // parses and displays data
                parseAndDisplayData(data, weather, temperatureStyle, locationInfoPanel, locationLabel, temperatureLabel, conditionLabel, weatherConditionImageLabel, weatherConditionImage);
            });


            // gets weather data -- FOR DEBUG PURPOSES ONLY. DELETE AFTER UI IS DONE!!!
            String data = getWeatherData(location);
            // parses and displays data -- FOR DEBUG PURPOSES ONLY. DELETE AFTER UI IS DONE!!!
            parseAndDisplayData(data, weather, temperatureStyle, locationInfoPanel, locationLabel, temperatureLabel, conditionLabel, weatherConditionImageLabel, weatherConditionImage);

            locationInfoPanel.revalidate();
            locationInfoPanel.repaint();
            //detectLocationButton.requestFocusInWindow(); // sets focus to button so that search box behavior is not broken
            applicationFrame.getRootPane().requestFocus();
            applicationFrame.getRootPane().setDefaultButton(detectLocationButton);
        });

    }

    // METHODS
    public static String getWeatherData(String location) {
        try {
            String weatherAPIKey = "8bb46e6e7ee445d3875222324240702"; // key
            if (location.isEmpty()) {
                location = getLocation(); // location
            }
            location = location.replace(" ", "+");

            // creates a URL object from the API URL
            URL url = new URL("https://api.weatherapi.com/v1/current.json?key=" + weatherAPIKey + "&q=" + location + "&aqi=no");

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

    public static void parseAndDisplayData(String data, Weather weather, Integer temperatureStyle, JPanel locationInfoPanel, JLabel locationLabel, JLabel temperatureLabel, JLabel conditionLabel, JLabel weatherConditionImageLabel, ImageIcon weatherConditionImage) {
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
        weather.setDay(currentWeather.getInt("is_day")); // day/night boolean
        weather.setVisionKM(currentWeather.getDouble("vis_km")); // vision (km)
        weather.setVisionMiles(currentWeather.getDouble("vis_miles")); // vision (miles)

        // updates outputs with weather data
        locationLabel.setText((weather.getCityName() + ", " + weather.getStateName()));
        // checks whether user has chosen to use C or F
        if (temperatureStyle == 0) {
            temperatureLabel.setText(weather.getTemperatureF() + "°F");
        }
        else {
            temperatureLabel.setText(weather.getTemperatureC() + "°C");
        }

        conditionLabel.setText(weather.getCondition());
        weatherConditionImage = getImageIcon(weather);
        weatherConditionImageLabel.setIcon(weatherConditionImage);
        locationInfoPanel.revalidate();
        locationInfoPanel.repaint();
    }

    //TODO: NEED TO SET UP THE REST OF THE WEATHER ICONS
    public static ImageIcon getImageIcon(Weather weather) {
        File file = new File("");
        ImageIcon weatherConditionImage = null;

        try {
            if (weather.getCondition().equalsIgnoreCase("sunny")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/113.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/113.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("clear")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/113.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/113.png");
                }
            }
            else if (weather.getCondition().equals("Partly cloudy")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/116.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/116.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("cloudy")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/119.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/119.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("overcast")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/122.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/122.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("mist")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/143.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/143.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("patchy rain possible")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/176.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/176.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("patchy rain nearby")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/176.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/176.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("patchy snow possible")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/179.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/179.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("patchy sleet possible")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/182.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/182.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("patchy freezing drizzle possible")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/185.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/185.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("thundery outbreaks possible")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/200.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/200.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("blowing snow")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/227.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/227.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("blizzard")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/230.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/230.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("fog")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/248.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/248.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("freezing fog")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/260.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/260.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("patchy light drizzle")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/263.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/263.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("light drizzle")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/266.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/266.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("freezing drizzle")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/281.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/281.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("heavy freezing drizzle")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/284.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/284.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("patchy light rain")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/293.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/293.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("light rain")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/296.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/296.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("moderate rain at times")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/299.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/299.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("moderate rain")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/302.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/302.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("heavy rain at times")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/305.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/305.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("heavy rain")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/308.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/308.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("light freezing rain")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/311.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/311.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("moderate or heavy freezing rain")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/314.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/314.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("light sleet")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/317.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/317.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("moderate or heavy sleet")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/320.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/320.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("patchy light snow")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/323.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/323.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("light snow")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/326.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/326.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("patchy moderate snow")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/329.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/329.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("moderate snow")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/332.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/332.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("patchy heavy snow")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/335.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/335.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("heavy snow")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/338.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/338.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("ice pellets")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/350.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/350.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("light rain shower")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/353.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/353.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("moderate or heavy rain shower")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/356.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/356.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("torrential rain shower")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/359.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/359.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("light sleet showers")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/362.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/362.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("moderate or heavy sleet showers")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/365.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/365.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("light snow showers")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/368.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/368.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("moderate or heavy snow showers")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/371.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/371.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("light showers of ice pellets")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/374.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/374.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("moderate or heavy showers of ice pellets")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/377.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/377.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("patchy light rain with thunder")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/386.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/386.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("moderate or heavy rain with thunder")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/389.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/389.png");
                }
            }
            else if (weather.getCondition().equalsIgnoreCase("patchy light snow with thunder")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/392.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/392.png");
                }
            }else if (weather.getCondition().equalsIgnoreCase("moderate or heavy snow with thunder")) {
                if (weather.getDay() == 0) {
                    file = new File("resources/images/WeatherIcons/night/395.png");
                }
                else {
                    file = new File("resources/images/WeatherIcons/day/395.png");
                }
            }
            else {
                // display image error
                JOptionPane.showMessageDialog(null, "No Weather Condition Image Available");
            }

            BufferedImage bufferedImage = ImageIO.read(file);
            weatherConditionImage = new ImageIcon(bufferedImage);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return weatherConditionImage;
    }
}


