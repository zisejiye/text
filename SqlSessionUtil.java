package com.bjpowernode.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtil {
    //构造方法私有化，其他类中无发创建本类对象，本类静态方法只有通过类加载方式调用
    private SqlSessionUtil(){}

    private static SqlSessionFactory sqlSessionFactory ;
    static {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
         sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

    }
    private static ThreadLocal<SqlSession>  t = new ThreadLocal<>();
    //取得sqlsession对象
    public static SqlSession  getSession(){
        SqlSession session = t.get();
        if (session == null) {
            session = sqlSessionFactory.openSession();
            t.set(session);
        }
        return session;
    }
    //关闭sqlsession对象
    public static void  myClose(SqlSession session){
        if (session != null) {
            session.close();
            //分离线程池与连接
            t.remove();

        }
        
    }
}
