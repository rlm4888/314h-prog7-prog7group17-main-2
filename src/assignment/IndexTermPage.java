package assignment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class IndexTermPage implements Serializable {
    private HashMap<Page, ArrayList<Integer>> pageMap = new HashMap<>();

    public void addWord(Page page, Integer tokenLocation) {
        if (pageMap.containsKey(page)) {
            pageMap.get(page).add(tokenLocation);
        } else {
            ArrayList<Integer> locationList = new ArrayList<>();
            locationList.add(tokenLocation);
            pageMap.put(page, locationList);
        }
    }

    public Set<Page> getPageSet() {
        return this.pageMap.keySet();
    }

    public ArrayList<Integer> tokenLocationsAtPage(Page page) {
        return this.pageMap.get(page);
    }

    public HashMap<Page, ArrayList<Integer>> getPageMap() {
        return pageMap;
    }
}
