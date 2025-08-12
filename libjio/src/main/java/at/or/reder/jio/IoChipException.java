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
package at.or.reder.jio;

import java.io.IOException;
import java.util.MissingResourceException;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

@Messages({
  "# {0} - methodName",
  "IoChipException_INVOKATION=Invokation error: {0}",
  "# {0} - errorCode",
  "IoChipException_UKNOWN=unknown error {0,number,0}"
})
@SuppressWarnings("serial")
public class IoChipException extends IOException {

  private final int errorCode;

  public IoChipException(String methodName, Throwable cause)
  {
    super(Bundle.IoChipException_INVOKATION(methodName), cause);
    this.errorCode = 0;
  }

  public IoChipException(int errorCode, String message)
  {
    super(loadErrorMessage(errorCode, message));
    this.errorCode = errorCode;
  }

  private static String loadErrorMessage(int errorCode, String message)
  {
    if (message == null) {
      try {
        return NbBundle.getMessage(IoChipException.class, "IoChipException_" + Integer.toString(Math.abs(errorCode)));
      } catch (MissingResourceException ex) {
        return Bundle.IoChipException_UKNOWN(errorCode);
      }
    }
    return message;
  }

  public int getErrorCode()
  {
    return errorCode;
  }
}
