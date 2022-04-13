package ch.ivyteam.ivy.addons.docfactory.test.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Order implements Serializable {

  private static final long serialVersionUID = 5377535249134268671L;

  private String name;
  private double price;
  private Collection<OrderDetail> details = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public Collection<OrderDetail> getDetails() {
    return details;
  }

  public void setDetails(Collection<OrderDetail> details) {
    this.details = details;
  }

  public void putDetail(OrderDetail detail) {
    details.add(detail);
  }

}
