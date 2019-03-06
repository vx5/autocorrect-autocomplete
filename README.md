# README

## Stars
**Introduction**

This README includes sections devoted to known bugs in my project, design details of my project, any new runtime or space optimizations I effected, instructions on how to run all tests I wrote on my project, the Stars project's given design questions, explanations of any Checkstyle errors, and an acknowledgements section. I chose to add the acknowledgments section because I used it in a previous CS class, and found it to be a useful way to keep quick-and-dirty tabs on where I had gotten what ideas.

Please note that because I have / had the flu / a flu-like illness through the last week of the project work period, I had to make some choices regarding what I did and didn't want to spend time on (I didn't want to request an extension as long as I had minimum functionality, since I didn't want this project to bleed over into time that should be spent on Autocorrect unless it was absolutely necessary). As such, my JUnit tests are woefully incomplete, and given more time, I would have written many more JUnit tests, including, in particular, tests for the methods of the KDTree class.

**Known Bugs**

I do not know of any bugs in my program.

**Design Details Specific to my Project**

I will describe my design decisions in the following stages: high-level REPL and GUI structure (and connection to core operations), core operations (including k-d tree implementation and details), and GUI details.

*High-Level REPL and GUI Structure*

My entire project repeatedly refers to two objects. One is of a class called AllStars. Instances of the AllStars class have the responsibility of storing all the Stars that the user has loaded, and of performing certain Star-level operations that require access to all the Stars, such as mapping between an unknown Star's ID, name, and coordinates. One is of a class called KDTree, which both a) stores this projects implementation of a k-d tree, and facilitates its construction and b) executes the search operations within the k-d tree that are relevant to this assignment -- namely, the nearest neighbors search, and the within a radius search.

My Main class instantiates one of each object and passes them to an instance of the StarsGUI class, which in turn runs the critical runSparkServer() method which the stencil code has in the Main class. That method then defines Spark routes critical to the GUI's functioning. Those routes' handlers, defined in private static classes further down in StarsGUI.java, in turn use an instance of the StarsGUIHandler class, which manages all actions relevant to the GUI and its forms on behalf of the handlers. It, in turn, alters the QueryParamsMaps that represent what is actually being displayed in the GUI according to what inputs were made to the GUI (e.g. displaying a table of Stars when the GUI's command form is correctly used to request the n closest neighbors to a given Star). It handles most of these processes itself, but delegates the actual execution of core algorithms (that load Stars, or perform neighbors or radius searches) to an instance of the StarsOperator class.

On the REPL side, my Main class passes those two instantiations -- of AllStars, and of KDTree -- as arguments to the static runREPL() method. That method, housed in the REPL class, parses user input to the command line REPL into a String array, and then, if the first word (the command) matches a command relevant to the Stars project, passes the command line input to the StarsREPLHandler class. This class, much like the StarsGUIHandler class, manages all actions relevant to the REPL, mostly by interpreting the REPL input, understanding what output is required, and then delegating the execution of core algorithms to an instance of the StarsOperator class.

The purpose of the StarsOperator class is to have a unified way for the GUI, the REPL, or some other means of user access (designed for generality), to execute the core tasks and algorithms. It takes in arguments that are in terms more easily understood by the GUI and REPL and passes them to the core operations and algorithms (e.g. the bare-bones neighbors and radius searches).

*Core Operations*

The center of this project's core operations is the KDTree class. The KDTree class stores the project's implementation of k-d tree. Each object stored in the tree is stored in its own node. I made that design decision so that, intuitively, operations performed within the tree would closely resemble how those operations look on paper. The objects stored in the tree must implement the Spatial interface, which guarantees that they have attributes needed for them to sensibly even be in a k-d tree: namely, that they have coordinates that refer to their position in space, and that they have some form of identifying String.

I will describe the core operations used in building the tree, performing the neighbors search, and performing the radius search, below.

To build the tree, the KDTree begins at the 0 dimension, sorts nodes by their position in that dimension, and then chooses the median node as the root. In then repeats that process to determine the root's children within the set that was sorted below the median and the set sorted above, but uses the next dimension. This process repeats, iterating through the dimensions, until all the nodes have been sorted -- each node stores the dimension along which it was the median, which is useful in navigating the trees. Please note that in my project, I tried to avoid the terminology "left" and "right" child and instead used "below" and "above" child, to represent how the given children were "below" and "above" the median in their respective dimensions.

In both the neighbor and radius searches, the k-d tree takes advantage of the fact that if two nodes are a certain distance apart along a particular dimension, they are at least that far apart through full-dimensional space. 

At a high level, the neighbor search starts at the root node and moves down the tree, all the way iterating down by choosing which child would be closer to the center of the search. This could be done by seeing how far away the given child was from the center of the search along the dimension that was used to sort the child into its position in the tree. This typically, at each step, eliminates half of the tree as necessary to search. There are exceptions, though, either when more neighbors are needed or when the position of the child makes it such that closer nodes may be found by iterating on the other child.

At a high level, the radius search starts at the root node and moves down the tree. This time, instead of choosing children by which is closer, we choose them while it is possible that they are within the search radius, again by checking whether they are within the radius in the dimension the child was sorted along. 

For a more granular view of the searches, please refer to the KDTree class's getNeighbors() and searchRadius() methods and inline comments.

*GUI Details [Important for Grader]*

My GUI allows Stars to be loaded through either the GUI or the REPL. While I am still not completely clear on the requirements for minimum functionality, I was assured that my implementation almost certainly meets them. In any case, graders should be aware that the GUI requires you to load Stars not through the REPL but through the GUI itself. While the GUI is running, neighbor and radius commands can be submitted through the GUI (in which case results will be displayed in the GUI) or the REPL (in which case results will be displayed there). The two are not "linked" in the sense that commands submitted through the GUI show up in the GUI and the REPL, or that commands submitted through the REPL show up in the REPL and the GUI. Just to be clear, in my implementation, running an incorrect stars command (like "stars data/stars/hahathisisnotarealfile") does not clear the existing Stars that have been loaded.

My GUI page is based on the query.ftl template -- no other templates are used. At the top is a button that the user can click at any time to reset the page to its just-loaded specifications. Next is the load stars form. The User can enter the end of the filepath of the file to be read, and then click the "Upload these stars!" button. On the click, the GUI will display a green message if the stars were successfully loaded, along with how many stars were loaded, or a red message if there was some error, whether in finding, reading from, or making Stars from, the given file. Next is the command form, which lets the user choose a type of command, a bound for that command, and the center of the search, which can be specified through either the name of a star or a set of coordinates in 3-dimensional space (one is required, and the GUI will default to using the Star name when both are provided). 

Below, the GUI will display an error message in red if there was an issue with the command form input, or it will display the number of stars found. If the number of stars found is greater than 0, it will print the stars in a green-colored table format.

The adjustment of the variables in query.ftl that define the GUI can be found in this project's StarsGUIHandler class in the stars package.

**Runtime/Space Optimizations**

I did not attempt any runtime/space optimizations beyond what the assignment specifications asked of us.

**How to Run Tests**

There are 3 general sets of tests to run on my Stars implementation -- JUnit tests, my personal system tests, and the TA system tests.

To run the JUnit tests, I took advantage of maven's automatic test-running while building, so I simply used the terminal command "mvn package" while in my project's root directory.

To run my personal system tests, I adapt the terminal command to run the TA system tests to my own test suite's location. From the project's root directory, I run "./cs32-test ./tests/student/stars/\*", which runs all of my personal system tests. Some of these tests look at .csv files with errors in them, so I created some .csv files, and stored them in at the location data/stars, right where the other .csv files are.

To run the TA test suite, I navigate to the project's root directory, and run "./cs32-test ./tests/ta/stars/\*", which runs the entire provided TA test suite.

Please note that, in order to take advantage of the full time limits, I did sometimes add the "-t 10" flag at the end of my system test commands to make sure they did not time out (I got the instruction to do this from Piazza).

**Answers to Design Questions**

*Suppose that in addition to "neighbors" and "radius" you wanted to support 10+ more commands. How would you change your code - particularly your repl parsing - to do this? Don't worry about algorithmic details with the k-d tree, we're interested in the design*

I will answer this question with respond to the REPL, and then with respect to the GUI.

With respect to the REPL, I would first adjust the runREPL() method so that it could recognize these additional commands, and, when they were found, pass the entire command lines to the StarsREPLHandler. There, I would adjust the handle() method so that that if-else if chain that currently discerns between the stars, neighbors, and radius commands also could recognize the additional commands. That method would parse them, check for valid input, and then pass them to the StarsOperator class. In the StarsOperator class, I would then add methods that call on the correct core algorithms of the k-d tree in order to process the commands. This might involve the existing getNeighbors() and searchRadius() methods, or new methods entirely.

With respect to the GUI, I may need to add forms to the .ftl template for the GUI in order to handle these new commands. Depending on the commands, I may also need to add new variables to the template that could display error messages when there was an error with the command and the proper output when the command runs correctly. These new variables and form inputs would be processed in the StarsGUIHandler class, which could read inputs from these new forms, make the necessary calls to the updated StarsOperator class (described in the above paragraph), and then update the appropriate variables

*What are some problems you would run into if you wanted to use your k-d tree to find points that are close to each other on the earth's surface? You do not need to explain how to solve these problems.*

To me, there are two ways to approach this question of why the k-d tree (as we have seen it, at least) might be inadequate for the task: the case where points are described in three-dimensional space, and the case where points are described in two-dimensional space.

If the points are described in three-dimensional space (in essence, where we treat the globe as a sphere), we, on paper, have a fairly good approach. This is because if AB is shorter than AC, then arc AB, the earth-surface distance between those two points, will necessarily be shorter than arc AC. But practical issues still linger. Consider, for example, the added complexity of elevation. If two points being "on the earth's surface" does not guarantee that the earth is Spherical, and the arcs described above would not, in fact, represent the distance that even a superhuman traveler (who can travel in any direction so long as they do not drop below the surface of the earth) would face between two points.

If the points are described in two-dimensional space (using, say, lattitude and longitude coordinates to describe their position on the earth), then we run into more structural problems. Because any coordinate plane we drape over the earth will be distorted (since the earth is, to the best of our knowledge, spherical, and not flat), the distances between points when we pretend they are in 2D space would not resemble their surface-of-the-earth distance. In fact, if AB is shorter than AC in this sytem, points A and B, over the earth's surface, could be further apart than A and C.

*Your k-d tree supports most of the methods required by the Collection interface. What would you have to add/change to make code like Collection<Star> db = new KDTree<Star>() compile and work properly?*

At a high level, I would not have to add too much functionality to the KDTree to make the code above work (note that my KDTree is of type KDNode, not Star, and my KDNodes are of type Star, but I will gloss over that detail for the purposes of this question). Essentally, I would need to have KDTree implement the Collection interface, and then implement the additional methods. The contains() and containsAll() methods would simply require recursive searches through the KDTree. The equals() method would likely require recursive searches of both the given Object and this KDTree, simultaneously, in order to determine whether the trees had identical structures and identical elements throughout. The hashCode() function would require the design of a hash function for KDTrees, possibly one that combines details of the tree's structure with details of the tree's contents. The remove- and retainAll() functions are tricker, and might require rebuilding the tree in full after the method calls. Other methods, like isEmpty(), iterator(), and toArray() essentially take advantage of existing functionality.

**Checkstyle Errors Explanation**

I do not have any Checkstyle errors. When I run mvn site, the resulting Checkstyle page displays no Checkstyle errors.

**Acknowledgements** 

I used online resources to help me review certain basic Java concepts, and understand areas of HTML and CSS I wanted to understand better. I also looked online for basic instructions on how to write in markdown language.

I also got some inspiration for my GUI (in particular, for the starry background) from the GUI we were shown during the Stars gear-up.

## Autocorrect

**Introduction**

This README includes sections devoted to known bugs in my project, explanations of any Checkstyle errors, design details of my project, any new runtime or space optimizations I effected, instructions on how to run all tests I wrote on my project, how to build and run my project from the command line, how my smart-ranking works, the Autocorrect project's given design questions, what I did for extra credit, and an acknowledgements section.

**Known Bugs**

I have no known bugs in my code.

**Checkstyle Errors**

I do not believe I have any checkstyle errors.

**Design Details Specific to my Project**

*Package Structure*

Most of the functionality in this project goes in the "ac" (short for Autocorrect) package. The exceptions are a new "common" package, which houses the Main and GUI class, which are common to Stars, Autocorrect, and (I am guessing) Bacon, a "trie" package, which houses all functionality related to the standard trie and radix trie, the "filereader" package, which houses utility classes related to reading files, including the TXTReader class that is relevant to this project, "repl" package, which houses the REPL used in both Stars and Autocorrect, and the "stringmanipulation" package, which houses the utility StringOps class, which allows for certain types of relevant String manipulation that might be useful in the future.

*High-Level REPL and GUI Structure*

Just as in Stars, my Main class is the highest-level class in my design. Most relevantly to Autocorrect, it stores an instance of the AcCoordinator class, which is the highest-level class of the AutoCorrect implementation. Its location at this high-level allows the GUI and REPL to both access the same instance of AcCoordinator, which allows them to make requests and changes to the same Autocorrect implementation. 

The REPL, just as in Stars, parses REPL input for commands relevant to Stars and commands relevant to Autocorrect. Commands relevant to Stars are delegated to the StarsREPLHandler, and from that point, are discussed further in the above Stars portion of this README. Commands relevant to Autocorrect are delegated to the AcREPLHandler. The AcREPLHandler's handle() method parses commands into what, logically, they are asking, and then makes requests to the AcCoordinator class's AcOperator instance, which manages key operations for a given set of corpora and autocorrect settings. Design-wise, the AcCoordinator class is a class that can contain multiple AcOperators - in the scenario painted by design question #1, where multiple, wholly distinct autocorrects are desired, the AcREPLHandler (and the GUI) could understand user input so as to know which AcOperator to use.

A GUI class is used to run the Spark server and launch the Spark routes necessary to support both the Stars and Autocorrect GUIs. Those routes rely on handlers defined in the GUI.java file. Those handlers take in requests from the GUI, make the appropriate requests to the AcOperator (through the AcCoordinator class, described in the paragraph above), and then returns the appropriate data, sometimes by loading a FreeMarker template, sometimes through JSON.

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

Most of all, I committed a lot of time to styling the pages. For both pages, a favicon I made myself appears in the browser tab. Some of the text, including the large "Autocorrect" title on the main page, is in an imported font, the "Good Times" font by Raymond Larabie. All of the buttons were styled, and JavaScript was used to make them change color and the cursor change form when the mouse hovers over them. Most of my styling effort was related to spacing items out on pages, and I made extensive use of Flexboxes to accomplish this task. All of my GUI work benefited greatly from online resources to better understand JavaScript event handlers and CSS styles. To be completely honest, I also spent a fair amount of time making design decisions (where to put elements, how to display matches).


**Runtime/Space Optimizations**

I did perform the suggested space optimization of implementing a compact trie, also known as a radix trie, and this Autocorrect submission, as of implementation, relies on a radix trie rather than a standard trie. The key feature of the radix trie is that consecutive, non-branching nodes can be coalesced into a single node that stores their ordered characters. This feature decreases the number of nodes required to store a given set of words in a trie. 

In my implementation, whereas each node in the standard trie contained a character, each node in the radix trie contained a sequence of at least one (but possible more) characters, which, in effect, represents the coalesced nodes stored within. Unlike with the standard trie, the addition of new words into the trie may require the division, or de-coalescing, of existing nodes. After all, one of the previously consecutive non-branching fundamental nodes may now need to branch off. More specification are available above, in the design details' section RadixTrie subsection.

Please note that the radix trie functionality is contained in my projects trie package, which includes the relevant RadixTrie and RadixTrieNode classes. Before implementing this optimized trie, I used a standard trie. In that same trie package, the Trie and TrieNode classes include the relevant functionality for the standard trie. If you wish to run my program with the standard trie, you need only change the data structure specified in the AcOperator class, at lines 27 and 78, from RadixTrie to Trie -- you will also need to change the import line at the top of AcOperator.java from RadixTrie to Trie.

**How to Run Tests**

There are 3 general sets of tests to run on my Autocorrect implementation -- JUnit tests, my personal system tests, and the TA system tests.

To run the JUnit tests, I took advantage of maven's automatic test-running while building, so I simply used the terminal command "mvn package" while in my project's root directory.

To run my personal system tests, I adapt the terminal command to run the TA system tests to my own test suite's location. From the project's root directory, I run "./cs32-test ./tests/student/autocorrect/\*", which runs all of my personal system tests. Some of these tests look at .txt files with errors in them, so I created some .txt files, and stored them in at the location data/autocorrect, right where the other .txt files are.

To run the TA test suite, I navigate to the project's root directory, and run "./cs32-test ./tests/ta/autocorrect/\*", which runs the entire provided TA test suite.

Please note that, in order to take advantage of the full time limits, I did sometimes add the "-t 10" flag at the end of my system test commands to make sure they did not time out (I got the instruction to do this from Piazza on the Stars project).

**How to Run and Build Project from the Command Line**

The user builds the project from the command line by building with the terminal command "mvn package".

The user runs the project from the REPL with the "./run" command, which then launches the REPL, into which the user can directly type REPL commands.

The user runs the GUI with its flag, which involves the "./run --gui" command in the command line. Once loaded, the user can either add a corpus through the REPL, in the terminal window, or click the "settings" button in the GUI, which takes the user to the settings page, in which the user can upload corpora. Once at least one corpus is loaded, typing into the only text box on the main Autocorrect page will produce a dropdown list of suggestions. Many more details on the GUI can be found in the GUI Features subsection of the Design Details section, above.

**How my Smart-Ranking Works**

My smart-ranking design process began with the core question of what I was optimizing for. What did I actually want my Autocorrect suggestions to be? I settled on two answers: 1) I want my Autocorrect answers to bend more in the direction of autocomplete, and want most to save time by having longer words be suggested (a greater emphasis on predicting the rest of a word than on correcting a given word), and 2) I want to reward suggestions that are real words. For number 2, this means making sure that suggestions are in the dictionary. In particular, because we remove punctuation from corpora when we load them, many non-words (e.g. "t" from "can't") are counted as words that are loaded into the Trie, so this also means valuing suggestions whose words are more than a character long. 

I translate this system into a series of comparisons, much like the default comparator. The first comparison checks if a suggestion has all its words in the dictionary, in the corpus, and of length greater than one. If exactly one suggestion meets this bar, it beats the other suggestion, then and there. If not, the next comparison repeats that check with the same criteria except for the dictionary component. The dictionary and length-greater-than-one criteria center on goal #2.

If those checks fail to offer a clear winner, then the comparator uses a point system to compare the suggestions. Points are awarded for bigram and unigram probabilities, as well as for length. The length addition meets my goal #1, as specified above, and the point system allows for the use of simultaneous criteria. I did not think that bigram probabilities, as used in the default comparator, were always more telling than unigram probabilities. If, for example, both bigram probabilities are extremely low, a slight different between them may not be the most telling sign of a good suggestion. My smart-ranking codifies this idea, by awarding points for low and high bigram probabilities on a dichotomous basis, rather than comparing them directly.

If the point system fails, then, in line with goal #1, the comparator sorts suggestions by their length. Longer suggestions are more likely to fill an autocomplete roll. If that, too, fails, then the comparator falls back on the default comparator's lexicographic order method.

**Answers to Design Questions**

*How would you change your frontend/backend code so that you could handle autocorrecting multiple input fields on the same page? Would you need to make any changes? There are two issues: two inputs on the same page and two inputs on different pages that should autocompleted using different corpora.*

First comes the case of two inputs on the same page. On the front-end, I would need to add another input text field in the "acmain.ftl" template. Assuming that this field relies on the same autocorrect (same corpora and same settings) as the first text field, in the relevant JavaScript file, I would add a POST request nearly identical to the existing one, where changes to the new text field trigger POST requests that return with autocorrect suggestions, which are then used to populate a drop-down menu similar to the one in the first text field. The drop-down menus functionality, including the divs in the .ftl template and the JavaScript, would also need to be attached to this second text field.

In the case of two inputs on different pages that should be autocompleted using different corpora, I would make use of the AcCoordinator class. The AcCoordinator class can store multiple AcOperators, each of which coordinates its own autocorrects with its own set of corpora and settings. Thus, the two different inputs could make their autocorrect POST requests include information on which AcOperator is to be used, information which would be passed to the handler and then on to the AcGUIHandler class. There, some slight changes would allow it to use the information on which AcOperator is to be used to delegate autocorrect requests to the correct AcOperator, since it currently assumes it will always use the first AcOperator, which is also the only AcOperator.

*Suppose some new letter, θ, has been introduced into the English alphabet. This letter can be appended to the end of any English word, to negate it. For example, badθ would mean "good". We are asking about the effects of this 1984-esque vocabularily enhanced universe on the size of your Trie. You will now need to store twice as many words in your trie as before. How many more nodes will you need to store in your trie? We are looking for you to support your answer with details about how your trie is implemented and what data it stores.*

Let us assume that we are using a standard Trie, in which some number n words are stored. In this case, while the Trie will store 2n words, we will only need n additional nodes to represent these new words. This is because the new words merely result from adding a letter to every existing word. In this manner, every terminal node, or node at which a word ends, would be given a new child node, which contains the character θ, which is also terminal. Since there are n words stored in the Trie, there are n existing terminal nodes, and thus we would add n new nodes containing θ to the Trie.

My implementation uses a radix trie, but that detail should not change the answer. In the radix trie, too, there is one terminal node for each word stored in the trie, and the addition of these new words would require a new child node, containing only the character θ, to each terminal node. We would thus also need to add n nodes to the trie in the case of the radix trie.

**Extra Credit**

I pursued two main avenues of extra credit: the compact trie optimization, and the GUI.

My compact trie optimization, or radix trie, is in use in the submitted version of Autocorrect. I was somewhat familiar with the structure, which I remember in theoretical terms from CSCI 1810. It has all the same functionality as the standard trie, and is housed in the trie package, where its functionality is spread between the RadixTrie and RadixTrieNode classes, located in RadixTrie.java and RadixTrieNode.java, respectively. The inner workings of the RadixTrie are discussed in its subsection of the design details section.

My GUI was one of the parts of my project I spent the most time on. A proper description of the functionality is described in the GUI subsection of the Design Details section above. Please read that section for a full description of my GUI. This section only means to list some of the tools I used in my GUI, beyond what was required. The ordering of this list is arbitrary:

1. Divs (may have been necessary in some ways anyways)
2. Flexboxes (and all associated attributes)
3. Range inputs
4. Favicons / icons that appear in brower tabs
5. Importing outside fonts
6. Handling overflowing text (CSS white-space, overflow, text-overflow attributes)
7. Changing the type of cursor
8. Div padding
9. The CSS "inherit" keyword
10. Spacing elements like width, height, and transform

I also made some higher-level design choices, like:

1. Making changes to a page based on mouse and key usage
2. Making sure key selections do not interfere with other text inputs on the same page
3. Preserving a form's inputs in the form after the form is submitted
4. Recognizing when elements of a form have changed (using the jQuery .change() method)
5. Design choices themself, such as making a separate settings page

As stated above, my GUI can be accessed by running "./run --gui" from the command line, waiting for the setup process to complete, and then opening a browser and going to the URL "http://localhost:4567/autocorrect". In the src/main/resources folder, the .ftl templates, "acmain.ftl" and acsettings.ftl" are are in spark/template/freemarker/, while the CSS is located in static/css/ with "acmain.css" and "acsettings.css", along with the resources "goodtimes.ttf" (the imported font) and "ACLogo.png" (for the browser icon). The JavaScript is located in static/css/, with all files within being relevant to Autocorrect.

**Acknowledgments**

I used online resources to help me review specific types of Java errors, and for how to write in markdown language. I also used extensive resources to better understand JavaScript, jQuery, HTML, and, in particular, CSS styling. I sometimes used mainstream resources such as formal jQuery documentation and w3schools (which we were directed to in CS 2), and sometimes used other, more niche resources, such as css-tricks.com, to understand specific topics like Flexboxes.

## Bacon

**Introduction**

This README includes sections devoted to known bugs in my project, explanations of any Checkstyle errors, design details of my project, any new runtime or space optimizations I effected, instructions on how to run all tests I wrote on my project, how to build and run my project from the command line, the Bacon project's given design questions, and an acknowledgements section.

**Known Bugs**

I have no known bugs in my code.

**Checkstyle Errors**

I do not believe I have any checkstyle errors.

**Design Details Specific to my Project**

*High-Level REPL and GUI Structure*

Just as in Stars and Autocorrect, my Main class is the highest-level class in my design. The REPL, just as before, parses REPL input for commands relevant to Stars, Autocorrect, and Bacon. Commands relevant to Stars are delegated to the StarsREPLHandler, and from that point, are discussed further in the above Stars portion of this README. Commands relevant to Autocorrect are delegated to the AcREPLHandler, and from that point, are discussed further in the Autocorrect portion of this README. The BaconREPLHandler's handle() method parses commands into what, logically, they are asking, and then makes requests to the BaconOperator instance, which manages the setting of databases and the construction of BaconPaths. For design purposes, there is exactly one instance of BaconOperator, a design achieved through a singleton pattern, which allows both the GUI and REPL to access it without it being passed as an argument through a method or constructor.

A GUI class is used to run the Spark server and launch the Spark routes necessary to support the Stars, Autocorrect, and Bacon GUIs. For Bacon, those routes rely on handlers defined in the GUI.java file. Those handlers take in requests from the GUI, and make appropriate requests to the methods of the BaconGUIHandler class, which stores variables relevant to the population of all Bacon-related GUI pages. Those methods, in turn, make the appropriate requests to the BaconOperator, and then returns the appropriate data. The GUI handler sometimes loads a FreeMarker template, and sometimes returns through JSON.

*Core Operations*

The BaconOperator class manages all high-level operations related to Bacon. It delegates requests related to database access to the BaconDbOp class, and delegates requests related to pathfinding to a wrapper PathOrg class, which contains a Dijkstra's object. The PathOrg class is used to find new paths, given a method to use -- it was inspired by the 1st design question, since other methods of finding paths could easily be added if they, like Dijkstra, implement the PathFinder interface.

My Dijkstra's implementation's generic (parametrized) form is found in the dijkstra package. Central are the DVertex and DEdge classes, which represent vertex and edges in a Dijkstra graph. The Dijkstra object manages the actual pathfinding process. When it needs to expand the graph by looking at new edges and nodes, it relies on an implementation using the DijkstraDbOp interface, which was inspired by the 2nd design question, since the interface leaves ambiguous how new edges and vertexes are obtained up to the specific implementation. In this project, the class implementing DijkstraDbOp is BaconDbOp, which handles the interactions with the SQL database needed for Dijkstra's, among other functions.

The specific extensions of DVertex and DEdge used by Bacon are called ActorVertex and FilmEdge. Their only added functionality (beyond the superclass's) is the inclusion of a name which, in this project, refers to Actor's names and Film's names (as opposed to their ids). Additionally, because the PathOrg returns a sequence of nodes (containing references to edges) called PathNodes (and PathEdges), meant to be generic across all pathfinding methods, DVertex and DEdge must implement these interfaces. This, too, was inspired by the 1st design question.

*Caching*

I implemented caching in this project. Because I was told that the most time-consuming aspect of my code was likely its interactions with the SQL database, I used caches in the BaconDbOp class. The methods in that class rely on several "building-block" methods that represent basic interactions with the database, of which there are 5. I thus made 5 caches, one for each of these basic operations. In each case, the building block method stores new SQL request results in the cache, and uses the cache instead of making a SQL request when possible.

*Runtime / Space Optimizations*

The only runtime or space optimizations I performed were caching, described above, and the use of a graph that incrementally loads information from the database as necessary, rather than all at once. I did not effect any optimizations beyond those discussed in the assignment handout, and do not believe any of them qualify for extra credit.

*GUI*

My GUI can be run by typing "./run --gui" into the command line from the project directory, and then navigating in a browser to "http://localhost:4567/bacon". Users should first load a SQLite database through the REPL, which can be done with the command "mdb pathname", where "pathname" is the file path from the project directory to the database (e.g. "mdb data/bacon/smallBacon.sqlite3"). If they miss this step, the GUI will display an error message. They can then type the names of the actors they want to connect into the two given boxes, and press the "Find path!" button. Please note two things: 1) when a path is not found, an error message is displayed (that still says "path not found"), which was a personal choice on my part. 2) when using the large database, the autocorrecting is very slow and lags behind the key-presses quite a bit. I showed it in TA hours to a TA, who told me it still met minimum functionality, but I wanted to document that slowness here for clarity. Because path requests on the large database take time, the page displays a loading message while the server processes the request.

On the actor and film pages, a button at the bottom allows the user the return to the original Bacon search page.

**Extra Runtime / Space Optimizations**

I did not effect any additional runtime or space optimizations. The ones I did effect are documented in the design details section's "runtime / space optimizations" subsections.

**How to Run Tests**

There are 3 general sets of tests to run on my Bacon implementation -- JUnit tests, my personal system tests, and the TA system tests.

To run the JUnit tests, I took advantage of maven's automatic test-running while building, so I simply used the terminal command "mvn package" while in my project's root directory.

To run my personal system tests, I adapt the terminal command to run the TA system tests to my own test suite's location. From the project's root directory, I run "./cs32-test ./tests/student/bacon/\*", which runs all of my personal system tests. Please note that one of my personal tests uses the large bacon.sqlite3 database, and will timeout. This is expected -- it must be run separately, using the command "./cs32-test ./tests/student/bacon/connect_no_name_actor_large_db.test -t 60". I use 60 as the time cutoff, because a TA told me it was the maximum time allowed for path requests when using the large database.

To run the TA test suite, I navigate to the project's root directory, and run "./cs32-test ./tests/ta/bacon/\*", which runs the entire provided TA test suite.

Please note that, in order to take advantage of the full time limits, I did sometimes add the "-t 10" flag at the end of my system test commands (except for the large database test) to make sure they did not time out (I got the instruction to do this from Piazza on the Stars project, and am doing that again here).

**How to Run and Build Project from the Command Line**

The user builds the project from the command line by building with the terminal command "mvn package".

The user runs the project from the REPL with the "./run" command, which then launches the REPL, into which the user can directly type REPL commands.

The user runs the GUI with its flag, which involves the "./run --gui" command in the command line. Once loaded, the user can add a database through the repl using the "mdb" command with the file path of the desired database, e.g. "mdb data/bacon/smallBacon.sqlite3". Once loaded, the user can navigate to the Bacon page in a browser, type in actor's names, and search for a path between them. More detail on GUI functionality is available in the GUI subsection of the design details section above.

**Answers to Design Questions**

*How could you modify your project, so other developers can easily add new graph search algorithms without having to worry about other constraints of the project (e.g. structure of the database, first initial -> last initial)?*

My project uses a PathOrg object which holds all of the different pathfinding methods -- method calls need only specify the name of the method to be used, as well as the starting and ending details, and a path built using the specified method is returned. The path returned by this object is made up of PathNodes (an interface), which contain references to PathEdges (also an interface). Thus, adding a new graph search method would require a new object for the new method, an object that must implement the PathFinder vertex, and that thus must implement a pathfinding method that returns a sequences of nodes, containing references to their previous edges, that implement the PathNode and PathEdge interfaces, respectively.

That class itself would deal with the database structure and valid neighbor criteria, likely using an interface like Dijkstra's DijkstraDbOp, which a specific implementation of the algorithm would implement and populate.

*How could you improve your code to make it able to accept multiple types of files? For example, what if you wanted your program to be able to accept both a SQL database or a number of CSV files containing the same data?*

My Dijkstra's implementation uses a DijkstraDbOp interface, with methods relating to getting the neighbors of a particular vertex and creating new vertices. A specific project using Dijkstra's would require a class that implements this interface, with contents for the interface's methods that specify how to get the needed information. This implementing class would store references to the database, whether it be a SQL database, or a set of different .csv files. Its methods would handle interactions with the database, whether they involved making requests of a database or reading from a .csv file. Notably, the interface would need to know what types of vertexes and edges the specific project is using, as some of the database interaction methods require the construction of new vertexes and edges (hence the DijkstraDbOp interface's parametrization).

*What would you need to change if movies now had an associated year of release and the chain of movies had to go in chronological order?*

This question is a question or altering Dijkstra's algorithm. Ostensibly, this new requirement is a requirement analogous to the last initial-first initial requirement this project already uses; each vertex carries an attribute, in this case, the year of the movie whose edge represents the current best path to this vertex, and that attribute has to be compared between nodes to understand if a path segment is valid. But unlike the initial's of an actor's name, the year of the movie whose edge represents the current best path to the vertex is not known from the moment the vertex is constructed. Treating this new requirement as analogous to the initials requirement could lead to a scenario where a vertex V, currently best-reached by X-Men (2000), only checks neighbors when the connecting movie was released in 2000 or later. Later on, however, V's path is updated, so that it is best-reached by First Contact (1996). All of V's neighbors released betweeb 1996 and 1999 should now be considered, but will not be, because V is visited.

My solution involves, as described above, vertices storing the year in which they are best-reached. It recognizes, however, that vertexes where the stored year is changed essentially need to be "re-visited", or again check what the valid neighbors are. This can be accomplished through brute force: the algorithm could add any node whose stored year is changed back to the front of the PriorityQueue, where a special condition would check which of its successive nodes require updating, which of their nodes require updating, and so forth. 

**Acknowledgements**

I used online resources to help me review specific types of Java errors, how to use parametrized classes, and for how to write in markdown language. I also used online resources regarding HTML, CSS, and JavaScript, mostly the same resources I used on Autocorrect.
