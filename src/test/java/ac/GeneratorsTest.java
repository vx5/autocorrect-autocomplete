package ac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.HashMultiset;

import filereader.TXTReader;

public class GeneratorsTest {
  // Stores variables for testing
  HashMultiset<String> corpusWords;
  // Stores variables for sherlock
  HashMultiset<String> sherlockWords;

  @Test
  public void testWhiteSpace() {
    HashSet<Suggestion> responses;
    // Tests for case of one-character input
    responses = Generators.whiteSpace(corpusWords, "a");
    assertEquals(responses.size(), 0);
    // Tests for case of multiple-character input
    responses = Generators.whiteSpace(corpusWords, "abc");
    assertEquals(responses.size(), 2);
  }

  @Test
  public void testGetLeds() {
    HashSet<Suggestion> responses;
    // Tests for case of LED equal to 1
    responses = Generators.getLeds(corpusWords, "a", 1);
    assertEquals(responses.size(), 3);
    // Test for case of LED equal to 2
    responses = Generators.getLeds(sherlockWords, "sherlo", 2);
    boolean sherlockFound = false;
    for (Suggestion s : responses) {
      if (s.getFirstWord().contentEquals("sherlock")) {
        sherlockFound = true;
      }
    }
    assertTrue(sherlockFound);
  }

  @Before
  public void setUp() {
    // Initializes Multisets of words, one based on simple template, another
    // based on sherlock.txt corpus
    corpusWords = HashMultiset.create();
    corpusWords.add("a");
    corpusWords.add("ab");
    corpusWords.add("c");
    corpusWords.add("bc");
    sherlockWords = HashMultiset.create();
    try {
      sherlockWords
          .addAll(TXTReader.readFile("data/autocorrect/sherlock.txt", " "));
    } catch (Exception e) {
      fail("Error in reading in sherlock.txt");
    }
  }

  @After
  public void tearDown() {
    // Clears instance field for re-initialization
    corpusWords = null;
  }

}
