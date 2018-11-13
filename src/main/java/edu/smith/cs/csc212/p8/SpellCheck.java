package edu.smith.cs.csc212.p8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class SpellCheck {

	public static List<String> loadReadMe() {
		List<String> lines;
		List<String> returningList = new ArrayList<>();
		try {
			lines = Files.readAllLines(new File("src/main/resources/smallBook").toPath());
			for (String line:lines) {
				List<String> wordsInBook = WordSplitter.splitTextToWords(line);
				returningList.addAll(wordsInBook);
			}
		} catch (IOException e) {
			throw new RuntimeException("Couldn't find Read Me.", e);
		}
		return returningList;
	}
	
	public static List<String> loadDictionary() {
		List<String> words;
		try {
			words = Files.readAllLines(new File("src/main/resources/words").toPath());
		} catch (IOException e) {
			throw new RuntimeException("Couldn't find dictionary.", e);
		}
		return words;
	}
	
	public static List<String> misSpelled(List<String>wordsInBook, Collection<String> dictionary) {
		List<String> newList = new ArrayList<>();

		for (String w : wordsInBook) {
			if (!dictionary.contains(w))  {
					newList.add(w);
				}
			}
		
		return newList;
	}
	
	public static void main(String[] args) {
		List<String> dictionary = loadDictionary();
		List<String> wordsInBook = loadReadMe();
		List<String> newList = misSpelled(wordsInBook ,dictionary);
		int found =0;
		for (int i=0; i<newList.size();i++) {
			found++;
		}
		double sizeOfBook = wordsInBook.size();
		double ratio = found/sizeOfBook;
		System.out.println(ratio);
	}
}
