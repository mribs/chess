package model;

public record UserData(String username, String password, String email) {
    //    constructor to hopefully allow to login
    public UserData(String username, String password) {
        this(username, password, null);
    }
}
