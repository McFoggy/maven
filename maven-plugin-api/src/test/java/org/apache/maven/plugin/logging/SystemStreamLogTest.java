package org.apache.maven.plugin.logging;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.function.Supplier;

/**
 * Tests for {@link SystemStreamLog} 
 */
public class SystemStreamLogTest 
{
  private static final String EXPECTED_MESSAGE = "expected message";

  /*
   * As SystemStreamLog log levels are hardcoded (only info/warn/error are active), this test checks that
   * a message supplier is really called/executed when logging at those levels.
   */
  @Test
  public void testLazyMessageIsEvaluatedForActiveLogLevelsOnly()
  {
    Supplier messageSupplier = Mockito.mock(Supplier.class);
    Mockito.when(messageSupplier.get()).thenReturn(EXPECTED_MESSAGE);

    Throwable fakeError = new RuntimeException();
    
    Log logger = new SystemStreamLog();
    
    logger.debug(messageSupplier);
    logger.info(messageSupplier);
    logger.warn(messageSupplier);
    logger.error(messageSupplier);

    logger.debug(messageSupplier, fakeError);
    logger.info(messageSupplier, fakeError);
    logger.warn(messageSupplier, fakeError);
    logger.error(messageSupplier, fakeError);

    // calls at debug log level should have not lead to a Supplier call
    Mockito.verify(messageSupplier, Mockito.times(6)).get();
  }
}
