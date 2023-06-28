package db;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by forest.
 */
public class DBManager {
    private Statement stmt;
    private int moves=0;
    private final int rows = 3;
    private final int columns = 3;
    private final int[][] puzzle = new int[rows][columns];

    //private boolean solved=false;
    public DBManager() {
        connect();
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_users", "root", "");
            stmt = con.createStatement();
        } catch(Exception ex) {
            System.out.println("eroare la connect:"+ex.getMessage());
            ex.printStackTrace();
        }
    }

    public ArrayList<Integer> random_list()
    {
        ArrayList<Integer> numberList = new ArrayList<>();
        for (int i = 0; i <= 8; i++) {
            numberList.add(i);
        }
        Random random = new Random();
        ArrayList<Integer> pickedNumbers = new ArrayList<>();
        while (pickedNumbers.size() < 9) {
            int randomNumber = random.nextInt(9);
            if (!pickedNumbers.contains(randomNumber)) {
                pickedNumbers.add(randomNumber);
            }
        }

        return pickedNumbers;
    }
    public String getPuzzle(int userId) {
        StringBuilder res = new StringBuilder();
        ResultSet rs;
        try {
            rs = stmt.executeQuery("SELECT score FROM users WHERE id ='"+userId+"'");
            if (rs.next()) {
                moves = rs.getInt("score");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        res.append("<p>Moves: ").append(moves).append("</p>");

        try {
            rs = stmt.executeQuery("SELECT * FROM puzzle where user_id ='"+userId+"'");
            int[] where = new int[rows  * columns ];
            while (rs.next()) {

                where[rs.getInt("position")] = rs.getInt("id");
            }

            int count  = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    this.puzzle[i][j] = where[count];
                    System.out.println("puzzle[" + i + "][" + j + "]=" + where[count]);
                    count ++;
                }
            }
            boolean solved = true;

            for (int i = 0; i < rows  * columns ; i++) {

                if (where[i] % 9 != i)
                    solved = false;
                res.append("<img id = '").append(where[i]).append("' class='puzzle_piece' src='img/image_part_00").append(where[i]%9+1).append(".jpg'/>");


            }
            if (solved) {
                res.append("<p style=\"color: green\">Congratulations, you finished the puzzle in: ").append(this.moves).append(" moves!</p>");
                res.append("<link rel='stylesheet' type='text/css' href='lock.css'>");
                res.append("<form class=\"reset\" action=\"Controller\" method=\"post\">\n" +
                        "        <input   type=\"submit\" value=\"Reset the puzzle\" id=\"reset\"/>\n" +
                        "    </form>");
            }
        } catch (Exception ex) {
            System.out.println("Error on get Puzzle: " + ex.getMessage());
        }
        return res.toString();
    }

    public void swap(int id1, int id2, int userId) {
        StringBuilder res = new StringBuilder();
        ResultSet rs;
        System.out.println(id1);
        System.out.println(id2);
        this.getPuzzle(userId);
        int x_id1 = 0, x_id2 = 0, y_id1 = 0, y_id2 = 0;

        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.println(puzzle[i][j]);
            }
        }
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                if(this.puzzle[i][j] == id1){
                    x_id1 = i;
                    y_id1 = j;
                }
                if(this.puzzle[i][j] == id2){
                    x_id2 = i;
                    y_id2 = j;
                }
            }
        }

        int poz1=-1;
        int poz2=-1;



        if (x_id1!= x_id2 || y_id1!=y_id2) {
            try {
                rs = stmt.executeQuery("SELECT position FROM puzzle WHERE id ='" + id1 + "'");
                if (rs.next()) {
                    poz1 = rs.getInt("position");
                }

                rs = stmt.executeQuery("SELECT position FROM puzzle WHERE id ='" + id2 + "'");
                if (rs.next()) {
                    poz2 = rs.getInt("position");
                }
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


            try {

                stmt.executeUpdate("UPDATE puzzle SET position ='" + poz2 + "' WHERE id='" + id1 + "'");
                stmt.executeUpdate("UPDATE puzzle SET position ='" + poz1 + "' WHERE id='" + id2 + "'");
                stmt.executeUpdate("UPDATE users SET score=score+1 WHERE id ='" + userId + "'");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
    public void resetGame(int userId) {

        ResultSet rs ;
        try {

            stmt.executeUpdate("UPDATE users SET score=0 WHERE id ='"+userId+"'");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {

            stmt.executeUpdate("DELETE FROM puzzle where user_id ='"+userId+"'");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ArrayList<Integer> random_poz=random_list();
            System.out.println(random_poz);
            for (int i=0;i<=8;i++)
            {
                stmt.executeUpdate("INSERT INTO puzzle (position,user_id) VALUES ('"+ random_poz.get(i) +"','"+ userId+"')");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public User authenticate(String username, String password) {
        ResultSet rs;
        User u = null;
        System.out.println(username+" "+password);
        try {
            rs = stmt.executeQuery("select * from users where username='"+username+"' and password='"+password+"'");
            if (rs.next()) {
                u = new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

}