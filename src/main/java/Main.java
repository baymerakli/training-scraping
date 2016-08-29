import com.google.gson.Gson;
import com.jaunt.*;
import com.jaunt.component.Form;
import model.City;
import model.Country;
import model.Entry;
import model.State;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

//        CountryFiller.scrape();
//        PrayTimeFiller.scrape();
//
//        City city = new City();
//        city.setId("9366");
//        city.setName("YAPRAKLI");
//        PrayTimeFiller.getCityWithPrayTimes("2","522",city);

        EntryScraper.scrape();


    }

}
