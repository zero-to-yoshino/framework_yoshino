package objects;

import java.util.List;

public class Search{
    private String searchInput;
    private boolean andSearch;
    private List<String> searchList;

    public String getSearchInput(){
        return searchInput;
    }
    public void setSearchInput(String searchInput){
        this.searchInput = searchInput;
    }

    public boolean getAndSearch(){
        return andSearch;
    }
    public void setAndSearch(boolean andSearch){
        this.andSearch = andSearch;
    }

    public List<String> getSearchList(){
        return searchList;
    }
    public void setSearchInput(List<String> searchList){
        this.searchList = searchList;
    }
}
