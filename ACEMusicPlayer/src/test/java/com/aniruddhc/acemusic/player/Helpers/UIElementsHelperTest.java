package com.aniruddhc.acemusic.player.Helpers;

import android.content.Context;
import android.graphics.Color;

import com.aniruddhc.acemusic.player.Utils.Common;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UIElementsHelperTest {

    private static final String NOW_PLAYING_COLOR = "NOW_PLAYING_COLOR";
    private static final String DEFAULT_COLOR = "BLUE";

    @Mock
    private Context contextMock;

    private Common applicationContext;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        applicationContext = mock(Common.class, RETURNS_DEEP_STUBS);
        when(contextMock.getApplicationContext()).thenReturn(applicationContext);
    }

    @Test
    public void whenGetTextColorGray_returnsGrayAsColor() {
        assertGetTextColorReturnsCorrectValue("GRAY", "#FFFFFF");
    }

    @Test
    public void whenGetTextColorWhite_returnsWhiteAsColor() {
        assertGetTextColorReturnsCorrectValue("WHITE", "#0F0F0F");
    }

    @Test
    public void whenGetTextColorNotGrayAndNotWhite_returnsThemeTextColor_lightTheme() {
        when(applicationContext.getCurrentTheme()).thenReturn(Common.LIGHT_THEME);

        assertGetTextColorReturnsCorrectValue("RED", "#5F5F5F");
    }

    @Test
    public void whenGetTextColorNotGrayAndNotWhite_returnsThemeTextColor_darkTheme() {
        when(applicationContext.getCurrentTheme()).thenReturn(Common.DARK_THEME);

        assertGetTextColorReturnsCorrectValue("RED", "#FFFFFF");
    }

    private void assertGetTextColorReturnsCorrectValue(String colorPreference, String expectedColor) {
        // arrange
        when(applicationContext.getSharedPreferences().getString(NOW_PLAYING_COLOR, DEFAULT_COLOR))
                .thenReturn(colorPreference);

        // act
        int actualColor = UIElementsHelper.getTextColor(contextMock);

        // assert
        assertEquals(Color.parseColor(expectedColor), actualColor);
    }
}