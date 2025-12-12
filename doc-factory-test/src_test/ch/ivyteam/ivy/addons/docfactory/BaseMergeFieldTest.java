package ch.ivyteam.ivy.addons.docfactory;

import org.junit.jupiter.api.BeforeEach;

import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class BaseMergeFieldTest {

  private static final String CYCLIC_SUPPORT_LEVELS = "com.axonivy.utils.docfactory.CyclicSupportLevels";

  @BeforeEach
  public void setup(AppFixture fixture) {
    fixture.var(CYCLIC_SUPPORT_LEVELS, "1");
  }
}
