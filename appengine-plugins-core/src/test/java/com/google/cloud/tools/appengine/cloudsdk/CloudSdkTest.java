package com.google.cloud.tools.appengine.cloudsdk;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import com.google.cloud.tools.appengine.api.AppEngineException;
import com.google.cloud.tools.appengine.cloudsdk.process.ProcessOutputLineListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Unit tests for {@link CloudSdk}.
 */
@RunWith(MockitoJUnitRunner.class)
public class CloudSdkTest {
  
  private Path root = Paths.get("/");
  private CloudSdk.Builder builder = new CloudSdk.Builder().sdkPath(root);

  @Mock
  private ProcessOutputLineListener outputListener;

  @Test
  public void testGetSdkPath() {
    assertEquals(root, builder.build().getSdkPath());
  }
  
  @Test
  public void testGetWindowsPythonPath() {
    assertEquals("python", builder.build().getWindowsPythonPath().toString());
  }

  @Test
  public void testGetJavaAppEngineSdkPath() {
    assertEquals(root.resolve("platform/google_appengine/google/appengine/tools/java/lib"),
        builder.build().getJavaAppEngineSdkPath());
  }

  @Test
  public void testGetJarPathJavaTools() {
    assertEquals(Paths.get("/platform/google_appengine/google/appengine"
        + "/tools/java/lib/appengine-tools-api.jar"),
        builder.build().getJarPath("appengine-tools-api.jar"));
  }

  @Test
  public void testNewCloudSdk_nullWaitingOutputListener() {
    CloudSdk sdk = builder
        .addStdOutLineListener(outputListener).runDevAppServerWait(10).async(false).build();

    assertNull(sdk.getRunDevAppServerWaitListener());

    sdk = builder.addStdOutLineListener(outputListener)
        .runDevAppServerWait(0).async(true).build();

    assertNull(sdk.getRunDevAppServerWaitListener());
  }

  @Test
  public void testNewCloudSdk_outListener() {
    builder.addStdOutLineListener(outputListener).runDevAppServerWait(10).async(true);

    CloudSdk sdk = builder.build();

    assertNotNull(sdk.getRunDevAppServerWaitListener());
    assertEquals(2, builder.getStdOutLineListeners().size());
    assertEquals(1, builder.getStdErrLineListeners().size());
    assertEquals(1, builder.getExitListeners().size());
  }

  @Test
  public void testNewCloudSdk_errListener() {
    builder.addStdErrLineListener(outputListener).runDevAppServerWait(10).async(true);
    CloudSdk sdk = builder.build();

    assertNotNull(sdk.getRunDevAppServerWaitListener());
    assertEquals(1, builder.getStdOutLineListeners().size());
    assertEquals(2, builder.getStdErrLineListeners().size());
    assertEquals(1, builder.getExitListeners().size());
  }

  @Test(expected = AppEngineException.class)
  public void testNewCloudSdk_inheritOutputAndOutListener() {
    builder.inheritProcessOutput(true).addStdOutLineListener(outputListener).build();
  }

  @Test(expected = AppEngineException.class)
  public void testNewCloudSdk_inheritOutputAndErrListener() {
    builder.inheritProcessOutput(true).addStdErrLineListener(outputListener).build();
  }
}
