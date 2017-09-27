/*
 * SonarQube CSS / SCSS / Less Analyzer
 * Copyright (C) 2013-2016 Tamas Kende and David RACODON
 * mailto:contact AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.css.visitors.metrics.css;

import com.google.common.base.Charsets;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.css.parser.css.CssParser;
import org.sonar.plugins.css.api.visitors.TreeVisitorContext;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CssMetricsVisitorTest {

  private SensorContextTester context;

  private void setUp(String fileName) {
    File moduleBaseDir = new File("src/test/resources/metrics/");
    context = SensorContextTester.create(moduleBaseDir);

    DefaultInputFile inputFile = new DefaultInputFile("moduleKey", fileName)
      .setModuleBaseDir(moduleBaseDir.toPath())
      .setLanguage("css")
      .setType(InputFile.Type.MAIN);

    context.fileSystem().add(inputFile);
    context.fileSystem().setEncoding(Charsets.UTF_8);

    CssMetricsVisitor metricsVisitor = new CssMetricsVisitor(context, mock(NoSonarFilter.class));

    TreeVisitorContext treeVisitorContext = mock(TreeVisitorContext.class);
    when(treeVisitorContext.getFile()).thenReturn(inputFile.file());
    when(treeVisitorContext.getTopTree()).thenReturn(CssParser.createParser(Charsets.UTF_8).parse(inputFile.file()));

    metricsVisitor.scanTree(treeVisitorContext);
  }

  @Test
  public void test_all_metrics() {
    setUp("metrics.css");

    String componentKey = "moduleKey:metrics.css";
    assertThat(context.measure(componentKey, CoreMetrics.NCLOC).value()).isEqualTo(17);
    assertThat(context.measure(componentKey, CoreMetrics.COMMENT_LINES).value()).isEqualTo(4);
    assertThat(context.measure(componentKey, CoreMetrics.STATEMENTS).value()).isEqualTo(11);
    assertThat(context.measure(componentKey, CoreMetrics.FUNCTIONS).value()).isEqualTo(6);
  }

  @Test
  public void test_complexity() {
    setUp("complexity.css");

    String componentKey = "moduleKey:complexity.css";
    assertThat(context.measure(componentKey, CoreMetrics.COMPLEXITY).value()).isEqualTo(25);
    assertThat(context.measure(componentKey, CoreMetrics.COMPLEXITY_IN_FUNCTIONS).value()).isEqualTo(25);
  }

}
