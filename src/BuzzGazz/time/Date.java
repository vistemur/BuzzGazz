package BuzzGazz.time;

public class Date extends TimeObject {
    private int year;
    private int month;
    private int day;


    public Date() {
        this.day = 0;
        this.year = 0;
        this.month = 0;
        bigToSmall = false;
    }

    public Date(int...numbers) throws Exception {
        setData(getData(numbers));
        checkData();
        bigToSmall = false;
    }

    public Date(String time) {
        try {
            setData(getData(time));
            checkData();
            bigToSmall = false;
        } catch(Exception error) {}
    }

    private void checkData() throws Exception {
        if (day > 31 || month > 12)
            throw new Exception("incorrect date");
    }

    @Override
    protected int[] getData() {
        return new int[] {day, month, year};
    }

    @Override
    protected void setData(int[] data) {
        day = data[0];
        month = data[1];
        year = data[2];
    }
}
