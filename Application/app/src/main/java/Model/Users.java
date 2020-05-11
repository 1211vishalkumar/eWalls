package Model;

public class Users {
    private String name,phoneNo,password;

    // constructor
    public Users(){

    }
    // forming the constructor
    public Users(String name, String phoneNo, String password) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.password = password;
    }

    //generating the getter and setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
