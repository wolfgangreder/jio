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
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.help.HelpFormatter;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@Messages({
  "Main_enum_chips_descr=Enumerate available chips"
})
public class Main {

  private static final Options OPTIONS = new Options();

  static {
    OPTIONS.addOption(Option.builder().longOpt("enum-chips").hasArg(false).desc(Bundle.Main_enum_chips_descr()).get());
  }

  public void showChipInfo()
  {
    LgIo lg = Lookup.getDefault().lookup(LgIo.class);
  }

  private void run(CommandLine cmdLine) throws IOException
  {
    printHelp();
  }

  private static void printHelp() throws IOException
  {
    HelpFormatter help = HelpFormatter.builder().setShowSince(false).get();
    help.printHelp("jlgpio", "jlgpio command line tool 0.0.1-SNAPSHOT", OPTIONS, null, false);
  }

  public static void main(String[] args) throws IOException
  {
    try {
      CommandLineParser parser = new DefaultParser();
      CommandLine cmd = parser.parse(OPTIONS, args);
      new Main().run(cmd);
    } catch (ParseException ex) {
      Exceptions.printStackTrace(ex);
      printHelp();
    }
  }
}
