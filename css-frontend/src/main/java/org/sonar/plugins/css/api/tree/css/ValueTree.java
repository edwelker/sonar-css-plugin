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
package org.sonar.plugins.css.api.tree.css;

import org.sonar.plugins.css.api.tree.Tree;

import java.util.List;
import java.util.Optional;

public interface ValueTree extends Tree {

  /**
   * @return First value element.
   */
  Tree firstValueElement();

  /**
   * @return Sanitized list of value elements. Value elements removed from the list are: ImportantTree
   */
  List<Tree> sanitizedValueElements();

  /**
   * @return All value elements (no filtering).
   */
  List<Tree> valueElements();

  /**
   * @return All value elements of a certain type.
   */
  <T extends Tree> List<T> valueElementsOfType(Class<T> treeType);

  /**
   * @return First value element of a certain type.
   */
  <T extends Tree> Optional<T> firstValueElementOfType(Class<T> treeType);

}
