package BuzzGazz;

import BuzzGazz.time.Date;
import BuzzGazz.time.Time;

import java.util.LinkedList;
import java.util.List;

public class Finder {

    Node rootByName;
    Node rootByAuthor;
    Node rootByLength;
    Node rootByDate;

    class Node {
        List<Film> films;
        String trace;
        Node[] nodes;

        public Node() {
            films = new LinkedList<Film>();
            nodes = new Node[65536];
            trace = "";
        }

        public Node(String trace) {
            films = new LinkedList<Film>();
            nodes = new Node[65536];
            this.trace = trace;
        }

        public Node(Film film, String trace) {
            films = new LinkedList<Film>();
            this.trace = trace;
            nodes = new Node[65537];
            insertByName(film);
        }

        public void remove(Film film) {
            films.remove(film);
        }

        public void insertByName(Film film) {
            int counter = 0;

            while (counter < films.size() && films.get(counter).getName().compareTo(film.getName()) < 0)
                counter++;
            films.add(counter, film);
        }

        public void insertByAuthor(Film film) {
            int counter = 0;

            while (counter < films.size() && films.get(counter).getAuthor().toString().compareTo(film.getAuthor().toString()) < 0)
                counter++;
            films.add(counter, film);
        }
    }

    public Finder() {
        rootByName = new Node();
        rootByAuthor = new Node();
        rootByLength = new Node();
        rootByDate = new Node();
    }

    public void add(Film film) {
        addRecursionByName(rootByName, film, "", 0);
        addRecursionByAuthor(rootByAuthor, film, "", 0);
        addRecursionByLength(rootByLength, film, "", 0);
        addRecursionByDate(rootByDate, film, "", 0);
    }

    private void addRecursionByName(Node node, Film film, String trace, int depth) {
        node.insertByName(film);
        if (film.getName().length() > depth) {
            char currentChar = film.getName().charAt(depth);
            int characterNumericValue = currentChar;
            trace += currentChar;

            if (node.nodes[characterNumericValue] == null) {
                node.nodes[characterNumericValue] = new Node(trace);
            }
            addRecursionByName(node.nodes[characterNumericValue],
                               film,
                               trace,
                        depth + 1);
        }
    }

    private void addRecursionByAuthor(Node node, Film film, String trace, int depth) {
        node.insertByAuthor(film);
        if (film.getAuthor().toString().length() > depth) {
            char currentChar = film.getAuthor().toString().charAt(depth);
            int characterNumericValue = currentChar;
            trace += currentChar;

            if (node.nodes[characterNumericValue] == null) {
                node.nodes[characterNumericValue] = new Node(trace);
            }
            addRecursionByAuthor(node.nodes[characterNumericValue],
                    film,
                    trace,
                    depth + 1);
        }
    }

    private void addRecursionByLength(Node node, Film film, String trace, int depth) {
        node.insertByAuthor(film);
        if (film.getLength().toString().length() > depth) {
            char currentChar = film.getLength().toString().charAt(depth);
            int characterNumericValue = currentChar;
            trace += currentChar;

            if (node.nodes[characterNumericValue] == null) {
                node.nodes[characterNumericValue] = new Node(trace);
            }
            addRecursionByLength(node.nodes[characterNumericValue],
                    film,
                    trace,
                    depth + 1);
        }
    }

    private void addRecursionByDate(Node node, Film film, String trace, int depth) {
        node.insertByAuthor(film);
        if (film.getCreationDate().toString().length() > depth) {
            char currentChar = film.getCreationDate().toString().charAt(depth);
            int characterNumericValue = currentChar;
            trace += currentChar;

            if (node.nodes[characterNumericValue] == null) {
                node.nodes[characterNumericValue] = new Node(trace);
            }
            addRecursionByDate(node.nodes[characterNumericValue],
                    film,
                    trace,
                    depth + 1);
        }
    }

    public void remove(Film film) {
        deleteRecursionByName(rootByName, film, 0);
        deleteRecursionByAuthor(rootByAuthor, film, 0);
        deleteRecursionByLength(rootByLength, film, 0);
        deleteRecursionByDate(rootByDate, film, 0);
    }

    private void deleteRecursionByName(Node node, Film film, int depth) {
        node.remove(film);
        if (film.getName().length() > depth) {
            char characterNumericValue = film.getName().charAt(depth);
            deleteRecursionByName(node.nodes[characterNumericValue],
                    film,
                    depth + 1);
        }
    }

    private void deleteRecursionByAuthor(Node node, Film film, int depth) {
        node.remove(film);
        if (film.getAuthor().toString().length() > depth) {
            char characterNumericValue = film.getAuthor().toString().charAt(depth);
            deleteRecursionByAuthor(node.nodes[characterNumericValue],
                    film,
                    depth + 1);
        }
    }

    private void deleteRecursionByLength(Node node, Film film, int depth) {
        node.remove(film);
        if (film.getLength().toString().length() > depth) {
            char characterNumericValue = film.getLength().toString().charAt(depth);
            deleteRecursionByLength(node.nodes[characterNumericValue],
                    film,
                    depth + 1);
        }
    }

    private void deleteRecursionByDate(Node node, Film film, int depth) {
        node.remove(film);
        if (film.getCreationDate().toString().length() > depth) {
            char characterNumericValue = film.getCreationDate().toString().charAt(depth);
            deleteRecursionByDate(node.nodes[characterNumericValue],
                    film,
                    depth + 1);
        }
    }

    public Film[] getFilmsByName(String name) {
        return getFilms(rootByName, name);
    }

    public Film[] getFilmsByAuthor(Author author) {
        return getFilms(rootByAuthor, author);
    }

    public Film[] getFilmsByAuthor(String author) {
        return getFilmsByAuthor(new Author(author));
    }

    public Film[] getFilmsByLength(Time length) {
        return getFilms(rootByLength, length);
    }

    public Film[] getFilmsByLength(String length) {
        Film[] answer;

        answer = getFilmsByLength(new Time(length));
        if (answer.length == 0)
            answer = getFilms(rootByLength, length);
        return answer;
    }

    public Film[] getFilmsByDate(Date date) {
        return getFilms(rootByDate, date);
    }

    public Film[] getFilmsByDate(String date) {
        Film[] answer;

        answer = getFilmsByDate(new Date(date));
        if (answer.length == 0)
            answer = getFilms(rootByDate, date);
        return answer;
    }

    private Film[] getFilms(Node root, Object search) {
        Node currentNode;
        int charCounter;

        charCounter = 0;
        currentNode = root;
        while (currentNode != null && charCounter < search.toString().length())
            currentNode = currentNode.nodes[search.toString().charAt(charCounter++)];
        if (currentNode != null)
            return currentNode.films.toArray(new Film[currentNode.films.size()]);
        return new Film[0];
    }
}
