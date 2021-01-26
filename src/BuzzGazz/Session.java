package BuzzGazz;

        import BuzzGazz.time.Date;
        import BuzzGazz.time.Time;

public class Session {
    Film film;
    Time time;
    Date date;

    Session(Film film, Time time, Date date) {
        this.film = film;
        this.time = time;
        this.date = date;
    }
}