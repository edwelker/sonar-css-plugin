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
import org.sonar.plugins.css.api.tree.css.AttributeMatcherTree;
import org.sonar.plugins.css.api.tree.css.AttributeSelectorTree;

import static org.fest.assertions.Assertions.assertThat;

public class AttributeSelectorTreeTest extends CssTreeTest {

  public AttributeSelectorTreeTest() {
    super(LexicalGrammar.ATTRIBUTE_SELECTOR);
  }

  @Test
  public void attributeSelector() {
    AttributeSelectorTree tree;

    tree = checkParsed("[attribute~=abc]", "attribute");
    assertThat(tree.matcherExpression()).isNotNull();
    assertThat(tree.matcherExpression().attributeMatcher()).isNotNull();
    assertThat(tree.matcherExpression().attributeMatcher().type()).isNotNull();
    assertThat(tree.matcherExpression().attributeMatcher().type()).isEqualTo(AttributeMatcherTree.MATCHER.INCLUDE);
    assertThat(tree.matcherExpression().attributeMatcher().value()).isEqualTo("~=");
    assertThat(tree.namespace()).isNull();

    tree = checkParsed("[ attribute ~= abc ]", "attribute");
    assertThat(tree.matcherExpression()).isNotNull();
    assertThat(tree.matcherExpression().attributeMatcher()).isNotNull();
    assertThat(tree.matcherExpression().attributeMatcher().type()).isNotNull();
    assertThat(tree.matcherExpression().attributeMatcher().type()).isEqualTo(AttributeMatcherTree.MATCHER.INCLUDE);
    assertThat(tree.matcherExpression().attributeMatcher().value()).isEqualTo("~=");
    assertThat(tree.namespace()).isNull();

    tree = checkParsed("[id]", "id");
    assertThat(tree).isNotNull();
    assertThat(tree.matcherExpression()).isNull();
    assertThat(tree.namespace()).isNull();

    tree = checkParsed("[|attribute ~= abc ]", "attribute");
    assertThat(tree.namespace()).isNotNull();
    assertThat(tree.namespace().prefix()).isNull();

    tree = checkParsed("[*|attribute ~= abc ]", "attribute");
    assertThat(tree.namespace()).isNotNull();
    assertThat(tree.namespace().prefix()).isNotNull();
    assertThat(tree.namespace().prefix().text()).isEqualTo("*");

    tree = checkParsed("[ *|attribute ~= abc ]", "attribute");
    assertThat(tree.namespace()).isNotNull();
    assertThat(tree.namespace().prefix()).isNotNull();
    assertThat(tree.namespace().prefix().text()).isEqualTo("*");

    checkParsed("[attribute=xxx]", "attribute");
    checkParsed("[attribute~=xxx]", "attribute");
    checkParsed("[attribute*=xxx]", "attribute");
    checkParsed("[attribute^=xxx]", "attribute");
    checkParsed("[attribute$=xxx]", "attribute");
    checkParsed("[attribute|=xxx]", "attribute");
    checkParsed("[attribute|=xxx i]", "attribute");
  }

  @Test
  public void notAttributeSelector() {
    checkNotParsed("[]");
    checkNotParsed("[|]");
    checkNotParsed("[=abc]");
    checkNotParsed("[abc=]");
    checkNotParsed("[* |attribute ~= abc ]");
    checkNotParsed("[*| attribute ~= abc ]");
  }

  private AttributeSelectorTree checkParsed(String toParse, String expectedAttribute) {
    AttributeSelectorTree tree = (AttributeSelectorTree) parser().parse(toParse);
    assertThat(tree).isNotNull();
    assertThat(tree.attribute()).isNotNull();
    assertThat(tree.closeBracket()).isNotNull();
    assertThat(tree.openBracket()).isNotNull();
    assertThat(tree.attribute().text()).isEqualTo(expectedAttribute);
    return tree;
  }

}
