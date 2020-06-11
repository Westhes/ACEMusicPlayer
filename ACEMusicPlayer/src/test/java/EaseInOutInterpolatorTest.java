import com.aniruddhc.acemusic.player.Utils.EaseInOutInterpolator;

import org.junit.Assert;
import org.junit.Test;

public class EaseInOutInterpolatorTest {
    @Test
    public void AssertEaseIn() {
        float expected = 0.29289323f;
        EaseInOutInterpolator interpolator = new EaseInOutInterpolator(EaseInOutInterpolator.EasingType.Type.IN);
        float response = interpolator.getInterpolation(0.5f);

        Assert.assertEquals(expected, response, 0.0f);
    }

    @Test
    public void AssertEaseOut() {
        float expected = 0.70710677f;
        EaseInOutInterpolator interpolator = new EaseInOutInterpolator(EaseInOutInterpolator.EasingType.Type.OUT);
        float response = interpolator.getInterpolation(0.5f);

        Assert.assertEquals(expected, response, 0.0f);
    }

    @Test
    public void AssertEase() {
        float expected = 1f;
        EaseInOutInterpolator in = new EaseInOutInterpolator(EaseInOutInterpolator.EasingType.Type.IN);
        EaseInOutInterpolator out = new EaseInOutInterpolator(EaseInOutInterpolator.EasingType.Type.OUT);

        float inResponse = in.getInterpolation(0.5f);
        float outResponse = out.getInterpolation(0.5f);


        Assert.assertEquals(expected - outResponse, inResponse, 0f);
        Assert.assertEquals(expected - inResponse, outResponse, 0f);
    }

    @Test
    public void AssertEaseInOut() {
        float expected = 0.5f;
        EaseInOutInterpolator interpolator = new EaseInOutInterpolator(EaseInOutInterpolator.EasingType.Type.INOUT);
        float response = interpolator.getInterpolation(0.5f);

        Assert.assertEquals(expected, response, 0.0f);
    }
}
