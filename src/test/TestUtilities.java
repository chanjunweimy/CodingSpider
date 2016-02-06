package test;

import static org.junit.Assert.*;
import ir.assignments.two.a.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;

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
		
	}
}