package PlaybackKickStarter;

import android.content.Context;
import android.content.Intent;

import com.aniruddhc.acemusic.player.PlaybackKickstarter.PlaybackKickstarter;
import com.aniruddhc.acemusic.player.Utils.Common;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlaybackKickStarterTest {

    private PlaybackKickstarter kickstarter;

    @Mock
    private Context contextMock;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        kickstarter = new PlaybackKickstarter(contextMock);
    }

    @Test
    public void whenInitPlayback_shouldStartPlayback_showNowPlayingActivity() {
        // arrange
        boolean showNowPlayingActivity = true;

        // act
        kickstarter.initPlayback(contextMock, "", 0, 0, showNowPlayingActivity, true);

        // assert: verify the NowPlayingActivity gets started.
        // Cannot check for any variables in the Intent object: when creating the Intent object
        // it will for some reason always stay null
        verify(contextMock).startActivity(any(Intent.class));
    }

    @Test
    public void whenInitPlayback_shouldStartPlayback_doNotShowNowPlayingActivity_serviceIsRunning() throws Exception {
        // arrange
        boolean showNowPlayingActivity = false;
        boolean serviceIsRunning = true;

        Common applicationContext = mock(Common.class, RETURNS_DEEP_STUBS);
        when(applicationContext.isServiceRunning()).thenReturn(serviceIsRunning);
        when(contextMock.getApplicationContext()).thenReturn(applicationContext);

        // act
        kickstarter.initPlayback(contextMock, "", 0, 0, showNowPlayingActivity, true);

        // assert: verify the service got called
        verify(applicationContext.getService().getPrepareServiceListener()).onServiceRunning(applicationContext.getService());
    }

    @Test
    public void whenInitPlayback_shouldStartPlayback_doNotShowNowPlayingActivity_serviceIsNotRunning() throws Exception {
        // arrange
        boolean showNowPlayingActivity = false;
        boolean serviceIsRunning = false;

        Common applicationContext = mock(Common.class);
        when(applicationContext.isServiceRunning()).thenReturn(serviceIsRunning);
        when(contextMock.getApplicationContext()).thenReturn(applicationContext);

        // act
        kickstarter.initPlayback(contextMock, "", 0, 0, showNowPlayingActivity, true);

        // assert: verify the service gets started
        verify(contextMock).startService(any(Intent.class));
    }

}
