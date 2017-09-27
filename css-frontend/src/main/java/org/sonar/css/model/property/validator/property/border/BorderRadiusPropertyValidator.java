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
package org.sonar.css.model.property.validator.property.border;

import java.util.List;

import org.sonar.css.model.property.validator.ValidatorFactory;
import org.sonar.css.model.property.validator.ValueValidator;
import org.sonar.plugins.css.api.tree.css.DelimiterTree;
import org.sonar.plugins.css.api.tree.Tree;
import org.sonar.plugins.css.api.tree.css.ValueTree;

public class BorderRadiusPropertyValidator implements ValueValidator {

  @Override
  public boolean isValid(ValueTree valueTree) {
    List<Tree> valueElements = valueTree.sanitizedValueElements();
    int numberOfValueElements = valueElements.size();

    if (numberOfValueElements > 9) {
      return false;
    }

    int numberDelimiters = 0;
    int positionDelimiter = 0;
    for (int i = 0; i < numberOfValueElements; i++) {
      if (!ValidatorFactory.getPositivePercentageValidator().isValid(valueElements.get(i))
        && !ValidatorFactory.getPositiveLengthValidator().isValid(valueElements.get(i))
        && !(valueElements.get(i) instanceof DelimiterTree)) {
        return false;
      }
      if (valueElements.get(i) instanceof DelimiterTree) {
        if (!"/".equals(((DelimiterTree) valueElements.get(i)).text())) {
          return false;
        }
        numberDelimiters++;
        positionDelimiter = i;
      }
    }

    if (numberDelimiters > 1
      || (numberDelimiters == 1 && (positionDelimiter == 0 || positionDelimiter == numberOfValueElements - 1 || positionDelimiter > 4))
      || (numberDelimiters == 0 && numberOfValueElements > 4)
      || (numberDelimiters == 1 && (numberOfValueElements - positionDelimiter) > 5)) {
      return false;
    }

    return true;
  }

  @Override
  public String getValidatorFormat() {
    return "[ <length>(>=0) | <percentage>(>=0) ]{1,4} [ / [ <length>(>=0) | <percentage>(>=0) ]{1,4} ]?";
  }

}
