package org.example.backend.Entities;


public class Service {

  private long service_id;
  private String name;
  private int price;
  private int duration;

  public Service(long service_id, String name, int price, int duration) {
    this.service_id = service_id;
    this.name = name;
    this.price = price;
    this.duration = duration;
  }

  public Service(String name, int price, int duration) {
    this.name = name;
    this.price = price;
    this.duration = duration;
  }

  public long getService_id() {
    return service_id;
  }

  public void setService_id(long service_id) {
    this.service_id = service_id;
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
