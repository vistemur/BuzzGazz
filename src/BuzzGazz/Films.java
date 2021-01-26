package BuzzGazz;

import BuzzGazz.exceptions.NoContentException;
import BuzzGazz.time.Date;
import BuzzGazz.time.Time;
import org.apache.log4j.Logger;

import java.io.*;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

interface FilmsInterface {
    public void addFilm(String name, Author author, Date creationDate, Time length);
    public Film[] getFilmsByName(String name);
    public Film[] getFilmsByAuthor(Author author);
    public Film[] getFilmsByLength(Time length);
    public Film[] getFilmsByDate(Date creationDate);
}

public class Films implements FilmsInterface {

    private List<Film> films;
    private Finder finder;
    private BigInteger nextId;

    private static final Logger logger = Logger.getLogger("Films.class");

    public Films() {
        films = new LinkedList<Film>();
        finder = new Finder();
        nextId = new BigInteger("0");
        load();
    }

    public void load() {
        try {
            LoadDataFromFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void LoadDataFromFile() throws IOException {
        int amount;
        BufferedReader reader;

        reader = new BufferedReader(new FileReader("./dataBase/films"));
        amount = Integer.parseInt(reader.readLine());
        while (amount-- > 0)
            loadFilm(reader.readLine(), reader.readLine(), reader.readLine(), reader.readLine(), reader.readLine());
        reader.close();
    }

    public void loadFilm(String name, String author, String creationDate, String length, String id) {
        BigInteger curId = new BigInteger(id);

        Film film = new Film(name, new Author(author), new Date(creationDate), new Time(length), curId);
        if (curId.compareTo(nextId) <= 0)
            nextId = new BigInteger(String.valueOf(curId.add(BigInteger.valueOf(1))));
        films.add(film);
        finder.add(film);
    }

    public void save() {
        try {
            saveDataToFile();
        } catch (IOException e) {
            System.out.println("ERROR!\ncan't save films");
        }
    }

    private void saveDataToFile() throws IOException {
        BufferedWriter writer;

        writer = new BufferedWriter(new FileWriter("./dataBase/films"));
        writer.write(String.valueOf(films.size()) + '\n');
        for (Film film : films)
            writeFilmDataToFile(film, writer);
        writer.close();
    }

    private void writeFilmDataToFile(Film film, BufferedWriter writer) throws IOException {
        writer.write(film.getName() + '\n');
        writer.write(film.getAuthor() + '\n');
        writer.write(film.getCreationDate().toString() + '\n');
        writer.write(film.getLength().toString() + '\n');
        writer.write(film.getId().toString() + '\n');
    }

    public void addFilm(String name, String author, String creationDate, String length) {
        addFilm(name, new Author(author), new Date(creationDate), new Time(length));
    }

    public void addFilm(String name, Author author, Date creationDate, Time length) {
        Film film = new Film(name, author, creationDate, length, nextId);
        nextId = nextId.add(BigInteger.ONE);
        films.add(film);
        finder.add(film);
    }

    public boolean has(String name, Author author, Date creationDate, Time length) {
        for (Film film : films)
            if (    film.getName().equals(name) &&
                    film.getAuthor().equals(author) &&
                    film.getCreationDate().equals(creationDate) &&
                    film.getLength().equals(length))
                return true;
        return false;
    }

    public void remove(int[] indexes, int searchParam, String searchText, Sessions sessions) throws NoContentException {
        Film[] content;

        deleteIndexCheck(indexes);
        content = getFilmsByParam(searchParam, searchText);
        for (int c = 0; c < content.length; c++)
            for (int index : indexes)
                if (c == index) {
                    if (sessions.hasSessionsWithFilm(content[c]))
                        throw new NoContentException("film " + content[c].getName() + " is in session list");
                    logger.info("film removed(film: " + content[c].getName() +
                            " length: " + content[c].getLength() + " creation date: " + content[c].getCreationDate() + ")");
                    remove(content[c]);
                }
    }

    public void remove (int[] indexes, Sessions sessions, String name, String author, String tFrom, String tTo, String dFrom, String dTo)
            throws NoContentException {
        Film[] content;

        deleteIndexCheck(indexes);
        content = getFilms(name, author, tFrom, tTo, dFrom, dTo);
        for (int c = 0; c < content.length; c++)
            for (int index : indexes)
                if (c == index) {
                    if (sessions.hasSessionsWithFilm(content[c]))
                        throw new NoContentException("film " + content[c].getName() + " is in session list");
                    logger.info("film removed(film: " + content[c].getName() +
                            " length: " + content[c].getLength() + " creation date: " + content[c].getCreationDate() + ")");
                    remove(content[c]);
                }
    }

    private void deleteIndexCheck(int[] indexes) throws NoContentException {
        if (films.size() == 0)
            throw new NoContentException("no films");
        if (indexes.length == 0)
            throw new NoContentException("nothing is chosen");
    }

    private void remove(Film film) {
        films.remove(film);
        finder.remove(film);
    }

    public void out() {
        for (Film film : films) {
            film.out();
        }
    }

    public void printFilmsByName(String name) {
        for (Film film : finder.getFilmsByName(name)) {
            film.out();
        }
    }

    public void printFilmsByAuthor(Author author) {
        for (Film film : finder.getFilmsByAuthor(author)) {
            film.out();
        }
    }

    public String[][] getDataByParam(int param, String searchText) {
        return getData(getFilmsByParam(param, searchText));
    }

    public Film getFilm(int[] indexes, int searchParam, String searchText) throws NoContentException {
        Film[] content;

        checkForSingleIndex(indexes);
        content = getFilmsByParam(searchParam, searchText);
        return content[indexes[0]];
    }

    public Film getFilm(int[] indexes, String name, String author, String tFrom, String tTo, String dFrom, String dTo)
            throws NoContentException {
        Film[] content;

        checkForSingleIndex(indexes);
        content = getFilms(name, author, tFrom, tTo, dFrom, dTo);
        return content[indexes[0]];
    }

    private void checkForSingleIndex(int[] indexes) throws NoContentException {
        if (films.size() == 0)
            throw new NoContentException("no films");
        if (indexes.length == 0)
            throw new NoContentException("film is not chosen");
        if (indexes.length > 1)
            throw new NoContentException("multiple films chosen");
    }

    public Film[] getFilmsByParam(int param, String searchText) {
        switch (param) {
            case 1:
                return getFilmsByAuthor(searchText);
            case 2:
                return getFilmsByLength(searchText);
            case 3:
                return getFilmsByDate(searchText);
            default:
                return getFilmsByName(searchText);
        }
    }

    public Film getFilmById(String idString) {
        BigInteger id;

        id = new BigInteger(idString);
        for (Film film : films) {
            if (film.getId().equals(id)) {
                return film;
            }
        }
        return null;
    }

    public String[][] getData(String name, String author, String tFrom, String tTo, String dFrom, String dTo) {
        String[][] data;
        Film[] dataInFilms;

        dataInFilms = getFilms(name, author, tFrom, tTo, dFrom, dTo);
        data = getData(dataInFilms);
        return data;
    }

    private Film[] getFilms(String name, String author, String tFrom, String tTo, String dFrom, String dTo) {
        Film[] data;
        Film film;
        Time timeFrom = new Time(tFrom);
        Time timeTo = new Time(tTo);
        Date dateFrom = new Date(dFrom);
        Date dateTo = new Date(dTo);
        int size = 0;

        for (int c = 0; c < films.size(); c++) {
            film = films.get(c);
            if (    (name.equals("") || film.getName().toLowerCase().contains(name.toLowerCase())) &&
                    (author.equals("") || film.getAuthor().toLowerCase().contains(author.toLowerCase())) &&
                    (tFrom.equals("") || film.getLength().compareTo(timeFrom) >= 0) &&
                    (tTo.equals("") || film.getLength().compareTo(timeTo) <= 0) &&
                    (dFrom.equals("") || film.getCreationDate().compareTo(dateFrom) >= 0) &&
                    (dTo.equals("") || film.getCreationDate().compareTo(dateTo) <= 0)) {
                size++;
            }
        }
        data = new Film[size];
        size = 0;
        for (int c = 0; c < films.size(); c++) {
            film = films.get(c);
            if (    (name.equals("") || film.getName().toLowerCase().contains(name.toLowerCase())) &&
                    (author.equals("") || film.getAuthor().toLowerCase().contains(author.toLowerCase())) &&
                    (tFrom.equals("") || film.getLength().compareTo(timeFrom) >= 0) &&
                    (tTo.equals("") || film.getLength().compareTo(timeTo) <= 0) &&
                    (dFrom.equals("") || film.getCreationDate().compareTo(dateFrom) >= 0) &&
                    (dTo.equals("") || film.getCreationDate().compareTo(dateTo) <= 0)) {
                data[size] = film;
                size++;
            }
        }
        return data;
    }

    public Film[] getFilmsByName(String name) {
        return finder.getFilmsByName(name);
    }

    public Film[] getFilmsByAuthor(Author author) {
        return finder.getFilmsByAuthor(author);
    }

    public Film[] getFilmsByAuthor(String  author) {
        return finder.getFilmsByAuthor(author);
    }

    public Film[] getFilmsByLength(Time length) {
        return finder.getFilmsByLength(length);
    }

    public Film[] getFilmsByLength(String length) {
        return finder.getFilmsByLength(length);
    }

    public Film[] getFilmsByDate(Date creationDate) {
        return finder.getFilmsByDate(creationDate);
    }

    public Film[] getFilmsByDate(String creationDate) {
        return finder.getFilmsByDate(creationDate);
    }

    public String[][] getData() {
        return getData(films.toArray(new Film[films.size()]));
    }

    public String[][] getDataByName(String name) {
        return getData(finder.getFilmsByName(name));
    }

    public String[][] getDataByAuthor(String author) {
        return getData(finder.getFilmsByAuthor(author));
    }

    public String[][] getDataByLength(String length) {
        return getData(finder.getFilmsByLength(length));
    }

    public String[][] getDataByDate(String date) {
        return getData(finder.getFilmsByDate(date));
    }

    private String[][] getData(Film[] films) {
        String[][] data = new String[films.length][4];
        int counter = 0;

        for (Film film : films) {
            data[counter][0] = film.getName();
            data[counter][1] = film.getAuthor();
            data[counter][2] = film.getLength().toString();
            data[counter][3] = film.getCreationDate().toString();
            counter++;
        }
        return data;
    }
}