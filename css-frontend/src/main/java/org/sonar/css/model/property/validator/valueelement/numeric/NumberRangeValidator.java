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
package org.sonar.css.model.property.validator.valueelement.numeric;

import java.util.Locale;

import org.sonar.plugins.css.api.tree.css.NumberTree;
import org.sonar.plugins.css.api.tree.Tree;

public class NumberRangeValidator extends NumberValidator {

  private final Double min;
  private final Double max;

  public NumberRangeValidator(double min, double max) {
    super(false);
    this.min = min;
    this.max = max;
  }

  public NumberRangeValidator(double min) {
    super(false);
    this.min = min;
    this.max = null;
  }

  @Override
  public boolean isValid(Tree tree) {
    if (super.isValid(tree)) {
      double value = ((NumberTree) tree).doubleValue();
      if (max != null) {
        return value >= min && value <= max;
      } else {
        return value >= min;
      }
    }
    return false;
  }

  @Override
  public String getValidatorFormat() {
    String format = "<number>(" + String.format(Locale.ENGLISH, "%.1f", min) + ",";
    if (max != null) {
      format += String.format(Locale.ENGLISH, "%.1f", max);
    } else {
      format += "infinite";
    }
    return format + ")";
  }

}
