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
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public class LineState {

  private static Set<LineFlag> parseFlags(CommandLine cmdLine)
  {
    String strFlags = cmdLine.getOptionValue("flags");
    if (strFlags != null) {
      return LineFlag.fromShortNames(strFlags);
    }
    return Set.of();
  }

  private static IoLine openLine(IoChip chip, int numLine, Set<LineFlag> flags, boolean state, boolean output) throws IOException
  {
    if (output) {
      return chip.openOutputLine(numLine, flags, state);
    } else {
      return chip.openInputLine(numLine, flags);
    }
  }

  public static boolean setLineState(CommandLine cmdLine, Io io) throws IOException, ParseException
  {
    String strChipId = cmdLine.getParsedOptionValue("chip", "-1");
    int chipId = Integer.parseInt(strChipId);
    try (IoChip chip = io.open(new ChipId(chipId))) {
      String[] strLine = cmdLine.getOptionValue("line-state").split("=");
      Set<LineFlag> flags = parseFlags(cmdLine);
      int numLine = Integer.parseInt(strLine[0]);
      final boolean output = strLine.length >= 2;
      final boolean state = output && Integer.parseInt(strLine[1]) != 0;

      try (IoLine line = openLine(chip, numLine, flags, state, output)) {
        if (!flags.isEmpty()) {
          line.setFlags();
        }
        System.out.println(MessageFormat.format("State of line {0} is {1}", line.getLineNum(), line.get()));
      }
    }
    return true;
  }

}
