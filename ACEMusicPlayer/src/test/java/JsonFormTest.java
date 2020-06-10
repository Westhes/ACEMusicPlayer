import com.aniruddhc.acemusic.player.GMusicHelpers.JSONForm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonFormTest {
    JSONForm jsonForm;

    @Before
    public void setup() {
        jsonForm = new JSONForm();
    }

    /**
     * Assert that a is inserted into the json with a value of one.
     */
    @Test
    public void assertInsertAOne() {
        String expected = "Content-Disposition: form-data; name=\"a\"\r\n\r\n1";
        jsonForm.addField("a", "1");
        jsonForm.close();

        String result = jsonForm.toString();

        Assert.assertTrue(result.contains(expected));
    }

    @Test
    public void assertInsertMultiple() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        jsonForm.addFields(map);

        jsonForm.close();
        String result = jsonForm.toString();

        String expectedA = "Content-Disposition: form-data; name=\"a\"\r\n\r\n1";
        String expectedB = "Content-Disposition: form-data; name=\"b\"\r\n\r\n2";

        Assert.assertTrue(result.contains(expectedA));
        Assert.assertTrue(result.contains(expectedB));
    }

    @Test(expected = IllegalArgumentException.class)
    public void assertIllegalArgument() {
        jsonForm.close().addField("a", "1");
    }
}
