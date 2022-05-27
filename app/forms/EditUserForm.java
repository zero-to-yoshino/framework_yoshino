package forms;

public class EditUserForm {
    private String name;
    private String email;
    private String prePassword;
    private String password;

    public EditUserForm() {
    }

    public EditUserForm(String name, String email){
        setName(name);
        setEmail(email);
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public String getPrePassword(){
        return prePassword;
    }
    public void setPrePassword(String prePassword){
        this.prePassword = prePassword;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
}
