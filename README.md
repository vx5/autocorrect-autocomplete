# README

**Introduction**

This README includes sections devoted to known bugs in my project, explanations of any Checkstyle errors, design details of my project, any new runtime or space optimizations I effected, instructions on how to run all tests I wrote on my project, how to build and run my project from the command line, how my smart-ranking works, additional features, and an acknowledgements section.

Please note that this project was largely developed as part of a course at Brown University, CS32 -- Introduction to Software Engineering.

**Known Bugs**

I have no known bugs in my code.

**Style Errors**

I do not believe I have any checkstyle errors.

**Design Details Specific to my Project**

*Package Structure*

Most of the functionality in this project goes in the "ac" (short for Autocorrect) package. The exceptions are a new "common" package, which houses the Main and GUI class, which are common to Autocorrect and other full stack applications I may work on, a "trie" package, which houses all functionality related to the standard trie and radix trie, the "filereader" package, which houses utility classes related to reading files, including the TXTReader class that is relevant to this project, "repl" package, which houses the REPL used in both Stars and Autocorrect, and the "stringmanipulation" package, which houses the utility StringOps class, which allows for certain types of relevant String manipulation that might be useful in the future.

*High-Level REPL and GUI Structure*

My Main class is the highest-level class in my design -- it stores an instance of the AcCoordinator class, which is the highest-level class of the AutoCorrect implementation. Its location at this high-level allows the GUI and REPL to both access the same instance of AcCoordinator, which allows them to make requests and changes to the same Autocorrect implementation. 

The REPL parses REPL input for commands relevant to Autocorrect, which are delegated to the AcREPLHandler. The AcREPLHandler's handle() method parses commands into what, logically, they are asking, and then makes requests to the AcCoordinator class's AcOperator instance, which manages key operations for a given set of corpora and autocorrect settings. Design-wise, the AcCoordinator class is a class that can contain multiple AcOperators - in the scenario painted by design question #1, where multiple, wholly distinct autocorrects are desired, the AcREPLHandler (and the GUI) could understand user input so as to know which AcOperator to use.

A GUI class is used to run the Spark server and launch the Spark routes necessary to support the Autocorrect GUI. Those routes rely on handlers defined in the GUI.java file. Those handlers take in requests from the GUI, make the appropriate requests to the AcOperator (through the AcCoordinator class, described in the paragraph above), and then returns the appropriate data, sometimes by loading a FreeMarker template, sometimes through JSON.

Note that the input "cleaning" process, of removing punctuation and numbers, and of ensuring that all characters are in lowercase, is handled in the GUI and REPL handler classes. 

*Core Operations*

The AcOperator class manages all high-level operations related to AutoCorrect. It essentially manages a single implmementation of Autocorrect, complete with its own corpora and settings. It can accept new corpuses, by reading their contents and storing them in a RadixTrie, and can make autocorrect requests by generating suggestions via the prefix, whitespace, and Levenshtein edit methods (all of which are delegated) and then ranking them by either the default ranking method of the smart ranking method, depending on which setting is activated.

Note that the prefix generation method is delegated to the RadixTrie, while the whitespace and LED generation methods are delegated to static methods outside the class. Crucially, my LED generation method works by calculating all possible Levenshtein edits from the critical word, given an edit distance, NOT by calculating the edit distance from the given word to all words fed in from the corpora.

Please also note that the RadixTrie is my implementation of the compact version of the Trie. It is described in greater detail below.

*RadixTrie*

The RadixTrie structure starts like many tree-like structures - with a root node. From that node follow RadixTrieNodes, nodes that contain sequences of characters (stored in ArrayLists) that represent consecutive characters in a prefix tree that do not have any side-branches. Nodes are decorated as terminal if the path that follows from the root to that node spells out a word meant to be stored in the RadixTrie.

Checking whether a word is contained in the RadixTrie and generating words for which a given word is a prefix in the RadixTrie both involve iterating from the root node down to the end of the given word. In the contain search, if the end of the given word is located in the RadixTrie and decorated as terminal, the word is contained. In the prefix search, the end node is used as the launching point for a recursive search through the rest of the tree that looks for other terminal-decorated nodes below and constructs the relevant Strings.

Adding a word to the RadixTrie involves going as far as one can in the existing tree structure from the root node, splitting off into a new branch if and when the word is no longer contained in the RadixTrie, and then decorating the new ending node as terminal. This process complicates when the splitting-off point is within an existing node, and requires breaking that existing node into two nodes, parent and child ("de-coalescing" them, if you will), transferring the key properties (such as terminal status and the children node) to the new child node, and then extending the new branch off as a new child from the new parent node.

*GUI Features*

My gui can be accessed after using the "./run --gui" command in the terminal window, letting the terminal operations complete, and then opening a browser and heading to the URL "http://localhost:4567/autocorrect". The main page is simple -- users type in to the text box, and autocorrect suggestions appear in a dropdown list below the box. I did not use any pre-build dropdown, but rather built using divs. If users type more than fits in the box, the box will scroll, but the dropdown answers below will be truncated using ellipses. If the user begins typing while a corpus is not loaded, an error message appears below the box.

Users can select suggestions in one of two ways. They can use the mouse to hover over one of the suggestions (anywhere on that suggestion's box, not only where the text is), in which case the suggestion's box style will change to have white text on a blue background. They can also use the down and up arrow keys to select suggestions. Pressing the up-arrow while in the suggestion box does nothing, while pressing it on the top-most suggestion deselects all suggestions. Pressing the down-arrow while on the bottom-most suggestion does nothing, and the bottom-most suggestion will remain selected. Notably, one can select a box with the mouse, and then change that selection with the arrow keys, and vice versa. 

Users can click the Settings button to head to the Settings page. The Generation and Sorting section allows users to select which settings they want to use. The text next to the LED setting slider displays which LED selection is currently in use (I had to add this functionality separately). The Load Corpora section allows users to load more corpora from the data/autocorrect/ subdirectory. Corpora that are already in use are displayed in a box above, and users can scroll through the list of loaded corpora as more and more are included.

Once the user is ready to save their new settings (be they changes to the generation and sorting settings, the addition of new corpora, or both), they can click "Save new settings". They can also click "Reset settings" to reset all settings to the assignment defaults. Normally, when forms submit, all of the values of inputs are reset to some default. Using JavaScript, I made sure that when new settings are saved, the form populates with the existing values in use by the underlying AcOperator, so that when users arrive at the settings page, they know what the current settings are. Additionally, if any settings are changed to a value different from the current ones, the message between the "Save new settings" and "Return to main" buttons will populate with a message telling the user that they have unsaved changes (until, of course, they click Save new settings). If the user changes their settings in the form back to the current underlying settings, the message will return to normal (a descriptive message when the page first loads, and a message that all settings are up-to-date at all other times).

For both GUI pages, a favicon I made myself appears in the browser tab. Some of the text, including the large "Autocorrect" title on the main page, is in an imported font, the "Good Times" font by Raymond Larabie. All of the buttons were styled, and JavaScript was used to make them change color and the cursor change form when the mouse hovers over them. Most of my styling effort was related to spacing items out on pages, and I made extensive use of Flexboxes to accomplish this task. All of my GUI work benefited greatly from online resources to better understand JavaScript event handlers and CSS styles. 

**Runtime/Space Optimizations**

I did perform the suggested space optimization of implementing a compact trie, also known as a radix trie, and this Autocorrect implementation therefore relies on a radix trie rather than a standard trie. The key feature of the radix trie is that consecutive, non-branching nodes can be coalesced into a single node that stores their ordered characters. This feature decreases the number of nodes required to store a given set of words in a trie. 

In my implementation, whereas each node in the standard trie contained a character, each node in the radix trie contained a sequence of at least one (but possible more) characters, which, in effect, represents the coalesced nodes stored within. Unlike with the standard trie, the addition of new words into the trie may require the division, or de-coalescing, of existing nodes. After all, one of the previously consecutive non-branching fundamental nodes may now need to branch off. More specification are available above, in the design details' section RadixTrie subsection.

Please note that the radix trie functionality is contained in my projects trie package, which includes the relevant RadixTrie and RadixTrieNode classes.

**How to Run Tests**

There are 3 general sets of tests to run on my Autocorrect implementation -- JUnit tests, my personal system tests, and the TA system tests.

To run the JUnit tests, I took advantage of maven's automatic test-running while building, so I simply used the terminal command "mvn package" while in my project's root directory.

To run my personal system tests, from the project's root directory, I run "./cs32-test ./tests/autocorrect/\*", which runs all of my personal system tests. Some of these tests look at .txt files with errors in them, so I created some .txt files, and stored them in at the location data/autocorrect, right where the other .txt files are.

Please note that, in order to take advantage of the full time limits, I did sometimes add the "-t 10" flag at the end of my system test commands to make sure they did not time out.

**How to Run and Build Project from the Command Line**

The user builds the project from the command line by building with the terminal command "mvn package".

The user runs the project from the REPL with the "./run" command, which then launches the REPL, into which the user can directly type REPL commands.

The user runs the GUI with its flag, which involves the "./run --gui" command in the command line. Once loaded, the user can either add a corpus through the REPL, in the terminal window, or click the "settings" button in the GUI, which takes the user to the settings page, in which the user can upload corpora. Once at least one corpus is loaded, typing into the only text box on the main Autocorrect page will produce a dropdown list of suggestions. Many more details on the GUI can be found in the GUI Features subsection of the Design Details section, above.

**How my Smart-Ranking Works**

ADD THIRD BELOW

My smart-ranking design process began with the core question of what I was optimizing for. What did I actually want my Autocorrect suggestions to be? I settled on tree factors: 1) I want my Autocorrect answers to bend more in the direction of autocomplete, and want most to save time by having longer words be suggested (a greater emphasis on predicting the rest of a word than on correcting a given word), 2) I want to reward suggestions that are real words, and 3) I want to reward suggestions that the user has previously suggested. For number 2, this means making sure that suggestions are in the dictionary. In particular, because we remove punctuation from corpora when we load them, many non-words (e.g. "t" from "can't") are counted as words that are loaded into the Trie, so this also means valuing suggestions whose words are more than a character long. 

I translate this system into a series of comparisons, much like the default comparator. The first comparison checks if a suggestion has all its words in the dictionary, in the corpus, and of length greater than one. If exactly one suggestion meets this bar, it beats the other suggestion, then and there. If not, the next comparison repeats that check with the same criteria except for the dictionary component. The dictionary and length-greater-than-one criteria center on goal #2.

If those checks fail to offer a clear winner, then the comparator uses a point system to compare the suggestions. Points are awarded for bigram and unigram probabilities, previous selection by the user, and length. The length addition meets my goal #1, as specified above, and the point system allows for the use of simultaneous criteria. I did not think that bigram probabilities, as used in the default comparator, were always more telling than unigram probabilities. If, for example, both bigram probabilities are extremely low, a slight different between them may not be the most telling sign of a good suggestion. My smart-ranking codifies this idea, by awarding points for low and high bigram probabilities on a dichotomous basis, rather than comparing them directly.

If the point system fails, then, in line with goal #1, the comparator sorts suggestions by their length. Longer suggestions are more likely to fill an autocomplete roll. If that, too, fails, then the comparator falls back on the default comparator's lexicographic order method.

**Additional GUI details**

As stated above, my GUI can be accessed by running "./run --gui" from the command line, waiting for the setup process to complete, and then opening a browser and going to the URL "http://localhost:4567/autocorrect". In the src/main/resources folder, the .ftl templates, "acmain.ftl" and acsettings.ftl" are are in spark/template/freemarker/, while the CSS is located in static/css/ with "acmain.css" and "acsettings.css", along with the resources "goodtimes.ttf" (the imported font) and "ACLogo.png" (for the browser icon). The JavaScript is located in static/css/, with all files within being relevant to Autocorrect.

**Acknowledgments**

I used online resources to help me review specific types of Java errors, and for how to write in markdown language. I also used extensive resources to better understand JavaScript, jQuery, HTML, and, in particular, CSS styling. I sometimes used mainstream resources such as formal jQuery documentation and w3schools, and sometimes used other, more niche resources, such as css-tricks.com, to understand specific topics like Flexboxes.

