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

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    String getContent() {
        return content;
    }

    void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PostPackage [status=" + status + ", mimetype=" + mimetype + ", id=" + id
                + ", timetolive=" + timetolive + ", content=" + content + ", filetype=" + fileType + "]";
    }

    Integer getTimetolive() {
        return timetolive;
    }

    void setTimetolive(Integer timetolive) {
        this.timetolive = timetolive;
    }

    String getFileType() {
        return fileType;
    }

    void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
