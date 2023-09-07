import org.apache.http.HttpEntity; // OpenWeatherMap imports
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;//JSON
import com.google.gson.JsonParser;

import java.io.*;

import java.text.DecimalFormat;
import  java.util.Scanner;

//basic plan:
//
public class WeatherApp {
    public static void main(String[] args) {
        Scanner scanner= new Scanner(System.in);
        boolean loggedIn = true;
        int option = 0;
        while(loggedIn) {
            getCities();
            System.out.print("What would you like to do?\n1.Add City\n2.Remove City\n3.Exit");
            option = scanner.nextInt();
            switch (option){
                case 1:
                    addCity();
                case 2:
                    removeCity();
                case 3:
                    loggedIn = false;
            }

        }
    }

    public static void addCity(){
        Scanner input = new Scanner(System.in);
        System.out.println("What city would you like to add?");
        String city = input.nextLine();
        JsonObject info = cityInfo(city);
        if(info != null) {
            System.out.println("Is this the city?");
            System.out.println(info);
            try (FileWriter fw = new FileWriter("UserCities.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {
                 out.println("the text");

            } catch (IOException e) {

            }
        }
    }

    public static void removeCity(){

    }

    public static void getCities() {
        System.out.println("Your cities:");
        try {
            File myObj = new File("/Users/bengray/Code/Java/Weather/src/main/java/UserCities.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                JsonObject info = cityInfo(data);
                if(info != null) {
                    printCityInfo(data, info);
                }
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }
    public static void printCityInfo(String name, JsonObject info){
        double temperatureKelvin = info.get("temp").getAsDouble();
        double Humidity = info.get("humidity").getAsDouble();
        double temperatureCelsius = temperatureKelvin - 273.15;
        String formattedTemp = new DecimalFormat("#.##").format(temperatureCelsius);
        System.out.println(name + ": " + formattedTemp + " degrees " + "Humidity " + Humidity + "%");
    }
    public static JsonObject cityInfo(String city){
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
                //System.out.println(responseBody); // Print the weather data (you'll parse this later
                // )
                JsonParser jsonParser = new JsonParser();
                JsonObject jsonObject = jsonParser.parse(responseBody).getAsJsonObject();

                JsonObject main = jsonObject.getAsJsonObject("main");
                return main;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
