package forms;

import play.data.validation.Constraints;

public class EntryForm {
    @Constraints.Required 
    private String name;
    @Constraints.Required 
    private String title;
    @Constraints.Required 
    private String message;

    public EntryForm() {

    }

    public EntryForm(String name, String title, String message) {
        setName(name);
        setTitle(title);
        setMessage(message);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
