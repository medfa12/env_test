public class muCountWords 
{
    /**
     * Counts the number of words in a string that end with either "r" or "t"	
     * @param str -- an incoming sentence
     * @return word_count -- the number of words
     */
    public static int count(String str) 
    {
        int word_count = 0;    
        char last = ' ';      

        for (int i = 0; i < str.length(); i++)
        {
            if (!Character.isLetter(str.charAt(i)) && (last == 'r' || last == 't'))
                word_count++;

            last = str.charAt(i);
        }

        if (last == 'r' || last == 't')
            word_count++;

        return word_count;       
    }
}
