package com.example.mylocation.database;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.example.mylocation.utils.AsyncResponse;

public class AwsDatabaseUpdate extends AsyncTask<String, Void, String> {
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

    String curUser;
    String newLocations;

    public AwsDatabaseUpdate(String u, String l) {
        this.curUser = u;
        this.newLocations = l;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            this.url = String.format(this.url, this.host, this.port, this.database);
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);

            PreparedStatement st = connection.prepareStatement("UPDATE "+ tableName +" SET user_locations = ? WHERE user_name = ?");
            st.setString(1, newLocations);
            st.setString(2,curUser);

            int rs = st.executeUpdate();
            res = "Updated";
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
            res = e.getMessage();
        }
        return res;
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processUpdated(result.equals("Updated"));
    }

    public void setDelegate(AsyncResponse asyncResponse) {
        delegate = asyncResponse;
    }
}
