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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

  private static Integer stringToInt(String str)
  {
    if (str != null) {
      try {
        return Integer.valueOf(str);
      } catch (NumberFormatException ex) {
        //
      }
    }
    return null;
  }

  private static List<ChipId> getChipIdList(CommandLine cmdLine)
  {
    String[] arguments = cmdLine.getOptionValue("line-info").split(",");
    return Stream.of(arguments)
            .map(ShowLineInfo::stringToInt)
            .filter(Objects::nonNull)
            .map(ChipId::new)
            .toList();
  }

  public static final boolean showLineInfo(CommandLine cmdLine, Io lgIo) throws IOException
  {
    for (ChipId chipId : getChipIdList(cmdLine)) {
      try (IoChip chip = lgIo.open(chipId)) {
        chip.enumerateLines().forEach(ShowLineInfo::printLineInfo);
      }
    }
    return true;
  }
}
