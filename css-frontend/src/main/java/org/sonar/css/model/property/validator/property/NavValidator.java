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

import org.sonar.css.model.property.validator.ValidatorFactory;
import org.sonar.css.model.property.validator.ValueValidator;
import org.sonar.plugins.css.api.tree.Tree;
import org.sonar.plugins.css.api.tree.css.*;

public class NavValidator implements ValueValidator {

  @Override
  public boolean isValid(ValueTree valueTree) {
    List<Tree> valueElements = valueTree.sanitizedValueElements();
    int numberOfValueElements = valueElements.size();

    if (numberOfValueElements > 2) {
      return false;
    }
    if (numberOfValueElements == 1) {
      return ValidatorFactory.getAutoValidator().isValid(valueElements.get(0)) || isID(valueElements.get(0));
    }
    if (numberOfValueElements == 2) {
      return isID(valueElements.get(0)) && isValidTargetName(valueElements.get(1));
    }
    return false;
  }

  @Override
  public String getValidatorFormat() {
    return "auto | <id> [ current | root | <target-name> ]?";
  }

  private boolean isID(Tree valueElement) {
    return valueElement instanceof HashTree;
  }

  private boolean isValidTargetName(Tree valueElement) {
    if (valueElement instanceof IdentifierTree) {
      String identifier = ((IdentifierTree) valueElement).text();
      return "current".equals(identifier) || "root".equals(identifier);
    } else if (valueElement instanceof StringTree) {
      String value = ((StringTree) valueElement).actualText();
      return !value.startsWith("_");
    } else {
      return false;
    }
  }

}
