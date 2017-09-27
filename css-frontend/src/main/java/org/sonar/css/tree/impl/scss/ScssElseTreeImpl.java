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
package org.sonar.css.tree.impl.scss;

import com.google.common.collect.Iterators;
import org.sonar.css.tree.impl.TreeImpl;
import org.sonar.plugins.css.api.tree.Tree;
import org.sonar.plugins.css.api.tree.css.StatementBlockTree;
import org.sonar.plugins.css.api.tree.scss.ScssDirectiveTree;
import org.sonar.plugins.css.api.tree.scss.ScssElseTree;
import org.sonar.plugins.css.api.visitors.DoubleDispatchVisitor;

import java.util.Iterator;

public class ScssElseTreeImpl extends TreeImpl implements ScssElseTree {

  private final ScssDirectiveTree directive;
  private final StatementBlockTree block;

  public ScssElseTreeImpl(ScssDirectiveTree directive, StatementBlockTree block) {
    this.directive = directive;
    this.block = block;
  }

  @Override
  public Kind getKind() {
    return Kind.SCSS_ELSE;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return Iterators.forArray(directive, block);
  }

  @Override
  public void accept(DoubleDispatchVisitor visitor) {
    visitor.visitScssElse(this);
  }

  @Override
  public ScssDirectiveTree directive() {
    return directive;
  }

  @Override
  public StatementBlockTree block() {
    return block;
  }

}
