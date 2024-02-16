public class Weather {

    // attributes
    private String cityName; // name of location
    private String stateName; // name of region/state
    private double temperatureC; // temp celsius
    private double temperatureF; // temp fahrenheit
    private String condition; // weather condition
    private double windMPH; // wind MPH
    private double windKPH; // wind KPH
    private String windDirection; // wind direction
    private double precipMM; // precipitation (mm)
    private double precipIN; // preciptation (inches)
    private double humidity; // humidity
    private double cloudCoverage; // cloud coverage
    private double feelsLikeC; // feels like (c)
    private double feelsLikeF; // feels like (f)
    private double visionKM; // vision (km)
    private double visionMiles; // vision (miles)

    // getters and setters

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public double getTemperatureC() {
        return temperatureC;
    }

    public void setTemperatureC(double temperatureC) {
        this.temperatureC = temperatureC;
    }

    public double getTemperatureF() {
        return temperatureF;
    }

    public void setTemperatureF(double temperatureF) {
        this.temperatureF = temperatureF;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getWindMPH() {
        return windMPH;
    }

    public void setWindMPH(double windMPH) {
        this.windMPH = windMPH;
    }

    public double getWindKPH() {
        return windKPH;
    }

    public void setWindKPH(double windKPH) {
        this.windKPH = windKPH;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public double getPrecipMM() {
        return precipMM;
    }

    public void setPrecipMM(double precipMM) {
        this.precipMM = precipMM;
    }

    public double getPrecipIN() {
        return precipIN;
    }

    public void setPrecipIN(double precipIN) {
        this.precipIN = precipIN;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getCloudCoverage() {
        return cloudCoverage;
    }

    public void setCloudCoverage(double cloudCoverage) {
        this.cloudCoverage = cloudCoverage;
    }

    public double getFeelsLikeC() {
        return feelsLikeC;
    }

    public void setFeelsLikeC(double feelsLikeC) {
        this.feelsLikeC = feelsLikeC;
    }

    public double getFeelsLikeF() {
        return feelsLikeF;
    }

    public void setFeelsLikeF(double feelsLikeF) {
        this.feelsLikeF = feelsLikeF;
    }

    public double getVisionKM() {
        return visionKM;
    }

    public void setVisionKM(double visionKM) {
        this.visionKM = visionKM;
    }

    public double getVisionMiles() {
        return visionMiles;
    }

    public void setVisionMiles(double visionMiles) {
        this.visionMiles = visionMiles;
    }
}
