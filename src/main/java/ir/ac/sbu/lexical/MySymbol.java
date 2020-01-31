package ir.ac.sbu.lexical;

public class MySymbol {
    private String token;
    private Object value;

    public MySymbol(String token) {
        this.token = token;
    }

    public MySymbol(String token, Object value) {
        this.token = token;

        this.value = value;
    }

    public String getToken() {
        return token;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
