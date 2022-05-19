package models;
 
import java.util.Date;
 
import javax.persistence.*;
// import javax.persistence.Id;
 
import io.ebean.Model;
 
import play.data.format.Formats.DateTime;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.NotNull;
 
@Entity
public class Entry extends Model {
   @Id
   private Long id;
   @NotNull
   private String name;
   @NotNull
   private String title;
   @NotNull
   private String message;
   @Column(columnDefinition = "timestamp default current_timestamp")
   private Date createDate;

   //  public Entry(String name, String title, String message) {
   //      this.name = name;
   //      this.title = title;
   //      this.message = message;
   //  }
 
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
 
    // public String toString() {
    //     return "Parent [id=" + id + ", name=" + name + ", createDate="
    //             + createDate + ", updateDate=" + updateDate + "]";
    // }

}
