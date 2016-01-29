package ir.assignments.three;

import java.util.ArrayList;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	
	/**
	 * (Very important for politeness) Wait at least 600ms between page requests 
	 * 	to the same subdomain. You need to set this in the crawler configuration.
	 */
	private final static int POLITENESS_DELAY = 600;
	
	/**
	 * (Very important for getting credit) Set the name of your crawler's User Agent to 
	 * this precise string: UCI Inf141-CS121 crawler StudentID(s), where the last part 
	 * is the eight-digit student ID of each team member, separated by one space.
	 */
	private final static String USER_AGENT_STRING = "UCI Inf141-CS121 crawler 13793954";
	
	/**
	 * The folder stores everything that we obtained from crawling
	 */
	public final static String CRAWL_STORAGE_FOLDER = "/data";

	private final static int NUMBER_OF_CRAWLER = 7;
	
	
	/**
	 * This method is used to start the controller
	 * @param seedURL url to start
	 * @return
	 */
	public boolean startController (String seedURL) {

	        CrawlConfig config = getTheConfigOfTheCrawler();


	        /*
	         * Instantiate the controller for this crawl.
	         */
	        PageFetcher pageFetcher = new PageFetcher(config);
	        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
	        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
	        CrawlController controller;
			try {
				controller = new CrawlController(config, pageFetcher, robotstxtServer);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

	        /*
	         * For each crawl, you need to add some seed urls. These are the first
	         * URLs that are fetched and then the crawler starts following links
	         * which are found in these pages
	         */
	        controller.addSeed(seedURL);

	        /*
	         * Start the crawl. This is a blocking operation, meaning that your code
	         * will reach the line after this only when crawling is finished.
	         */
	        controller.start(CrawlerLibrary.class, NUMBER_OF_CRAWLER);
	        return CrawlerLibrary.getCrawledUrls() != null;
	}

	/**
	 * @return
	 */
	public CrawlConfig getTheConfigOfTheCrawler() {
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder(CRAWL_STORAGE_FOLDER);
		config.setUserAgentString(USER_AGENT_STRING);
		config.setPolitenessDelay(POLITENESS_DELAY);
		return config;
	}
	
	public ArrayList<String> getCrawledUrls() {
		return CrawlerLibrary.getCrawledUrls();
	}
}
