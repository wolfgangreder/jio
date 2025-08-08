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

import at.or.reder.jlgpio.spi.NativeSpi;
import com.sun.jna.FunctionMapper;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import java.lang.reflect.Method;
import java.util.Map;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = NativeSpi.class)
public class JniNativeImpl implements Library, NativeSpi {

  static {
    initialize();
  }

  private static void initialize()
  {
    NativeLibrary nativeLib = NativeLibrary.getInstance("lgpio",
                                                        Map.of(Library.OPTION_FUNCTION_MAPPER,
                                                               (FunctionMapper) JniNativeImpl::functionMapper,
                                                               Library.OPTION_CLASSLOADER, JniNativeImpl.class.
                                                                       getClassLoader()));
    Native.register(nativeLib);
  }

  private static String functionMapper(NativeLibrary library, Method method)
  {
    if (method.getName().startsWith("_")) {
      return method.getName().substring(1);
    }
    return method.getName();
  }

  private static native int _lguVersion();

  @Override
  public int lguVersion()
  {
    return _lguVersion();
  }

}
