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
package at.or.reder.jio.impl;

import at.or.reder.jio.LineFlag;
import at.or.reder.jio.LineInfo;
import java.util.Set;
import lombok.Data;

@Data
public final class LineInfoImpl implements LineInfo {

  private final int line;
  private final Set<LineFlag> flags;
  private final String name;
  private final String user;

  public LineInfoImpl(int line, Set<LineFlag> flags, String name, String user)
  {
    this.line = line;
    this.flags = Set.copyOf(flags);
    this.name = name;
    this.user = user;
  }
  
  
}
