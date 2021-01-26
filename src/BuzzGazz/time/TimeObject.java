package BuzzGazz.time;

import java.util.Arrays;

abstract class TimeObject {
    protected static char[] dividers = {'.', '|', '/', ':'};
    protected int dataAmount = 3;
    protected boolean bigToSmall = true;

    private boolean isDivider(char c) {
        for (char divider : dividers)
            if (c == divider)
                return true;
        return false;
    }

    private void checkCharForInt(char c) throws Exception {
        if (c < '0' || c > '9')
            throw new Exception("incorrect time string type (example 2:10:15)");
    }

    public String toString() {
        StringBuilder answer;
        int[] data;

        data = getData();
        answer = new StringBuilder();
        for (int i = 0; i < dataAmount; i++) {
            answer.append(data[i]);
            if (i != dataAmount - 1)
                answer.append(':');
        }
        return answer.toString();
    }

    public int compareTo(TimeObject that) {
        int[] thisData = this.getData();
        int[] thatData = that.getData();

        if (bigToSmall) {
            for (int c = 0; c < thisData.length; c++) {
                if (c >= thatData.length || thisData[c] > thatData[c]) {
                    return 1;
                } else if (thisData[c] < thatData[c]) {
                    return -1;
                }
            }
        } else {
            for (int c = thisData.length - 1; c >= 0; c--) {
                if (thisData[c] > thatData[c]) {
                    return 1;
                } else if (thisData[c] < thatData[c]) {
                    return -1;
                }
            }
        }
        return 0;
    }

    protected int[] getData(String time) throws Exception {
        int[] data = new int[dataAmount];
        int counter;
        int mn;

        Arrays.fill(data, 0);
        counter = time.length();
        for (int dataCounter = dataAmount - 1; dataCounter >= 0; dataCounter--) {
            mn = 1;
            while (--counter >= 0 && !isDivider(time.charAt(counter))) {
                checkCharForInt(time.charAt(counter));
                data[dataCounter] += mn * Character.getNumericValue(time.charAt(counter));
                mn *= 10;
            }
        }
        return data;
    }

    protected int[] getData(int...numbers) {
        int[] data = new int[dataAmount];

        Arrays.fill(data, 0);
        for (int i = 0; i < numbers.length && i < dataAmount; i++)
            data[i] = numbers[i];
        return data;
    }

    abstract protected int[] getData();
    abstract protected void setData(int[] data);
}