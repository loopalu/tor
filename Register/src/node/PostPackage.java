package node;

public class PostPackage {
    private Integer status;
    private String mimetype;
    private String id;
    private Integer timetolive;
    private String content;
    private String fileType;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PostPackage [status=" + status + ", mimetype=" + mimetype + ", id=" + id
                + ", timetolive=" + timetolive + ", content=" + content + ", filetype=" + fileType + "]";
    }

    public Integer getTimetolive() {
        return timetolive;
    }

    public void setTimetolive(Integer timetolive) {
        this.timetolive = timetolive;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
