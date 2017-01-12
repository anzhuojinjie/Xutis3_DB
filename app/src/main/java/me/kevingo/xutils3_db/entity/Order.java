package me.kevingo.xutils3_db.entity;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

/**
 * Created by Joe on 2017/01/11.
 */
@Table(name = "order")
public class Order {
    @Column(name = "ID", isId = true, autoGen = true)
    private int id;
    @Column(name = "NUMBER")
    private String number;
    @Column(name = "SUBJECT")
    private String subject;
    @Column(name = "USERID")
    private int userId;
    public User getUser(DbManager db) throws DbException {
        return db.findById(User.class, userId);
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    @Override
    public String toString() {
        return this.getClass().getName() + "{id=" + id + ",number=" + number + ",subject=" + subject + ",userId=" + userId + "}";
    }
}
