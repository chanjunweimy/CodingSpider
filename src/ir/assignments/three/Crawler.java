package ir.assignments.three;

import java.util.Collection;

public class Crawler {

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
		
		return controller.getCrawledUrls();
	}
}
