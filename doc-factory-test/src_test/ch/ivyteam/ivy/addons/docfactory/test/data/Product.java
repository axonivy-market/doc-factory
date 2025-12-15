package ch.ivyteam.ivy.addons.docfactory.test.data;

import java.io.Serializable;
import java.math.BigDecimal;

import ch.ivyteam.ivy.scripting.objects.DateTime;

public class Product implements Serializable {

  private static final long serialVersionUID = -369263576498866268L;
  private String id;
  private String name;
  private BigDecimal price;
  private DateTime manufacturingDate;
  private Product relatedProduct;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public DateTime getManufacturingDate() {
    return manufacturingDate;
  }

  public void setManufacturingDate(DateTime manufacturingDate) {
    this.manufacturingDate = manufacturingDate;
  }

  public Product getRelatedProduct() {
    return relatedProduct;
  }

  public void setRelatedProduct(Product relatedProduct) {
    this.relatedProduct = relatedProduct;
  }
}
