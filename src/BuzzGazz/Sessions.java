package BuzzGazz;

import BuzzGazz.exceptions.NoContentException;
import BuzzGazz.time.Date;
import BuzzGazz.time.Time;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.*;

public class Sessions {

    private List<Session> sessions;
    private Films films;
    private SessionsReporter sessionsReporter;

    private Object mutex;

    private LoadDataFromFileThread loadDataFromFileThread;

    private static final Logger logger = Logger.getLogger("Sessions.class");

    private class SessionsReporter {
        private List<Session> sessions;
        private XMLReportBuilderThread xmlReportBuilderThread;
        private PDFReportBuilderThread pdfReportBuilderThread;

        public SessionsReporter(List<Session> sessions, Object mutex) {
            this.sessions = sessions;
            xmlReportBuilderThread = new XMLReportBuilderThread(mutex);
            pdfReportBuilderThread = new PDFReportBuilderThread(mutex);
        }

        public void buildReports() {
            startThreads();
            joinThreads();
        }

        private void startThreads() {
            xmlReportBuilderThread.start();
            pdfReportBuilderThread.start();
        }

        private void joinThreads() {
            try {
                xmlReportBuilderThread.join();
                pdfReportBuilderThread.join();
            } catch (InterruptedException e) {
                System.out.println("ERROR!\ncan't join threads in reports Building process");
            }
        }

        private class XMLReportBuilderThread extends MutexedThread {

            public XMLReportBuilderThread(Object mutex) {
                super(mutex);
            }

            @Override
            public void make() {
                try {
                    buildXMLReport();
                } catch (IOException | ParserConfigurationException | TransformerException e) {
                    System.out.println("ERROR!\ncan't make XML sessions reports");
                }
            }

            private void buildXMLReport() throws IOException, ParserConfigurationException, TransformerException {
                Document document = getDataDocument();
                java.io.FileWriter fileWriter = new FileWriter("./dataBase/sessionsReport.xml");
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                trans.transform(new DOMSource(document), new StreamResult(fileWriter));
            }

            private Document getDataDocument() throws ParserConfigurationException {
                DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = documentBuilder.newDocument();
                Node sessionsNode = document.createElement("sessions");

                for (Session session : sessions)
                    addSessionDataToNode(session, sessionsNode, document);
                document.appendChild(sessionsNode);
                return document;
            }

            private void addSessionDataToNode(Session session, Node sessionsNode, Document document) {
                Element sessionElement = document.createElement("session");

                sessionElement.setAttribute("film", session.film.getName());
                sessionElement.setAttribute("time", session.time.toString());
                sessionElement.setAttribute("date", session.date.toString());
                sessionsNode.appendChild(sessionElement);
            }
        }

        private class PDFReportBuilderThread extends MutexedThread {

            public PDFReportBuilderThread(Object mutex) {
                super(mutex);
            }

            @Override
            public void make() {
                try {
                    buildPDFReport();
                } catch (JRException e) {
                    System.out.println("ERROR!\ncan't make PDF sessions reports");
                }
            }

            private void buildPDFReport() throws JRException {
                JRDataSource ds = new JRXmlDataSource("./dataBase/sessionsReport.xml", "/sessions/session");
                JasperReport jasperReport = JasperCompileManager.compileReport("./reportsDesigns/sessionsReport.jrxml");
                JasperPrint print = JasperFillManager.fillReport(jasperReport, new HashMap(), ds);
                JRExporter exporter = null;

                exporter = new JRPdfExporter();
                exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, "./reports/sessionsReport.pdf");
                exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
                exporter.exportReport();
            }
        }
    }

    public Sessions(Films films) {
        sessions = new ArrayList<Session>();
        mutex = new Object();
        loadDataFromFileThread = new LoadDataFromFileThread(mutex);
        sessionsReporter = new SessionsReporter(sessions, mutex);
        this.films = films;
        load();
    }

    public void load() {
        try {
            loadDataFromFileThread.start();
            loadDataFromFileThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class LoadDataFromFileThread extends MutexedThread {
        public LoadDataFromFileThread(Object mutex) {
            super(mutex);
        }

        @Override
        public void make() {
            try {
                LoadDataFromFile();
            } catch (IOException e) {
                System.out.println("ERROR!\ncan't load sessions");
                logger.error("unable to load sessions");
            }
        }

        public void LoadDataFromFile() throws IOException {
            int amount;
            BufferedReader reader;

            reader = new BufferedReader(new FileReader("./dataBase/sessions"));
            amount = Integer.parseInt(reader.readLine());
            while (amount-- > 0)
                add(films.getFilmById(reader.readLine()), new Time(reader.readLine()), new Date(reader.readLine()));
            reader.close();
        }
    }

    public void save() {
        try {
            saveDataToFile();
            sessionsReporter.buildReports();
        } catch (IOException e) {
            System.out.println("ERROR!\ncan't save sessions");
            logger.error("unable to save sessions");
        }
    }

    public void saveDataToFile() throws IOException {
        BufferedWriter writer;

        writer = new BufferedWriter(new FileWriter("./dataBase/sessions"));
        writer.write(String.valueOf(sessions.size()) + '\n');
        for (Session session : sessions)
            writeSessionDataToFile(session, writer);
        writer.close();
    }

    public void writeSessionDataToFile(Session session, BufferedWriter writer) throws IOException {
        writer.write(session.film.getId().toString() + '\n');
        writer.write(session.time.toString() + '\n');
        writer.write(session.date.toString() + '\n');
    }

    public boolean hasSessionsWithFilms(Film[] films) {
        for (Film film : films)
            if (hasSessionsWithFilm(film))
                return true;
        return false;
    }

    public boolean hasSessionsWithFilm(Film film) {
        for (Session session : sessions)
            if (session.film.equals(film)) {
                return true;
            }
        return false;
    }

    public void remove(int[] indexes, int searchParam, String searchText) throws NoContentException {
        Session[] content;

        if (sessions.size() == 0)
            throw new NoContentException("no sessions");
        if (indexes.length == 0)
            throw new NoContentException("no session chosen");

        content = getContent(searchParam, searchText);
        for (int c = 0; c < content.length; c++)
            for (int index : indexes)
                if (c == index) {
                    logger.info("session removed(film: " + content[c].film.getName() +
                                " time: " + content[c].time + " date: " + content[c].date + ")");
                    sessions.remove(content[c]);
                }
    }

    public void remove(int[] indexes, String name, String author, String tFrom, String tTo, String dFrom, String dTo)
            throws NoContentException {
        Session[] content;

        if (sessions.size() == 0)
            throw new NoContentException("no sessions");
        if (indexes.length == 0)
            throw new NoContentException("no session chosen");

        content = getSessions(name, author, tFrom, tTo, dFrom, dTo);
        for (int c = 0; c < content.length; c++)
            for (int index : indexes)
                if (c == index) {
                    logger.info("session removed(film: " + content[c].film.getName() +
                            " time: " + content[c].time + " date: " + content[c].date + ")");
                    sessions.remove(content[c]);
                }
    }

    public Session getSession(int[] indexes, String name, String author, String tFrom, String tTo, String dFrom, String dTo)
            throws NoContentException {
        Session[] content;

        if (sessions.size() == 0)
            throw new NoContentException("no sessions");
        if (indexes.length == 0)
            throw new NoContentException("session is not chosen");
        if (indexes.length > 1)
            throw new NoContentException("multiple sessions chosen");

        content = getSessions(name, author, tFrom, tTo, dFrom, dTo);
        return content[indexes[0]];
    }

    public Session getSession(int[] indexes, int searchParam, String searchText) throws NoContentException {
        Session[] content;

        if (sessions.size() == 0)
            throw new NoContentException("no sessions");
        if (indexes.length == 0)
            throw new NoContentException("session is not chosen");
        if (indexes.length > 1)
            throw new NoContentException("multiple sessions chosen");

        content = getContent(searchParam, searchText);
        return content[indexes[0]];
    }

    private Session[] getContent(int searchParam, String searchText) {
        switch (searchParam) {
            case 1:
                return getSessionsByFilmAuthor(searchText);
            case 2:
                return getSessionsByTime(searchText);
            case 3:
                return getSessionsByDate(searchText);
            default:
                return getSessionsByFilmName(searchText);
        }
    }

    public void add(Film film, Time time, Date date) {
        int position;

        position = 0;
        while ( position < sessions.size() &&
                (date.compareTo(sessions.get(position).date) < 0 ||
                (date.compareTo(sessions.get(position).date) == 0 && time.compareTo(sessions.get(position).time) < 0)))
            position++;
        sessions.add(position, new Session(film, time, date));
    }

    public String[][] getData() {
        String[][] data;
        Session session;

        data = new String[sessions.size()][4];
        for (int c = 0; c < sessions.size(); c++) {
            session = sessions.get(c);
            constructDataSection(data[c], session);
        }
        return data;
    }

    public String[][] getData(String name, String author, String tFrom, String tTo, String dFrom, String dTo) {
        String[][] data;
        Session[] dataInSessions;

        dataInSessions = getSessions(name, author, tFrom, tTo, dFrom, dTo);
        data = convertToData(dataInSessions);
        return data;
    }

    private Session[] getSessions(String name, String author, String tFrom, String tTo, String dFrom, String dTo) {
        Session[] data;
        Session session;
        Time timeFrom = new Time(tFrom);
        Time timeTo = new Time(tTo);
        Date dateFrom = new Date(dFrom);
        Date dateTo = new Date(dTo);
        int size = 0;

        for (int c = 0; c < sessions.size(); c++) {
            session = sessions.get(c);
            if (    (name.equals("") || session.film.getName().toLowerCase().contains(name.toLowerCase())) &&
                    (author.equals("") || session.film.getAuthor().toLowerCase().contains(author.toLowerCase())) &&
                    (tFrom.equals("") || session.time.compareTo(timeFrom) >= 0) &&
                    (tTo.equals("") || session.time.compareTo(timeTo) <= 0) &&
                    (dFrom.equals("") || session.date.compareTo(dateFrom) >= 0) &&
                    (dTo.equals("") || session.date.compareTo(dateTo) <= 0)) {
                size++;
            }
        }
        data = new Session[size];
        size = 0;
        for (int c = 0; c < sessions.size(); c++) {
            session = sessions.get(c);
            if (    (name.equals("") || session.film.getName().toLowerCase().contains(name.toLowerCase())) &&
                    (author.equals("") || session.film.getAuthor().toLowerCase().contains(author.toLowerCase())) &&
                    (tFrom.equals("") || session.time.compareTo(timeFrom) >= 0) &&
                    (tTo.equals("") || session.time.compareTo(timeTo) <= 0) &&
                    (dFrom.equals("") || session.date.compareTo(dateFrom) >= 0) &&
                    (dTo.equals("") || session.date.compareTo(dateTo) <= 0)) {
                data[size] = session;
                size++;
            }
        }
        return data;
    }

    private String[][] convertToData(Session[] dataInSessions) {
        int counter;
        String[][] data;

        data = new String[dataInSessions.length][4];
        counter = 0;
        for (Session session : dataInSessions) {
            constructDataSection(data[counter], session);
            counter++;
        }
        return data;
    }

    private void constructDataSection(String[] dataSection, Session session) {
        dataSection[0] = session.film.getName();
        dataSection[1] = session.film.getAuthor();
        dataSection[2] = session.time.toString();
        dataSection[3] = session.date.toString();
    }


    public String[][] getDataByFilmName(String name) {
        String[][] data;
        Session[] dataInSessions;

        dataInSessions = getSessionsByFilmName(name);
        data = convertToData(dataInSessions);
        return data;
    }

    private Session[] getSessionsByFilmName(String name) {
        Session[] answer;
        int amount;
        int counter;

        amount = 0;
        counter = 0;
        for (Session session : sessions)
            if (session.film.getName().toLowerCase().contains(name.toLowerCase()))
                amount++;
        answer = new Session[amount];
        if (amount > 0)
            for (Session session : sessions)
                if (session.film.getName().toLowerCase().contains(name.toLowerCase())) {
                    answer[counter] = session;
                    counter++;
                }
        return answer;
    }

    public String[][] getDataByFilmAuthor(String name) {
        String[][] data;
        Session[] dataInSessions;

        dataInSessions = getSessionsByFilmAuthor(name);
        data = convertToData(dataInSessions);
        return data;
    }

    private Session[] getSessionsByFilmAuthor(String name) {
        Session[] answer;
        int amount;
        int counter;

        amount = 0;
        counter = 0;
        for (Session session : sessions)
            if (session.film.getAuthor().toLowerCase().contains(name.toLowerCase()))
                amount++;
        answer = new Session[amount];
        if (amount > 0)
            for (Session session : sessions)
                if (session.film.getAuthor().toLowerCase().contains(name.toLowerCase())) {
                    answer[counter] = session;
                    counter++;
                }
        return answer;
    }

    public String[][] getDataByTime(String time) {
        String[][] data;
        Session[] dataInSessions;

        dataInSessions = getSessionsByTime(time);
        data = convertToData(dataInSessions);
        return data;
    }

    private Session[] getSessionsByTime(String time) {
        Session[] answer;
        int amount;
        int counter;

        amount = 0;
        counter = 0;
        for (Session session : sessions)
            if (session.time.toString().contains(new Time(time).toString()) || session.time.toString().contains(time))
                amount++;
        answer = new Session[amount];
        if (amount > 0)
            for (Session session : sessions)
                if (session.time.toString().contains(new Time(time).toString()) || session.time.toString().contains(time)) {
                    answer[counter] = session;
                    counter++;
                }
        return answer;
    }

    public String[][] getDataByDate(String date) {
        String[][] data;
        Session[] dataInSessions;

        dataInSessions = getSessionsByDate(date);
        data = convertToData(dataInSessions);
        return data;
    }

    private Session[] getSessionsByDate(String date) {
        Session[] answer;
        int amount;
        int counter;

        amount = 0;
        counter = 0;
        for (Session session : sessions)
            if (session.date.toString().contains(new Date(date).toString()) || session.date.toString().contains(date))
                amount++;
        answer = new Session[amount];
        if (amount > 0)
            for (Session session : sessions)
                if (session.date.toString().contains(new Date(date).toString()) || session.date.toString().contains(date)) {
                    answer[counter] = session;
                    counter++;
                }
        return answer;
    }
}