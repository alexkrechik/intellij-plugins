package org.angularjs.performance;

import com.intellij.testFramework.PlatformTestUtil;
import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;
import com.intellij.util.ThrowableRunnable;
import org.angularjs.AngularTestUtil;

/**
 * @author Konstantin.Ulitin
 */
public class AngularJSPerformanceTest extends LightPlatformCodeInsightFixtureTestCase {

  @Override
  protected String getTestDataPath() {
    return AngularTestUtil.getBaseTestDataPath(getClass());
  }

  @Override
  protected boolean isWriteActionRequired() {
    return false;
  }

  public void testManyInjectionsHighlighting() {
    myFixture.configureByFiles("manyInjections.highlight.html", "angular.js", "custom.js");
    PlatformTestUtil.startPerformanceTest(getTestName(false), 30000, new ThrowableRunnable() {
      @Override
      public void run() throws Throwable {
        myFixture.checkHighlighting();
      }
    }).attempts(1).cpuBound().usesAllCPUCores().assertTiming();
  }

}
