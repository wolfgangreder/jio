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

import at.or.reder.jlgpio.LgChipInfo;
import at.or.reder.jlgpio.LgException;
import at.or.reder.jlgpio.spi.NativeSpi;
import com.sun.jna.FunctionMapper;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Structure;
import java.lang.reflect.Method;
import java.util.Map;
import lombok.NonNull;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = NativeSpi.class)
public class JniNativeImpl implements Library, NativeSpi {

  public static final int LG_GPIO_NAME_LEN = 32;
  public static final int LG_GPIO_LABEL_LEN = 32;

  static {
    initialize();
  }

  private static void initialize()
  {
    NativeLibrary nativeLib = NativeLibrary.getInstance("lgpio",
                                                        Map.of(Library.OPTION_FUNCTION_MAPPER,
                                                               (FunctionMapper) JniNativeImpl::functionMapper,
                                                               Library.OPTION_CLASSLOADER,
                                                               JniNativeImpl.class.getClassLoader()));
    Native.register(nativeLib);
  }

  private static String functionMapper(NativeLibrary library, Method method)
  {
    if (method.getName().startsWith("_")) {
      return method.getName().substring(1);
    }
    return method.getName();
  }

  private static native int _lgGpiochipOpen(int gpioDev);

  private int checkLgResult(int resultCode) throws LgException
  {
    if (resultCode < 0) {
      String message = lguErrorText(resultCode);
      throw new LgException(resultCode, message);
    }
    return resultCode;
  }

  @Override
  public int lgGpiochipOpen(int gpioDev) throws LgException
  {
    return checkLgResult(_lgGpiochipOpen(gpioDev));
  }

  public static native int _lgGpiochipClose(int handle);

  @Override
  public void lgGpiochipClose(int handle) throws LgException
  {
    checkLgResult(_lgGpiochipClose(handle));
  }

  public static native int _lgGpioGetChipInfo(int handle, Structure struture);

  @Override
  public LgChipInfo lgGpioGetChipInfo(int handle) throws LgException
  {
    NativeChipInfo nativeInfo = new NativeChipInfo();
    checkLgResult(_lgGpioGetChipInfo(handle, nativeInfo));
    return new LgChipInfo(nativeInfo.getNumLines(), nativeInfo.getName(), nativeInfo.getLabel());
  }

  private static native int _lguVersion();

  @Override
  public int lguVersion()
  {
    return _lguVersion();
  }

  private static native String _lguErrorText(int errorCode);

  @Override
  public String lguErrorText(int errorCode)
  {
    return _lguErrorText(errorCode);
  }

  private static native void _lguSetWorkDir(String workingDir);

  @Override
  public void lguSetWorkDir(@NonNull String workingDir)
  {
    _lguSetWorkDir(workingDir);
  }

  private static native String _lguGetWorkDir();

  @Override
  public String lugGetWorkDir()
  {
    return _lguGetWorkDir();
  }

}
