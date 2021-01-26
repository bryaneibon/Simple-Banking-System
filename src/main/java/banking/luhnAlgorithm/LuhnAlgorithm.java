package banking.luhnAlgorithm;

import banking.model.Constant;

import java.util.ArrayList;

public class LuhnAlgorithm {

    public static boolean checkLuhnAlgorithm(String accountIdentifier){
        //BIN (Bank Identification Number) + Account Identifier Number
        String creditCardNumber;
        if (accountIdentifier.length() <= 10){
            creditCardNumber = Constant.ISSUER_IDENTIFICATION_NUMBER + accountIdentifier;
        } else {
            creditCardNumber = accountIdentifier;
        }

        String[] originalNumbers = creditCardNumber.split("");

        ArrayList<Integer> luhnArrayList = new ArrayList<>();

        if(originalNumbers.length == Constant.CREDIT_CARD_ALLOWED_DIGIT_LIMIT){
            for (int i = 0; i < originalNumbers.length -1; i++) { //Drop the last digit with length-1.
                luhnArrayList.add(Integer.parseInt(originalNumbers[i]));
            }
        } else {
            for (String originalNumber : originalNumbers) { //Do not drop anything if we have less than 16 digits.
                luhnArrayList.add(Integer.parseInt(originalNumber));
            }
        }

        int oddIndex = 0;
        while (oddIndex <= luhnArrayList.size()){
            luhnArrayList.set(oddIndex, luhnArrayList.get(oddIndex)*2); // Update the arrayList by multiplying odd digits by 2.
            oddIndex += 2;
        }

        for (int i = 0; i < luhnArrayList.size(); i++) {
            if (luhnArrayList.get(i) > 9){
                luhnArrayList.set(i, luhnArrayList.get(i)-9); // Subtract 9 to numbers over 9.
            }
        }

        int lastDigitCreditCard;
        if (originalNumbers.length == Constant.CREDIT_CARD_ALLOWED_DIGIT_LIMIT){
            lastDigitCreditCard = Integer.parseInt(String.valueOf(accountIdentifier.charAt(accountIdentifier.length()-1)));
            luhnArrayList.add(lastDigitCreditCard); // add the last digit from the original card.
        } else {
            int totalValueInArray = 0;
            for (int value : luhnArrayList) {
                totalValueInArray = totalValueInArray + value;
            }
            lastDigitCreditCard = 10 - totalValueInArray % 10;
            luhnArrayList.add(lastDigitCreditCard); // add a last digit created according to the luhn algorithm.
        }

        int sum = 0;
        for (int value : luhnArrayList) {
            sum = sum + value;
        }
        return sum % 10 == 0;
    }
}
