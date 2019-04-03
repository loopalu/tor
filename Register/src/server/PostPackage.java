package server;

public class PostPackage {
    private Integer status;
    private String mimetype;
    private String messageid;
    private Integer timetolive;
    private String content;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PostPackage [status=" + status + ", mimetype=" + mimetype + ", messageid=" + messageid
                + ", timetolive=" + timetolive + ", content=" + content + "]";
    }

    public Integer getTimetolive() {
        return timetolive;
    }

    public void setTimetolive(Integer timetolive) {
        this.timetolive = timetolive;
    }
}
