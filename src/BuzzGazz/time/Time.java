package BuzzGazz.time;

public class Time extends TimeObject {
    protected int hours;
    protected int minutes;
    protected int seconds;

    public Time(int...numbers) {
        setData(getData(numbers));
        normalizeData();
    }

    public Time(String time) {
        try {
            setData(getData(time));
        } catch(Exception error) {
            error.printStackTrace();
        }
        normalizeData();
    }

    private void normalizeData() {
        while (this.seconds >= 60) {
            this.seconds -= 60;
            this.minutes += 1;
        }
        while (this.minutes >= 60) {
            this.minutes -= 60;
            this.hours += 1;
        }
    }

    @Override
    protected int[] getData() {
        return new int[] {hours, minutes, seconds};
    }

    @Override
    protected void setData(int[] data) {
        hours = data[0];
        minutes = data[1];
        seconds = data[2];
        normalizeData();
    }
}
