package org.example.backend.Entities;


public class Booking {

  private long id;
  private long staffId;
  private long customerId;
  private java.sql.Timestamp startDatetime;
  private java.sql.Timestamp endDatetime;
  private long serviceId;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public long getStaffId() {
    return staffId;
  }

  public void setStaffId(long staffId) {
    this.staffId = staffId;
  }


  public long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(long customerId) {
    this.customerId = customerId;
  }


  public java.sql.Timestamp getStartDatetime() {
    return startDatetime;
  }

  public void setStartDatetime(java.sql.Timestamp startDatetime) {
    this.startDatetime = startDatetime;
  }


  public java.sql.Timestamp getEndDatetime() {
    return endDatetime;
  }

  public void setEndDatetime(java.sql.Timestamp endDatetime) {
    this.endDatetime = endDatetime;
  }


  public long getServiceId() {
    return serviceId;
  }

  public void setServiceId(long serviceId) {
    this.serviceId = serviceId;
  }

}
