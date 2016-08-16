import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.jaunt.Element;
import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;
import com.jaunt.component.Table;
import model.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class PrayTimeFiller {

    private final static String prayTimeURL = "http://www.diyanet.gov.tr/tr/PrayerTime/PrayerTimesList";

    public static void startFilling() {
        try {
            System.out.println(System.currentTimeMillis());
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(PrayTimeFiller.class.getResource("countryList.json").getFile()));
            Country[] countryArray = gson.fromJson(reader, Country[].class);
            System.out.println(System.currentTimeMillis());
            List<Country> countries = Arrays.asList(countryArray);

            for (Country country : countries) {
                for (State state : country.getStateList()) {
                    if (state.getCityList().isEmpty()) {
                        getStateWithPrayTimes(country.getId(), state);
                    } else {
                        for (City city : state.getCityList()) {
                            getCityWithPrayTimes(country.getId(), state.getId(), city);
                        }
                    }
                }
            }

            System.out.println("a");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void fillPrayTimes(Table table, Base base) {
        for (int i = 1; i <= 31; i++) {
            PrayTime prayTime = new PrayTime();
            Element row = table.getRow(i);
            String tarih = row.getChildElements().get(0).innerText();
            prayTime.setImsak(row.getChildElements().get(1).innerText());
            prayTime.setGunes(row.getChildElements().get(2).innerText());
            prayTime.setOgle(row.getChildElements().get(3).innerText());
            prayTime.setIkindi(row.getChildElements().get(4).innerText());
            prayTime.setAksam(row.getChildElements().get(5).innerText());
            prayTime.setYatsi(row.getChildElements().get(6).innerText());
            prayTime.setKible(row.getChildElements().get(7).innerText());
            base.getPrayTimeMap().put(tarih, prayTime);
        }
    }

    private static String getParameterString(String country, String state, String city) {
        return "Country=" + country + "&State=" + state + "&City=" + city + "&period=Aylik";
    }

    public static State getStateWithPrayTimes(String countryId, State state) {
        try {
            UserAgent userAgent = new UserAgent();                       //create new userAgent (headless browser).
            String parameterString = getParameterString(countryId, state.getId(), null);
            userAgent.sendPOST(prayTimeURL, parameterString);
            Table table = userAgent.doc.getTable("<table class=form>");   //get Table component via search query
            fillPrayTimes(table, state);
        } catch (ResponseException | NotFound e) {
            e.printStackTrace();
        }
        return state;
    }

    public static City getCityWithPrayTimes(String countryId, String stateId, City city) {
        try {
            UserAgent userAgent = new UserAgent();
            String parameterString = getParameterString(countryId, stateId, city.getId());
            userAgent.sendPOST(prayTimeURL, parameterString);
            Table table = userAgent.doc.getTable("<table class=form>");   //get Table component via search query
            fillPrayTimes(table, city);
        } catch (ResponseException | NotFound e) {
            e.printStackTrace();
        }
        return city;
    }


}
