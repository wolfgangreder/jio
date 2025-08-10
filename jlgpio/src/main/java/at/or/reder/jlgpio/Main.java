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
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.help.HelpFormatter;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@Messages({
  "# {0} - errorMessage",
  "Main_err_parseCmdLine=Cannot parse command line: {0}",
  "# {0} - commandList",
  "Main_err_noCommand=Provide one of {0}",
  "Main_err_cannotFindService=Cannot find lg service instance.",
  "Main_enum_chips_descr=Enumerate available chips.",
  "Main_enum_chips_showall=Show all items. Don't care if they are accessible.",
  "Main_chip_info=Show chip info"
})
public class Main {

  private static final Options OPTIONS = new Options();
  private static final Set<String> COMMANDS = Set.of("enum-chips", "chip-info");

  static {
    OptionGroup group = new OptionGroup();
    group.setRequired(true);
    group.addOption(Option.builder().longOpt("enum-chips").hasArg(false).desc(Bundle.Main_enum_chips_descr()).get());
    group.addOption(Option.builder().longOpt("chip-info").hasArg(true).desc(Bundle.Main_chip_info()).numberOfArgs(1).
            optionalArg(true).
            valueSeparator().get());
    OPTIONS.addOptionGroup(group);
    OPTIONS.addOption(Option.builder().longOpt("show-all").hasArg(false).required(false).desc(Bundle.
            Main_enum_chips_showall()).get());

  }

  private static String listCommands(String limiter)
  {
    return COMMANDS.stream()
            .map(cmd -> "--" + cmd)
            .collect(Collectors.joining(limiter, "[", "]"));
  }

  private void run(CommandLine cmdLine) throws IOException
  {
    boolean success = false;
    LgIo lg = Lookup.getDefault().lookup(LgIo.class);
    if (lg != null) {
      if (cmdLine.hasOption("enum-chips")) {
        success = EnumerateChips.run(cmdLine, lg);
      } else if (cmdLine.hasOption("chip-info")) {
        success = ShowChipInfo.showChipInfo(cmdLine, lg);
      }
    } else {
      System.err.println(Bundle.Main_err_cannotFindService());
    }
    if (!success) {
      printHelp();
    }
  }

  private static void printHelp() throws IOException
  {
    HelpFormatter help = HelpFormatter.builder().setShowSince(false).get();
    help.printHelp("jlgpio " + listCommands("|"),
                   "jlgpio command line tool 0.0.1-SNAPSHOT", OPTIONS,
                   null, false);
  }

  public static void main(String[] args) throws IOException
  {
    try {
      CommandLineParser parser = new DefaultParser();
      CommandLine cmd = parser.parse(OPTIONS, args);
      new Main().run(cmd);
    } catch (ParseException ex) {
      System.err.println(Bundle.Main_err_parseCmdLine(ex.getLocalizedMessage()));
      printHelp();
    }
  }
}
