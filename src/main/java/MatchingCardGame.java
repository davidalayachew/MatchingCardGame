import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.charset.Charset;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MatchingCardGame
{

   public enum Mode
   {
   
      SINGLE,
      GRID_WITH_TIMER,
      GRID_WITHOUT_TIMER,
      ;
   
   };

   private static final char SPLIT_CHAR = '~';
   protected static final Font DEFAULT_FONT = new Font("Consolas", Font.BOLD, 15);
   
   public static final String[] specialCharacters = "שפןמכךיטחזהגא".split("");
   public static final List<String> specialCharactersShuffled = new ArrayList<>();
   
   static
   {
   
      specialCharactersShuffled.addAll(Arrays.asList(specialCharacters));
      Collections.shuffle(specialCharactersShuffled);
               
   }
   
   public static void main(String[] args) throws IOException
   {
   
      System.out.println("Starting");
      
      List<String> pairs = null;  
   
      try (InputStream in = MatchingCardGame.class.getClassLoader().getResource("input.txt").openStream())
      {
      
         Path tempFile = Files.createTempFile("MatchingCardGameInputFile", null);
      
         Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
         
         pairs = Files.readAllLines(tempFile, Charset.forName("UTF-8"));
         
         Files.delete(tempFile);
      
      }
      
      catch (IOException e)
      {
      
         throw new IllegalStateException("Couldn't open the file", e);
      
      }
      
      Objects.requireNonNull(pairs);
      
      List<String> elements = extractElementsFromPairs(pairs);
      
      switch (fetchMode())
      {
         
         case SINGLE:
            SingleGUI.playGame(elements, constructAnswerKey(elements, true));
            break;
            
         case GRID_WITH_TIMER:
            GridGUI.playGame(true, elements, constructAnswerKey(elements, true));
            break;
         
         case GRID_WITHOUT_TIMER:
            GridGUI.playGame(false, elements, constructAnswerKey(elements, true));
            break;
            
      }
           
   }
   
   public static Mode fetchMode()
   {
   
      Object selectedValue = JOptionPane.showInputDialog(
            null,
            "Choose your mode.",
            "Matching Card Game",
            JOptionPane.INFORMATION_MESSAGE,
            null,
            Mode.values(),
            Mode.values()[0]
         );
   
      if (selectedValue == null)
      {
      
         //This is the equivalent of the user pressing cancel
         throw new IllegalArgumentException("No option was selected!");
      
      }
      
      else if (selectedValue != null && !(selectedValue instanceof Mode))
      {
         
         throw new IllegalArgumentException("Unacceptable output! selectedValue = " + selectedValue);
         
      } else {
      
         return ( (Mode) selectedValue );
      
      }
      
   }
   
   protected static List<String> extractElementsFromPairs(List<String> pairs)
   {
   
      List<String> elements = new ArrayList<>();
   
      for (String each : pairs)
      {
      
         elements.addAll(splitString(each));
      
      }
      
      return elements;
   
   }
   
   private static List<String> splitString(String each)
   {
   
      List<String> elements = new ArrayList<>();
   
      if (each.contains(SPLIT_CHAR + "") && each.indexOf(SPLIT_CHAR) != each.length() - 1 && each.indexOf(SPLIT_CHAR) > 0)
      {
         
         String[] elementPair = each.split(String.valueOf(SPLIT_CHAR), 2);
         elements.add(elementPair[0]);
         elements.add(elementPair[1]);
         
      }
         
      else
      {
         
         throw new IllegalArgumentException("Cannot split invalid String! -> [" + each + "]");
         
      }
      
      return elements;
   
   }
   
   private static Map<String, String> constructAnswerKey(List<String> elements, boolean bothWays)
   {
   
      Map<String, String> answerKey = new HashMap<>();
      
      for (int i = 0; i < elements.size(); i = i + 2)
      {
         answerKey.put(elements.get(i), elements.get(i + 1));
         
         if (bothWays)
         {
         
            answerKey.put(elements.get(i + 1), elements.get(i));
         
         }
      
      }
      
      return answerKey;
   
   }

}
