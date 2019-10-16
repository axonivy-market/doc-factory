package ch.ivyteam.ivy.docfactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import ch.ivyteam.di.restricted.DiCore;
import ch.ivyteam.ivy.ThirdPartyLicenses;
import ch.ivyteam.ivy.server.IServer;

public class TestAsposeLic
{

  @Test
  public void licStream_withoutEngine() throws IOException
  {
    InputStream licStream = ThirdPartyLicenses.getDocumentFactoryLicense();
    assertEmptyLicence(licStream);
  }

  private void assertEmptyLicence(InputStream licStream) throws IOException
  {
    Assert.assertNotNull("not available in headless env", licStream);

    String licVal = IOUtils.toString(licStream, StandardCharsets.UTF_8);
    Assert.assertEquals("empty license when engine not running", StringUtils.EMPTY, licVal);
  }

  @Test
  public void licStream_withEngine() throws IOException
  {
    DiCore.installModules(new Module());
    try
    {
      InputStream licStream = ThirdPartyLicenses.getDocumentFactoryLicense();
      assertValidLicence(licStream);
    }
    finally
    {
      DiCore.reset();
    }
  }
  
  private void assertValidLicence(InputStream licStream) throws IOException
  {
    Assert.assertNotNull("not available in headless env", licStream);

    String licVal = IOUtils.toString(licStream, StandardCharsets.UTF_8);
    Assert.assertTrue("licence must contain ivyTeam AG", licVal.contains("ivyTeam AG"));
  }

  private static final class Module extends AbstractModule
  {
    @Provides
    public IServer server()
    {
      return (IServer)Proxy.newProxyInstance(TestAsposeLic.class.getClassLoader(), new Class[] {IServer.class}, this::invoke);
    }
    
    private Object invoke(@SuppressWarnings("unused") Object proxy, Method method, @SuppressWarnings("unused") Object[] args)
            throws Throwable
    {
      if ("isRunning".equals(method.getName()))
      {
        return true;
      }
      throw new UnsupportedOperationException("Method "+method.getName()+" not implemented");
      
    }
  }

}
