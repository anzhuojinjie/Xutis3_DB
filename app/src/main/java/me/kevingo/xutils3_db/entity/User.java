package me.kevingo.xutils3_db.entity;

import org.xutils.DbManager;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import org.xutils.ex.DbException;

import java.util.Date;
import java.util.List;

/**
 * Created by Joe on 2017/01/11.
 */
@Table(name = "user",
    onCreated = "CREATE UNIQUE INDEX realative_unique ON user(NAME, EMAIL)") //为表创建NAME,EMAIL联合唯一索引
public class User {
    @Column(
            name = "ID",
            isId = true,
            autoGen = true
    )
    private int id;
    @Column(name = "NAME",property = "NOT NULL")//NAME字段非空
    private String name;

    @Column(name = "EMAIL",property = "NOT NULL")
    private String email;

    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "REGTIME")
    private Date regTime;

    public User(){

    }
    public User(String name, String email, String mobile,Date regTime){
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.regTime = regTime;
    }

    public List<Order> getOrders(DbManager db) throws DbException {
        return db.selector(Order.class).where("USERID", "=", this.id).findAll();
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public Date getRegTime() {
        return regTime;
    }
    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }
    public String toString(){
        return "{id="+id+",name="+name+",email="+email+",mobile="+mobile+",regTime="+regTime+"}";
    }
}
