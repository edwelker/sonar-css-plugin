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
package org.sonar.css.visitors.highlighter;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.css.parser.scss.ScssParser;
import org.sonar.plugins.css.api.tree.Tree;
import org.sonar.plugins.css.api.visitors.TreeVisitorContext;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.sonar.api.batch.sensor.highlighting.TypeOfText.*;

public class ScssSyntaxHighlighterVisitorTest {

  private ScssSyntaxHighlighterVisitor highlighterVisitor;
  private TreeVisitorContext visitorContext;
  private SensorContextTester sensorContext;
  private File file;
  private DefaultInputFile inputFile;

  @Rule
  public final TemporaryFolder tempFolder = new TemporaryFolder();

  @Before
  public void setUp() throws IOException {
    DefaultFileSystem fileSystem = new DefaultFileSystem(tempFolder.getRoot());
    fileSystem.setEncoding(Charsets.UTF_8);
    file = tempFolder.newFile();
    inputFile = new DefaultInputFile("moduleKey", file.getName())
      .setLanguage("scss")
      .setType(InputFile.Type.MAIN);
    fileSystem.add(inputFile);

    sensorContext = SensorContextTester.create(tempFolder.getRoot());
    sensorContext.setFileSystem(fileSystem);

    visitorContext = mock(TreeVisitorContext.class);
    highlighterVisitor = new ScssSyntaxHighlighterVisitor(sensorContext);
    when(visitorContext.getFile()).thenReturn(file);
  }

  @Test
  public void empty_input() throws Exception {
    highlight("");
    assertThat(sensorContext.highlightingTypeAt("moduleKey:" + file.getName(), 1, 0)).isEmpty();
  }

  @Test
  public void string_simple_quote() throws Exception {
    highlight(".mybox {\nprop: 'string';\n}");
    assertHighlighting(2, 6, 8, STRING);
  }

  @Test
  public void string_double_quote() throws Exception {
    highlight(".mybox {\nprop: \"string\";\n}");
    assertHighlighting(2, 6, 8, STRING);
  }

  @Test
  public void property() throws Exception {
    highlight(".mybox {\nprop: \"string\";\n}");
    assertHighlighting(2, 0, 4, CONSTANT);
  }

  @Test
  public void variable() throws Exception {
    highlight(".mybox {\n--prop: \"string\";\n}");
    assertHighlighting(2, 0, 6, CONSTANT);
  }

  @Test
  public void class_selector() throws Exception {
    highlight(".mybox {\n--prop: \"string\";\n}");
    assertHighlighting(1, 0, 6, KEYWORD_LIGHT);
  }

  @Test
  public void class_selector2() throws Exception {
    highlight(".mybox.mybox2 {\n--prop: \"string\";\n}");
    assertHighlighting(1, 0, 13, KEYWORD_LIGHT);
  }

  @Test
  public void id_selector() throws Exception {
    highlight(" #abc {\n--prop: \"string\";\n}");
    assertHighlighting(1, 1, 4, KEYWORD_LIGHT);
  }

  @Test
  public void comment() throws Exception {
    highlight(" #abc {\n--prop: \"string\"; /* blabla */\n}");
    assertHighlighting(2, 18, 12, COMMENT);
  }

  @Test
  public void multiline_comment() throws Exception {
    highlight("/* blabla...\nblabla...\n blabla...\n */");
    assertHighlighting(1, 0, 13, COMMENT);
    assertHighlighting(2, 0, 13, COMMENT);
    assertHighlighting(3, 1, 13, COMMENT);
  }

  @Test
  public void byte_order_mark() throws Exception {
    highlight("\uFEFF#abc {\n--prop: \"string\";\n}");
    assertHighlighting(1, 0, 4, KEYWORD_LIGHT);
    assertHighlighting(2, 0, 4, CONSTANT);
  }

  @Test
  public void scss_variable() throws Exception {
    highlight("$my-color: green;\n$my-other-color: red;");
    assertHighlighting(1, 0, 9, CONSTANT);
    assertHighlighting(2, 0, 14, CONSTANT);
  }

  @Test
  public void scss_directive() throws Exception {
    highlight("@if $x == 0 {}");
    assertHighlighting(1, 0, 3, ANNOTATION);
    highlight("@for $i from 0 through 10 {}");
    assertHighlighting(1, 0, 4, ANNOTATION);
  }

  private void highlight(String string) throws Exception {
    inputFile.initMetadata(string);
    Tree tree = ScssParser.createParser(Charsets.UTF_8).parse(string);

    when(visitorContext.getTopTree()).thenReturn(tree);

    Files.write(string, file, Charsets.UTF_8);
    highlighterVisitor.scanTree(visitorContext);
  }

  private void assertHighlighting(int line, int column, int length, TypeOfText type) {
    for (int i = column; i < column + length; i++) {
      List<TypeOfText> typeOfTexts = sensorContext.highlightingTypeAt("moduleKey:" + file.getName(), line, i);
      assertThat(typeOfTexts).hasSize(1);
      assertThat(typeOfTexts.get(0)).isEqualTo(type);
    }
  }

}
