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
package at.or.reder.jlgpio;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.apache.commons.cli.CommandLine;
import org.openide.util.NbBundle.Messages;

@Messages({
  "# {0} - intToParse",
  "ShowChipInfo_cannot_parse_int=Cannot parse \"{0}\" to integer.",
  "# {0} - chipId",
  "ShowChipInfo_cannot_access_chip=Cannot access device \"{0}\"",
  "# {0} - name",
  "ShowChipInfo_nameLine=\tName : {0}",
  "# {0} - label",
  "ShowChipInfo_labelLine=\tLabel: {0}",
  "# {0} - numberOfLines",
  "ShowChipInfo_numberOfLines=\tLines: {0,number,0}",
  "# {0} - chipId",
  "ShowChipInfo_cannotfind_chip=Cannot find io chip {0}"
})
public class ShowChipInfo {

  private static List<String> extractValues(CommandLine cmdLine)
  {
    String[] masterValues = cmdLine.getOptionValues("chip-info");
    if (masterValues != null) {
      return Stream.of(masterValues)
              .flatMap(mv -> Stream.of(mv.split(",")))
              .toList();
    }
    return List.of();
  }

  private static List<LgChipId> getRequiredChipIds(CommandLine cmdLine, LgIo lgIo) throws IOException
  {
    Predicate<LgChipId> filter = (LgChipId chipId) -> chipId.isAccesible();
    List<String> values = extractValues(cmdLine);
    if (!values.isEmpty()) {
      Set<LgChipId> requiredChips = new HashSet<>();

      for (String value : values) {
        LgChipId newId;
        if (LgChipId.matchDeviceName(value).matches()) {
          newId = new LgChipId(value);
        } else {
          try {
            int id = Integer.parseInt(value);
            newId = new LgChipId(id);
          } catch (NumberFormatException ex) {
            throw new IOException(Bundle.ShowChipInfo_cannot_parse_int(value), ex);
          }
        }
        if (!newId.isAccesible()) {
          throw new IOException(Bundle.ShowChipInfo_cannot_access_chip(newId));
        }
        requiredChips.add(newId);
      }
      filter = filter.and(requiredChips::contains);
    }
    return lgIo.enumerateChips().filter(filter).toList();
  }

  public static boolean showChipInfo(CommandLine cmdLine, LgIo lgIo) throws IOException
  {
    List<LgChipId> chipIds = getRequiredChipIds(cmdLine, lgIo);
    if (!chipIds.isEmpty()) {
      for (LgChipId id : chipIds) {
        try (LgIoChip chip = lgIo.open(id)) {
          System.out.println("ChipInfo for " + chip.getId());
          LgChipInfo info = chip.getChipInfo();
          System.out.println(Bundle.ShowChipInfo_nameLine(info.getName()));
          System.out.println(Bundle.ShowChipInfo_labelLine(info.getLabel()));
          System.out.println(Bundle.ShowChipInfo_numberOfLines(info.getNumLines()));
          System.out.println();
        }
      }
    } else {
      System.out.println(Bundle.ShowChipInfo_cannotfind_chip(cmdLine.getOptionValue("chip-info")));
    }
    return true;
  }
}
