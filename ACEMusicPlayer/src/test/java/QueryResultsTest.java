import com.aniruddhc.acemusic.player.GMusicHelpers.QueryResults;
import com.aniruddhc.acemusic.player.GMusicHelpers.WebClientSongsSchema;
import com.google.api.client.json.JsonParser;

import org.json.JSONException;
//import org.json.JSONObject;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class QueryResultsTest {
    QueryResults queryResults = new QueryResults();
    ArrayList<WebClientSongsSchema> songsSchemas = new ArrayList<>();
    String jsonString = "{\"artists\":[" +
                "\"artistA\"," +
                "\"artistB\"," +
                "\"artistC\"]," +
            "\"albums\":["+
                "\"albumA\"," +
                "\"albumB\"," +
                "\"albumC\"]," +
            "\"songs\":[" +
                "\"songA\"," +
                "\"songB\"," +
                "\"songC\"]" +
            "}";

    @Before
    public void setup() throws JSONException {
//        JsonParser parser = new JSONParser();
//        JSONObject json = new JSONObject(jsonString);
        JSONObject json = new JSONObject();
        queryResults.fromJsonObject(json);
    }

    @Test
    public void test() {
        ArrayList arr = queryResults.getArtists();

        System.out.println("");
    }
}
