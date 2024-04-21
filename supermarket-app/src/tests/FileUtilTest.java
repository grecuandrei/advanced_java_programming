package tests;

import org.junit.Test;
import static org.junit.Assert.*;
import utils.FileUtil;
import java.io.IOException;
import java.util.List;

public class FileUtilTest {

    @Test
    public void testWriteAndReadFromFile() throws IOException {
        String testContent = "Test Line";
        FileUtil.writeToFile(testContent);

        List<String> lines = FileUtil.readFromFile();
        assertFalse(lines.isEmpty());
        assertEquals(testContent, lines.get(0));
    }
}
