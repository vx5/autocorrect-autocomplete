package ac;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashMultiset;

// Construction is implicitly tested in all
public class SuggestComparatorTest {
  // Stores all Objects needed to construct SuggestComparator
  BigramMap bmap;
  HashMultiset<String> corpusWords;

  @Test
  public void testComparePerfectMatch() {
    // Tests for comparison on first case -- perfect match
    SuggestComparator c = new SuggestComparator("given", corpusWords);
    Suggestion s1 = new Suggestion("given");
    Suggestion s2 = new Suggestion("notGiven");
    assertTrue(c.compare(s1, s2) == -1);
  }

  @Test
  public void testCompareBigram() {
    // Test for comparison on second criterion -- probability in the bigram map
    SuggestComparator c = new SuggestComparator("four", bmap, "given",
        corpusWords);
    Suggestion s1 = new Suggestion("five");
    Suggestion s2 = new Suggestion("badString");
    assertTrue(c.compare(s1, s2) == -1);
  }

  @Test
  public void testCompareUnigram() {
    // Tests for comparison on third criterion -- unigram probability in corpus
    SuggestComparator c = new SuggestComparator("given", corpusWords);
    Suggestion s1 = new Suggestion("four");
    Suggestion s2 = new Suggestion("six");
    assertTrue(c.compare(s1, s2) == -1);
  }

  @Test
  public void testCompareAlphabetically() {
    // Tests for lexicographic criterion, final of the set
    SuggestComparator c = new SuggestComparator("given", corpusWords);
    Suggestion s1 = new Suggestion("four");
    Suggestion s2 = new Suggestion("six");
    assertTrue(c.compare(s1, s2) == -1);
  }

  @Before
  public void setUp() {
    // Stores instances of all key Objects used by SuggestComparator
    bmap = new BigramMap();
    corpusWords = HashMultiset.create();
    // Creates sequence of fake corpus words
    ArrayList<String> corpus = new ArrayList<String>();
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
  }

  @After
  public void tearDown() {
    // Clears instance variables for re-initializations
    bmap = null;
    corpusWords = null;
  }

}
