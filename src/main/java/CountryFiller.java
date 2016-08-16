import com.google.gson.Gson;
import com.jaunt.*;
import com.jaunt.component.Form;
import model.City;
import model.Country;
import model.State;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class CountryFiller {

    private final static String diyanetURL = "http://www.diyanet.gov.tr/tr/PrayerTime/WorldPrayerTimes";

    public static void getCountries() {
        try {
            UserAgent userAgent = new UserAgent();                       //create new userAgent (headless browser).
            userAgent.visit(diyanetURL);                        //visit a url

            Form form = userAgent.doc.getForm(1);
            List<Country> countries = new ArrayList<>();

            Element countrySelect = userAgent.doc.findFirst("<select id='Country'>");
            Elements countryList = countrySelect.findEvery("<option>");

            for (Element countryElement : countryList) {
                Country country = new Country();
                country.setId(countryElement.getAt("value"));
                country.setName(countryElement.innerHTML());
                countries.add(country);
                form.setSelect("country", country.getId());
                form.submit();
                form = userAgent.doc.getForm(1);
                Elements stateList = userAgent.doc.findFirst("<select id='State'>").findEvery("<option>");

                for (Element stateElement : stateList) {
                    if (stateElement.getAt("value").equals("")) {
                        continue;
                    }
                    State state = new State();
                    state.setName(stateElement.innerHTML());
                    state.setId(stateElement.getAt("value"));
                    country.getStateList().add(state);
                    form.setSelect("state", state.getId());
                    form.submit();
                    form = userAgent.doc.getForm(1);

                    if (!userAgent.doc.findFirst("<div id='cityHtml'>").getAt("style").equals("display: none")) {
                        Elements cityList = userAgent.doc.findFirst("<select id='City'>").findEvery("<option>");

                        for (Element cityElement : cityList) {
                            if (cityElement.getAt("value").equals("")) {
                                continue;
                            }
                            City city = new City();
                            city.setName(cityElement.innerHTML());
                            city.setId(cityElement.getAt("value"));
                            state.getCityList().add(city);
                        }
                    }
                }
            }

            Gson gson = new Gson();
            String json = gson.toJson(countries);
            Writer writer = new FileWriter("C:\\Development\\file.json");
            writer.write(json);
            writer.close();
        } catch (ResponseException | IOException | NotFound e) {
            e.printStackTrace();
        }
    }
}
