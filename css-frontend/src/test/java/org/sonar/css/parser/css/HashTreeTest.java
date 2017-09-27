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
package org.sonar.css.parser.css;

import org.junit.Test;
import org.sonar.css.parser.LexicalGrammar;
import org.sonar.plugins.css.api.tree.css.HashTree;

import static org.fest.assertions.Assertions.assertThat;

public class HashTreeTest extends CssTreeTest {

  public HashTreeTest() {
    super(LexicalGrammar.HASH);
  }

  @Test
  public void hash() {
    checkParsed("#123456", "123456");
    checkParsed(" #123456", "123456");
    checkParsed("#123", "123");
    checkParsed(" #123", "123");
    checkParsed("#abc", "abc");
  }

  @Test
  public void notHash() {
    checkNotParsed("# 123456");
    checkNotParsed(" # 123456");
    checkNotParsed("# 123");
    checkNotParsed(" # 123");
  }

  private void checkParsed(String toParse, String expectedValue) {
    HashTree tree = (HashTree) parser().parse(toParse);
    assertThat(tree.hashSymbol()).isNotNull();
    assertThat(tree.hashSymbol().text()).isEqualTo("#");
    assertThat(tree.value()).isNotNull();
    assertThat(tree.value().text()).isEqualTo(expectedValue);
  }

}
