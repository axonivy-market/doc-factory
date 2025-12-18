package ch.ivyteam.ivy.addons.docfactory;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;

import ch.ivyteam.ivy.addons.docfactory.test.data.Product;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.environment.IvyTest;
import ch.ivyteam.ivy.scripting.objects.DateTime;

@IvyTest
public class BaseMergeFieldTest {

  public static final String CYCLIC_SUPPORT_LEVELS = "com.axonivy.utils.docfactory.CyclicSupportLevels";

  @BeforeEach
  public void setup(AppFixture fixture) {
    fixture.var(CYCLIC_SUPPORT_LEVELS, "1");
  }

  public Product makeComplexProductWithIncludingCyclicRelation() {
    Product product = new Product();
    product.setId("123");
    product.setName("Table");
    product.setManufacturingDate(new DateTime());
    product.setPrice(new BigDecimal(50.5));

    Product relatedProduct = new Product();
    relatedProduct.setId("456");
    relatedProduct.setName("Chair");
    relatedProduct.setManufacturingDate(new DateTime());
    relatedProduct.setPrice(new BigDecimal(5.15));
    relatedProduct.setRelatedProduct(product);

    product.setRelatedProduct(relatedProduct);
    return product;
  }
}
