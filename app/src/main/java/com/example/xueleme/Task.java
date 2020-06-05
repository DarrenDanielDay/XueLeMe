package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/*
* Task可以看成两种角色：一种是只传入了User和上下文，这个时候它是用户的任务工具
* 一种是传入了各种参数，这个时候就是具体的任务
* 在进行增删改查的时候也会修改Task对象的值
* */
public class Task {
  private Users users;//用户
  private int year;//年份
  private int month;//几月
  private int day;//几号
  private int start_hour;//开始时间小时,24小时制
  private int start_min;//开始时间分钟
  private int end_hour;//结束时间小时
  private int end_min;//结束时间分钟
  private String task_name;//任务名
  private int state;//状态
  private DB_Tools my_tool;
  //构造函数，接收上下文环境和用户类
  public Task(Context context, Users users) {
        this.users = users;
        my_tool=new DB_Tools(context,users.getAccount());
    }
    //任务的日期设置
    public void set_data(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
    //将任务插入数据库中
    private String insert()
    {
        String data=year+"-"+month+"-"+day;
        String start_time=start_hour+":"+start_min;
        String end_time=end_hour+":"+end_min;
        int last;
        if((end_hour-start_hour)*60+(end_min-start_min)<0)
            return "时间不合法";
        //获取系统时间,并和当前时间比较
        Calendar calendar=Calendar.getInstance();
        int s_year=calendar.get(Calendar.YEAR);
        int s_month = calendar.get(Calendar.MONTH)+1;
        int s_day = calendar.get(Calendar.DAY_OF_MONTH);
        int s_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int s_minute = calendar.get(Calendar.MINUTE);
        if(s_year>year)
            return "不可创建过去时间的任务";
        else if(s_year==year)
        {
            if(s_month>month)
                return "不可创建过去时间的任务";
            else if(s_month==month)
            {
                if(s_day>day)
                    return "不可创建过去时间的任务";
                else if(s_day==day)
                {
                    if(s_hour>start_hour)
                        return "不可创建过去时间的任务";
                    else if(s_hour==start_hour)
                    {
                        if(s_minute>start_min)
                            return "不可创建过去时间的任务";
                    }
                }
            }
        }
        SQLiteDatabase sqLiteDatabase=my_tool.getWritableDatabase();
        String query="select * from calendar";
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        int tag=0;
        while(cursor.moveToNext())
        {
            int index1=cursor.getColumnIndex("data");
            int index2=cursor.getColumnIndex("task");
            String ex_data=cursor.getString(index1);
            String ex_task=cursor.getString(index2);
            //Log.d("Mainacitivity",ex_data+" "+ex_task);
            if(ex_data.equals(data) && ex_task.equals(task_name))
            {tag=1;
            break;}
        }
        cursor.close();
        if(tag==1)
        {
            sqLiteDatabase.close();
            return "任务已存在";
        }
        final String sql="insert into calendar(data,task,start_time,end_time) values(?,?,?,?)";
        //Log.d("Mainacitivity",data+" "+task_name+" "+state+" "+start_time+" "+end_time);
       sqLiteDatabase.execSQL(sql,new Object[]{data,task_name,start_time,end_time});
        sqLiteDatabase.close();
        return "创建成功";
    }
     //另一种构造函数
    public Task(Users users, Context context,int year, int month, int day, int start_hour, int start_min, int end_hour, int end_min, String task_name) {
        this.users = users;
        this.year = year;
        this.month = month;
        this.day = day;
        this.start_hour = start_hour;
        this.start_min = start_min;
        this.end_hour = end_hour;
        this.end_min = end_min;
        this.task_name = task_name;
        my_tool=new DB_Tools(context,users.getAccount());
    }
    //get方法

    public Users getUsers() {
        return users;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getStart_hour() {
        return start_hour;
    }

    public int getStart_min() {
        return start_min;
    }

    public int getEnd_hour() {
        return end_hour;
    }

    public int getEnd_min() {
        return end_min;
    }

    public String getTask_name() {
        return task_name;
    }
    //set方法

    public void setStart_hour(int start_hour) {
        this.start_hour = start_hour;
    }

    public void setStart_min(int start_min) {
        this.start_min = start_min;
    }

    public void setEnd_hour(int end_hour) {
        this.end_hour = end_hour;
    }

    public void setEnd_min(int end_min) {
        this.end_min = end_min;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }
    /*
   用户创建任务，传输参数：年、月、日、开始的小时、开始的分钟、结束的小时、结束的分钟、任务名
   返回的是任务创建的成功与否：
   1.若用户时间不合法，开始时间大于结束时间，返回“时间不合法”
   2.若用户的任务已创建过，返回“任务已存在”
   3.若用户创建的任务小于系统时间，返回“不可创建过去时间的任务”
   4.创建成功
   */
    public String create(int year, int month, int day, int start_hour, int start_min, int end_hour, int end_min,  String task_name) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.start_hour = start_hour;
        this.start_min = start_min;
        this.end_hour = end_hour;
        this.end_min = end_min;
        this.task_name = task_name;
        return this.insert();
    }
    //任务的删除,传入年，月，日和任务名
    public String delete(int year, int month, int day, String task_name)
    {
        this.year = year;
        this.month = month;
        this.day = day;
        this.task_name = task_name;
        String data=year+"-"+month+"-"+day;
        SQLiteDatabase sqLiteDatabase=my_tool.getWritableDatabase();
        int tag=sqLiteDatabase.delete("calendar","data=? and task=?",new String[]{data,task_name});
        if(tag==1)
        return "删除成功";
        else
            return "删除失败";
    }
    //任务的修改,传入的参数是5个,将Task里原本的参数替换成updata传入的，同时将数据库中对应的值进行修改
    //用户应该只能修改任务名、开始时间和结束时间
    /*
      updata在使用的时候需要注意的是要清楚Task对象的原本的值，updata修改的是task里原本的值
     */
    public String updata(int start_hour, int start_min, int end_hour, int end_min,  String task_name)
    {
        String old_task_name=this.task_name;
        String data=year+"-"+month+"-"+day;
        String start_time=start_hour+":"+start_min;
        String end_time=end_hour+":"+end_min;
        //先判断修改后的任务在不在数据库里
        SQLiteDatabase sqLiteDatabase=my_tool.getWritableDatabase();
        String query="select * from calendar";
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        int tag=0;
        while(cursor.moveToNext())
        {
            int index1=cursor.getColumnIndex("data");
            int index2=cursor.getColumnIndex("task");
            String ex_data=cursor.getString(index1);
            String ex_task=cursor.getString(index2);
           // Log.d("Mainacitivity",ex_data+" "+ex_task);
            if(ex_data.equals(data) && ex_task.equals(task_name))
            {tag=1;
                break;}
        }
        cursor.close();
        if(tag==1)
            return "修改后的任务已存在";
        //进行修改
        ContentValues values=new ContentValues();
        values.put("task",task_name);
        values.put("start_time",start_time);
        values.put("end_time",end_time);
        tag=sqLiteDatabase.update("calendar",values,"data=? and task=?",new String[]{data,old_task_name});
        sqLiteDatabase.close();
        if(tag==1)
            return "修改成功";
        else
            return "修改失败";
    }
    //查询，这个是用于显示的，给定年、月、日，显示当日的所有任务，每个任务的显示格式是这样的：
    //任务名 开始时间--结束时间
    //返回的是一个List<String>
    public List<String> query(int year, int month, int day)
    {
        List<String> task=new ArrayList<>();
        SQLiteDatabase sqLiteDatabase=my_tool.getWritableDatabase();
        String query="select * from calendar";
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        while(cursor.moveToNext())
        {
            int index1=cursor.getColumnIndex("task");
            String task_n=cursor.getString(index1);
            int index2=cursor.getColumnIndex("start_time");
            String task_s=cursor.getString(index2);
            int index3=cursor.getColumnIndex("end_time");
            String task_e=cursor.getString(index3);
            String task_message=task_n+" "+task_s+"--"+task_e;
            task.add(task_message);
        }
        cursor.close();
        sqLiteDatabase.close();
        return task;
    }
}
