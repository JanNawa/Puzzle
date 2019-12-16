package assignment_4;

import java.io.*;
import java.util.*;

/**
 *
 * @author Jan
 */
public class PuzzleTesting {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        test("instructor.txt");
        test("test5x4.txt");
        test("test5x4_1word_hor.txt");
        test("test5x4_1word_ver.txt");
        test("test5x4_2word.txt");
        test("test5x4_FAIL.txt");
        test("test5x4_noWord.txt");
        test("test5x4_noPuzzle.txt");
        test("test6x6.txt");
        test("test6x6_FAIL.txt");

        FillInPuzzle f = new FillInPuzzle();
        System.out.println("choices on empty object: " + f.choices());
        System.out.println("solve on empty object: " + f.solve());
        f.print(null);
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(new FileWriter("src\\output_files\\nothing"));
        } catch (Exception e){
            System.out.println("error on PrintWriter");
        }
        f.print(pw);
        System.out.println("null to loadPuzzle: " + f.loadPuzzle(null));

        testLoadPuzzle();
    }
    
    public static void testLoadPuzzle(){
        FillInPuzzle f = new FillInPuzzle();
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader("src\\input_files\\notexists.txt"));
        } catch (Exception e){
            System.out.println("error on BufferedReader");
        }
        System.out.println("loadPuzzle file not exists: " + f.loadPuzzle(br));
        System.out.println("===");
        try{
            br = new BufferedReader(new FileReader("src\\input_files\\emptyFile.txt"));
        } catch (Exception e){
            System.out.println("error on BufferedReader");
        }
        System.out.println("loadPuzzle empty file: " + f.loadPuzzle(br));
        System.out.println("===");
    }
    
    public static void test(String name){
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader("src\\input_files\\" + name));
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("error on BufferedReader");
        }
        
        PrintWriter pw = null;
        try{
            pw = new PrintWriter(new FileWriter("src\\output_files\\" + name));
        } catch (Exception e){
            System.out.println("error on PrintWriter");
        }
        
        FillInPuzzle f = new FillInPuzzle();
        f.loadPuzzle(br);
        f.solve();
        System.out.println("solve: " + f.solve());
        System.out.println("guess: " + f.choices());
        f.print(pw);
        
        System.out.println("==========================================");
    }
}