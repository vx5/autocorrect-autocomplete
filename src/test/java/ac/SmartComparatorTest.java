package ac;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashMultiset;

public class SmartComparatorTest {
  // Stores all Objects needed to construct SuggestComparator
  private BigramMap bmap;
  private HashMultiset<String> corpusWords;
  private HashSet<String> dictionary;

  @Test
  public void testCompareDomDictionary() {
    // Looks at case where one Suggestion dominates the other in the first
    // condition -- that one's first word appears in the dictionary, corpus, and
    // is long, and the other's does not
    SmartComparator c = new SmartComparator(corpusWords, dictionary);
    Suggestion s1 = new Suggestion("one");
    Suggestion s2 = new Suggestion("orxqw");
    assertTrue(c.compare(s1, s2) < 0);
  }

  @Test
  public void testCompareDomNoDiction() {
    // Looks at case where one Suggestion beats the other in the second
    // condition -- appearance in the corpus and sufficient length, without
    // presence in dictionary
    SmartComparator c = new SmartComparator(corpusWords, dictionary);
    Suggestion s1 = new Suggestion("one");
    Suggestion s2 = new Suggestion("notInCorpus");
    assertTrue(c.compare(s1, s2) < 0);
  }

  @Test
  public void testComparePoints() {
    // Looks at case where one Suggestion must beat the other through the
    // SmartComparator's point system
    SmartComparator c = new SmartComparator("before", bmap, corpusWords,
        dictionary);
    Suggestion s1 = new Suggestion("four");
    Suggestion s2 = new Suggestion("one");
    assertTrue(c.compare(s1, s2) < 0);
  }

  @Test
  public void testCompareLength() {
    // Looks at case where one Suggestion must beat the other through the
    // SmartComparator's last non-lexicographic attribute, length
    SmartComparator c = new SmartComparator(corpusWords, dictionary);
    Suggestion s1 = new Suggestion("three");
    Suggestion s2 = new Suggestion("six");
    assertTrue(c.compare(s1, s2) < 0);
  }

  @Before
  public void setUp() {
    // Stores instances of all key Objects used by SuggestComparator
    bmap = new BigramMap();
    corpusWords = HashMultiset.create();
    // Creates sequence of fake corpus words
    ArrayList<String> corpus = new ArrayList<String>();
    corpus.add("orxqw");
    corpus.add("one");
    corpus.add("two");
    corpus.add("three");
    corpus.add("four");
    corpus.add("five");
    corpus.add("six");
    corpus.add("four");
    corpus.add("seven");
    // Uses sequence to populate bmap, corpusWords
    bmap.addSequence(corpus);
    corpusWords.addAll(corpus);
    // Loads the dictionary
    dictionary = new HashSet<String>();
    dictionary.add("one");
    dictionary.add("two");
    dictionary.add("three");
    dictionary.add("four");
    dictionary.add("five");
    dictionary.add("six");
    dictionary.add("seven");
  }

  @After
  public void tearDown() {
    // Clears all instance variables for re-initialization in setUp()
    bmap = null;
    corpusWords = null;
    dictionary = null;
  }

}
