package banking_v0_2;

import java.util.Scanner;

public class Constant {
    public final static Scanner SCANNER = new Scanner(System.in);
    public final static int ISSUER_IDENTIFICATION_NUMBER = 400000; // The first six digits are the Issuer Identification Number (IIN). The IIN must be 400000 in this project
    public final static int CREDIT_CARD_ALLOWED_DIGIT_LIMIT = 16; // This is the maximum numer a credit have can have in our banking system.
}
