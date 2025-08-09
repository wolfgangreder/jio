/*
 * Copyright 2025 Wolfgang Reder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.or.reder.jlgpio.jni;

import com.sun.jna.Structure;

@SuppressWarnings("FieldMayBeFinal")
public class NativeChipInfo extends Structure implements Structure.ByReference {

  private int numLines;
  private char[] name;
  private char[] label;

  public NativeChipInfo()
  {
    numLines = 0;
    name = new char[JniNativeImpl.LG_GPIO_NAME_LEN];
    label = new char[JniNativeImpl.LG_GPIO_LABEL_LEN];
  }

  public int getNumLines()
  {
    return numLines;
  }

  public String getName()
  {
    return NativeUtils.ntsToString(name);
  }

  public String getLabel()
  {
    return NativeUtils.ntsToString(label);
  }
}
