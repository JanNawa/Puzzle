# Puzzle
This program convert the text file that come in certain format to the puzzle with words that need to be filled. 
The formatting requirements are in the csci 3901 course assignment #4. 
The program could solve the puzzle if the given puzzle is solvable. 
The result could be printed to the file in puzzle format.

The solution uses serveral divisions between the kinds of information in the input files:
* create puzzle board by using number of column and row
* store all the slots in the data structure
* store all the words in the data structure

The puzzle will be solved by 
* Identify slots
* Try each slot with the word with the same length as slot
* If every slots can be filled without conflict, it is solved.

The puzzle will be written to the file as a puzzle in the borad format.
For example: 
```
			 f    
			 r  b 
			 array
			 i  s 
			plush 
```

## Files and external data
There are 2 main files:
* FillInPuzzle.java - class that load puzzle, solve puzzle and write puzzle to file
* PuzzleBox.java - class that indicate the starting box and direction in the puzzle

## Data structures and their relations to each other
### puzzle
The program store the board puzzle in 2D static array as the board 
will never changed in size. Easy to identify the board from the input file
and use to fill the word in the box to prepare it to be printed in the
output file. The symbol in puzzle have the meaning as listed below:
* the signed "+" incidate that it is not PuzzleBox (can't be filled)
* the signed "-" incidate that it is a PuzzleBox (can be filled)

### words
All the words in the puzzle is stored in Map. Use the word length as a key
and fill the word as set of value. Use set to make sure that all the words
are unique.

### usedWords
The word that is already used in the puzzle will be stored 
in the set of usedWord. This will help exclude the word that already used 
in puzzle when looping through the slots and trying to solve the problem. 

### wordSlots
The wordSlots used to store the starting puzzle box detail as key and 
word length as value. This will help when retriving the word key in map.
The puzzleBox used to store the starting row and column of that box and
the direction (either horizontal or vertical).

### wordCount & isNumberOfWordMatched
using to check the word that mentioned in file. 
If the word mentioned in file and number of words given is mismatch,
the returned value will be false and skip to solve the puzzle right away
(as the number of words are mismatch, no way the puzzle could be solved).

## Assumptions
* no repeated word in the puzzle
* no space in any word

## Choices
* all the words will be treated as lowercase
* row 0 and column 0 will be started at the top left corner
* if the puzzle can't be solved, the unfilled puzzle will be print.

## Key algorithms and design elements
Encapsulation is used for information hiding.

The program processes the input file one line at a time. The data is 
distinguished by using the regex and assigned the information into appropriated
data structure.

The puzzle is solved by following the steps below (back-tracking with recursion): &nbsp;
	1. Get 1 slot from the wordSlots&nbsp;
* extract the key-value to get information about that slot
* if the wordSlots is empty, stop solving. &nbsp;
	2. Create copy of puzzle for back-tracking
	3. Get all the words that have the same length as the value from slot this will help in improve efficiency 
			by only choosing the word that is possible fit to the slot &nbsp;
	4. Loop through all the words with same length &nbsp;
* if the word is already used, skip that word
			Increase the efficiency by having the data structure 
			to keep track of the words used. Therefore, 
			the duplication won't be made while iterate through 
			the word with the same length.
* if part of the slot is filled, check for conflict
			* if there is conflict, exit and continue the loop to next word
			* if no conflict, fill the slot with the word &nbsp;
	5. Try next slot (Recursion from the step 1) &nbsp;
	6. If the recursion can't find the solution, revert back and try next word. &nbsp;
	7. If no solution, revert back and put slot back and go back. &nbsp;

The program writes the output to the file one line at a time from the puzzle
that stored in 2D array. The symbol "+" is changed to " ", 
which helpful to identify the word in the puzzle.

## Limitations
The current design is limited to solve one puzzle at a time.

## References
* Sheepy. (2017) Solving crosswords [source code].https://stackoverflow.com/questions/44606961/solving-crosswords
