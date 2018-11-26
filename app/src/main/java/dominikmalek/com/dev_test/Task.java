package dominikmalek.com.dev_test;

public class Task {
    private  int id;
    private String name;
    private String status;

    public Task(){}

    public Task(String name, String status){
        this.name=name;
        this.status=status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
