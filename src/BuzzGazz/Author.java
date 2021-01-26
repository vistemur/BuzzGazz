package BuzzGazz;

public class Author {
    private String name;
    private String secondName;

    public Author(String name) {
        this.name = name;
        this.secondName = "";
    }

    public Author(String name, String secondName) {
        this.name = name;
        this.secondName = secondName;
    }

    public String toString() {
        StringBuilder answer = new StringBuilder(name);
        answer.append(secondName);
        return answer.toString();
    }
}