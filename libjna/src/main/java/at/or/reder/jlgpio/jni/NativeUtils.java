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

import lombok.experimental.UtilityClass;

@UtilityClass
public class NativeUtils {
  
  static String ntsToString(char[] charArray)
  {
    if (charArray != null) {
      if (charArray.length == 0) {
        return "";
      }
      int lastIndex = 0;
      while (lastIndex < charArray.length && charArray[lastIndex] != 0) {
        lastIndex++;
      }
      return String.valueOf(charArray, 0, lastIndex);
    }
    return null;
  }
}
