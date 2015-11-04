package urlconnection;

import crawler.CrawledSites;
import de.l3s.boilerpipe.BoilerpipeProcessingException;
import de.l3s.boilerpipe.extractors.ArticleExtractor;
import de.l3s.boilerpipe.extractors.DefaultExtractor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pagecontent.FetchPage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLParser {
    
    private final static Pattern Filters= Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    public static synchronized void getLinks(CrawledSites crawledSites,Boolean getContent, String fileName, String outDirectory) throws IOException, BoilerpipeProcessingException {
        String url = crawledSites.getSeedUrl();
        Elements links = null;
        try{
        Document doc= Jsoup.connect(url).get();
        crawledSites.addCrawledSites(url);

        /*String currentDir = System.getProperty("user.dir");
        File visitedFile = new File(currentDir,fileName);
        
        BufferedWriter out = new BufferedWriter
         (new FileWriter(visitedFile));
         out.write(url+"\n");
         out.close();*/
  
        links = doc.select("a[href]");
        }catch(Exception e){
        links=null;
        }
        finally{
               if(links!=null){
        for (Element link : links) {
              if(shouldVisit(link.attr("abs:href").toString())) {
              crawledSites.addListOfSites(link.attr("abs:href").toString());
              Queue<String> listOfSites = crawledSites.getListOfSites();
              //System.out.println(link.attr("abs:href").toString());
              //PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(visitedFile, true)));
        // save url to txt
              if(getContent==true) {
               // writer.println(link.attr("abs:href").toString());
                  FetchPage.getContent(link.attr("abs:href").toString(),crawledSites,listOfSites.size(),outDirectory);
              }
            }
        }
        }
        }
    }

    public static String getDomain(String url) throws MalformedURLException {
              String cleanUrl = url.toLowerCase().trim();
              URL link = new URL(cleanUrl);
              String domain = link.getHost();
              System.out.println(domain);
             return domain;



    }

   public static boolean shouldVisit(String url){
      String cleanUrl = url.toLowerCase().trim();
      return !((Filters.matcher(cleanUrl).matches()));

   }


}
