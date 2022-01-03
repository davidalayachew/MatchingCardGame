import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


public class MatchingCardGameTest
{

   @Test
   public void testCalculateDimensions()
   {
   
      Assert.assertEquals(2, MatchingCardGame.calculateDimensions(2));
      Assert.assertEquals(2, MatchingCardGame.calculateDimensions(3));
      Assert.assertEquals(2, MatchingCardGame.calculateDimensions(4));
      Assert.assertEquals(3, MatchingCardGame.calculateDimensions(5));
      Assert.assertEquals(3, MatchingCardGame.calculateDimensions(6));
      Assert.assertEquals(3, MatchingCardGame.calculateDimensions(7));
      Assert.assertEquals(3, MatchingCardGame.calculateDimensions(8));
      Assert.assertEquals(3, MatchingCardGame.calculateDimensions(9));
      Assert.assertEquals(4, MatchingCardGame.calculateDimensions(10));
      Assert.assertEquals(4, MatchingCardGame.calculateDimensions(11));
      Assert.assertEquals(4, MatchingCardGame.calculateDimensions(12));
      Assert.assertEquals(4, MatchingCardGame.calculateDimensions(13));
      Assert.assertEquals(4, MatchingCardGame.calculateDimensions(14));
      Assert.assertEquals(4, MatchingCardGame.calculateDimensions(15));
      Assert.assertEquals(4, MatchingCardGame.calculateDimensions(16));
      Assert.assertEquals(5, MatchingCardGame.calculateDimensions(17));
      Assert.assertEquals(5, MatchingCardGame.calculateDimensions(18));
      Assert.assertEquals(5, MatchingCardGame.calculateDimensions(19));
      Assert.assertEquals(5, MatchingCardGame.calculateDimensions(20));
      Assert.assertEquals(5, MatchingCardGame.calculateDimensions(21));
      Assert.assertEquals(5, MatchingCardGame.calculateDimensions(22));
      Assert.assertEquals(5, MatchingCardGame.calculateDimensions(23));
      Assert.assertEquals(5, MatchingCardGame.calculateDimensions(24));
      Assert.assertEquals(5, MatchingCardGame.calculateDimensions(25));
      Assert.assertEquals(6, MatchingCardGame.calculateDimensions(26));
      Assert.assertEquals(6, MatchingCardGame.calculateDimensions(27));
      Assert.assertEquals(6, MatchingCardGame.calculateDimensions(28));
      Assert.assertEquals(6, MatchingCardGame.calculateDimensions(29));
      Assert.assertEquals(6, MatchingCardGame.calculateDimensions(30));
   
   }
   
   @Test
   public void testExtractElementsFromPairs()
   {
   
      List<String> pairs = null;
      
      pairs = Arrays.asList(
         "abc=xyz",
         "123=DoReMi",
         "you=me"
         );
         
      Assert.assertEquals(
            Arrays.asList("abc", "xyz", "123", "DoReMi", "you", "me"),
            MatchingCardGame.extractElementsFromPairs(pairs)
         );
   
   }

}
