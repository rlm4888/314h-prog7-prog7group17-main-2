package assignment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.HashSet;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.MalformedInputException;

// class WebQueryEngineTest {

//     @Test
//     void testQueryEngine() throws MalformedURLException {
//         // Create a WebIndex and add some test pages
//         WebIndex index = new WebIndex();
//         Page page1 = new Page(new URL("http://test.com/page1"));
//         Page page2 = new Page(new URL("http://test.com/page2"));
//         Page page3 = new Page(new URL("http://test.com/page3"));

//         index.addWord("java", 1, page1);
//         index.addWord("program", 2, page1);
//         index.addWord("web", 3, page1);

//         index.addWord("java", 1, page2);
//         index.addWord("crawler", 2, page2);
//         index.addWord("program", 3, page2);

//         index.addWord("java", 1, page3);
//         index.addWord("program", 2, page3);
//         index.addWord("query", 3, page3);

//         WebQueryEngine queryEngine = WebQueryEngine.fromIndex(index);

//         System.out.println(index.findPhrase("java program"));

//         // Test a simple query
//         Collection<Page> result1 = queryEngine.query("java & program");
//         Collection<Page> expected1 = new HashSet<>();
//         expected1.add(page1);
//         expected1.add(page2);
//         expected1.add(page3);
//         assertEquals(expected1, result1);

//         // Test a negation query
//         // Collection<Page> result2 = queryEngine.query("!java");
//         // Collection<Page> expected2 = new HashSet<>();
//         // expected2.add(page2);  // Only page2 doesn't contain "java"
//         // assertEquals(expected2, result2);

//         // Test a phrase query
//         Collection<Page> result3 = queryEngine.query("\"java program\"");
//         Collection<Page> expected3 = new HashSet<>();
//         expected3.add(page1);
//         expected3.add(page2);
//         assertEquals(expected3, result3);
//     }
// }
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

// public class WebQueryEngineTest {
//     public static void main(String[] args) {
//         // Create a WebIndex
//         WebIndex index = new WebIndex();

//         // Create some test pages
//         Page page1 = createPage("http://example.com/page1");
//         Page page2 = createPage("http://example.com/page2");
//         Page page3 = createPage("http://example.com/page3");

//         // Add words to the index
//         index.addWord("ja$v$a", 1, page1);
//         index.addWord("programming", 2, page1);
//         index.addWord("ja$v$a", 1, page2);
//         index.addWord("web", 3, page2);
//         index.addWord("python", 4, page3);
//         index.addWord("programming", 5, page3);

//         // Create a WebQueryEngine
//         WebQueryEngine queryEngine = WebQueryEngine.fromIndex(index);

//         // Test simple queries
//         testQuery(queryEngine, "ja$v$a", set(page1, page2));
//         testQuery(queryEngine, "web", set(page2));
//         testQuery(queryEngine, "python", set(page3));

//         // // Test phrase queries
//         testQuery(queryEngine, "\"java programming\"", set(page1, page2));
//         testQuery(queryEngine, "\"web programming\"", set());

//         // // Test negation queries
//         //testQuery(queryEngine, "!python", set(page1, page2));

//         // Test implicit AND queries
//         testQuery(queryEngine, "java programming", set(page1, page2));

//         System.out.println("All tests passed");
//     }

//     private static Page createPage(String url) {
//         try {
//             return new Page(new URL(url));
//         } catch (MalformedURLException e) {
//             throw new RuntimeException("Failed to create URL", e);
//         }
//     }
    

//     private static <T> HashSet<T> set(T... elements) {
//         HashSet<T> set = new HashSet<>();
//         for (T element : elements) {
//             set.add(element);
//         }
//         return set;
//     }

//     private static void testQuery(WebQueryEngine queryEngine, String queryString, HashSet<Page> expected) {
//         Collection<Page> result = queryEngine.query(queryString);
//         if (!result.equals(expected)) {
//             throw new AssertionError("Query failed for '" + queryString + "'. Expected: " + expected + ", Actual: " + result);
//         }
//     }
// }
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
public class WebQueryEngineTest {
    public static void main(String[] args) {
        // Create a WebIndex
        WebIndex index = new WebIndex();

        // Create some test pages
        Page page1 = createPage("http://example.com/page1");
        Page page2 = createPage("http://example.com/page2");
        Page page3 = createPage("http://example.com/page3");

        // Add words to the index
        index.addWord("ja&va", 1, page1);
        index.addWord("programming", 2, page1);
        index.addWord("web", 3, page1);
        index.addWord("java", 1, page2);
        index.addWord("programming", 2, page2);
        index.addWord("py'thon", 4, page3);
        index.addWord("programming", 5, page3);

        // Create a WebQueryEngine
        WebQueryEngine queryEngine = WebQueryEngine.fromIndex(index);

        // Test queries
        testQuery(queryEngine, "ja&va", set(page1));
//        testQuery(queryEngine, "programming", set(page1, page2, page3));
//        testQuery(queryEngine, "web", set(page1));

//        testQuery(queryEngine, "java programming", set(page1, page2));
        testQuery(queryEngine, "\"programming\" \"programming\"", set(page1, page2));
        testQuery(queryEngine, "((java & programming) | python)", set(page1, page2, page3));

        testQuery(queryEngine, "\"java programming\" monkey", set(page1, page2));
        testQuery(queryEngine, "", set(page1,page2,page3));
        testQuery(queryEngine, "py'thon", set(page3));

        //testQuery(queryEngine, "!python", set(page1, page2));
        testQuery(queryEngine, "java& !programming", set(page2));

    }

    private static void testQuery(WebQueryEngine queryEngine, String queryString, Set<Page> expectedResults) {
        Collection<Page> results = queryEngine.query(queryString);

        System.out.println("Query: " + queryString);
        System.out.println("Expected Results: " + expectedResults);
        System.out.println("Actual Results: " + results);
        System.out.println("Query Passes: " + results.equals(expectedResults));
        System.out.println();
    }

        private static Page createPage(String url) {
        try {
            return new Page(new URL(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to create URL", e);
        }
    }
    

    private static Set<Page> set(Page... pages) {
        return new HashSet<>(Arrays.asList(pages));
    }
}
