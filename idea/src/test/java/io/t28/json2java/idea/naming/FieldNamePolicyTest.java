/*
 * Copyright (c) 2017 Tatsuya Maki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.t28.json2java.idea.naming;

import com.intellij.openapi.project.Project;
import com.intellij.psi.codeStyle.JavaCodeStyleManager;
import com.squareup.javapoet.TypeName;
import io.t28.json2java.idea.IdeaProjectTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FieldNamePolicyTest extends IdeaProjectTest {
    private FieldNamePolicy underTest;

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        final Project project = getProject();
        final JavaCodeStyleManager codeStyleManager = JavaCodeStyleManager.getInstance(project);
        underTest = new FieldNamePolicy(codeStyleManager);
    }

    @Test
    public void convertShouldReturnLowerCamelText() throws Exception {
        // exercise
        final String actual = underTest.convert("Foo", TypeName.OBJECT);

        // verify
        assertThat(actual)
                .isEqualTo("foo");
    }

    @Test
    public void convertShouldReturnTextWithPrefixWhenReserved() throws Exception {
        // exercise
        final String actual = underTest.convert("Private", TypeName.OBJECT);

        // verify
        assertThat(actual)
                .isEqualTo("aPrivate");
    }

    @Test
    public void convertShouldRemoveInvalidCharacter() throws Exception {
        // exercise
        final String actual = underTest.convert("1nva|id", TypeName.OBJECT);

        // verify
        assertThat(actual)
                .isEqualTo("a1nvaid");
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void convertShouldThrowExceptionWhenInvalidName() throws Exception {
        assertThatThrownBy(() -> {
            // exercise
             underTest.convert("+-*/", TypeName.OBJECT);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}