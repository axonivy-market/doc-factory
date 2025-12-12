package ch.ivyteam.ivy.addons.docfactory.test.data;

import java.io.Serializable;

public class Product implements Serializable {

  private static final long serialVersionUID = -369263576498866268L;
  private String id;
  private String name;
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

  public Product getRelatedProduct() {
    return relatedProduct;
  }

  public void setRelatedProduct(Product relatedProduct) {
    this.relatedProduct = relatedProduct;
  }
}
