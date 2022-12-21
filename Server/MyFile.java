package file;

public class MyFile {
    private int id;
    private String name;
    private byte[] data;
    private String fileextension;
    
    public MyFile(int id, String name, byte[] data, String fileextension) {
        this.id = id;
        this.name = name;
        this.data = data;
        this.fileextension = fileextension;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileextension() {
        return fileextension;
    }

    public void setFileextension(String fileextension) {
        this.fileextension = fileextension;
    }
}
