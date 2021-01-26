package tests;

import BuzzGazz.time.Date;
import BuzzGazz.time.Time;
import org.junit.Assert;
import org.junit.Test;

public class DateTest extends TestModel {
    @Test(expected = Exception.class)
    public void exception() throws Exception {
        Date date = new Date(100, 12, 2000);
    }

    @Test
    public void compareTo() {
        Assert.assertTrue(new Date("1.1.1").compareTo(new Date("1.1.0")) > 0);
        Assert.assertTrue(new Date("0.1.0").compareTo(new Date("1.0.0")) > 0);
        Assert.assertTrue(new Date("5.10.2020").compareTo(new Date("10.11.2020")) < 0);
    }
}
