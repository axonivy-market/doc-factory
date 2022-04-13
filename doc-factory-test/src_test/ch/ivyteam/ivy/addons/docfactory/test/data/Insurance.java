package ch.ivyteam.ivy.addons.docfactory.test.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Insurance implements Serializable {

  private static final long serialVersionUID = 1L;

  private String name;
  private String contractNumber;
  Collection<Address> locations = new ArrayList<>();

  private Insurance() {}

  public static Insurance withName(String name) {
    Insurance ins = new Insurance();
    ins.setName(name);
    return ins;
  }

  @SuppressWarnings("hiding")
  public Insurance withContractNumber(String contractNumber) {
    this.setContractNumber(contractNumber);
    return this;
  }

  public Insurance withLocation(Address address) {
    this.locations.add(address);
    return this;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getContractNumber() {
    return contractNumber;
  }

  public void setContractNumber(String contractNumber) {
    this.contractNumber = contractNumber;
  }

  public Collection<Address> getLocations() {
    return locations;
  }

  public void setLocations(Collection<Address> locations) {
    this.locations = locations;
  }

}
