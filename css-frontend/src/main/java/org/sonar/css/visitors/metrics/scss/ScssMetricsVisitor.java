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
package org.sonar.css.visitors.metrics.scss;

import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.css.visitors.metrics.AbstractMetricsVisitor;
import org.sonar.css.visitors.metrics.CommentLinesVisitor;
import org.sonar.plugins.css.api.tree.Tree;

public class ScssMetricsVisitor extends AbstractMetricsVisitor {

  public ScssMetricsVisitor(SensorContext sensorContext, NoSonarFilter noSonarFilter) {
    super(sensorContext, noSonarFilter);
  }

  @Override
  public void leaveFile(Tree tree) {
    super.leaveFile(tree);

    CommentLinesVisitor commentLinesVisitor = new CommentLinesVisitor(tree, new ScssCommentAnalyser());
    saveMetricOnFile(CoreMetrics.COMMENT_LINES, commentLinesVisitor.getNumberOfCommentLines());
    noSonarFilter.noSonarInFile(inputFile, commentLinesVisitor.getNoSonarLines());
  }

}
