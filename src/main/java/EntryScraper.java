import com.google.gson.Gson;
import com.jaunt.*;
import model.DailyBundle;
import model.Entry;
import org.joda.time.LocalDate;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by Samet (Enderun) on 8/26/2016.
 */
public class EntryScraper {

    public static void scrape() {
        try {
            UserAgent userAgent = new UserAgent();                       //create new userAgent (headless browser).
            userAgent.visit("https://eksisozluk.com/debe");                        //visit a url

            Elements debeList = userAgent.doc.findFirst("<ol class='stats topic-list'/>").findEvery("<li/>");
            DailyBundle dailyBundle = new DailyBundle();
            dailyBundle.setDate(LocalDate.now().toString());
            dailyBundle.setWebsite("eksisozluk.com");
            for (Element element : debeList) {
                userAgent.visit(element.getFirst("<a/>").getAt("href"));
                Element content = userAgent.doc.findFirst("<div class='content'");
                Entry entry = new Entry();
                entry.setBody(content.getText());
                dailyBundle.getEntryList().add(entry);
            }


            Gson gson = new Gson();
            String json = gson.toJson(dailyBundle);
            Writer writer = new FileWriter("C:\\Development\\" + dailyBundle.getWebsite() + "_" + dailyBundle.getDate() + ".json");
            writer.write(json);
            writer.close();

        } catch (ResponseException | NotFound | IOException e) {
            e.printStackTrace();
        }
    }
}
