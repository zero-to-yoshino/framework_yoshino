package models;

import javax.persistence.*;
// import javax.persistence.Id;
 
import io.ebean.Model;
// import io.ebean.annotation.*;
import java.util.List;
import javax.validation.constraints.NotNull;
 
@Entity
public class User extends Model{
    @Id
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull 
    private String password;
    @OneToMany(mappedBy="user")
    private List<Entry> Entries;
    private boolean hasAdmin = false;

    // 新規登録用
    public User(String name, String email, String password) {
        setName(name);
        setEmail(email);
        setPassword(password);
    }

    // ユーザ情報編集用
    public User(Long id, String name, String email, String password){
        setUserId(id);
        setName(name);
        setEmail(email);
        setPassword(password);
    }

    public Long getUserId(){
        return id;
    }
    public void setUserId(Long id){
        this.id = id;
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

    public List<Entry> getEntries(){
        return Entries;
    }
    public void setEntries(List<Entry> Entries){
        this.Entries = Entries;
    }

    public boolean getHasAdmin(){
        return hasAdmin;
    }

    public void setHasAdmin(boolean hasAdmin){
        this.hasAdmin = hasAdmin;
    }
}
