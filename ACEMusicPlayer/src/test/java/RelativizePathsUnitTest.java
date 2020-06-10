import com.aniruddhc.acemusic.player.PlaylistUtils.RelativizePaths;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RelativizePathsUnitTest {
    String path;
    String relativeTo;
    String expectedResult;


    @Before
    public void setup() {
        path = "C:\\users\\test\\projectA\\subdirectory";
    }


    /**
     * Checks whether the relative path of C:/users/test/projectB equals ../../projectB.
     */
    @Test
    public void AssertRelativeDifferentProjectPath() {
        relativeTo = "C:\\users\\test\\projectB";
        expectedResult = "../../projectB";

        String response = RelativizePaths.convertToRelativePath(path, relativeTo);

        Assert.assertEquals(response, expectedResult);
    }

    /**
     * Checks whether the relative path of C:/users/test/ equals ../../
     * @// FIXME: 10-6-2020 The method fails to return the correct response.
     */
    @Test
    public void AssertRelativeParentPath() {
        relativeTo = "C:\\users\\test\\";
        expectedResult = "../../";

        String response = RelativizePaths.convertToRelativePath(path, relativeTo);
        Assert.assertEquals(response, expectedResult);
    }

    /**
     * Checks whether the relativePath to C:/users/test/projectA/subdirectory equals projectA/subdirectory.
     */
    @Test
    public void AssertRelativeProjectPath() {
        path = "C:\\users\\test\\";
        relativeTo = "C:\\users\\test\\projectA\\subdirectory";
        expectedResult = "projectA/subdirectory";

        String response = RelativizePaths.convertToRelativePath(path, relativeTo);

        Assert.assertEquals(response, expectedResult);
    }

    /**
     * Checks whether the convertToRelativePath method returns null when providing the same paths.
     */
    @Test
    public void AssertRelativeSamePath() {
        relativeTo = path;
        expectedResult = null;

        String response = RelativizePaths.convertToRelativePath(path, relativeTo);

        Assert.assertEquals(response, expectedResult);
    }

    /**
     * Checks whether the convertToRelativePath method returns the correct relative path when multiple directory names match.
     */
    @Test
    public void AssertRepeatingNestedDirectories() {
        relativeTo = "C:\\users\\notTest\\projectA\\subdirectory";
        expectedResult = "../../../notTest/projectA/subdirectory";

        String response = RelativizePaths.convertToRelativePath(path, relativeTo);

        Assert.assertEquals(response, expectedResult);
    }
}
