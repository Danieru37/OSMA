package com.example.osmarealtest;

/******** Class for checking the validity of an activation key ********/

import android.os.Build;
import static java.lang.Character.*;

public class ValidityCheck {
    public static int check(String key) {
        // Use if-else statement to check the format of the
        // String using the checkKey() method. Should the method
        // return false, print that the entered key's format is
        // incorrect.
        if(checkKey(key)){
            // Use replace() on the string to remove '-'.
            String rawKey = key.replace("-","");
            // Use Decode() on the rawKey to get the integer
            // equivalent of the alphabet letters in the key.
            StringBuilder decodedKey = Decode(rawKey);
            // Initialize two strings for separating the activation
            // key into two.
            String tempKey1 = "", tempKey2 = "";
            // Access the decodedKey from the last index and add
            // them to the initialized string variables to reverse
            // there arrangements.
            for(int i=7;i>=0;i--){
                if(i%2==1){
                    tempKey2+=decodedKey.charAt(i);
                } else {
                    tempKey1+=decodedKey.charAt(i);
                }
            }
            // Use checkValidityOf() to see if the two parts of the
            // activation key is valid or invalid.
            if(checkValidityOf(tempKey1,tempKey2)){
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }
    }

    // Method for checking the activation key's format.
    private static boolean checkKey(String key) {
        // Initialize two counter's to 0.
        int cntr1=0,cntr2=0;
        // If the string exceeds 9 characters, return false.
        if(key.length()==9){
            // Check if the middle character of the string is '-'.
            for(int i=0;i<9;i++){
                if(key.charAt(i)=='-' && i==4){
                    cntr1+=1;
                }
            }
            // If there is exactly one '-', continue.
            if(cntr1==1){
                // Remove '-' from the string.
                String rawKey = key.replace("-","");
                // For the integers-only or the first part of the
                // activation key, we check if the character is an
                // integer. In checking the alphabet part or the 2nd
                // part of the activation key, we check if the
                // character is an alphabet in uppercase.
                for(int i=0;i<8;i++){
                    if(isDigit(rawKey.charAt(i)) && i%2==0){
                        cntr2+=1;
                    } else if(rawKey.charAt(i) >= 'A' && rawKey.charAt(i) <= 'Z' && i%2==1){
                        cntr2+=1;
                    }
                }
                // If all characters are true for their conditions,
                // return true.
                if(cntr2==8){ return true; }
            }
        }
        return false;
    }
    // This method checks if the value of the two keys are valid.
    private static boolean checkValidityOf(String key1, String key2) {
        // The inverted integer of the first part of the activation
        // key should be divisible to 24 while the second should be
        // divisible by 16.
        if(Integer.valueOf(key1)%24==0 && Integer.valueOf(key2)%16==0) {
            return true;
        } else { return false; }
    }
    // Method in decoding the letters of the activation key.
    private static StringBuilder Decode(String key) {
        StringBuilder newKey = new StringBuilder();
        // Append the same character of the odd numbered characters
        // or the characters with even index while using the
        // equalCharacterOf() method to get the integer value of the
        // letter.
        for(int i=0;i<8;i++){
            if(i%2==1) {
                newKey.append(equalCharacterOf(key.charAt(i)));
            } else {
                newKey.append(key.charAt(i));
            }
        }
        // Return the new string.
        return newKey;
    }
    // Method that returns equivalent integer value of the character.
    private static int equalCharacterOf(char ch) {
        // Based on the ASCII table values, we subtract the passed
        // uppercase of the character to 74 as 65 is the ASCII value
        // for 'A' and 9 is the number of letters to be used.
        int newChar = 74-toUpperCase(ch);
        return newChar;
    }
}
