package com.example;


import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class MyTask {
  public static void main(String[] a)
      throws Exception {
    Class.forName("org.h2.Driver");
    Connection conn = DriverManager.
        getConnection("jdbc:h2:mem:", "sa", "");
    // add application code here
    Statement stmt = null;
    stmt = conn.createStatement();
    String sql = "CREATE TABLE ZJCSJTU " +
        "(number DOUBLE, sum DOUBLE)";
    stmt.executeUpdate(sql);

    double sum = 0.0;
    // insert
    for (int i = 0; i < 100000; i++) {
      if ( i % 1000 == 0) {
        sum = 0.0;
      }
      Double num = sndGenerator();
      sum += num;
//      System.out.println(num);
      sql = "INSERT INTO ZJCSJTU " +"VALUES (" + Double.toString(num) + "," + Double.toString(sum) +")";
      stmt.executeUpdate(sql);
    }


    PreparedStatement selectPreparedStatement = null;
    // One single query
    sql = "SELECT sum FROM ZJCSJTU";
    selectPreparedStatement = conn.prepareStatement(sql);
    ResultSet rs = selectPreparedStatement.executeQuery();
    int index = 0;

    // compute mean of 1000 values
    ArrayList<Double> means = new ArrayList<Double>();
    while (rs.next()) {
      if ((index + 1)%1000 == 0) {
        means.add(rs.getDouble("sum")/1000);
      }
      index++;
    }
    selectPreparedStatement.close();

    conn.close();

    System.out.println(standardDeviation(means));



  }

  public static double sndGenerator() {
    Random r = new Random();
    return r.nextGaussian();
  }

  public static double standardDeviation(ArrayList<Double> means) {
    double mean = 0.0;
    for (int i = 0; i < means.size(); i++) {
      mean += means.get(i);
    }
    mean /= means.size();

    double squeareOfDeviation = 0.0;
    for (int i = 0; i < means.size(); i++) {
      squeareOfDeviation += (means.get(i) - mean) * (means.get(i) - mean);
    }

    return (Math.sqrt(squeareOfDeviation/means.size()));

  }

}
