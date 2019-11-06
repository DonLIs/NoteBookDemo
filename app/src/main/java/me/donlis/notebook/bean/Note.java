package me.donlis.notebook.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Note {

    @Id(autoincrement = true)
    private Long id;

    private String title;

    private String content;

    private String describe;

    private String dateTime;


    @Generated(hash = 721867085)
    public Note(Long id, String title, String content, String describe,
            String dateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.describe = describe;
        this.dateTime = dateTime;
    }

    @Generated(hash = 1272611929)
    public Note() {
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
