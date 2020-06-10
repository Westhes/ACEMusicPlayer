import com.aniruddhc.acemusic.player.PlaylistUtils.WriteToM3UPlaylist;

import org.junit.Assert;
import org.junit.Test;

public class WriteToM3UPlaylistUnitTest {
    WriteToM3UPlaylist playlist = new WriteToM3UPlaylist("", "", "", "");

    String workingDirectory = System.getProperty("user.dir");
    String fileWithTitle = workingDirectory + "\\src\\test\\res\\audio\\audioWithTitle.mp3";
    String fileWithoutTitle =  workingDirectory + "\\src\\test\\res\\audio\\audioWithoutTitle.mp3";

    /**
     * Assert that the method works when the file exist and contains a file.
     */
    @Test
    public void AssertSongWithTitle() {
        String title = playlist.getSongTitle(fileWithTitle);
        Assert.assertEquals("Success", title);
    }

    /**
     * Test whether the method throws an exception when the file doesn't contain a title.
     */
    @Test(expected = StringIndexOutOfBoundsException.class)
    public void AssertStringOutOfBoundsException() {
        playlist.getSongTitle(fileWithoutTitle);
    }

    /**
     * Test whether the method throws an exception when the file doesn't exist.
     */
    @Test(expected = NullPointerException.class)
    public void AssertNull() {
        playlist.getSongTitle("");
    }
}
