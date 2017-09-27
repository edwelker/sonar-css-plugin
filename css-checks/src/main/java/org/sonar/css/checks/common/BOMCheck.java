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
package org.sonar.css.checks.common;

import com.google.common.base.Charsets;

import java.nio.charset.Charset;

import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.css.checks.Tags;
import org.sonar.css.visitors.CharsetAwareVisitor;
import org.sonar.plugins.css.api.tree.css.StyleSheetTree;
import org.sonar.plugins.css.api.visitors.DoubleDispatchVisitorCheck;
import org.sonar.squidbridge.annotations.ActivatedByDefault;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;

@Rule(
  key = "bom-utf8-files",
  name = "Byte Order Mark (BOM) should not be used for UTF-8 files",
  priority = Priority.MAJOR,
  tags = {Tags.PITFALL})
@SqaleConstantRemediation("5min")
@ActivatedByDefault
public class BOMCheck extends DoubleDispatchVisitorCheck implements CharsetAwareVisitor {

  private Charset charset;

  @Override
  public void visitStyleSheet(StyleSheetTree tree) {
    if (Charsets.UTF_8.equals(charset) && tree.hasByteOrderMark()) {
      addFileIssue("Remove the Byte Order Mark (BOM).");
    }
    super.visitStyleSheet(tree);
  }

  @Override
  public void setCharset(Charset charset) {
    this.charset = charset;
  }

}
