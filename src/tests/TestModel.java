package tests;

import org.junit.*;
import org.junit.rules.TestName;

public abstract class TestModel {

    @Rule
    public TestName testName = new TestName();

    @BeforeClass
    public static void allTestsStarted() {
        System.out.println("Starting tests");
    }

    @AfterClass
    public static void allTestsFinished() {
        System.out.println("tests ended");
    }

    @Before
    public void testStarted() {
        System.out.println(testName.getMethodName() + " test start");
    }

    @After
    public void testFinished() {
        System.out.println(testName.getMethodName() + " test end");
    }
}
