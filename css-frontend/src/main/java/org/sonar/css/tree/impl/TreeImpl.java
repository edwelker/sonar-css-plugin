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
package org.sonar.css.tree.impl;

import org.sonar.plugins.css.api.tree.Tree;
import org.sonar.plugins.css.api.tree.css.SyntaxToken;

import javax.annotation.Nullable;
import java.util.Iterator;

public abstract class TreeImpl implements Tree {

  @Nullable
  private Tree parent;

  @Override
  public Tree parent() {
    return parent;
  }

  @Override
  public void setParent(Tree parent) {
    this.parent = parent;
  }

  @Override
  public boolean hasAncestor(Class<? extends Tree> clazz) {
    while (this.parent() != null) {
      if (clazz.isAssignableFrom(this.parent().getClass())) {
        return true;
      } else {
        return this.parent().hasAncestor(clazz);
      }
    }
    return false;
  }

  @Override
  public int getLine() {
    return getFirstToken().line();
  }

  @Override
  public final boolean is(Tree.Kind... kind) {
    if (getKind() != null) {
      for (Kind kindIter : kind) {
        if (getKind() == kindIter) {
          return true;
        }
      }
    }
    return false;
  }

  public abstract Kind getKind();

  @Override
  public String treeValue() {
    StringBuilder value = new StringBuilder();
    Iterator<Tree> children = childrenIterator();
    while (children.hasNext()) {
      Tree next = children.next();
      if (next != null) {
        value.append(next.treeValue());
      }
    }
    return value.toString();
  }

  @Override
  public boolean isLeaf() {
    return false;
  }

  public SyntaxToken getLastToken() {
    SyntaxToken lastToken = null;
    Iterator<Tree> childrenIterator = childrenIterator();
    while (childrenIterator.hasNext()) {
      TreeImpl child = (TreeImpl) childrenIterator.next();
      if (child != null) {
        SyntaxToken childLastToken = child.getLastToken();
        if (childLastToken != null) {
          lastToken = childLastToken;
        }
      }
    }
    return lastToken;
  }

  public SyntaxToken getFirstToken() {
    Iterator<Tree> childrenIterator = childrenIterator();
    Tree child;
    do {
      if (childrenIterator.hasNext()) {
        child = childrenIterator.next();
      } else {
        throw new IllegalStateException("Tree has no non-null children " + getKind());
      }
    } while (child == null);
    return ((TreeImpl) child).getFirstToken();
  }
}
