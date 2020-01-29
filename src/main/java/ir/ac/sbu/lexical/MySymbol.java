package ir.ac.sbu.lexical;

public class MySymbol {
    private String token;
    private Object value;

    MySymbol(String token) {
        this.token = token;
    }

    MySymbol(String token, Object value) {
        this.token = token;

        this.value = value;
    }

    public String getToken() {
        return token;
    }

    public Object getValue() {
        return value;
    }
}
