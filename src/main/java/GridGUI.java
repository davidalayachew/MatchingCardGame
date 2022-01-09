import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.charset.Charset;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class GridGUI
{

   public enum SelectState { UNSELECTED, SELECTED, SOLVED; };
   
   private static final Color UNSELECTED_COLOR  = new JButton().getBackground();
   private static final Color SELECTED_COLOR    = Color.YELLOW;
   private static final Color SOLVED_COLOR      = Color.GREEN;
   private static final Color CTRL_F_COLOR      = Color.MAGENTA;
   
   protected static void playGame(boolean timer, List<String> elements, Map<String, String> answerKey) throws IOException
   {
   
      Date startTime = (timer ? new Date() : null);
   
      JFrame frame = constructJFrameWithElements(elements, answerKey, startTime);
      
      frame.setVisible(true);
      
   }
   
   private static JFrame constructJFrameWithElements(List<String> elements, Map<String, String> answerKey, Date startTime)
   {
   
      JFrame frame = new JFrame();
      
      frame.setTitle("Matching Card Game!");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
   
      final int dimensions = calculateDimensions(elements.size());
      
      frame.add(constructJPanelWithElements(elements, answerKey, dimensions, startTime));
      
      
      
      return frame;
   
   }
   
   private static JPanel constructJPanelWithElements(List<String> elements, Map<String, String> answerKey, int dimensions, Date startTime)
   {
   
      JPanel panel = new JPanel();
      panel.setLayout(new GridLayout(dimensions, dimensions));
      
      JTextField ctrlF = new JTextField();
      ctrlF.addKeyListener(
         new KeyListener()
         {
         
            public void keyReleased(KeyEvent e) { }
            public void keyTyped(KeyEvent e) { }
            public void keyPressed(KeyEvent e) {
               
               // If the user pressed ctrl+f
               if (e.getKeyCode() == 70 && e.getModifiersEx() == 128)
               {
               
                  ctrlF.selectAll();
               
               }
               
            }
         
         }
         );
      
      addElementsToPanel(panel, elements, answerKey, dimensions, startTime, ctrlF);
      
      return panel;
      
   }
   
   /** Calculates the dimensions based on the given numOfElements. Since we are making a square grid, we return a single int. */
   protected static int calculateDimensions(final int numOfElements)
   {
   
      //Find the square root, as that will give us a number that, multiplied by itself, equals the dimensions we need.
      //For example, if we have 9 elements, the square root is 3, giving us a 3x3 grid, which is exactly what we want.
      //The reason why we are using square root is because we are trying to make a square grid.
      final double sqrt = Math.sqrt(numOfElements);
      
      //However, it may not return a whole number. For example, what if we have 10 elements?
      //If that happens, we must find the next largest integer by using Math.ceil().
      //In the case of 10 elements, our square root is ~3.16, so we find the next largest integer, which is 4.
      //This now gives us a 4x4 grid, which has more than enough space to house 10 elements.
      final int side = (int) Math.ceil(sqrt);
      
      //Now, no matter what, the side of our square grid will have enough space to house all of our elements
      return side;
      
   }
   
   private static boolean contains(JButton button, String text)
   {
   
      return button.getActionCommand().toUpperCase().contains(text.trim().toUpperCase()) && !text.trim().isEmpty();
   
   }
   
   private static boolean valueEquals(JButton button, String text)
   {
   
      return button.getActionCommand().equalsIgnoreCase(text.trim());
   
   }
   
   private static void addElementsToPanel(JPanel panel, List<String> elements, final Map<String, String> answerKey, final int dimensions, Date startTime,
   JTextField ctrlF)
   {
   
      final Map<String, SelectState> elementStates = createElementStates(elements);
      final List<JButton> buttons = new ArrayList<>();
      
      Collections.shuffle(elements);
      
      panel.add(ctrlF);
   
      for (final String each : elements)
      {
         
         JButton button = new JButton();
         button.setText(multilineString(each));
         button.setActionCommand(each);
         button.setFont(MatchingCardGame.DEFAULT_FONT);
         button.addActionListener(createButtonActionListener(elementStates, buttons, answerKey, startTime, ctrlF));
         
         buttons.add(button);
         panel.add(button);
            
      }
      
      ctrlF.addActionListener(
         new ActionListener()
         {
         
            public void actionPerformed(ActionEvent e)
            {
            
               final List<JButton> ctrlFButtons = new ArrayList<>();
               JButton selected = null;
               final String text = ctrlF.getText().trim();
            
               for (JButton button : buttons)
               {
               
                  final Color color = button.getBackground();
               
                  if (CTRL_F_COLOR.equals(color))
                  {
                  
                     if (valueEquals(button, text))
                     {
                     
                        ctrlFButtons.clear();
                        ctrlFButtons.add(button);
                        break;
                     
                     }
                     
                     ctrlFButtons.add(button);
                  
                  }
                  
                  else if (SELECTED_COLOR.equals(color) && contains(button, text))
                  {
                  
                     selected = button;
                  
                  }
               
               }
               
               if (ctrlFButtons.size() == 1)
               {
               
                  ctrlFButtons.get(0).doClick();
               
               }
               
               else if (selected != null)
               {
               
                  selected.doClick();
               
               }
            
            }
         
         }
         );
      
      ctrlF.getDocument().addDocumentListener( 
         new DocumentListener() {
         
            public void changedUpdate(DocumentEvent e) { performCtrlF(e, buttons, elementStates); }
         
            public void insertUpdate(DocumentEvent e) { performCtrlF(e, buttons, elementStates); }
         
            public void removeUpdate(DocumentEvent e) { performCtrlF(e, buttons, elementStates); }
            
            private void performCtrlF(DocumentEvent e, List<JButton> buttons, Map<String, SelectState> elementStates)
            {
            
               try
               {
               
                  final String text = e.getDocument().getText(0, e.getDocument().getLength());
                  
                  
                  for (JButton each : buttons)
                  {
                  
                     if (!text.trim().isEmpty() && contains(each, text))
                     {
                     
                        Color tempColor;
                     
                        switch(elementStates.get(each.getActionCommand())) {
                           
                           case UNSELECTED: tempColor = CTRL_F_COLOR; 
                              break;
                           case SELECTED:   tempColor = SELECTED_COLOR; 
                              break;
                           case SOLVED:     tempColor = SOLVED_COLOR; 
                              break;
                           default:
                              throw new IllegalStateException("Shouldn't be able to get here!");
                           
                        }
                     
                        each.setBackground(tempColor);
                     
                     }
                     
                     else
                     {
                     
                        Color tempColor;
                     
                        switch(elementStates.get(each.getActionCommand())) {
                           
                           case UNSELECTED: tempColor = UNSELECTED_COLOR; 
                              break;
                           case SELECTED:   tempColor = SELECTED_COLOR; 
                              break;
                           case SOLVED:     tempColor = SOLVED_COLOR; 
                              break;
                           default:
                              throw new IllegalStateException("Shouldn't be able to get here!");
                           
                        }
                     
                        each.setBackground(tempColor);
                     
                     }
                  
                  }
               
               }
               
               catch (BadLocationException ble)
               {
               
                  System.err.println(e);
               
               }
            
            }
         
         });
      
   }
   
   private static String multilineString(String phrase)
   {
   
      return "<html><p style=\"word-break: break-all;\">" + phrase + "</p></html>";
   
   }
   
   private static ActionListener createButtonActionListener(
      final Map<String, SelectState> elementStates,
      final List<JButton> buttons,
      final Map<String, String> answerKey,
      final Date startTime,
      final JTextField ctrlF
   )
   {
   
      return 
         new ActionListener()
         {
               
            @Override
            public void actionPerformed(ActionEvent e)
            {
            
               ctrlF.requestFocus();
               final String text = ctrlF.getText().trim();
                  
               String element = e.getActionCommand();
                  
               SelectState elementState = elementStates.get(element);
                  
               switch (elementState)
               {
                     
                  case UNSELECTED:
                        
                     String matchingElement = answerKey.get(element);
                        
                     switch (elementStates.get(matchingElement))
                     {
                           
                        case UNSELECTED:
                              
                           if (elementStates.containsValue(SelectState.SELECTED))
                           {
                                 
                              resetElementStates(elementStates, buttons, text);
                                 
                           }
                                 
                           else
                           {
                                
                              elementStates.put(element, SelectState.SELECTED);
                              
                              JButton elementButton = fetchButtonByActionCommand(element, buttons);
                              elementButton.setBackground(SELECTED_COLOR);
                           
                                 
                           }
                                 
                           break;
                                 
                        case SELECTED:
                        
                           Date finishTime = new Date();
                        
                           elementStates.put(element, SelectState.SOLVED);
                           elementStates.put(matchingElement, SelectState.SOLVED);
                           
                           JButton elementButton = fetchButtonByActionCommand(element, buttons);
                           elementButton.setEnabled(false);
                           elementButton.setBackground(SOLVED_COLOR);
                           
                           JButton matchingElementButton = fetchButtonByActionCommand(matchingElement, buttons);
                           matchingElementButton.setEnabled(false);
                           matchingElementButton.setBackground(SOLVED_COLOR);
                           
                           if (!elementStates.containsValue(SelectState.UNSELECTED) && startTime != null)
                           {
                           
                              JOptionPane.showMessageDialog(
                                    null,
                                    "Well done - Completion time = " + Double.valueOf(finishTime.getTime() - startTime.getTime()) / 1000 + " seconds",
                                    "Well done",
                                    JOptionPane.PLAIN_MESSAGE
                                 );
                                 
                           }
                           
                           else
                           {
                           
                              //System.out.println(elementStates);
                           
                           }
                           
                           break;
                           
                        case SOLVED:
                           throw new IllegalStateException("How can " + matchingElement + " be SOLVED, but " + element + " be UNSELECTED? - " + elementStates);
                              
                        default:
                           throw new IllegalArgumentException("Impossible SelectState - " + elementState);
                     
                     }
                        
                     break;
                     
                  case SELECTED:
                     elementStates.put(element, SelectState.UNSELECTED);
                     JButton elementButton = fetchButtonByActionCommand(element, buttons);
                     elementButton.setBackground(contains(elementButton, ctrlF.getText()) ? CTRL_F_COLOR : UNSELECTED_COLOR);   
                     break;
                     
                  case SOLVED:
                     //do nothing
                     break;
                     
                  default:
                     throw new IllegalArgumentException("Impossible SelectState - " + elementState);
                     
               }
               
            } 
              
            private JButton fetchButtonByActionCommand(String actionCommand, List<JButton> buttons)
            {
            
               for (JButton each : buttons)
               {
               
                  if (each.getActionCommand().equals(actionCommand))
                  {
                  
                     return each;
                  
                  }
               
               }
            
               throw new IllegalStateException("Cannot find the button with an ActionCommand of " + actionCommand);
            
            }
         
            private void resetElementStates(Map<String, SelectState> elementStates, List<JButton> buttons, String text)
            {
            
               for (Map.Entry<String, SelectState> each : elementStates.entrySet())
               {
               
                  if (each.getValue() != SelectState.SOLVED)
                  {
                  
                     each.setValue(SelectState.UNSELECTED);
                  
                  }
               
               }
            
               for (JButton each : buttons)
               {
               
                  if (!each.getBackground().equals(SOLVED_COLOR))
                  {
                  
                     if (contains(each, text))
                     {
                     
                        each.setBackground(CTRL_F_COLOR);
                     
                     }
                     
                     else
                     {
                     
                        each.setBackground(UNSELECTED_COLOR);
                     
                     }
                  
                  }
               
               }
            
            }
         
         };
   }
   
   private static Map<String, SelectState> createElementStates(List<String> elements)
   {
            
      Map<String, SelectState> elementStates = new HashMap<>();
            
      for (String each : elements)
      {
               
         elementStates.put(each, SelectState.UNSELECTED);
               
      }
            
      return elementStates;
            
   }

}