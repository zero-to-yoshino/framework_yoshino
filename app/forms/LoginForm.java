package forms;

import play.data.validation.Constraints;

public class LoginForm {
    @Constraints.Required 
    private String email;
    @Constraints.Required 
    private String password;

    public LoginForm() {
    }

    public LoginForm(String email, String password){
        setEmail(email);
        setPassword(password);
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
