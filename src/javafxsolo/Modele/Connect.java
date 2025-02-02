package javafxsolo.Modele;
import javafxsolo.Utils.AccesLocal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
/*
 * cette class g�re principalement tout de qui est connection avec la database pouvant etre d�tach� du code principale
 */
public class Connect {
	//static Connection connection = null;
	static Connection connectionSqlite = null;
	static int idconnect ;
	

		
	public static int idconnect() {
		return idconnect;
	}
	
	//return object type connection off sqlite database 
	public static Connection conSqlite (){
		return AccesLocal.getInstance();
	}


	/**
	 * test login(if username and user password is in database)
	 * @param conn
	 * @param login
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static boolean dataTest(Connection conn,String login, String password) throws Exception{
		
		
			while(true) {
				String strSql = "SELECT * FROM T_Users WHERE login=?  AND password= ?;";
				try( PreparedStatement statement = conn.prepareStatement( strSql ) ) { 
					statement.setString(1, login);
					statement.setString(2, password);
					try(ResultSet  resultSet= statement.executeQuery() ){
						
							if(resultSet.next()) {
								strSql = "UPDATE T_Users SET  nbConnect= nbConnect+1 WHERE  IdUser=? ;";
								try( PreparedStatement stUpdate = conn.prepareStatement( strSql ) ) { 
									idconnect= resultSet.getInt(1);
									stUpdate.setInt(1, resultSet.getInt(1));
									
									stUpdate.executeUpdate();
								}
								catch (Exception e) {
						            System.err.println("ConnectionUtil : "+e.getMessage());
								}
								break;
							 }
							return false;
						}
			
					}
					
				}
			
			return true;
			
	}

	/**
	 * get prog version
	 * @return
	 */
	public static String progV () {
		 String sql = "SELECT * FROM T_prog_perf;";
		 try(Connection connection =conSqlite();Statement statement = connection.createStatement();ResultSet  resultSet= statement.executeQuery(sql ) ){
			 while(resultSet.next()) {
					return resultSet.getString(5);
					
				}
		 } catch (SQLException ex) {
			 System.err.println(ex.getMessage());
			 return "";
		 }
		return "";
	}

	/**
	 * define if is first launch or not
	 * @return
	 */
	//
	public static boolean fristLaunchTest () {
		 String sql = "SELECT * FROM T_prog_perf Where progFirstlaunch = ? ";
         try (Connection conn =conSqlite() ; PreparedStatement preparedStatement = conn.prepareStatement(sql)){
   
             preparedStatement.setString(1, "true");
             
             try(ResultSet resultSet = preparedStatement.executeQuery() ){
	             if (!resultSet.next()) {
	            	 return false;
	             } else {
	            	 return true;
	             }
             }
         } catch (SQLException ex) {
             System.err.println(ex.getMessage());
             return false;
         }
	}

	/**
	 * update database after first launch
	 */
	public static void fristLaunchD() {
		String strSql = "UPDATE T_prog_perf SET  progFirstlaunch=false WHERE  id_prog_perf=? ;";
		try( Connection conn =conSqlite() ; PreparedStatement  preparedStatement = conn.prepareStatement( strSql ) ) { 
			 preparedStatement.setInt(1, 1);
			 preparedStatement.executeUpdate();
		}  catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
	}
	public static void updateNbSave(Connection conn ,int userid) {
		String strSql = "UPDATE T_save SET  nb= nb+1 WHERE  saveAuthor=? ;";
		try(PreparedStatement  preparedStatement = conn.prepareStatement( strSql ) ) {
			preparedStatement.setInt(1,userid );
			preparedStatement.executeUpdate();
		}  catch (SQLException ex) {
			System.err.println(ex.getMessage());

		}
	}
	/**
	 * questionList
	 * @return
	 */
	public static List<String> questionList() {
		String strSql = "SELECT * FROM T_Question ;";
		List<String> tab = new ArrayList<>();
			try(Connection connection =conSqlite();Statement statement = connection.createStatement();ResultSet  resultSet= statement.executeQuery(strSql ) ){
						while(resultSet.next()) {
							//int idUser = resultSet.getInt(1);
							String textQ = resultSet.getString(2);
							tab.add(textQ);
						}
				return tab;
			} catch (SQLException ex) {
	            System.err.println(ex.getMessage());
	            return null;
	        }
			
		
	}

	/**
	 *
	 * @param Question
	 * @return
	 */
	public static int questionId(String Question){
		int retour = 0;
		try( Connection conn =conSqlite() ){
			String strSql = "SELECT * FROM T_Question WHERE questionText=? ;";
			while(true) {
				try( PreparedStatement statement = conn.prepareStatement( strSql ) ) { 
					statement.setString(1, Question);
					
					try(ResultSet  resultSet= statement.executeQuery() ){
							if(resultSet.next()) {
								retour = resultSet.getInt(1);
								break;
							 }
							return 0;
						}
			
					}
					
				}
			return retour;
		}catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return 0;
        }
	}

	/**
	 * test if username exit
	 * @param username
	 * @return
	 */
	public static boolean userExist(String username){
		try( Connection conn =conSqlite() ){
			String strSql = "SELECT * FROM T_Users WHERE login=? ;";
			while(true) {
				try( PreparedStatement statement = conn.prepareStatement( strSql ) ) { 
					statement.setString(1, username);
					try(ResultSet  resultSet= statement.executeQuery() ){
							if(resultSet.next()) {
								break;
							 }
							return false;
						}
			
					}
					
				}
			return true;
		}catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return true;
        }
	} 
}