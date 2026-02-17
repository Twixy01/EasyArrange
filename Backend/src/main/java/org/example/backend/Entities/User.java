package org.example.backend.Entities;


public class User {

  private long user_id;
  private String name;
  private String email;
  private String profilePicture;
  private String password;
  private long roleId;

  public User(long user_id, String name, String email, String profilePicture, String password, long roleId) {
    this.user_id = user_id;
    this.name = name;
    this.email = email;
    this.profilePicture = profilePicture;
    this.password = password;
    this.roleId = roleId;
  }

  public User(String name, String email, String profilePicture, String password, long roleId) {
    this.name = name;
    this.email = email;
    this.profilePicture = profilePicture;
    this.password = password;
    this.roleId = roleId;
  }

  public long getUser_id() {
    return user_id;
  }

  public void setUser_id(long user_id) {
    this.user_id = user_id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }


  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


  public long getRoleId() {
    return roleId;
  }

  public void setRoleId(long roleId) {
    this.roleId = roleId;
  }

  @Override
  public String toString() {
    return "User{" +
            "id=" + user_id +
            ", name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", profilePicture='" + profilePicture + '\'' +
            ", password='" + password + '\'' +
            ", roleId=" + roleId +
            '}';
  }
}
