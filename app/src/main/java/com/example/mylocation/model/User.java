package com.example.mylocation.model;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * User Object to store the user details like username, password, preferences, user selected locations
 */
public class User implements Serializable {

    private String userId = UUID.randomUUID().toString();
    private String userName;
    private String password;
    private List<UserSelectedLocation> userSelectedLocations;

    /**
     * this method is used to fetch userId
     * @return - userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * this method is used to store userId
     * @param userId - userId to store for user
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * this method is used to fetch username
     * @return - username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * this method is used to store username
     * @param userName - username of user
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * this method is used to fetch password
     * @return - password of user
     */
    public String getPassword() {
        return password;
    }

    /**
     * this method is used to store password of user
     * @param password - password of user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * this method is used to fetch user selected locations
     * @return - list of user selected locations
     */
    public List<UserSelectedLocation> getUserSelectedLocations() {
        return userSelectedLocations;
    }

    /**
     * this method is used to store user selected locations
     * @param userSelectedLocations - list of user selected locations
     */
    public void setUserSelectedLocations(List<UserSelectedLocation> userSelectedLocations) {
        this.userSelectedLocations = userSelectedLocations;
    }

    /**
     * Paramterised contructor to create user object
     * @param userId - Id of user
     * @param userName - user name of user
     * @param password - password of user
     * @param userSelectedLocations - user selected lcoations
     */
    public User(String userId, String userName, String password, List<UserSelectedLocation> userSelectedLocations) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.userSelectedLocations = userSelectedLocations;
    }

    /**
     * Default constructor to create user object
     */
    public User(){}

    /**
     * The toString method to print user details
     * @return - string value of user details
     */
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", userSelectedLocations=" + userSelectedLocations +
                '}';
    }
}
