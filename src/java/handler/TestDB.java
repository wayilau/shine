package handler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


public class TestDB {

    String url = "jdbc:mysql://10.154.12.125:3306/cis_auth";

    String driver = "com.mysql.cj.jdbc.Driver";

    String username = "root";
    String pw = "8cDcos11!";

    String sql = "select * from ldap_user where username = 'e_kcs'";

    public static void main(String[] args) throws Exception {
        TestDB db = new TestDB();
        db.test();
    }

    public void test() throws Exception {
        Class.forName(driver);

        Connection conn = DriverManager.getConnection(url, username, pw);
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            System.out.println(rs.getString(2));
        }
    }
}
