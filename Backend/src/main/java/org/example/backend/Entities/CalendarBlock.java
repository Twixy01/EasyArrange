package org.example.backend.Entities;


import java.time.LocalDateTime;

public class CalendarBlock {

  private long id;
  private LocalDateTime startDatetime;
  private LocalDateTime endDatetime;
  private long staffId;

  public CalendarBlock(long id, LocalDateTime startDatetime, LocalDateTime endDatetime, long staffId) {
    this.id = id;
    this.startDatetime = startDatetime;
    this.endDatetime = endDatetime;
    this.staffId = staffId;
  }

  public CalendarBlock(LocalDateTime startDatetime, LocalDateTime endDatetime, long staffId) {
    this.startDatetime = startDatetime;
    this.endDatetime = endDatetime;
    this.staffId = staffId;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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


  public long getStaffId() {
    return staffId;
  }

  public void setStaffId(long staffId) {
    this.staffId = staffId;
  }

}
