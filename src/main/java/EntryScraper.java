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
                String link = element.getFirst("<a/>").getAt("href");
                userAgent.visit(link);
                Element title = userAgent.doc.findFirst("<span itemprop='name'");
                Element body = userAgent.doc.findFirst("<div class='content'");
                Element entryDate = userAgent.doc.findFirst("<a class='entry-date permalink'");
                Element author = userAgent.doc.findFirst("<a class='entry-author'");
                Entry entry = new Entry();
                entry.setTitle(title.getText());
                entry.setBody(body.getText());
                entry.setEntryDate(entryDate.getText());
                entry.setAuthor(author.getText());
                entry.setWebsite("eksisozluk");
                entry.setWebLink(link);
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
