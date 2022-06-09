package forms;

import play.data.validation.Constraints;

public class EditUserForm {
    @Constraints.Required 
    private String name;
    @Constraints.Required 
    private String email;
    @Constraints.Required 
    private String prePassword;
    @Constraints.Required 
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
