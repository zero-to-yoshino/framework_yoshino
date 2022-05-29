package forms;

public class EntryForm {
    private Long id;
    private String name;
    private String title;
    private String message;
    private Date createDate;

    public EntryForm() {

    }

    public EntryForm(String name, String title, String message) {
        setName(name);
        setTitle(title);
        setMessage(message);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
