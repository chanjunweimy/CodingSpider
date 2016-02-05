package ir.assignments.three;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class CrawlerLibrary extends WebCrawler {
	/**
	 * The set containing all the unique urls that are crawled
	 */
	private static TreeSet <String> _crawledUrls = null;
	
	/**
	 * The set containing all the subdomains of the ics.uci.edu
	 */
	private static TreeMap <String, Integer> _subdomains = null;
	
	/**
	 * every url that we crawled must contain this string
	 */
	public final static String CRAWLING_SERVER = "ics.uci.edu";
	
	/**
	 * url that we blocked
	 */
	public final static String CRAWLING_BLOCK_SERVER = "duttgroup.ics.uci.edu";

	/**
	 * We store html files into this directory
	 */
	public final static String DIRECTORY_HTML = "html/";
	
	/**
	 * we store all file lengths of the html file here
	 */
	public final static String FILE_LENGTHS = "file_lengths.txt";
	
	/**
	 * we save html files into text files
	 */
	private final static String HTML_SAVED_EXTENSION = ".txt";
	
	
	
	/**
	 * map that holds html url and map to the index
	 */
	private static HashMap <String, Integer> _htmlFiles = null;
	
	/**
	 * Delimeters which separate my tag with the html file content
	 */
	//private static final String DELIMETER = ":==:";
	
	/**
	 * Delimeters which separate my tag with the html file content (specialy for outgoing links)
	 */
	//private static final String DELIMETER_LINKS = "-==-";
	
	/**
	 * We need filters to help us filter out non html files
	 */
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
            + "|png|mp3|mp3|zip|gz))$");
	
	/**
	 * This method receives two parameters. The first parameter is the page in
	 * which we have discovered this new url and the second parameter is the new
	 * url. You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic). In this example,
	 * we are instructing the crawler to ignore urls that have css, js, git, ...
	 * extensions and to only accept urls that contains 
	 * "ics.uci.edu". In this case, we didn't need the referringPage
	 * parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		if (_crawledUrls == null) {
			resetCrawledUrls();
		}
		
		return  !FILTERS.matcher(href).matches()
                && 
                href.contains(CRAWLING_SERVER)
                &&
                !href.contains(CRAWLING_BLOCK_SERVER)
                &&
                !_crawledUrls.contains(href)
                &&
                referringPage.getParseData() instanceof HtmlParseData;
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		if (_crawledUrls == null) {
			resetCrawledUrls();
		}
		
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);
		
		saveHtmlFiles(page);
	}

	/**
	 * add every subdomains, ie: anything.ics.uci.edu, to the set 
	 * @param url crawled url
	 */
	public void saveSubdomains(String url) {
		url = url.replaceFirst("http://", "");
		url = url.replaceFirst("https://", "");
		url = url.replaceFirst("www.", "");
		if (!url.startsWith(CRAWLING_SERVER)) {
			
			String[] tokens = url.split(CRAWLING_SERVER);
			StringBuffer buffer = new StringBuffer();
			buffer.append(tokens[0]);
			buffer.append(CRAWLING_SERVER);
			
			int frequency = 1;
			boolean isFileUnique = _subdomains.containsKey(buffer.toString()) && !_crawledUrls.contains(url);
			if (isFileUnique) {
				frequency = _subdomains.get(buffer.toString());
				frequency++;
			}
			_subdomains.put(buffer.toString(), frequency);
		}
	}

	/**
	 * Todo: save html files to the DIRECTORY_HTML and index them
	 * Use writeToFile to help saving html files
	 * @param page
	 */
	public void saveHtmlFiles(Page page) {
		if (page.getParseData() instanceof HtmlParseData) {
			String filename = getFileName();
			
			saveHtmlToFile(page, filename);
			
			String url = page.getWebURL().getURL();
			saveSubdomains(url);
			_crawledUrls.add(url);
		}
	}

	/**
	 * helper method to save all html files into local database
	 * @param page
	 * @param filename
	 */
	public void saveHtmlToFile(Page page, String filename) {
		HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
		String text = htmlParseData.getText();
		String html = htmlParseData.getHtml();
		Set<WebURL> links = htmlParseData.getOutgoingUrls();
		String url = page.getWebURL().getURL();
		saveHtmlToFile(text, html, links, url, filename);
	}

	/**
	 * 
	 * @param text
	 * @param html
	 * @param links
	 * @param url
	 * @param filename
	 */
	public void saveHtmlToFile(String text, String html, Set<WebURL> links, String url,
			String filename) {
		writeToFile(filename, false, url + System.lineSeparator());
		writeToFile(filename, true, text);
		/*
		writeToFile(filename, false, "Url" + DELIMETER + url + "\n");
		writeToFile(filename, true, "Text length" + DELIMETER + text.length() + "\n");
		writeToFile(filename, true, "Text" + DELIMETER + text + "\n");
		writeToFile(filename, true, "Html length" + DELIMETER + html.length() + "\n");
		writeToFile(filename, true, "Html" + DELIMETER + html + "\n");
		writeToFile(filename, true, "Number of outgoing links" + DELIMETER + links.size() + "\n");
		writeToFile(filename, true, "Outgoinglinks" + DELIMETER);
		
		if (links.size() > 0) {
			boolean isStart = true;
			for (WebURL link : links) {
				if (isStart) {
					writeToFile(filename, true, link.getURL());
					isStart = false;
				} else {
					writeToFile(filename, true, DELIMETER_LINKS + link.getURL());
				}
			}
		}
		*/
	}

	/**
	 * @return
	 */
	public String getFileName() {
		int index = _crawledUrls.size();
		StringBuffer filenameBuffer = new StringBuffer();
		filenameBuffer.append(DIRECTORY_HTML);
		filenameBuffer.append(index);
		filenameBuffer.append(HTML_SAVED_EXTENSION);
		String filename = filenameBuffer.toString();
		_htmlFiles.put(filename, index);
		return filename;
	}
	
	/**
	 * Helper method to help us to save the html file
	 * @param filename indexed_file_name
	 * @param isAppend whether is it for appending
	 * @param line line to add
	 * @return
	 */
	private boolean writeToFile(String filename, boolean isAppend, String line) {
		try (FileWriter fw = new FileWriter(filename, isAppend)) {
			fw.write(line);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	public static void resetCrawledUrls() {
		if (_crawledUrls == null) {
			_crawledUrls = new TreeSet <String>();
			_subdomains = new TreeMap <String, Integer>();
			_htmlFiles = new HashMap <String, Integer>();
		}
		_crawledUrls.clear();
		_subdomains.clear();
		_htmlFiles.clear();
	}
	
	public static TreeSet<String> getCrawledUrls() {
		return _crawledUrls;
	}
	
	public static TreeMap<String, Integer> getSubDomains() {
		return _subdomains;
	}

}
