package ch.ivyteam.ivy.addons.docfactory.test.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class InsuranceBasket implements Serializable {

  private static final long serialVersionUID = 1L;

  Collection<Insurance> insurances = new ArrayList<>();

  private InsuranceBasket() {}

  public static InsuranceBasket withInsurance(Insurance insurance) {
    InsuranceBasket basket = new InsuranceBasket();
    basket.insurances.add(insurance);
    return basket;
  }

  public InsuranceBasket putInsurance(Insurance insurance) {
    insurances.add(insurance);
    return this;
  }

  public Collection<Insurance> getInsurances() {
    return insurances;
  }

  public void setInsurances(Collection<Insurance> insurances) {
    this.insurances = insurances;
  }

}
