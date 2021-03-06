package forms;

import play.data.validation.Constraints;

public class UserForm {
    @Constraints.Required 
    private String name;
    @Constraints.Required 
    private String email;
    @Constraints.Required 
    private String password;

    public UserForm() {
    }

    public UserForm(String name, String email, String password){
        setName(name);
        setEmail(email);
        setPassword(password);
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

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
}