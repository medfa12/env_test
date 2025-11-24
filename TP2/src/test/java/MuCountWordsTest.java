import static org.junit.Assert.*;
import org.junit.Test;

public class MuCountWordsTest 
{
    @Test
    public void testRICC() 
    {
        // Clause 'a' (!isLetter) major: b=T, c=F fixed
        // Row 2 (T,T,F) vs Row 6 (F,T,F)
        assertEquals(1, muCountWords.count("r ")); 
        assertEquals(1, muCountWords.count("rr")); 

        // Clause 'b' (last=='r') major: a=T, c=F fixed
        // Row 2 (T,T,F) vs Row 4 (T,F,F)
        assertEquals(1, muCountWords.count("r ")); 
        assertEquals(0, muCountWords.count("x ")); 

        // Clause 'c' (last=='t') major: a=T, b=F fixed
        // Row 3 (T,F,T) vs Row 4 (T,F,F)
        assertEquals(1, muCountWords.count("t ")); 
        assertEquals(0, muCountWords.count("x ")); 
    }

    @Test
    public void testGACC() 
    {
        // Clause 'a' (!isLetter) major: b|c is True (values can differ)
        // Row 2 (T,T,F) vs Row 7 (F,F,T) -> Unique to GACC logic
        assertEquals(1, muCountWords.count("r ")); 
        assertEquals(1, muCountWords.count("tt")); 

        // Clause 'b' (last=='r') major: a=T, c=F
        // Row 2 (T,T,F) vs Row 4 (T,F,F)
        assertEquals(1, muCountWords.count("r ")); 
        assertEquals(0, muCountWords.count("x ")); 

        // Clause 'c' (last=='t') major: a=T, b=F
        // Row 3 (T,F,T) vs Row 4 (T,F,F)
        assertEquals(1, muCountWords.count("t ")); 
        assertEquals(0, muCountWords.count("x ")); 
    }
}
