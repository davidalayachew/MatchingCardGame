import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

public class SingleGUI
{

   private static Border border(Color color)
   {
    
      return BorderFactory.createLineBorder(color, 5);
   
   }

   public static void playGame(List<String> elements, Map<String, String> answerKey)
   {
   
      JFrame frame = createJFrame();
      
      JPanel panel = new JPanel(new GridLayout(0, 1));
      
      frame.add(panel);
      
      for (JComponent each : createComponents(answerKey))
      {
      
         panel.add(each);
      
      }
      
      frame.setVisible(true);
   
   }
   
   private static JFrame createJFrame()
   {
   
      JFrame frame = new JFrame();
      frame.setTitle("Single Card Matching");
      frame.setSize(900, 400);
      frame.setLocation(500, 200);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         
      return frame;
         
   }
   
   private static List<JComponent> createComponents(Map<String, String> answerKey)
   {
   
      List<Map.Entry<String, String>> pairs = new ArrayList<>(answerKey.entrySet());
      System.out.println(pairs);
      Collections.shuffle(pairs);
   
      List<JComponent> components = new ArrayList<>();
      
      JLabel label = new JLabel();
      label.setText(generateLabelText(pairs));
      label.setFont(MatchingCardGame.DEFAULT_FONT);
      label.setHorizontalAlignment(SwingConstants.CENTER);
      
      components.add(label);
      
      JTextField textBox = new JTextField();
      textBox.setFont(MatchingCardGame.DEFAULT_FONT);
      textBox.setHorizontalAlignment(SwingConstants.CENTER);
      
      components.add(textBox);
      
      JPanel specialCharacterPanel = new JPanel(new GridLayout(2, 8));
      
      for (String each : MatchingCardGame.specialCharactersShuffled)
      {
      
         specialCharacterPanel.add(createSpecialCharacterButton(each.charAt(0), textBox));
      
      }
      
      components.add(specialCharacterPanel);
      
      JButton submitButton = new JButton();
      submitButton.setText("NEXT");
      submitButton.setFont(MatchingCardGame.DEFAULT_FONT);
      submitButton.addActionListener(submitButtonActionListener(submitButton, pairs, textBox, label));
      
      components.add(submitButton);
      
      textBox.addActionListener(
         new ActionListener()
         {
         
            public void actionPerformed(ActionEvent e)
            {
            
               submitButton.doClick();
            
            }
         
         });
      
      return components;
   
   }
   
   private static ActionListener submitButtonActionListener(
      final JButton submitButton,
      final List<Map.Entry<String, String>> pairs,
      final JTextComponent textBox,
      final JLabel label
      )
   {
   
      return 
         new ActionListener()
         {
         
            public void actionPerformed(ActionEvent e)
            {
            
               if (textBox.getText().trim().toLowerCase().contains(pairs.get(0).getValue().toLowerCase()))
               {
               
                  if (pairs.size() == 1)
                  {
                  
                     submitButton.setEnabled(false);
                     submitButton.setText("COMPLETED");
                  
                  }
                  
                  else
                  {
                  
                     pairs.remove(0);
                     textBox.setText("");
                     label.setText(generateLabelText(pairs));
                  
                     if (pairs.size() == 1)
                     {
                     
                        submitButton.setText("SUBMIT");
                     
                     }
                  
                  }
               
               }
            
            }
         
         };
   
   }
   
   private static String generateLabelText(List<Map.Entry<String, String>> pairs)
   {
   
      return pairs.size() + " - " + pairs.get(0).getKey();
   
   }
   
   private static JButton createSpecialCharacterButton(final char specialCharacter, final JTextComponent textBox)
   {
   
      JButton button = new JButton();
      button.setText("" + specialCharacter);
      button.setFont(MatchingCardGame.DEFAULT_FONT);
   
      button.addActionListener(
         new ActionListener()
         {
         
            public void actionPerformed(ActionEvent e)
            {
            
               textBox.setText(textBox.getText() + specialCharacter);
               textBox.requestFocus();
            
            }
         
         });
         
      return button;
      
   }
   
}