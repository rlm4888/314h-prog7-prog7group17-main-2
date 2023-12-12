package assignment;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.net.*;
import org.attoparser.simple.*;

/**
 * A markup handler which is called by the Attoparser markup parser as it parses the input;
 * responsible for building the actual web index.
 *
 * TODO: Implement this!
 */
public class CrawlingMarkupHandler extends AbstractSimpleMarkupHandler {

    public CrawlingMarkupHandler() {
        webIndex = new WebIndex();
        textRead = new ArrayList<>();
    }

    private WebIndex webIndex;

    private ArrayList<String> textRead;

    private Page currentPage;
    private URI currentURI;

    private Integer curWordLocation = 0;

    /**
     * Might use to signal to the crawler that a new page is to be scanned
     *
     * possibly should clear the text cache
     */
    public void newPage(Page page) {
        //TODO: implement this shit
        textRead.clear();
        curWordLocation = 0;

        currentPage = page;

        try {
            currentURI = page.getURL().toURI();
        } catch (Exception e) {
            System.out.println("Mega fuck up this time buddy");
        }
    }

    /**
     * called when the page is done being parsed and any stuff should be called to parse the text in case that is best
     * idk fuck this stupid ass project
     */
    public void pageDone() {
        StringBuilder textCombination = new StringBuilder();

        for (String string : textRead) {
            textCombination.append(string);
        }

        String textToParse = textCombination.toString();

        String[] splitText = textToParse.split("\\R");

        for (String string : splitText) {
            String[] tokens = string.split("\\s+");

            for (String token : tokens)  {
                curWordLocation++;

                handleToken(token, curWordLocation);
            }
        }
    }

    private void handleToken(String token, Integer wordLocation) {
        token = trimStringPunctuation(token);

        if (token.isEmpty() || token.isBlank()) {
            return;
        } else {
            webIndex.addWord(token, wordLocation, currentPage);
        }
    }



    public String trimStringPunctuation(String token) {
        if (token.isEmpty()) {
            return token;
        } else if (token.length() == 1) {
            if (isCharNumberLetter(token.charAt(0))) {
                return token;
            } else {
                return "";
            }
        } else {
            // what a stupid fucking line of code
            int frontCount = 0;
            int backCount = 0;

            for (int i = 0; i < token.length(); i++) {
                if (!isCharNumberLetter(token.charAt(i))) {
                    frontCount++;
                } else {
                    break;
                }
            }

            token = token.substring(frontCount);

            if (token.isEmpty()) return token;

            for (int i = token.length() - 1; i >= 0; i--) {
                if (!isCharNumberLetter(token.charAt(i))) {
                    backCount++;
                } else {
                    break;
                }
            }

            token = token.substring(0, token.length()-backCount);
        }

        return token;
    }

    // HAHA THIS SHIT WORKS !
    private boolean isCharNumberLetter(char character) {
        if (character >= 48 && character <= 57) {
            return true;
        } else if (character >= 65 && character <= 90) {
            return true;
        } else if (character >= 97 && character <= 122) {
            return true;
        } else {
            return false;
        }
    }

    /**
    * This method returns the complete index that has been crawled thus far when called.
    */
    public Index getIndex() {
        return webIndex;
    }

    /**
    * This method returns any new URLs found to the Crawler; upon being called, the set of new URLs
    * should be cleared.
    */
    public List<URL> newURLs() {
        List<URL> returnedList = new LinkedList<>();

        for (URL url : urlList) {
            if (!fullURLSet.contains(url)) {
                returnedList.add(url);
                fullURLSet.add(url);
            }
        }

        urlList.clear();

        return returnedList;
    }

    private HashSet<URL> fullURLSet = new HashSet<>();

    private LinkedList<URL> urlList = new LinkedList<>();

    /**
    * These are some of the methods from AbstractSimpleMarkupHandler.
    * All of its method implementations are NoOps, so we've added some things
    * to do; please remove all the extra printing before you turn in your code.
    *
    * Note: each of these methods defines a line and col param, but you probably
    * don't need those values. You can look at the documentation for the
    * superclass to see all of the handler methods.
    */

    /**
    * Called when the parser first starts reading a document.
    * @param startTimeNanos  the current time (in nanoseconds) when parsing starts
    * @param line            the line of the document where parsing starts
    * @param col             the column of the document where parsing starts
    */
    public void handleDocumentStart(long startTimeNanos, int line, int col) {
    }

    /**
    * Called when the parser finishes reading a document.
    * @param endTimeNanos    the current time (in nanoseconds) when parsing ends
    * @param totalTimeNanos  the difference between current times at the start
    *                        and end of parsing
    * @param line            the line of the document where parsing ends
    * @param col             the column of the document where the parsing ends
    */
    public void handleDocumentEnd(long endTimeNanos, long totalTimeNanos, int line, int col) {
    }

    /**
    * Called at the start of any tag.
    * @param elementName the element name (such as "div")
    * @param attributes  the element attributes map, or null if it has no attributes
    * @param line        the line in the document where this elements appears
    * @param col         the column in the document where this element appears
    */
    public void handleOpenElement(String elementName, Map<String, String> attributes, int line, int col) {
        ignoreText = false;

        if (elementName.equalsIgnoreCase("a")) {
            attributes.forEach((key, value) -> {
                if (key.equalsIgnoreCase("href")) {
                    try {
                        URL urlToAdd = pathToURL(value);
                        if (urlToAdd != null) {
                            urlList.add(urlToAdd);
                        }
                    } catch (Exception e) {
                        //System.out.println("bad path/url");;
                    }
                }
            });

        } else if (elementName.equalsIgnoreCase("script") || elementName.equalsIgnoreCase("style")) {
            ignoreText = true;
        }
    }

    private URL pathToURL(String path) throws MalformedURLException, InvalidPathException {
        URI uri;
        try {
            uri = currentURI.resolve(path);
        } catch (Exception e) {
            //
            // System.out.println("bad path: " + path);
            return null;
        }

        if (!Objects.equals(uri.getScheme(), "file")) {
            return null;
        }

        try {
            uri.toURL().openStream();
        } catch (Exception e) {
            return null;
        }

        try {
            uri = new URI(uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    null, // Ignore the query part of the input url
                    uri.getFragment());
        } catch (Exception e) {
            return null;
        }

        int dotIndex = uri.toString().lastIndexOf('.');

        String fileExtension = uri.toString().substring(dotIndex);

        if (fileExtension.equalsIgnoreCase(".html") || fileExtension.equalsIgnoreCase(".htm") || fileExtension.equalsIgnoreCase(".xhtml")) {
            return uri.toURL();
        } else {
            return null;
        }
    }

    /**
    * Called at the end of any tag.
    * @param elementName the element name (such as "div").
    * @param line        the line in the document where this elements appears.
    * @param col         the column in the document where this element appears.
    */
    public void handleCloseElement(String elementName, int line, int col) {
        // haha nothign to do here dumbass
    }

    /**
    * Called whenever characters are found inside a tag. Note that the parser is not
    * required to return all characters in the tag in a single chunk. Whitespace is
    * also returned as characters.
    * @param ch      buffer containint characters; do not modify this buffer
    * @param start   location of 1st character in ch
    * @param length  number of characters in ch
    */

    boolean ignoreText = false;

    public void handleText(char ch[], int start, int length, int line, int col) {
        // this shit is well written asf
        if (!ignoreText) {
            this.textRead.add(new String(ch, start, length));
        }
    }
}
