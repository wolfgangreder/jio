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
import java.text.MessageFormat;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;

public class ShowLineInfo {

  private static void printLineInfo(LineInfo lineInfo)
  {
    System.out.println(MessageFormat.format("\tLine: {0,number,0}", lineInfo.getLine()));
    System.out.println(MessageFormat.format("\tFlags: {0}", lineInfo.getFlags().stream().map(LineFlag::name).collect(
                                            Collectors.joining(","))));
    System.out.println(MessageFormat.format("\tName: {0}", lineInfo.getName()));
    System.out.println(MessageFormat.format("\tUser: {0}", lineInfo.getUser()));
  }

  public static final boolean showLineInfo(CommandLine cmdLine, Io lgIo) throws IOException
  {
    try (IoChip chip = lgIo.open(new ChipId(0))) {
      chip.enumerateLines()
              .forEach(ShowLineInfo::printLineInfo);
    }
    return true;
  }
}
