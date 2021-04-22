package com.example.osmarealtest;    /******** Class for generating an Activation key. ********/

import java.util.Random;

public class ActivationKey {
    public static String mainKeyGenerate() {
        // Initialize a string for appending the characters
        // of the activation key as well as j to 0 for index.
        String activationKey = "";
        int j=0;
        // Create 2 different keys by means of generateKey().
        StringBuilder key1 = generateKey(1);
        StringBuilder key2 = generateKey(2);
        // Reverse the arrangement of the characters of the
        // 2 keys.
        key1.reverse();
        key2.reverse();

        // Use for-loop to add the characters of the 1st key
        // and 2nd key to the initialized string by alternating
        // the characters of the two keys.
        for(int i=0;i<4;i++){
            activationKey+=key1.charAt(i);
            activationKey+=key2.charAt(j);
            j++;
        }

        // Use the Encode() method to hide the numerical values
        // of the second key and return that new string.
        StringBuilder encodedKey = Encode(activationKey);
        // Get the length of the string and divide it to half
        // and print the two strings with "-" in the middle.
        int half = encodedKey.length()/2;
        String[] keyParts = {encodedKey.substring(0, half),encodedKey.substring(half)};
        System.out.println("Activation Key: "+keyParts[0]+"-"+keyParts[1]);

        return keyParts[0]+"-"+keyParts[1];
    }

    // Method for generating a new key based on its type.
    private static StringBuilder generateKey(int keyType){
        // Create a new StringBuilder and Random variable.
        StringBuilder key = new StringBuilder();
        Random rand = new Random();
        // Initialize two integer variables to 0 and determine
        // the value based on its keyType.
        int bound=0, multiplier=0;
        // The bound variable is used to determine what the
        // range of the random value is based on the keyType.
        // If the 1st key is in question, the random integer
        // goes from 0 to 416 and while its multiplier is 24.
        // This results in a key that ranges from 0 to 9984.
        // Similarly, using the bound and multiplier when the
        // keyType is 2, the range would then be 0 to 9984 too.
        if(keyType==1) {
            bound=417;
            multiplier=24;
        } else if(keyType==2) {
            bound=625;
            multiplier=16;
        }
        // Use the Random variable to get a random integer based
        // on the bound and multiply it to its multiplier.
        int num = rand.nextInt(bound);
        int keyNum = num*multiplier;
        // As we will append the value to a string, you would
        // first need to append the extra '0' to the StringBuilder
        // to make sure that the key has 4 characters.
        if(keyNum<10) {
            key.append("000");
        } else if(keyNum<100) {
            key.append("00");
        } else if(keyNum<1000) {
            key.append("0");
        }
        key.append(keyNum);
        // Return the new string value.
        return key;
    }

    // Encode() gets the second key's character and returns its
    // letter equivalent.
    private static StringBuilder Encode(String key) {
        StringBuilder newKey = new StringBuilder();
        // (i%2==1) lets you access the characters for the 2nd key.
        // Per character, we make use of the equalCharacterOf()
        // method to get the letter equivalent of the character and
        // append it to the declared StringBuilder variable. Should
        // the accessed character be part of the 1st key, we append
        // the same character.
        for(int i=0;i<8;i++){
            if(i%2==1) {
                newKey.append(equalCharacterOf(Integer.parseInt(String.valueOf(key.charAt(i)))));
            } else {
                newKey.append(key.charAt(i));
            }
        }
        // Return the newly encoded StringBuilder.
        return newKey;
    }

    // Method to get an integer and return its letter equivalent.
    private static String equalCharacterOf(int i) {
        // To get the character equivalent of the integer, we make
        // use of the ASCII table values, however, we edit it in a
        // way that the accessed letters are reversed.
        return String.valueOf(Character.toChars(9-i+65));
    }
}
