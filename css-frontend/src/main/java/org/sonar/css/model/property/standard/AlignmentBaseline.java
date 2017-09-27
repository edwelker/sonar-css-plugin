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
package org.sonar.css.model.property.standard;

import org.sonar.css.model.property.StandardProperty;
import org.sonar.css.model.property.validator.valueelement.IdentifierValidator;

public class AlignmentBaseline extends StandardProperty {

  public AlignmentBaseline() {
    setExperimental(true);
    addLinks(
      "http://www.w3.org/TR/css3-linebox/#alignment-baseline",
      "https://drafts.csswg.org/css-inline/#propdef-alignment-baseline");
    addValidators(new IdentifierValidator("auto", "baseline", "use-script", "before-edge", "text-before-edge",
      "after-edge", "text-after-edge", "central", "middle", "ideographic", "alphabetic", "hanging", "mathematical"));
  }

}
