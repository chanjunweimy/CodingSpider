package test;

import static org.junit.Assert.*;
import ir.assignments.two.a.Frequency;
import ir.assignments.two.a.Utilities;
import ir.assignments.two.b.WordFrequencyCounter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestUtilities {
	private final ByteArrayOutputStream _outContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
	    System.setOut(new PrintStream(_outContent));
	}

	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	}
	

	@Test
	public void testFileExtremeEmpty() {
		final String filename = "file_extreme_empty.txt";
		File file = new File(filename);
		assertTrue ("check if the file is in the directory", file.exists() && file.isFile());
		
		ArrayList <String> words = Utilities.tokenizeFile(file);
		ArrayList <String> expectedArrayList = new ArrayList <String>();
		assertTrue("number of words is correct", words.size() == expectedArrayList.size());
		for (int i = 0; i < words.size(); i++) {
			assertTrue("word extracted from file is same with expected", words.get(i).equals(expectedArrayList.get(i)));
		}
		
		List<Frequency> frequencies = WordFrequencyCounter.computeWordFrequencies(words);
		Utilities.printFrequencies(frequencies);		
		String expectedOutput = "Total 0-gram count: 0\r\nUnique 0-gram count: 0\r\n";
		assertTrue("the output is correct", expectedOutput.equals(_outContent.toString()));
		
	}
	
	@Test
	public void testFileNormalMultiline() {
		final String filename = "file_normal_multilines.txt";
		File file = new File(filename);
		assertTrue ("check if the file is in the directory", file.exists() && file.isFile());
		
		ArrayList <String> words = Utilities.tokenizeFile(file);
		ArrayList <String> expectedArrayList = new ArrayList <String>();
		expectedArrayList.add("an");
		expectedArrayList.add("input");
		expectedArrayList.add("string");
		expectedArrayList.add("this");
		expectedArrayList.add("is");
		expectedArrayList.add("or");
		expectedArrayList.add("is");
		expectedArrayList.add("it");
		
		expectedArrayList.add("this");
		expectedArrayList.add("sentence");
		expectedArrayList.add("repeats");
		expectedArrayList.add("the");
		expectedArrayList.add("word");
		expectedArrayList.add("sentence");
		
		expectedArrayList.add("you");
		expectedArrayList.add("think");
		expectedArrayList.add("you");
		expectedArrayList.add("know");
		expectedArrayList.add("how");
		expectedArrayList.add("you");
		expectedArrayList.add("think");
		
		expectedArrayList.add("do");
		expectedArrayList.add("geese");
		expectedArrayList.add("see");
		expectedArrayList.add("god");
		expectedArrayList.add("abba");
		expectedArrayList.add("bat");
		expectedArrayList.add("tab");
		
		assertTrue("number of words is correct", words.size() == expectedArrayList.size());
		for (int i = 0; i < words.size(); i++) {
			assertTrue("word extracted from file is same with expected", words.get(i).equals(expectedArrayList.get(i)));
		}
		
		List<Frequency> frequencies = WordFrequencyCounter.computeWordFrequencies(words);
		Utilities.printFrequencies(frequencies);		
		String expectedOutput = "Total item count: 28\r\nUnique item count: 22\r\n"
				+ "you\t3\r\n"
				+ "is\t2\r\n"
				+ "sentence\t2\r\n"
				+ "think\t2\r\n"
				+ "this\t2\r\n"
				+ "abba\t1\r\n"
				+ "an\t1\r\n"
				+ "bat\t1\r\n"
				+ "do\t1\r\n"
				+ "geese\t1\r\n"
				+ "god\t1\r\n"
				+ "how\t1\r\n"
				+ "input\t1\r\n"
				+ "it\t1\r\n"
				+ "know\t1\r\n"
				+ "or\t1\r\n"
				+ "repeats\t1\r\n"
				+ "see\t1\r\n"
				+ "string\t1\r\n"
				+ "tab\t1\r\n"
				+ "the\t1\r\n"
				+ "word\t1\r\n";

		assertTrue("the output is correct", expectedOutput.equals(_outContent.toString()));

		_outContent.reset();
		Collections.sort(frequencies, new Utilities.SorterNGrams());
		Utilities.printFrequencies(frequencies);
		assertTrue("the output is correct", expectedOutput.equals(_outContent.toString()));
	}
}