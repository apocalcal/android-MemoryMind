package com.example.androidproject1;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class User {
    private String name;
    private String birth;
    private String username;
    private String password;

    // Constructor
    public User(String name, String birth, String username, String password) {
        this.name = name;
        this.birth = birth;
        this.username = username;
        this.password = password;
    }

    // Getter and Setter methods
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBirth() { return birth; }
    public void setBirth(String birth) { this.birth = birth; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
