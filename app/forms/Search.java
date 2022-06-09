package forms;

import play.data.validation.Constraints;
import java.util.List;

public class Search{
    @Constraints.Required 
    private String searchInput;
    // 将来的なandor検索などを想定した変数
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
