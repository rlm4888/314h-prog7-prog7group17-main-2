package assignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * A web-index which efficiently stores information about pages. Serialization is done automatically
 * via the superclass "Index" and Java's Serializable interface.
 *
 * TODO: Implement this!
 */
public class WebIndex extends Index {
    /**
     * Needed for Serialization (provided by Index) - don't remove this!
     */
    private static final long serialVersionUID = 1L;
    
    public WebIndex() {
        tokenMap = new HashMap<>();
    }

    private HashMap<String, IndexTermPage> tokenMap;

    public int getTokenMapSize() {
        return this.tokenMap.size();
    }

    private HashSet<Page> pageSet = new HashSet<>();

    public int getNumPages() {
        return pageSet.size();
    }

    public Set<Page> getAllPages() {
        return pageSet;
    }

    public void addWord(String word, Integer wordLocation, Page page) {
        word = word.toLowerCase();

        pageSet.add(page);

        if (tokenMap.containsKey(word)) {
            tokenMap.get(word).addWord(page, wordLocation);
        } else {
            IndexTermPage newITP = new IndexTermPage();
            newITP.addWord(page, wordLocation);
            tokenMap.put(word, newITP);
        }
    }

    public Set<Page> findWord(String word) {
        if (this.tokenMap.get(word) != null) {
            return this.tokenMap.get(word).getPageSet();
        } else {
            return new HashSet<>();
        }
    }

    public Set<Page> findPhrase(String phrase) {
        String[] tokens = phrase.split("\\s+");

//        for (int i = 0; i < tokens.length; i++) {
//            tokens[i] = trimStringPunctuation(tokens[i]);
//        }

        // Going to avoid trimming for now until clarifying punctuation handling
        // TODO: Check for punctuation handling clarification before finalizing this code

        Set<Page> pageSet = new HashSet<>();

        IndexTermPage itp = this.tokenMap.get(tokens[0]);

        if (itp == null) {
            return new HashSet<>();
        }

        if (tokens.length == 1) return this.findWord(tokens[0]);

        itp.getPageMap().forEach((page, locations) -> {
            for (Integer location : locations) {
                findPhraseImpl(pageSet, tokens, 1, location, page);
            }
        });

        return pageSet;
    }

    private void findPhraseImpl(Set<Page> pageSet, String[] tokenList, int depth, int lastPosition, Page page) {
        String currentToken = tokenList[depth];

        IndexTermPage itp = this.tokenMap.get(currentToken);

        // If token not indexed at all
        if (itp == null) return;

        ArrayList<Integer> positionList = itp.tokenLocationsAtPage(page);

        // page doesn't contain token
        if (positionList == null) return;

        // should trigger only on the last token
        if (tokenList.length == (depth + 1)) {
            if (positionList.contains(lastPosition + 1)) {
                pageSet.add(page);
            }
        } else {
            // For all other cases
            if (positionList.contains(lastPosition + 1)) {
                findPhraseImpl(pageSet, tokenList, depth + 1, lastPosition + 1, page);
            }
        }
    }

    private String trimStringPunctuation(String token) {
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
        }

        return false;
    }
}
