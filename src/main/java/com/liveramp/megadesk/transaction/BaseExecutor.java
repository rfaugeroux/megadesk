/**
 *  Copyright 2014 LiveRamp
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.liveramp.megadesk.transaction;

public class BaseExecutor implements Executor {

  @Override
  public <V> ExecutionResult<V> execute(Transaction transaction, Function<V> function) throws Exception {
    if (transaction.execution().tryBegin(function.dependency())) {
      try {
        V result = function.run(transaction.data());
        transaction.execution().commit(transaction.data());
        return new ExecutionResult<V>(true, result);
      } catch (Exception e) {
        transaction.execution().abort();
        throw e;
      }
    } else {
      return new ExecutionResult<V>(false, null);
    }
  }
}
