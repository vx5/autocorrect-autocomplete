# Autocorrect and autocomplete

## Introduction

Autocorrect and autocomplete is an application designed to offer autocorrect/autocomplete suggestions to the user, given an input string. Users can select text files used to help generate suggestions, and can toggle settings to influence suggestion generation.

There are multiple ways in which suggestions can be generated including the word typed so far (use as a prefix for possible suggestions), and making edits to the word typed so far (Levenshtein Edit Distance or LED, potentially including whitespace). Users are able to adjust these settings, and how they are ranked (only the top 5 suggestions are displayed) on a dedicated settings page. 

![Main page screenshot](https://github.com/vx5/autocorrect-autocomplete/blob/master/main_page_screenshot.png?raw=true)

![Settings page screenshot](https://github.com/vx5/autocorrect-autocomplete/blob/master/settings_page_screenshot.png?raw=true)

## Key details

#### How to access

The deployed app can be accessed at the web address: [https://autocorrect-autocomplete-vx5.herokuapp.com/autocorrect](https://autocorrect-autocomplete-vx5.herokuapp.com/autocorrect)

On the main page, the user can type and observe suggestions. The user can navigate through suggestions using the mouse or using arrow keys, and can click or press 'enter' to select a suggestion, so that it will be included in the main input bar.

#### Acknowledgments

I used online resources to help me review specific types of Java errors, and for how to write in markdown language. I also used extensive resources to better understand JavaScript, jQuery, HTML, and, in particular, CSS styling. I sometimes used mainstream resources such as formal jQuery documentation and w3schools, and sometimes used other, more niche resources, such as css-tricks.com, to understand specific topics like Flexboxes. The primary title font "goodtimes.ttf" is by Raymond Larabie.

This project's first iteration was submitted as part a course at Brown University, though subsequent changes were made developed after completing the course. As such, some of the skeleton content used (e.g., run file, pom.xml, parts of directory structure) are adapted/taken from content provided by the course. I modified some of these files after the fact, so that the app would stand distinct from the course.

I obtained written permission from the course instructor to share the project publicly. Please let me know if there are any issues or questions!

## Additional details 

#### Package structure

Most of the functionality in this project goes in the "ac" (short for Autocorrect) package. The exceptions are a new "common" package, which houses the Main and GUI class, which are common to Autocorrect and other full stack applications I may work on, a "trie" package, which houses all functionality related to the standard trie and radix trie, the "filereader" package, which houses utility classes related to reading files, including the TXTReader class that is relevant to this project, "repl" package, which houses the REPL (not in use in the deployed app), and the "stringmanipulation" package, which houses the utility StringOps class, which allows for certain types of relevant String manipulation.

#### High-level GUI Structure

A GUI class is used to run the Spark server and launch the Spark routes necessary to support the Autocorrect GUI. Those routes rely on handlers defined in the GUI.java file. Those handlers take in requests from the GUI, make the appropriate requests to the AcOperator (through the AcCoordinator class, described in the paragraph above), and then returns the appropriate data, sometimes by loading a FreeMarker template, sometimes through JSON.

Note that the input "cleaning" process, of removing punctuation and numbers, and of ensuring that all characters are in lowercase, is handled in the GUI classes. 

#### GUI features

The main page is simple -- users type in to the text box, and autocorrect suggestions appear in a dropdown list below the box. I did not use any pre-build dropdown, but rather built using divs. If users type more than fits in the box, the box will scroll, but the dropdown answers below will be truncated using ellipses. If the user begins typing while a corpus is not loaded, an error message appears below the box.

Users can select suggestions in one of two ways. They can use the mouse to hover over one of the suggestions (anywhere on that suggestion's box, not only where the text is), in which case the suggestion's box style will change to have white text on a blue background. They can also use the down and up arrow keys to select suggestions. Pressing the up-arrow while in the suggestion box does nothing, while pressing it on the top-most suggestion deselects all suggestions. Pressing the down-arrow while on the bottom-most suggestion does nothing, and the bottom-most suggestion will remain selected. Notably, one can select a box with the mouse, and then change that selection with the arrow keys, and vice versa. 

Users can click the Settings button to head to the Settings page. The Generation and Sorting section allows users to select which settings they want to use. The text next to the LED setting slider displays which LED selection is currently in use (I had to add this functionality separately). The Load Corpora section allows users to load more corpora from the data/autocorrect/ subdirectory. Corpora that are already in use are displayed in a box above, and users can scroll through the list of loaded corpora as more and more are included.

Once the user is ready to save their new settings (be they changes to the generation and sorting settings, the addition of new corpora, or both), they can click "Save new settings". They can also click "Reset settings" to reset all settings to the assignment defaults. Normally, when forms submit, all of the values of inputs are reset to some default. Using JavaScript, I made sure that when new settings are saved, the form populates with the existing values in use by the underlying AcOperator, so that when users arrive at the settings page, they know what the current settings are. Additionally, if any settings are changed to a value different from the current ones, the message between the "Save new settings" and "Return to main" buttons will populate with a message telling the user that they have unsaved changes (until, of course, they click Save new settings). If the user changes their settings in the form back to the current underlying settings, the message will return to normal (a descriptive message when the page first loads, and a message that all settings are up-to-date at all other times).

For both GUI pages, a favicon I made myself appears in the browser tab. Some of the text, including the large "Autocorrect" title on the main page, is in an imported font, the "Good Times" font by Raymond Larabie. All of the buttons were styled, and JavaScript was used to make them change color and the cursor change form when the mouse hovers over them. Most of my styling effort was related to spacing items out on pages, and I made extensive use of Flexboxes to accomplish this task. All of my GUI work benefited greatly from online resources to better understand JavaScript event handlers and CSS styles. 

#### How my Smart-Ranking works

My smart-ranking design process began with the core question of what to optimize for. What did I actually want my Autocorrect suggestions to be? I settled on tree factors: 1) I want my Autocorrect answers to bend more in the direction of autocomplete, and want most to save time by having longer words be suggested (a greater emphasis on predicting the rest of a word than on correcting a given word), 2) I want to reward suggestions that are real words, and 3) I want to reward suggestions that the user has previously suggested. For number 2, this means making sure that suggestions are in the dictionary. In particular, because we remove punctuation from corpora when we load them, many non-words (e.g. "t" from "can't") are counted as words that are loaded into the Trie, so this also means valuing suggestions whose words are more than a character long. 

I translate this system into a series of comparisons, much like the default comparator. The first comparison checks if a suggestion has all its words in the dictionary, in the corpus, and of length greater than one. If exactly one suggestion meets this bar, it beats the other suggestion, then and there. If not, the next comparison repeats that check with the same criteria except for the dictionary component. The dictionary and length-greater-than-one criteria center on goal #2.

If those checks fail to offer a clear winner, then the comparator uses a point system to compare the suggestions. Points are awarded for bigram and unigram probabilities, previous selection by the user, and length. The length addition meets my goal #1, as specified above, and the point system allows for the use of simultaneous criteria. I did not think that bigram probabilities, as used in the default comparator, were always more telling than unigram probabilities. If, for example, both bigram probabilities are extremely low, a slight different between them may not be the most telling sign of a good suggestion. My smart-ranking codifies this idea, by awarding points for low and high bigram probabilities on a dichotomous basis, rather than comparing them directly.

If the point system fails, then, in line with goal #1, the comparator sorts suggestions by their length. Longer suggestions are more likely to fill an autocomplete roll. Next, the comparator will check for words previously selected by the user (suggestions that have been validated). If that, too, fails, then the comparator falls back on the default comparator's lexicographic order method.