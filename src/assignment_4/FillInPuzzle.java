package assignment_4;

import java.io.*;
import java.util.*;
import java.util.function.*;

/**
 * This code was referenced from :
 * Sheepy. (2017) Solving crosswords [source code].
 * https://stackoverflow.com/questions/44606961/solving-crosswords
 * 
 * @author Jan
 */
public class FillInPuzzle {

    // Define symbols to use in the program as a way of knowing what we're seeing in the text.
    public static final String SPACES_SEPARATOR = "\\s+";
    public static final String NUMBER = "[0-9]";
    public static final String DIRECTION = "[v|V|h|H]";
    public static final String VERTICAL = "v";
    public static final String HORIZONTAL = "h";
    public static final String NO_FILL_PUZZLE = "+";
    public static final String FILL_PUZZLE = "-";
    public static final String PUZZLE_DETAIL = NUMBER + SPACES_SEPARATOR + NUMBER + SPACES_SEPARATOR + NUMBER;
    public static final String WORD_LOCATION = PUZZLE_DETAIL + SPACES_SEPARATOR + DIRECTION;
    public static final String NEW_LINE = "\r\n";

    // Store value associated to puzzle
    private int wordCount = 0;
    private int guess = 0;
    private boolean isSolved = false;
    private boolean isNumberOfWordMatched = true;
    // Store data from reading the loadPuzzle
    private String[][] puzzle = null;
    private Map<Integer, Set<String>> words = new HashMap<>();
    private Set<String> usedWords = new HashSet<>();
    private Map<PuzzleBox, Integer> wordSlots = new HashMap<>();

    // read the puzzle
    public Boolean loadPuzzle(BufferedReader stream) {
        if (stream == null) {
            return false;
        }
        try (BufferedReader br = stream) {
            // read data from file
            String input;
            int countWords = 0;
            while ((input = br.readLine()) != null) {
                input = input.trim();
                if (input.equals("")) {
                    continue;
                }
                if (input.matches(PUZZLE_DETAIL)) {
                    // create empty puzzle
                    createEmptyPuzzle(input);
                } else if (input.matches(WORD_LOCATION)) {
                    // places word location in the puzzle
                    placeWord(input);
                } else {
                    // put the word into data structure
                    addWord(input);
                    countWords++;
                }
            }
            // if puzzle isn't created at this point, return false
            if(puzzle == null){
                return false;
            }
            // the the number of words mentioned in file is not the same as amount of word given 
            if (wordCount != countWords) {
                isNumberOfWordMatched = false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // create the puzzle board
    private void createEmptyPuzzle(String input) {
        String[] line = input.split(SPACES_SEPARATOR);
        int column = Integer.parseInt(line[0]);
        int row = Integer.parseInt(line[1]);
        wordCount = Integer.parseInt(line[2]);
        puzzle = new String[row][column];
        // create empty puzzle board to 2D array
        for (int i = 0; i < puzzle.length; i++) {
            for (int j = 0; j < puzzle[i].length; j++) {
                puzzle[i][j] = NO_FILL_PUZZLE;
            }
        }
    }

    // place the unfilled puzzle box in puzzle board
    private void placeWord(String input) {
        String[] line = input.split(SPACES_SEPARATOR);
        int column = Integer.parseInt(line[0]);
        int row = Math.abs(Integer.parseInt(line[1]) - (puzzle.length - 1));
        int wordLength = Integer.parseInt(line[2]);
        String direction = line[3].toLowerCase();
        // put starting puzzle box into data structure (easier to retrieve data related to that box)
        wordSlots.put(new PuzzleBox(row, column, direction), wordLength);
        // assign unfilled puzzle into 2D array
        switch (direction) {
            case HORIZONTAL:
                for (int i = column; i < column + wordLength; i++) {
                    puzzle[row][i] = FILL_PUZZLE;
                }
                break;
            case VERTICAL:
                for (int i = row; i < row + wordLength; i++) {
                    puzzle[i][column] = FILL_PUZZLE;
                }
                break;
        }
    }

    // add word to data structure
    private void addWord(String input) {
        int wordLength = input.length();
        // if key is not exist
        if (!words.containsKey(wordLength)) {
            words.put(wordLength, new HashSet<>());
        }
        // add value to appropriate length (add value to key)
        words.get(wordLength).add(input.toLowerCase());
    }

    //----------------------------------------------------------------------------//
    // solve the puzzle
    public Boolean solve() {
        if (!isNumberOfWordMatched || 0 == wordCount) {
            return false;
        } else {
            isSolved = solvePuzzle(wordSlots, this::fitWord);
        }
        return isSolved;
    }

    // check if the box is empty
    private boolean isEmpty(int row, int column) {
        return puzzle[row][column].equals(FILL_PUZZLE);
    }

    // Try to fit a word to a slot
    private boolean fitWord(PuzzleBox puzzleBox, String word) {
        int row = puzzleBox.getRow();
        int column = puzzleBox.getColumn();
        String direction = puzzleBox.getDirection();
        // loop through all the character and assign to the puzzle
        for (int i = 0; i < word.length(); i++) {
            String string = String.valueOf(word.charAt(i));
            // if there is a conflict, return false
            if (!isEmpty(row, column) && !puzzle[row][column].equals(string)) {
                return false;
            }
            // assign character to the puzzle
            puzzle[row][column] = string;

            switch (direction) {
                case HORIZONTAL:
                    column++;
                    break;
                case VERTICAL:
                    row++;
                    break;
            }
        }
        // after add word to puzzle, add the word to usedWords to indicate that the word is already used
        usedWords.add(word);
        return true;
    }

    // solve the puzzle by selecting the slot and find the word that match the slot size
    // if there is conflict, revert back and try another word
    private boolean solvePuzzle(Map<PuzzleBox, Integer> slot, BiFunction<PuzzleBox, String, Boolean> fill) {
        // if no more slot to filled, return true
        if (slot.isEmpty()) {
            return true;
        }
        // get the slot key-value
        PuzzleBox key = slot.keySet().iterator().next();
        int wordLength = slot.remove(key);

        // create the copy of puzzle for using in back tracking the puzzle
        String[][] copyPuzzle = Arrays.stream(puzzle).map(String[]::clone).toArray(String[][]::new);

        // get all the word that match the length of slot
        Set<String> allWords = words.get(wordLength);
        // run through all the words that match the length of slot
        for (String word : allWords) {
            // if the word already used, skip it
            if (usedWords.contains(word)) {
                continue;
            }

            // if the word fit, do the recursion
            if (fill.apply(key, word)) {
                // If recursion succeed, it's done.
                if (solvePuzzle(slot, fill)) {
                    return true;
                }
            }

            // If the word doesn't match, restore the puzzle and try next word
            guess++;
            usedWords.remove(word);
            puzzle = Arrays.stream(copyPuzzle).map(String[]::clone).toArray(String[][]::new);
        }
        // Not match. Restore the slot and return false
        slot.put(key, wordLength);
        return false;
    }

    //----------------------------------------------------------------------------//
    // print the result of solved puzzle
    public void print(PrintWriter outstream) {
        if (outstream == null || puzzle == null) {
            return;
        }
        try (PrintWriter pw = outstream) {
            for (int i = 0; i < puzzle.length; i++) {
                String line = "";
                // read each row individually
                for (int j = 0; j < puzzle[i].length; j++) {
                    if (puzzle[i][j].equals(NO_FILL_PUZZLE)) {
                        line += " ";
                    } else {
                        line += puzzle[i][j];
                    }
                }
                pw.write(line + NEW_LINE);
            }
        }
    }

    //----------------------------------------------------------------------------//
    // number of guesses made
    public int choices() {
        return guess;
    }
}