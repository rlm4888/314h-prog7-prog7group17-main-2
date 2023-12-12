package assignment;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CrawlingMarkupHandlerTest {

    @Test
    public void CrawlingMarkupHandlerTestOne() throws MalformedURLException {
        CrawlingMarkupHandler handler = new CrawlingMarkupHandler();

        String bumbumbum = "womp womp womp womp\n" +
                "WOMAWOIDMASIDNADJKFNKDSNF\n" +
                "java \uD83D\uDC4E\n";

        URL url = new URL("file:/home/jsinghal/Code/cs314/314h-prog7-prog7group17/test/assignment/test.txt");

        handler.newPage(new Page(url));

        handler.handleText(bumbumbum.toCharArray(), 0, bumbumbum.toCharArray().length, 0, 0);

        Map<String, String> attributes = new HashMap<>();

        attributes.put("href", "https://google.com");

        handler.handleOpenElement("a", attributes, 1, 1);

        handler.pageDone();

        System.out.println(handler.newURLs());

        WebIndex wbidx = (WebIndex) handler.getIndex();

        System.out.println(wbidx.findWord("womp"));

        System.out.println(wbidx.findPhrase("womp womp"));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objOutput;

        try {
            objOutput = new ObjectOutputStream(outputStream);
            objOutput.writeObject(wbidx);
        } catch (Exception e) {
            fail("serialization failed");
        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        ObjectInputStream objInput;

        WebIndex webnew;

        try {
            objInput = new ObjectInputStream(inputStream);
            webnew = (WebIndex) objInput.readObject();

            System.out.println(webnew.findWord("womp"));
        } catch (Exception e) {
            fail("serialization failed");
        }

    }

}