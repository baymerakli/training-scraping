import com.google.gson.Gson;
import com.jaunt.*;
import model.DailyBundle;
import model.Entry;
import org.joda.time.LocalDate;
import org.jsoup.Jsoup;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Created by Samet (Enderun) on 8/26/2016.
 */
public class EntryScraper {

    private static final String baseAdress = "https://www.eksisozluk.com";

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

    public static void scrapeWithJSoup() {
        try {
            org.jsoup.nodes.Document doc = Jsoup.connect("https://eksisozluk.com/debe").get();

            org.jsoup.select.Elements elements = doc.getElementsByAttributeValue("class", "stats topic- list").select("li");

            DailyBundle dailyBundle = new DailyBundle();
            dailyBundle.setDate(LocalDate.now().toString());
            dailyBundle.setWebsite("eksisozluk.com");
            for (org.jsoup.nodes.Element element : elements) {
                String link = element.select("a").attr("href");
                doc = Jsoup.connect(baseAdress + link).get();
                Entry entry = new Entry();
                entry.setTitle(doc.select("span[itemprop]").text());
                entry.setBody(doc.select("div[class=content]").text());
                entry.setEntryDate(doc.select("a[class=entry-date permalink]").text());
                entry.setAuthor(doc.select("a[class=entry-author]").text());
                entry.setWebsite("eksisozluk");
                entry.setWebLink(baseAdress + doc.select("a[class=entry-date permalink]").attr("href"));
                dailyBundle.getEntryList().add(entry);
            }


            Gson gson = new Gson();
            String json = gson.toJson(dailyBundle);
            Writer writer = new FileWriter("C:\\Development\\" + dailyBundle.getWebsite() + "_" + dailyBundle.getDate() + ".json");
            writer.write(json);
            writer.close();
            System.out.println("sada");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
