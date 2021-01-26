package BuzzGazz;

import BuzzGazz.time.Date;
import BuzzGazz.time.Time;

import java.math.BigInteger;

public class Film {
    private String name;
    private Author author;
    private Date creationDate;
    private Time length;
    private BigInteger id;

    public Film(String name, Author author, Date creationDate, Time length, BigInteger id) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.length = length;
        this.creationDate = creationDate;
    }

    public void setData(String name, Author author, Date creationDate, Time length) {
        this.name = name;
        this.author = author;
        this.length = length;
        this.creationDate = creationDate;
    }

    public void out() {
        System.out.println("name: " + name +
                         "  author: " + author.toString() +
                         "  creation date: " + creationDate.toString() +
                         "  length: " + length.toString() +
                         "  ID: " + id.toString());
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author.toString();
    }

    public Time getLength() {
        return length;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public BigInteger getId() {
        return id;
    }
}