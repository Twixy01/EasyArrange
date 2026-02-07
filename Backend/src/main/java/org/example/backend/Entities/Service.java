package org.example.backend.Entities;


public class Service {

  private long id;
  private String name;
  private int price;
  private int duration;

  public Service(long id, String name, int price, int duration) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.duration = duration;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }


  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

}
