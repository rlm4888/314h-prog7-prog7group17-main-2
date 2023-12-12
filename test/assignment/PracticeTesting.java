package assignment;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PracticeTesting {
    @Test
    public void testURIParsing() throws URISyntaxException, IOException {
        String path = "file:/home/jsinghal/Code/cs314/314h-prog7-prog7group17/test/assignment/test.txt";

        URI uri = new URI(path);
        URL url = uri.toURL();

        System.out.println(uri);
        System.out.println(url);

        Scanner s = new Scanner(url.openStream());

        System.out.println(s.nextLine());

        URI test1uri = uri.resolve("https://google.com");

        System.out.println(test1uri);
    }

    @Test void stringTrimTest() {
        CrawlingMarkupHandler crawlingMarkupHandler = new CrawlingMarkupHandler();

        String token = "can't.";

        String newToken = crawlingMarkupHandler.trimStringPunctuation(token);

        assertEquals(newToken, "can't");

        System.out.println("\uD83D\uDC12");
    }
}
