package ir.assignments.three;

import ir.assignments.two.a.Frequency;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class Crawler {

	private static class FrequencyComparator implements Comparator<Frequency> {

		public FrequencyComparator() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public int compare(Frequency o1, Frequency o2) {
			if (o1.getFrequency() != o2.getFrequency())
				return o2.getFrequency() - o1.getFrequency();

			return o1.getText().compareTo(o2.getText());
		}

	}

	/**
	 * file used to save the files in subdomains
	 */
	private final static String FILE_SUBDOMAINS = "Subdomains.txt";

	/**
	 * file used to store stop words
	 */
	private final static String FILE_STOPWORDS = "StopWords.txt";

	/**
	 * The set containing all the unique urls that are crawled
	 */
	private static TreeSet<String> _crawledUrls = null;

	/**
	 * The set containing all the subdomains of the ics.uci.edu
	 */
	private static TreeMap<String, Integer> _subdomains = null;

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

		// error happens
		if (!controller.startController(seedURL)) {
			return null;
		}

		_crawledUrls = controller.getCrawledUrls();
		_subdomains = controller.getSubDomains();

		System.out.println(_crawledUrls);

		return controller.getCrawledUrls();
	}

	/**
	 * helper method to write content to file
	 * 
	 * @param filename
	 *            the file you want to write into
	 * @param isAppend
	 *            whether to overwrite the file
	 * @param line
	 *            the content that you want to write into
	 * @return
	 */
	private static boolean writeToFile(String filename, boolean isAppend,
			String line) {
		try (FileWriter fw = new FileWriter(filename, isAppend)) {
			fw.write(line);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		crawl("http://www.ics.uci.edu");

		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;

		System.out
				.println("How much time did it take to crawl the entire domain?");
		System.out.println(totalTime);

		System.out
				.println("How many unique pages did you find in the entire domain? "
						+ "(Uniqueness is established by the URL, not the page's content.)");
		System.out.println(_crawledUrls.size());

		System.out
				.println("How many subdomains did you find? Submit the list of subdomains "
						+ "ordered alphabetically and the number of unique pages detected in each subdomain. "
						+ "The file should be called Subdomains.txt, and its content should be lines "
						+ "containing the URL, a comma, a space, and the number.");
		System.out.println(_subdomains.size());
		printAndSaveSubdomains(_subdomains, FILE_SUBDOMAINS);

		countLongestPageAndWordFrequency(CrawlerLibrary.DIRECTORY_HTML);

	}

	/**
	 * Helper method to prepare stuff for counting longest page and word
	 * frequency
	 * 
	 * @param directoryHtml
	 * @return
	 */
	private static boolean countLongestPageAndWordFrequency(String directoryHtml) {
		File dir = new File(directoryHtml);
		if (dir.isDirectory()) {
			File[] htmlFiles = dir.listFiles();
			countLongestPage(htmlFiles);
			countWordFrequency(htmlFiles);
			return true;
		}
		return false;
	}

	/**
	 * counting word frequencies from existing files
	 * 
	 * @author Amy
	 * @param htmlFiles
	 *            the html files obtained while crawling
	 */
	private static void countWordFrequency(File[] htmlFiles) {
		// TODO Auto-generated method stub
		String word;
		ArrayList<String> stopWordsList = new ArrayList<String>();
		ArrayList<String> wordList = new ArrayList<String>();

		/**
		 * do not forget to exclude the stopwords
		 */
		File stopWordsFile = new File(FILE_STOPWORDS);
		try (Scanner stopWords = new Scanner(stopWordsFile)) {

			while (stopWords.hasNext()) {
				word = stopWords.next();
				word = word.toLowerCase();
				word = word.replaceAll("[^a-z0-9 ]", "");
				word = "\"" + word + "\"";
				stopWordsList.add(word);
			}
			stopWords.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}

		if (htmlFiles == null) {
			System.out.println("No Files");
			return;
		}

		for (int each = 0; each < htmlFiles.length; each++) {
			try (Scanner inFile = new Scanner(htmlFiles[each])) {

				while (inFile.hasNext()) {
					word = inFile.next();
					word = word.toLowerCase();
					word = word.replaceAll("[^a-z0-9 ]\"", "");
					word = "\"" + word + "\"";
					wordList.add(word);
				}
				for (int eachWord = 0; eachWord < wordList.size(); eachWord++) {
					if (stopWordsList.contains(wordList.get(eachWord))) {
						wordList.remove(wordList.get(eachWord));
					}
				}
				inFile.close();
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
			}
		}

		Map<String, Integer> map = new HashMap<String, Integer>();
		ArrayList<Frequency> frequencies = new ArrayList<Frequency>();

		if (wordList.size() == 0)
			System.out.println("No Words");

		for (String finalWord : wordList) {
			Integer value = map.get(finalWord);
			if (value != null) {
				map.put(finalWord, value + 1);
			} else
				map.put(finalWord, 1);
		}

		for (String lst : map.keySet()) {
			frequencies.add(new Frequency(lst, map.get(lst)));
		}

		FrequencyComparator comparator = new FrequencyComparator();
		Collections.sort(frequencies, comparator); // in alphabetical order

		if (frequencies.isEmpty()) {
			return;
		}
		
		for (int finalList = 0; finalList < 500; finalList++) {
			writeToFile("CommonWords.txt", true, frequencies.get(finalList)
					.toString() + System.lineSeparator());
		}

	}

	/**
	 * counting longest page based on the pages
	 * 
	 * @author Matt
	 * @param htmlFiles
	 *            the html files obtained while crawling
	 */
	private static void countLongestPage(File[] htmlFiles) {
		if (htmlFiles == null) {
			return;
		}
		
		String word;
		ArrayList<Integer> countList = new ArrayList<Integer>();
		for (int each = 0; each < htmlFiles.length; each++) {
			try (Scanner inFile = new Scanner(htmlFiles[each])) {
				ArrayList<String> wordList = new ArrayList<String>();
				while (inFile.hasNext()) {
					word = inFile.next();
					word = word.toLowerCase();
					word = word.replaceAll("[^a-z0-9 ]", "");
					word = "\"" + word + "\"";
					wordList.add(word);
				}
				countList.add(wordList.size());
				inFile.close();
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
			}
		}
		try (Scanner LongestPage = new Scanner(
				htmlFiles[countList.indexOf(Collections.max(countList))])) {
			System.out.println("Longest Page: " + LongestPage.next());
			LongestPage.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (NoSuchElementException e2) {
			System.out.println("no such element");
		}

	}

	/**
	 * iterate the map, print all the values and saved to the file The list
	 * should be sorted in default (no need to sort again)
	 * 
	 * @author Laureen
	 * 
	 * @param subdomains
	 *            it is a map that map the subdomain (anything.ics.uci.edu) to
	 *            the number of unique pages in that subdomain
	 * @param fileSubdomains
	 *            it is the file you should save to
	 */
	private static void printAndSaveSubdomains(
			TreeMap<String, Integer> subdomains, String fileSubdomains) {
		for (Entry<String, Integer> entry : subdomains.entrySet()) {
			String key = entry.getKey();
			Integer value = entry.getValue();
			System.out.println(key + " => " + value);
			String line = entry.getKey() + ":" + entry.getValue();
			writeToFile("subdomains.txt", true, line);
		}

	}
}
