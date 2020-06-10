import com.aniruddhc.acemusic.player.GMusicHelpers.QueryResults;
import com.aniruddhc.acemusic.player.GMusicHelpers.WebClientSongsSchema;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class QueryResultsTest {
    private QueryResults queryResults = new QueryResults();
    private ArrayList<WebClientSongsSchema> songsSchemas = new ArrayList<>();

    private String[] songNames = {"a", "b", "c"};
    private String[] artists = {"artistA", "artistB", "artistC"};
    private String[] albums = {"albumA", "albumB", "albumC"};

    @Before
    public void setup() {
        for (int i = 0; i < songNames.length; i++) {
            WebClientSongsSchema song = new WebClientSongsSchema();

            song.setTitle(songNames[i]);
            song.setArtist(artists[i]);
            song.setAlbum(albums[i]);

            songsSchemas.add(song);
        }

        queryResults.setWebClientSongsSchemas(songsSchemas);
        queryResults.setArtists(songsSchemas);
        queryResults.setAlbums(songsSchemas);
    }

    @Test
    public void whenGetArtists_returnsCorrectArtistsInCorrectOrder() {
        ArrayList<WebClientSongsSchema> artists = queryResults.getArtists();

        for (int i = 0; i < songNames.length; i++) {
            assertEquals(songNames[i], artists.get(i).getTitle());
            assertEquals(this.artists[i], artists.get(i).getArtist());
            assertEquals(albums[i], artists.get(i).getAlbum());
        }
    }
}
