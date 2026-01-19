package Entities;


public class CalendarBlock {

  private long id;
  private java.sql.Timestamp startDatetime;
  private java.sql.Timestamp endDatetime;
  private long staffId;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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


  public long getStaffId() {
    return staffId;
  }

  public void setStaffId(long staffId) {
    this.staffId = staffId;
  }

}
