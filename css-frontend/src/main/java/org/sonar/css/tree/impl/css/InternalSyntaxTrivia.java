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
package org.sonar.css.tree.impl.css;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.sonar.css.tree.impl.TreeImpl;
import org.sonar.plugins.css.api.tree.css.SyntaxToken;
import org.sonar.plugins.css.api.tree.css.SyntaxTrivia;
import org.sonar.plugins.css.api.tree.Tree;
import org.sonar.plugins.css.api.visitors.DoubleDispatchVisitor;

public class InternalSyntaxTrivia extends TreeImpl implements SyntaxTrivia {

  private final String comment;
  private final int column;
  private int startLine;
  private int endLine;
  private int endColumn;

  public InternalSyntaxTrivia(String comment, int startLine, int column) {
    this.comment = comment;
    this.startLine = startLine;
    this.column = column;
    calculateEndOffsets();
  }

  private void calculateEndOffsets() {
    String[] lines = comment.split("\r\n|\n|\r", -1);
    endColumn = column + comment.length();
    endLine = startLine + lines.length - 1;

    if (endLine != startLine) {
      endColumn = lines[lines.length - 1].length();
    }
  }

  @Override
  public int endLine() {
    return endLine;
  }

  @Override
  public int endColumn() {
    return endColumn;
  }

  @Override
  public String text() {
    return comment;
  }

  @Override
  public List<SyntaxTrivia> trivias() {
    return Collections.emptyList();
  }

  @Override
  public int line() {
    return startLine;
  }

  @Override
  public int column() {
    return column;
  }

  @Override
  public Kind getKind() {
    return Kind.TRIVIA;
  }

  @Override
  public boolean isLeaf() {
    return true;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    throw new UnsupportedOperationException();
  }

  public static SyntaxTrivia create(String comment, int startLine, int column) {
    return new InternalSyntaxTrivia(comment, startLine, column);
  }

  @Override
  public int getLine() {
    return startLine;
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitComment(this);
  }

  @Override
  public SyntaxToken getFirstToken() {
    return this;
  }

  @Override
  public SyntaxToken getLastToken() {
    return this;
  }
}
