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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.java.Log;

@UtilityClass
@Log
public class ThrowableWrapper {

  @FunctionalInterface
  public interface ThrowingFunction<A, R> {

    R apply(A arg) throws Exception;
  }

  @FunctionalInterface
  public interface ThrowingRunnable {

    void run() throws Exception;
  }

  @FunctionalInterface
  public interface ThrowingConsumer<I> {

    void accept(I item) throws Exception;
  }

  private static Error createThrowError(Throwable th, Class<? extends Error> errorClass)
  {
    if (errorClass != null) {
      try {
        Constructor<? extends Error> ctor = errorClass.getConstructor(Throwable.class);
        return ctor.newInstance(th);
      } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
        log.log(Level.SEVERE, MessageFormat.format(
                "Cannot create instanceof Class {0}. Throwing at.or.reder.dcccontrol.utils.WrappedError instead.",
                errorClass.getName()));
      }
    }
    return new WrappedError(th);
  }

  public <A, R> Function<A, R> wrapFunction(@NonNull ThrowingFunction<A, R> toWrap)
  {
    return wrapFunction(toWrap, null);
  }

  public <A, R> Function<A, R> wrapFunction(@NonNull ThrowingFunction<A, R> toWrap, Class<? extends Error> errorToThrow)
  {
    return arg -> {
      try {
        return toWrap.apply(arg);
      } catch (Exception ex) {
        throw createThrowError(ex, errorToThrow);
      }
    };
  }

  public Runnable wrapRunnable(@NonNull ThrowingRunnable toWrap)
  {
    return wrapRunnable(toWrap, null);
  }

  public Runnable wrapRunnable(@NonNull ThrowingRunnable toWrap, Class<? extends Error> errorToThrow)
  {
    return () -> {
      try {
        toWrap.run();
      } catch (Exception ex) {
        throw createThrowError(ex, errorToThrow);
      }
    };
  }

  public <I> Consumer<I> wrapConsumer(@NonNull ThrowingConsumer<I> toWrap)
  {
    return wrapConsumer(toWrap, null);
  }

  public <I> Consumer<I> wrapConsumer(@NonNull ThrowingConsumer<I> toWrap, Class<? extends Error> errorToThrow)
  {
    return (item) -> {
      try {
        toWrap.accept(item);
      } catch (Exception ex) {
        throw createThrowError(ex, errorToThrow);
      }
    };
  }
  
}
