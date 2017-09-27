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
package org.sonar.css.model.property;

import java.util.stream.Collectors;

import org.junit.Test;
import org.sonar.css.model.Vendor;
import org.sonar.css.model.property.standard.Border;
import org.sonar.css.model.property.standard.BorderEnd;
import org.sonar.css.model.property.standard.TransitionProperty;

import static org.junit.Assert.assertEquals;

public class StandardPropertyFactoryTest {

  @Test
  public void should_return_a_valid_border_property_object() {
    StandardProperty property = StandardPropertyFactory.getByName("border");
    assertEquals(Border.class, property.getClass());
    assertEquals(property.getName(), "border");
    assertEquals(property.getLinks().size(), 2);
    assertEquals(property.getLinks().get(0), "https://www.w3.org/TR/CSS22/box.html#propdef-border");
    assertEquals(property.getLinks().get(1), "https://drafts.csswg.org/css-backgrounds-3/#border");
    assertEquals(property.getVendors().size(), 0);
    assertEquals(property.isObsolete(), false);
  }

  @Test
  public void should_return_a_valid_border_property_object_uppercase_test() {
    StandardProperty property = StandardPropertyFactory.getByName("BORDER");
    assertEquals(Border.class, property.getClass());
    assertEquals(property.getName(), "border");
    assertEquals(property.getLinks().size(), 2);
    assertEquals(property.getLinks().get(0), "https://www.w3.org/TR/CSS22/box.html#propdef-border");
    assertEquals(property.getLinks().get(1), "https://drafts.csswg.org/css-backgrounds-3/#border");
    assertEquals(property.getVendors().size(), 0);
    assertEquals(property.isObsolete(), false);
  }

  @Test
  public void should_return_a_valid_border_end_property_object() {
    StandardProperty property = StandardPropertyFactory.getByName("border-end");
    assertEquals(BorderEnd.class, property.getClass());
    assertEquals(property.getName(), "border-end");
    assertEquals(property.getLinks().size(), 0);
    assertEquals(property.getValidators().size(), 0);
    assertEquals(property.getVendors().size(), 0);
    assertEquals(property.isObsolete(), true);
  }

  @Test
  public void should_return_a_valid_transition_property_object() {
    StandardProperty property = StandardPropertyFactory.getByName("transition-property");
    assertEquals(TransitionProperty.class, property.getClass());
    assertEquals(property.getName(), "transition-property");
    assertEquals(property.getLinks().size(), 1);
    assertEquals(property.getLinks().get(0), "https://drafts.csswg.org/css-transitions-1/#propdef-transition-property");
    assertEquals(property.getValidators().size(), 0);
    assertEquals(property.getVendors().size(), 1);
    assertEquals(property.getVendors().contains(Vendor.WEBKIT), true);
    assertEquals(property.getVendors().contains(Vendor.MICROSOFT), false);
    assertEquals(property.isObsolete(), false);
  }

  @Test
  public void should_return_an_unknown_property_object() {
    StandardProperty property = StandardPropertyFactory.getByName("Bla-bla");
    assertEquals(UnknownProperty.class, property.getClass());
    assertEquals("bla-bla", property.getName());
    assertEquals(property.getLinks().size(), 0);
    assertEquals(property.getValidators().size(), 0);
    assertEquals(property.getVendors().size(), 0);
    assertEquals(property.isObsolete(), false);
  }

  @Test
  public void number_of_standard_properties() {
    assertEquals(614, StandardPropertyFactory.getAll().size());
  }

  @Test
  public void should_not_find_any_property_set_to_both_obsolete_and_experimental() {
    assertEquals(
      0,
      StandardPropertyFactory
        .getAll()
        .stream()
        .filter(p -> p.isExperimental() && p.isObsolete())
        .collect(Collectors.toList())
        .size());
  }

  @Test
  public void should_not_find_any_property_set_to_obsolete_with_vendors() {
    assertEquals(
      0,
      StandardPropertyFactory
        .getAll()
        .stream()
        .filter(p -> p.isObsolete() && p.hasVendors())
        .collect(Collectors.toList())
        .size());
  }

  @Test
  public void should_not_find_any_property_not_set_to_experimental_with_vendors() {
    assertEquals(
      0,
      StandardPropertyFactory
        .getAll()
        .stream()
        .filter(p -> !p.isExperimental() && p.hasVendors())
        .collect(Collectors.toList())
        .size());
  }

  @Test
  public void should_not_find_any_property_set_to_obsolete_with_validators() {
    assertEquals(
      0,
      StandardPropertyFactory
        .getAll()
        .stream()
        .filter(p -> p.isObsolete() && p.hasValidators())
        .collect(Collectors.toList())
        .size());
  }

  @Test
  public void should_not_find_any_property_set_to_obsolete_with_shorthand() {
    assertEquals(
      0,
      StandardPropertyFactory
        .getAll()
        .stream()
        .filter(p -> p.isObsolete() && p.isShorthand())
        .collect(Collectors.toList())
        .size());
  }

}
