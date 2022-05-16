package models;
 
import java.util.Date;
 
import javax.persistence.*;
 
import io.ebean.Model;
 
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.NotNull;
 
@Entity
public class Entry extends Model {
   private Long id;
   private String name;
   private String title;
   private String message;

    public Entry(String name, String title, String message) {
        this.name = name;
        this.title = title;
        this.message = message;
    }
 
    @Id
    public Long getId() {
        return id;
     }
  
     public void setId(Long id) {
        this.id = id;
     }
 
    @NotNull
    public String getName() {
        return name;
     }
  
     public void setName(String name) {
        this.name = name;
     }

    @NotNull
    public String getTitle() {
        return title;
     }
  
     public void setTitle(String title) {
        this.title = title;
     }

    @NotNull
    public String getMessage() {
        return message;
     }
  
     public void setMessage(String message) {
        this.message = message;
     }
 
    @PastOrPresent
    public Date createDate;

    public Date getCreateDate() {
        return createDate;
     }
  
     public void setCreateDate(Date createDate) {
        this.createDate = createDate;
     }
 
    @Version
    public Date updateDate;

    public Date getUpdateDate() {
        return updateDate;
     }
  
     public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
     }
 
    // public String toString() {
    //     return "Parent [id=" + id + ", name=" + name + ", createDate="
    //             + createDate + ", updateDate=" + updateDate + "]";
    // }

}
