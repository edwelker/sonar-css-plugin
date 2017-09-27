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
package org.sonar.css.parser.scss;

import org.junit.Test;
import org.sonar.css.parser.LexicalGrammar;
import org.sonar.plugins.css.api.tree.scss.ScssForTree;

import static org.fest.assertions.Assertions.assertThat;

public class ScssForTreeTest extends ScssTreeTest {

  public ScssForTreeTest() {
    super(LexicalGrammar.SCSS_FOR);
  }

  @Test
  public void scssFor() {
    ScssForTree tree;

    tree = checkParsed("@for $i from 1 to 5 {}");
    assertThat(tree.block().content()).isEmpty();

    tree = checkParsed("@for $i from 1 to 5 {color: green;}");
    assertThat(tree.block().content()).hasSize(1);
    assertThat(tree.block().propertyDeclarations()).hasSize(1);
  }

  @Test
  public void ScssNotFor() {
    checkNotParsed("@for");
    checkNotParsed("@for {}");
    checkNotParsed("@for;");
  }

  private ScssForTree checkParsed(String toParse) {
    ScssForTree tree = (ScssForTree) parser().parse(toParse);
    assertThat(tree).isNotNull();
    assertThat(tree.directive()).isNotNull();
    assertThat(tree.directive().at()).isNotNull();
    assertThat(tree.directive().name().text()).isEqualTo("for");
    assertThat(tree.condition()).isNotNull();
    assertThat(tree.block()).isNotNull();
    return tree;
  }

}
