package org.example.backend.Entities;


public class Role {

    private long role_id;
    private String name;

    public Role(long role_id, String name) {
        this.role_id = role_id;
        this.name = name;
    }

    public long getRole_id() {
        return role_id;
    }

    public void setRole_id(long id) {
        this.role_id = role_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
