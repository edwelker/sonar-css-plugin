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
package org.sonar.css.model.property.validator.property;

import java.util.List;

import org.sonar.css.model.property.validator.ValueValidator;
import org.sonar.css.model.property.validator.valueelement.IdentifierValidator;
import org.sonar.plugins.css.api.tree.Tree;
import org.sonar.plugins.css.api.tree.css.ValueTree;

public class HangingPunctuationValidator implements ValueValidator {

  private static final IdentifierValidator SINGLE_ELEMENT_VALIDATOR = new IdentifierValidator("none", "first", "force-end", "allow-end", "last");
  private static final IdentifierValidator FIRST_VALIDATOR = new IdentifierValidator("first");
  private static final IdentifierValidator LAST_VALIDATOR = new IdentifierValidator("last");
  private static final IdentifierValidator FORCE_END_ALLOW_END_VALIDATOR = new IdentifierValidator("force-end", "allow-end");

  @Override
  public boolean isValid(ValueTree valueTree) {
    List<Tree> valueElements = valueTree.sanitizedValueElements();
    int numberOfValueElements = valueElements.size();

    if (numberOfValueElements > 3) {
      return false;
    }
    if (numberOfValueElements == 1) {
      return SINGLE_ELEMENT_VALIDATOR.isValid(valueElements.get(0));
    }
    if (numberOfValueElements > 1) {
      return validateMultiElementValue(valueElements);
    }
    return false;
  }

  @Override
  public String getValidatorFormat() {
    return "none | [ first || [ force-end | allow-end ] || last ]";
  }

  private boolean validateMultiElementValue(List<Tree> valueElements) {
    int first = 0;
    int forceEndAllowEnd = 0;
    int last = 0;

    for (Tree valueElement : valueElements) {
      if (FIRST_VALIDATOR.isValid(valueElement)) {
        first++;
      } else if (LAST_VALIDATOR.isValid(valueElement)) {
        last++;
      } else if (FORCE_END_ALLOW_END_VALIDATOR.isValid(valueElement)) {
        forceEndAllowEnd++;
      }
    }

    if (first > 1 || last > 1 || forceEndAllowEnd > 1) {
      return false;
    } else {
      return first + forceEndAllowEnd + last == valueElements.size();
    }
  }

}
