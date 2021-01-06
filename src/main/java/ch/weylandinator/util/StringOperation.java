package ch.weylandinator.util;

public class StringOperation
{
    public static String removeDuplicateSpaces(String string){
        return string.trim().replaceAll("  +", " ");
    }
}
