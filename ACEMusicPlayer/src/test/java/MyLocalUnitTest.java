import org.junit.Test;


public class MyLocalUnitTest {
    private static String demo = "Demo";

    @Test
    public void isDemo() {
        assert demo == "Demo";
    }

}
