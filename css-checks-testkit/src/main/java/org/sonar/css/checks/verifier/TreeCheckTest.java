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
package org.sonar.css.checks.verifier;

import com.sonar.sslr.api.typed.ActionParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sonar.css.tree.impl.TreeImpl;
import org.sonar.css.visitors.CssTreeVisitorContext;
import org.sonar.plugins.css.api.CssCheck;
import org.sonar.plugins.css.api.tree.Tree;
import org.sonar.plugins.css.api.visitors.issue.FileIssue;
import org.sonar.plugins.css.api.visitors.issue.Issue;
import org.sonar.plugins.css.api.visitors.issue.LineIssue;
import org.sonar.plugins.css.api.visitors.issue.PreciseIssue;
import org.sonar.squidbridge.api.CheckMessage;

class TreeCheckTest {

  private TreeCheckTest() {
  }

  public static Collection<CheckMessage> getIssues(String relativePath, CssCheck check, ActionParser<Tree> parser) {
    File file = new File(relativePath);

    TreeImpl tree = (TreeImpl) parser.parse(file);
    CssTreeVisitorContext context = new CssTreeVisitorContext(tree, file, "css");
    List<Issue> issues = check.scanFile(context);

    return getCheckMessages(issues);
  }

  private static Collection<CheckMessage> getCheckMessages(List<Issue> issues) {
    List<CheckMessage> checkMessages = new ArrayList<>();
    for (Issue issue : issues) {
      CheckMessage checkMessage;
      if (issue instanceof FileIssue) {
        FileIssue fileIssue = (FileIssue) issue;
        checkMessage = new CheckMessage(fileIssue.check(), fileIssue.message());

      } else if (issue instanceof LineIssue) {
        LineIssue lineIssue = (LineIssue) issue;
        checkMessage = new CheckMessage(lineIssue.check(), lineIssue.message());
        checkMessage.setLine(lineIssue.line());

      } else {
        PreciseIssue preciseIssue = (PreciseIssue) issue;
        checkMessage = new CheckMessage(preciseIssue.check(), preciseIssue.primaryLocation().message());
        checkMessage.setLine(preciseIssue.primaryLocation().startLine());
      }

      if (issue.cost() != null) {
        checkMessage.setCost(issue.cost());
      }

      checkMessages.add(checkMessage);
    }

    return checkMessages;
  }

}
