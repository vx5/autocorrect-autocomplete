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
_Coming soon!_

## Bacon
_Coming soon!_
