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

import org.sonar.css.model.Vendor;
import org.sonar.css.model.property.StandardProperty;

public class HyphenateLimitLines extends StandardProperty {

  public HyphenateLimitLines() {
    setExperimental(true);
    addLinks(
      "https://drafts.csswg.org/css-text-4/#propdef-hyphenate-limit-lines",
      "https://msdn.microsoft.com/en-us/library/hh771867(v=vs.85).aspx");
    addVendors(Vendor.MICROSOFT);
  }

}
