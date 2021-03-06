/*
 * Copyright 2000-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jetbrains.dart.analysisServer;

import com.intellij.codeInsight.daemon.GutterMark;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ModalityState;
import com.intellij.testFramework.fixtures.CodeInsightFixtureTestCase;
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl;
import com.intellij.util.Alarm;
import com.jetbrains.lang.dart.util.DartTestUtils;

import javax.swing.*;
import java.util.List;

public class DartServerImplementationsMarkerProviderTest extends CodeInsightFixtureTestCase {

  protected String getBasePath() {
    return "/analysisServer/implementationsMarker";
  }

  @Override
  public void setUp() throws Exception {
    super.setUp();
    DartTestUtils.configureDartSdk(myModule, getTestRootDisposable(), true);
    myFixture.setTestDataPath(DartTestUtils.BASE_TEST_DATA_PATH + getBasePath());
    ((CodeInsightTestFixtureImpl)myFixture).canChangeDocumentDuringHighlighting(true);
  }

  private void checkHasGutterAtCaret(final String expectedText, final Icon expectedIcon) {
    final String testName = getTestName(false);
    myFixture.configureByFile(testName + ".dart");

    myFixture.doHighlighting(); // make sure server is warmed up

    final List<GutterMark> gutters = myFixture.findGuttersAtCaret();

    if (!gutters.isEmpty()) {
      DartServerOverrideMarkerProviderTest.checkGutter(gutters, expectedText, expectedIcon);
    }
    else {
      new Alarm(Alarm.ThreadToUse.SWING_THREAD, myTestRootDisposable).addRequest(new Runnable() {
        @Override
        public void run() {
          myFixture.doHighlighting();
          DartServerOverrideMarkerProviderTest.checkGutter(myFixture.findGuttersAtCaret(), expectedText, expectedIcon);
        }
      }, 500, ModalityState.NON_MODAL);
    }
  }

  public void testClassExtended() throws Throwable {
    checkHasGutterAtCaret("Has subclasses", AllIcons.Gutter.OverridenMethod);
  }

  public void testClassImplemented() throws Throwable {
    checkHasGutterAtCaret("Has subclasses", AllIcons.Gutter.OverridenMethod);
  }

  public void testMethodExtended() throws Throwable {
    checkHasGutterAtCaret("Is overridden in subclasses", AllIcons.Gutter.OverridenMethod);
  }

  public void testMethodImplemented() throws Throwable {
    checkHasGutterAtCaret("Is overridden in subclasses", AllIcons.Gutter.OverridenMethod);
  }
}
