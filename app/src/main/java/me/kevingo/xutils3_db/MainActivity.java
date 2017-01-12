package me.kevingo.xutils3_db;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.kevingo.xutils3_db.entity.Order;
import me.kevingo.xutils3_db.entity.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDb();

        try {
            this.dbAdd();
            this.dbFind();
            this.dbDelete();
            this.dbUpdate();
            this.one2Money();
        } catch (DbException e) {
            showDbMessage(e.getMessage() + "\n" + e.getCause());
            e.printStackTrace();
        }
    }

    protected void dbAdd() throws DbException {
        //User user = new User("张林周","963893628@qq.com","1888888888",new Date());
        //db.save(user);//保存成功之后【不会】对user的主键进行赋值绑定
        //db.saveOrUpdate(user);//保存成功之后【会】对user的主键进行赋值绑定
        //db.saveBindingId(user);//保存成功之后【会】对user的主键进行赋值绑定,并返回保存是否成功

        List<User> users = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            //User的@Table注解onCreated属性加了name,email联合唯一索引.
            User user = new User("张林周" + System.currentTimeMillis()+i, "963893628@qq.com", "1888888888"+i, new Date());
            users.add(user);
        }
        db.saveBindingId(users);
        showDbMessage("【dbAdd】第一个对象:" + users.get(0).toString());//user的主键Id不为0
        Log.i("123", "dbAdd: "+"【dbAdd】第一个对象:" + users.get(0).toString());
    }

    protected void dbFind() throws DbException {
        //List<User> users = db.findAll(User.class);
        //showDbMessage("【dbFind#findAll】第一个对象:"+users.get(0).toString());

        //User user = db.findById(User.class, 1);
        //showDbMessage("【dbFind#findById】第一个对象:" + user.toString());

        //long count = db.selector(User.class).where("name","like","%林周%").and("email","=","963893628@qq.com").count();//返回复合条件的记录数
        //showDbMessage("【dbFind#selector】复合条件数目:" + count);

        List<User> users = db.selector(User.class)
                .where("name","like","%林周%")
                .and("email", "=", "963893628@qq.com")
                .orderBy("regTime",true)
                .limit(2) //只查询两条记录
                .offset(2) //偏移两个,从第三个记录开始返回,limit配合offset达到sqlite的limit m,n的查询
                .findAll();
        if(users == null || users.size() == 0){
           return;//请先调用dbAdd()方法
        }
        showDbMessage("【dbFind#selector】复合条件数目:" + users.size());
        Log.i("123", "dbFind: "+"【dbFind#selector】复合条件数目:" + users.size());
    }

    protected void dbDelete() throws DbException {
        List<User> users = db.findAll(User.class);
        if(users == null || users.size() == 0){
            return;//请先调用dbAdd()方法
        }
        //db.delete(users.get(0)); //删除第一个对象
        //db.delete(User.class);//删除表中所有的User对象【慎用】
        //db.delete(users); //删除users对象集合
        //users =  db.findAll(User.class);
        // showDbMessage("【dbDelete#delete】数据库中还有user数目:" + users.size());

        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("id",">","5").or("id","=","1").expr(" and mobile > '2015-12-29 00:00:01' ");
        db.delete(User.class, whereBuilder);
        users =  db.findAll(User.class);
        showDbMessage("【dbDelete#delete】数据库中还有user数目:" + users.size());
        Log.i("123", "dbDelete: "+"【dbDelete#delete】数据库中还有user数目:" + users.size());
    }

    protected void dbUpdate() throws DbException {
        List<User> users = db.findAll(User.class);
        if(users == null || users.size() == 0){
            return;//请先调用dbAdd()方法
        }
        User user = users.get(0);
        user.setEmail(System.currentTimeMillis() / 1000 + "@qq.com");
        //db.replace(user);
        //db.update(user);
        //db.update(user,"email");//指定只对email列进行更新

        WhereBuilder whereBuilder = WhereBuilder.b();
        whereBuilder.and("id",">","5").or("id","=","1").expr(" and mobile > '2015-12-29 00:00:01' ");
        db.update(User.class, whereBuilder,
                new KeyValue("email", System.currentTimeMillis() / 1000 + "@qq.com")
                , new KeyValue("mobile", "18988888888"));//对User表中复合whereBuilder所表达的条件的记录更新email和mobile
    }

    protected void one2Money() throws DbException {
        User user = db.findById(User.class,1);
        if(user == null){
            user = new User("Kevingo" + System.currentTimeMillis(), "xutis@gmail.com", "188888882", new Date());
            db.saveBindingId(user);
        }
        for (int i=0;i<5;i++){
            Order order = new Order();
            long timeStamp = System.currentTimeMillis() / 1000;
            order.setNumber(timeStamp + "");
            order.setSubject("this is a oder-->" + timeStamp);
            order.setUserId(user.getId());
            db.save(order);
        }
        List<Order> orders = user.getOrders(db);
        showDbMessage("共有订单"+orders.size()+"个");
        Log.i("123", "one2Money: "+"共有订单"+orders.size()+"个");
    }

    private void showDbMessage(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("xUitls3 Db").setMessage(message)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialogBuilder.create().show();
    }

    protected DbManager db;

    protected void initDb() {
        //本地数据的初始化
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("xutils3_db") //设置数据库名
                .setDbVersion(1) //设置数据库版本,每次启动应用时将会检查该版本号,发现数据库版本低于这里设置的值将进行数据库升级并触发DbUpgradeListener
                .setAllowTransaction(true)//设置是否开启事务,默认为false关闭事务
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {
                        //balabala...
                    }
                })//设置数据库创建时的Listener
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        //balabala...
                    }
                });//设置数据库升级时的Listener,这里可以执行相关数据库表的相关修改,比如alter语句增加字段等
        //.setDbDir(null);//设置数据库.db文件存放的目录,默认为包名下databases目录下
        db = x.getDb(daoConfig);
    }
}
