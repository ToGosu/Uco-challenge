package co.edu.uco.ucochallenge.user.registeruser.application.usecase.domain;

import java.util.UUID;

public class RegisterUserDomain {

    private UUID idType;
    private String idNumber;
    private String firstName;
    private String secondName;
    private String firstSurname;
    private String secondSurname;
    private UUID homeCity;
    private String email;
    private String mobileNumber;

    public RegisterUserDomain() {
    }

    public RegisterUserDomain(UUID idType, String idNumber, String firstName, String secondName,
                              String firstSurname, String secondSurname, UUID homeCity,
                              String email, String mobileNumber) {
        this.idType = idType;
        this.idNumber = idNumber;
        this.firstName = firstName;
        this.secondName = secondName;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
        this.homeCity = homeCity;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    public UUID getIdType() { return idType; }
    public void setIdType(UUID idType) { this.idType = idType; }

    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getSecondName() { return secondName; }
    public void setSecondName(String secondName) { this.secondName = secondName; }

    public String getFirstSurname() { return firstSurname; }
    public void setFirstSurname(String firstSurname) { this.firstSurname = firstSurname; }

    public String getSecondSurname() { return secondSurname; }
    public void setSecondSurname(String secondSurname) { this.secondSurname = secondSurname; }

    public UUID getHomeCity() { return homeCity; }
    public void setHomeCity(UUID homeCity) { this.homeCity = homeCity; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
}
