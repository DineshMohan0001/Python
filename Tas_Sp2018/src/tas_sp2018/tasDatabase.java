/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tas_sp2018;

import java.sql.*;
/**
 *
 * @author Andrew
 */
public class tasDatabase {
    public static void main(String[] args) {
        try{
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://localhost:3306/tas";
        Connection con = DriverManager.getConnection(url , "root", "Te#4RoPa");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM badge Where id = '3282F212'");
        if (rs != null){
            rs.next();
            String id = rs.getString("id");
            String desc = rs.getString("description");
        }
        rs.close();
        stmt.close();
        con.close();
        }
        catch(Exception e){System.out.println(e);}
    }
}
