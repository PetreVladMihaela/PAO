package project.library.members;

import java.util.Objects;

public class Address {
    private String country;
    private String city;
    private String street;

    public Address(String country, String city, String street) {
        if (country == null) this.country = "-";
        else this.country = country;
        if (city == null) this.city = "-";
        else this.city = city;
        this.street = street;
    }

    public Address(String country, String city) {
        if (country == null) this.country = "-";
        else this.country = country;
        if (city == null) this.city = "-";
        else this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;
        Address address = (Address) o;
        return Objects.equals(getCountry(), address.getCountry()) && Objects.equals(getCity(), address.getCity()) && Objects.equals(getStreet(), address.getStreet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCountry(), getCity(), getStreet());
    }
}
