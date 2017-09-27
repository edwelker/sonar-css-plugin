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
package org.sonar.css.checks.scss;

import org.junit.Test;
import org.sonar.css.checks.CheckTestUtils;
import org.sonar.css.checks.verifier.CssCheckVerifier;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

public class PlaceholderSelectorNamingConventionCheckTest {

  private final PlaceholderSelectorNamingConventionCheck check = new PlaceholderSelectorNamingConventionCheck();

  @Test
  public void test_with_default_format() {
    check.setFormat("^[a-z][-a-z0-9]*$");
    CssCheckVerifier.verifyScssFile(check, getTestFile("selectorNamingConvention.scss"));
  }

  @Test
  public void test_with_custom_format() {
    check.setFormat("^[-a-z]+$");
    CssCheckVerifier.verifyScssFile(check, getTestFile("selectorNamingConventionCustomFormat.scss"));
  }

  @Test
  public void should_throw_an_illegal_state_exception_as_the_format_parameter_is_not_valid() {
    try {
      check.setFormat("(");

      CssCheckVerifier.issuesOnScssFile(check, CheckTestUtils.getCommonTestFile("selectorNamingConvention/selectorNamingConvention.css")).noMore();

    } catch (IllegalStateException e) {
      assertThat(e.getMessage()).isEqualTo("Check scss:placeholder-selector-naming-convention (Placeholder selectors should follow a naming convention): "
        + "format parameter \"(\" is not a valid regular expression.");
    }
  }

  private File getTestFile(String fileName) {
    return CheckTestUtils.getScssTestFile("placeholder-selector-naming-convention/" + fileName);
  }

}
