package banking_v0_2;

public class CreditCardHandler<K, V> {
    private final K creditCardNumber;
    private final V creditCardPin;

    public K getCreditCardNumber() {
        return creditCardNumber;
    }

    public V getCreditCardPin() {
        return creditCardPin;
    }

    public CreditCardHandler(K creditCardNumber, V creditCardPin){
        this.creditCardNumber = creditCardNumber;
        this.creditCardPin = creditCardPin;
    }
}
