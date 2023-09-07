import org.apache.http.HttpEntity; // OpenWeatherMap imports
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;//JSON
import com.google.gson.JsonParser;

import java.io.IOException;
import java.text.DecimalFormat;
import  java.util.Scanner;

public class WeatherApp {
    public static void main(String[] args) {
        Scanner scanner= new Scanner(System.in);//ERROR
        boolean passed = false;
        String city = "";
        while(!passed) {
            System.out.print("What city would you like to know the weather of? ");
            city = scanner.next();
            passed = Check(city);
            if(!passed) {
                System.out.println("That is not a valid city.\nPlease check and try again");
            }
        }
        // Replace "YOUR_API_KEY" with your actual OpenWeatherMap API key
        String apiKey = "bf8f764c56058ac0ccdc96141608afbf";// Replace with the city you want to get weather data for

        // Create an HttpClient
        HttpClient httpClient = HttpClients.createDefault();

        // Create an HttpGet request with the API URL
        HttpGet httpGet = new HttpGet("http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey);

        try {
            // Execute the request
            HttpResponse response = httpClient.execute(httpGet);

            // Get the response entity
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Convert the response entity to a string
                String responseBody = EntityUtils.toString(entity);
                System.out.println(responseBody); // Print the weather data (you'll parse this later
                // )
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(responseBody).getAsJsonObject();

                // Extract the temperature (in Kelvin) from the JSON object
                JsonObject main = jsonObject.getAsJsonObject("main");
                double temperatureKelvin = main.get("temp").getAsDouble();
                double Humidity = main.get("humidity").getAsDouble();

                // Convert the temperature to Celsius or Fahrenheit, depending on your preference
                double temperatureCelsius = temperatureKelvin - 273.15; // Convert from Kelvin to Celsius
                double temperatureFahrenheit = (temperatureKelvin - 273.15) * 9 / 5 + 32; // Convert from Kelvin to Fahrenheit
                String i = new DecimalFormat("#.##").format(temperatureCelsius);
                System.out.println("Temperature in Celsius: " + i);
                System.out.println("Temperature in Fahrenheit: " + temperatureFahrenheit);
                System.out.println("Humidity: " + Humidity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean Check(String city){
        String [] cities = {"London", "Paris", "Bordeaux"};
        for (int i = 0; i < cities.length; i++){
            if(city.equalsIgnoreCase(cities[i])){
                return true;
            }
        }

        return false;
    } // only update is to get a list of all the cities in the world and put in like a hash map for better searching
}
