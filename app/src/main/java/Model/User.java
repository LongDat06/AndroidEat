package Model;

public class User {
    private String Name;
    private String Password;
    private int Status;


    public User(String name, String password, int status) {
        Name = name;
        Password = password;
        Status = status;
    }

    public User() {
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}

