package cs578_topic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

 
public class JDBCMySQLDemo {
    public static void main(String[] args) throws ClassNotFoundException { 
    	System.out.println("inside main");
    }
    
    public void updateTable(String name,int msg) throws ClassNotFoundException{
    	
    	
    	
//        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Enter the EmployeeID:");
//         
//        int employeeId;
//        try {
//            employeeId = Integer.parseInt(br.readLine());
//            JDBCMySQLDemo demo = new JDBCMySQLDemo();
//            Employee employee = demo.getEmployee(employeeId);
//            System.out.println(employee);           
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }       
//    }
 
//    public Employee getEmployee(int employeeId)  {      
        ResultSet rs = null;
        Connection con = null;
        Statement statement = null; 
         
        //Employee employee = null;
        String query1 = "set sql_safe_updates=0";
        String query = "update consumerlist set producer2_access="+msg+" where consumer_name=\""+name+"\"";//"SELECT * FROM emp WHERE emp_id=" + msg;
        //String query = "SELECT * FROM consumerlist;";
        	try{  
			Class.forName("com.mysql.jdbc.Driver");  
			con= DriverManager.getConnection(  
			"jdbc:mysql://localhost/sys","root","cs578");  
			//here sonoo is database name, root is username and password  
			Statement stmt=con.createStatement();  
//			String q2 = "show tables";
//			rs = stmt.executeQuery(q2);
//			while(rs.next()){
//				//System.out.println("printingfirst" + rs.getString(1) + "  " + rs.getString(2) + " " + rs.getString(3));
//				System.out.println(rs.getString(1));
//			}
			rs=stmt.executeQuery(query1);  
			int a = stmt.executeUpdate(query);
			
			
//			if(!rs.next()){
				System.out.println(a);
//			}
//				String q1 = "select * from consumerlist";
//				rs = stmt.executeQuery(q1);
//				while(rs.next()){
//					System.out.println("printingf" + rs.getString(3));
//				}
				con.close();
			
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
//        return employee;
   }
    
    public boolean checkAccess(String value){
    	 ResultSet rs = null;
         Connection connection = null;
         Statement statement = null; 
          boolean va=false;
         //Employee employee = null;
        // String query1 = "set sql_safe_updates=0";
         String query = "select producer2_access from consumerList where consumer_name=\""+value+"\"";//"update ConsumerList set producer2_access="+msg+" where consumer_name=\""+name+"\"";//"SELECT * FROM emp WHERE emp_id=" + msg;
         try {           
             connection = JDBCMySQLConnection.getConnection();
             statement = connection.createStatement();
             rs = statement.executeQuery(query);
             System.out.println(rs);
            if(rs.next()) {
                  String a = rs.getString(1);
                 System.out.println("rs = "+a);
                 if(a.equals("1"))
                 {
                	 va=true;
                 }
                 else
                 {
                 	va=false;
                 }
             }
           
             
             
         } catch (SQLException e) {
             e.printStackTrace();
         } finally {
             if (connection != null) {
                 try {
                     connection.close();
                 } catch (SQLException e) {
                     e.printStackTrace();
                 }
             }
         }
    	return va;
    }
}	