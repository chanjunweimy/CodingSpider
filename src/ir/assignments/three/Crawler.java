package ir.assignments.three;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.TreeMap;
import java.util.TreeSet;

public class Crawler {
	/**
	 * file used to save the files in subdomains
	 */
	private final static String FILE_SUBDOMAINS = "subdomains.txt";
	
	/**
	 * The set containing all the unique urls that are crawled
	 */
	private static TreeSet <String> _crawledUrls = null;
	
	/**
	 * The set containing all the subdomains of the ics.uci.edu
	 */
	private static TreeMap <String, Integer> _subdomains = null;
	
	
	/**
	 * This method is for testing purposes only. It does not need to be used to
	 * answer any of the questions in the assignment. However, it must function
	 * as specified so that your crawler can be verified programatically.
	 * 
	 * This methods performs a crawl starting at the specified seed URL. Returns
	 * a collection containing all URLs visited during the crawl.
	 */
	public static Collection<String> crawl(String seedURL) {
		// TODO implement me
		Controller controller = new Controller();
		
		//error happens
		if (!controller.startController(seedURL)) {
			return null;
		}
		
		_crawledUrls = controller.getCrawledUrls();
		_subdomains = controller.getSubDomains();
		
		return controller.getCrawledUrls();
	}
	
	private static boolean writeToFile(String filename, boolean isAppend, String line) {
		try (FileWriter fw = new FileWriter(filename, isAppend)) {
			fw.write(line);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public static void main (String[] args) {
		long startTime = System.currentTimeMillis();

		crawl(CrawlerLibrary.CRAWLING_SERVER);
		
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		
		System.out.println("How much time did it take to crawl the entire domain?");
		System.out.println(totalTime);
		
		
		System.out.println("How many unique pages did you find in the entire domain? "
				+ "(Uniqueness is established by the URL, not the page's content.)");
		System.out.println(_crawledUrls.size());
		
		System.out.println("How many subdomains did you find? Submit the list of subdomains "
				+ "ordered alphabetically and the number of unique pages detected in each subdomain. "
				+ "The file should be called Subdomains.txt, and its content should be lines "
				+ "containing the URL, a comma, a space, and the number.");
		System.out.println(_subdomains.size());
		printAndSaveSubdomains(_subdomains, FILE_SUBDOMAINS);
		
		countLongestPageAndWordFrequency(CrawlerLibrary.DIRECTORY_HTML);
		
	}

	/**
	 * Helper method to prepare stuff for counting longest page and word frequency
	 * @param directoryHtml
	 * @return
	 */
	private static boolean countLongestPageAndWordFrequency(String directoryHtml) {
		File dir = new File (directoryHtml);
		if (dir.isDirectory()) {
			File[] htmlFiles = dir.listFiles();
			countLongestPage(htmlFiles);
			countWordFrequency(htmlFiles);
			return true;
		}
		return false;
	}

	/**
	 * Todo: counting word frequencies from existing files
	 * @param htmlFiles the html files obtained while crawling
	 */
	private static void countWordFrequency(File[] htmlFiles) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Todo: counting longest page based on the pages
	 * @param htmlFiles the html files obtained while crawling
	 */
	private static void countLongestPage(File[] htmlFiles) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Todo: iterate the map, print all the values and saved to the file
	 * The list should be sorted in default (no need to sort again)
	 * 
	 * @param subdomains it is a map that map the subdomain (anything.ics.uci.edu) to the number of unique pages in that subdomain
	 * @param fileSubdomains it is the file you should save to
	 */
	private static void printAndSaveSubdomains(
			TreeMap<String, Integer> subdomains, String fileSubdomains) {
		// TODO Auto-generated method stub
		
	}
}
