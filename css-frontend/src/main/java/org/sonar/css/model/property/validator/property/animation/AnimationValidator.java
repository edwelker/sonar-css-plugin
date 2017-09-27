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
package org.sonar.css.model.property.validator.property.animation;

import java.util.List;

import org.sonar.css.model.property.validator.ValueValidator;
import org.sonar.plugins.css.api.tree.Tree;
import org.sonar.plugins.css.api.tree.css.ValueTree;

public class AnimationValidator implements ValueValidator {

  private static final AnimationDelayValidator ANIMATION_DELAY_VALIDATOR = new AnimationDelayValidator();
  private static final AnimationDirectionValidator ANIMATION_DIRECTION_VALIDATOR = new AnimationDirectionValidator();
  private static final AnimationDurationValidator ANIMATION_DURATION_VALIDATOR = new AnimationDurationValidator();
  private static final AnimationFillModeValidator ANIMATION_FILL_MODE_VALIDATOR = new AnimationFillModeValidator();
  private static final AnimationIterationCountValidator ANIMATION_ITERATION_COUNT_VALIDATOR = new AnimationIterationCountValidator();
  private static final AnimationNameValidator ANIMATION_NAME_VALIDATOR = new AnimationNameValidator();
  private static final AnimationPlayStateValidator ANIMATION_PLAY_STATE_VALIDATOR = new AnimationPlayStateValidator();
  private static final AnimationTimingFunctionValidator ANIMATION_TIMING_FUNCTION_VALIDATOR = new AnimationTimingFunctionValidator();

  @Override
  public boolean isValid(ValueTree valueTree) {
    List<Tree> valueElements = valueTree.sanitizedValueElements();
    int numberOfValueElements = valueElements.size();

    if (numberOfValueElements > 8) {
      return false;
    }
    for (Tree valueElement : valueElements) {
      if (!ANIMATION_DELAY_VALIDATOR.isValid(valueElement)
        && !ANIMATION_DIRECTION_VALIDATOR.isValid(valueElement)
        && !ANIMATION_DURATION_VALIDATOR.isValid(valueElement)
        && !ANIMATION_FILL_MODE_VALIDATOR.isValid(valueElement)
        && !ANIMATION_ITERATION_COUNT_VALIDATOR.isValid(valueElement)
        && !ANIMATION_NAME_VALIDATOR.isValid(valueElement)
        && !ANIMATION_PLAY_STATE_VALIDATOR.isValid(valueElement)
        && !ANIMATION_TIMING_FUNCTION_VALIDATOR.isValid(valueElement)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String getValidatorFormat() {
    return "<time> || <single-timing-function> || <time> || <single-animation-iteration-count> || <single-animation-direction> || <single-animation-fill-mode> || <single-animation-play-state> || <single-animation-name>";
  }

}
