package tests;

import BuzzGazz.time.Time;
import org.junit.Assert;
import org.junit.Test;

public class TimeTest extends TestModel {
    @Test
    public void zeroCheck() {
        Assert.assertEquals(new Time(0).toString(), "0:0:0");
    }

    @Test
    public void normalize() {
        Assert.assertEquals(new Time(1, 10, 100).toString(), "1:11:40");
        Assert.assertEquals(new Time("3.120.0").toString(), "5:0:0");
        Assert.assertEquals(new Time("0.1.3600").toString(), "1:1:0");
    }

    @Test
    public void compareTo() {
        Assert.assertTrue(new Time("1:1:1").compareTo(new Time("1:1:0")) > 0);
        Assert.assertTrue(new Time("12:12:12").compareTo(new Time("12:12:12")) == 0);
        Assert.assertTrue(new Time("1:59:59").compareTo(new Time("2:0:0")) < 0);
        Assert.assertTrue(new Time("0:1:0").compareTo(new Time("0:0:1")) > 0);
    }
}
