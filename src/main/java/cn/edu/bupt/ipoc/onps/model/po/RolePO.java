package cn.edu.bupt.ipoc.onps.model.po;

public class RolePO {
    private String id;

    private String name;

    public RolePO(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public RolePO() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}