package org.example.backend.Entities;


public class Service {

  private long id;
  private String name;
  private long price;
  private long duration;

  public Service(long id, String name, long price, long duration) {
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


  public long getPrice() {
    return price;
  }

  public void setPrice(long price) {
    this.price = price;
  }


  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

}
