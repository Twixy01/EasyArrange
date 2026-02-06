package org.example.backend.Entities;

import java.time.LocalDateTime;

public class Booking {

  private long id;
  private long staffId;
  private long customerId;
  private LocalDateTime startDatetime;
  private LocalDateTime endDatetime;
  private long serviceId;

  public Booking(long id, long staffId, long customerId, LocalDateTime startDatetime, LocalDateTime endDatetime, long serviceId) {
    this.id = id;
    this.staffId = staffId;
    this.customerId = customerId;
    this.startDatetime = startDatetime;
    this.endDatetime = endDatetime;
    this.serviceId = serviceId;
  }

  public Booking(long staffId, long customerId, java.time.LocalDateTime startDatetime, java.time.LocalDateTime endDatetime, long serviceId) {
    this.staffId = staffId;
    this.customerId = customerId;
    this.startDatetime = startDatetime;
    this.endDatetime = endDatetime;
    this.serviceId = serviceId;
  }

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



  public LocalDateTime getStartDatetime() {
    return startDatetime;
  }


  public void setStartDatetime(LocalDateTime startDatetime) {
    this.startDatetime = startDatetime;
  }


  public LocalDateTime getEndDatetime() {
    return endDatetime;
  }

  public void setEndDatetime(LocalDateTime endDatetime) {
    this.endDatetime = endDatetime;
  }


  public long getServiceId() {
    return serviceId;
  }

  public void setServiceId(long serviceId) {
    this.serviceId = serviceId;
  }

  @Override
  public String toString() {
    return "Booking{" +
            "id=" + id +
            ", staffId=" + staffId +
            ", customerId=" + customerId +
            ", startDatetime=" + startDatetime +
            ", endDatetime=" + endDatetime +
            ", serviceId=" + serviceId +
            '}';
  }
}
