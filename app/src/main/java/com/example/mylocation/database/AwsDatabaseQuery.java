package com.example.mylocation.database;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import com.example.mylocation.utils.AsyncResponse;

public class AwsDatabaseQuery extends AsyncTask<String, Void, String> {
    private  Connection connection;
    private final String host = "testdatabasehost.us-west-2.rds.amazonaws.com";

    private final String database = "UserSelectedLocationData";
    private final String tableName = "Users";
    private final int port = 5432;
    private final String user = "testuser";
    private final String password = "testpassword";
    private String url = "jdbc:postgresql://%s:%d/%s";
    private static String res;
    public AsyncResponse delegate = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String userName = strings[0];
        try {
            this.url = String.format(this.url, this.host, this.port, this.database);
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);

            PreparedStatement st = connection.prepareStatement("SELECT user_locations FROM "+ tableName +" WHERE user_name = ?");
            st.setString(1, userName);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                res = rs.getString(1);
            }
            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
            res = e.getMessage();
        }
        return res;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

    public void setDelegate(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }
}
