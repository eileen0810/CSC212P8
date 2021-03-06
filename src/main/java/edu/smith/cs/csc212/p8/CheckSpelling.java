package edu.smith.cs.csc212.p8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class CheckSpelling {
	/**
	 * Read all lines from the UNIX dictionary.
	 * @return a list of words!
	 */
	public static List<String> loadDictionary() {
		long start = System.nanoTime();
		List<String> words;
		try {
			words = Files.readAllLines(new File("src/main/resources/words").toPath());
		} catch (IOException e) {
			throw new RuntimeException("Couldn't find dictionary.", e);
		}
		long end = System.nanoTime();
		double time = (end - start) / 1e9;
		System.out.println("Loaded " + words.size() + " entries in " + time +" seconds.");
		System.out.println("-----------------------------------------------------------");
		return words;
	}
	
	/**
	 * Read the lines from a small book
	 * @return a list of the words
	 */
	
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
	
	/**
	 * Looks for all the misSpelled words in the book
	 * @param wordsInBook
	 * @param dictionary
	 * @return all the misSpelled words
	 */
	public static List<String> misSpelled(List<String>wordsInBook, Collection<String> dictionary) {
		List<String> newList = new ArrayList<>();

		for (String w : wordsInBook) {
			if (!dictionary.contains(w))  {
					newList.add(w);
				}
			}
		
		return newList;
	}
	
	/**
	 * This method looks for all the words in a dictionary.
	 * @param words - the "queries"
	 * @param dictionary - the data structure.
	 */
	public static void timeLookup(List<String> words, Collection<String> dictionary) {
		long startLookup = System.nanoTime();
		
		int found = 0;
		for (String w : words) {
			if (dictionary.contains(w)) {
				found++;
			}
		}
		long endLookup = System.nanoTime();
		double fractionFound = found / (double) words.size();
		double timeSpentPerItem = (endLookup - startLookup) / ((double) words.size());
		int nsPerItem = (int) timeSpentPerItem;
		System.out.println(dictionary.getClass().getSimpleName()+": Lookup of items found="+fractionFound+" time="+nsPerItem+" ns/item");
		
	}
	
	public static List<String> createMixedDataset(List<String> words, int numSample, double fractionYes) {
		List<String> yesWords = new ArrayList<>();

		int size = words.size();

		for (int i = 0; i < size; i+= size/numSample) {
			String word = words.get(i);
			yesWords.add(word);
		}

		double fractionNo =(1- fractionYes) * yesWords.size();
		for (int i = 0; i < fractionNo; i++) {
			if (yesWords.size() > 0) {
				yesWords.remove(0);
			}
			yesWords.add(words.get(i) + "zzzzz");
		}
		return yesWords;
	}
	
	
	public static void main(String[] args) {
		// --- Load the dictionary.
		List<String> listOfWords = loadDictionary();
		
		// --- Load the book.
		List<String> wordsInBook = loadReadMe();
	
		// --- Create a bunch of data structures for testing:
	
		//Input Data
		long startTreeTime = System.nanoTime();
		TreeSet<String> treeOfWords = new TreeSet<>(listOfWords);
		long endTreeTime = System.nanoTime();
		double timeSpentTree = (endTreeTime - startTreeTime) / treeOfWords.size();
		System.out.println("TreeSet Input Data Time: "+ timeSpentTree + " ns/insert");
		
		//For loop
		long startTreeTime2 = System.nanoTime();
		TreeSet<String> treeOfWords2 = new TreeSet<>();
		for (String w : listOfWords) {
			treeOfWords2.add(w);
		}
		long endTreeTime2 = System.nanoTime();
		double timeSpentTree2 = (endTreeTime2 - startTreeTime2) / treeOfWords.size();
		System.out.println("TreeSet Loop Time: "+ timeSpentTree2 + " ns/insert");
		
		//Input Data
		long startHashTime = System.nanoTime();
		HashSet<String> hashOfWords = new HashSet<>(listOfWords);
		long endHashTime = System.nanoTime();
		double timeSpentHash = (endHashTime - startHashTime) / hashOfWords.size();
		System.out.println("HasSet Time Input Data: "+ timeSpentHash + " ns/insert");
				
		//For loop
		long startHashTime2 = System.nanoTime();
		HashSet<String> hashOfWords2 = new HashSet<>();
		for (String w : listOfWords) {
			hashOfWords2.add(w);
		}
		long endHashTime2 = System.nanoTime();
		double timeSpentHash2 = (endHashTime2 - startHashTime2) / hashOfWords2.size();
		System.out.println("HashSet Loop Time: " +timeSpentHash2 +" ns/insert");
				
		long startSortedStringTime = System.nanoTime();
		SortedStringListSet bsl = new SortedStringListSet(listOfWords);
		long endSortedStringTime = System.nanoTime();
		double timeSpentSortedString = (endSortedStringTime - startSortedStringTime) / bsl.size();
		System.out.println("SortedStringListSet Time: " +timeSpentSortedString +" ns/insert");
		
		long startCharTrieTime = System.nanoTime();
		CharTrie trie = new CharTrie();
		for (String w : listOfWords) {
			trie.insert(w);
		}
		long endCharTrieTime = System.nanoTime();
		double timeSpentCharTrie = (endCharTrieTime - startCharTrieTime) / trie.size();
		System.out.println("CharTrie Time: " +timeSpentCharTrie + " ns/insert" );
		
		long startLLHashTime = System.nanoTime();
		LLHash hm100k = new LLHash(100000);
		for (String w : listOfWords) {
			hm100k.add(w);
		}
		long endLLHashTime = System.nanoTime();
		double timeSpentLLHash = (endLLHashTime - startLLHashTime) / hm100k.size();
		System.out.println("LLHash Time: " +timeSpentLLHash + " ns/insert");
		System.out.println("---------------------------------------------------------");
		
		// --- Make sure that every word in the dictionary is in the dictionary:
		System.out.println("This is the timeLookup for Query Speed Section 2");
		timeLookup(listOfWords, treeOfWords);
		timeLookup(listOfWords, hashOfWords);
		timeLookup(listOfWords, bsl);
		timeLookup(listOfWords, trie);
		timeLookup(listOfWords, hm100k);
		System.out.println("---------------------------------------------------------");
		System.out.println("---------------------------------------------------------");
		
		System.out.println("Section 3 for book");
		for (int i=0; i<11; i++) {
		    double fraction = i / 10.0;
		// --- Create a dataset of mixed hits and misses:
	    List<String> hitsAndMisses = createMixedDataset(listOfWords, 10000, fraction);
		timeLookup(hitsAndMisses, treeOfWords);
		timeLookup(hitsAndMisses, hashOfWords);
		timeLookup(hitsAndMisses, bsl);
		timeLookup(hitsAndMisses, trie);
		timeLookup(hitsAndMisses, hm100k);
		System.out.println("---------------------------------------------------------");
		}
		
		// --- linear list timing:
		// Looking up in a list is so slow, we need to sample:
		System.out.println("Start of list: ");
		timeLookup(listOfWords.subList(0, 1000), listOfWords);
		System.out.println("End of list: ");
		timeLookup(listOfWords.subList(listOfWords.size()-100, listOfWords.size()), listOfWords);
		
	
		// --- print statistics about the data structures:
		System.out.println("Count-Nodes: "+trie.countNodes());
		System.out.println("Count-Items: "+hm100k.size());

		System.out.println("Count-Collisions[100k]: "+hm100k.countCollisions());
		System.out.println("Count-Used-Buckets[100k]: "+hm100k.countUsedBuckets());
		System.out.println("Load-Factor[100k]: "+hm100k.countUsedBuckets() / 100000.0);

		
		System.out.println("log_2 of listOfWords.size(): "+listOfWords.size());
		
		// --- Print book info
		System.out.println("---------------------------------------------------------");
		List<String> newList = misSpelled(wordsInBook ,listOfWords);
		int found =0;
		for (int i=0; i<newList.size();i++) {
			found++;
		}
		//System.out.println(newList);
		double sizeOfBook = wordsInBook.size();
		double ratio = found/sizeOfBook;
		System.out.println("The ratio of the misSpelled words in the book is : " +ratio);
		
		timeLookup(wordsInBook, treeOfWords);
		timeLookup(wordsInBook, hashOfWords);
		timeLookup(wordsInBook, bsl);
		timeLookup(wordsInBook, trie);
		timeLookup(wordsInBook, hm100k);
		
		System.out.println("---------------------------------------------------------");
		System.out.println("Done!");
	}
}

