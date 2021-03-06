package banking.model;

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
