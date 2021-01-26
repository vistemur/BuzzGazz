package tests;

import BuzzGazz.Author;
import BuzzGazz.Film;
import BuzzGazz.Finder;
import BuzzGazz.time.Date;
import BuzzGazz.time.Time;
import org.junit.*;

import java.math.BigInteger;

public class FinderTest extends TestModel{

    Film filmA = new Film("A", new Author("A"), new Date("10.10.1010"), new Time("1"), new BigInteger("1"));
    Film filmB = new Film("B", new Author("B"), new Date("10.10.1010"), new Time("1"), new BigInteger("1"));
    Film filmAA = new Film("AA", new Author("A"), new Date("10.10.1010"), new Time("1"), new BigInteger("1"));
    Film filmAB = new Film("AB", new Author("A"), new Date("10.10.1010"), new Time("1"), new BigInteger("1"));

    @Test
    public void emptyCheck() {
       Finder finder = new Finder();
       Assert.assertEquals(finder.getFilmsByName(""), new Film[0]);
    }

    @Test
    public void singleSearchCheck() {
        Finder finder = new Finder();
        finder.add(filmA);
        Assert.assertEquals(finder.getFilmsByName(""), new Film[]{filmA});
    }

    @Test
    public void nameSearchCheck() {
        Finder finder = new Finder();
        finder.add(filmA);
        finder.add(filmB);
        Assert.assertEquals(finder.getFilmsByName(""), new Film[]{filmA, filmB});
        Assert.assertEquals(finder.getFilmsByName("A"), new Film[]{filmA});
        Assert.assertEquals(finder.getFilmsByName("B"), new Film[]{filmB});
    }

    @Test
    public void namesSearchCheck() {
        Finder finder = new Finder();
        finder.add(filmA);
        finder.add(filmAA);
        finder.add(filmAB);
        finder.add(filmB);
        Assert.assertEquals(finder.getFilmsByName("A"), new Film[]{filmA, filmAA, filmAB});
        Assert.assertEquals(finder.getFilmsByName("AA"), new Film[]{filmAA});
    }
}