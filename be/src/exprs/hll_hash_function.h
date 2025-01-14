// This file is made available under Elastic License 2.0.
// This file is based on code available under the Apache license here:
//   https://github.com/apache/incubator-doris/blob/master/be/src/exprs/hll_hash_function.h

// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

#ifndef STARROCKS_BE_SRC_QUERY_EXPRS_HLL_HASH_FUNCTION_H
#define STARROCKS_BE_SRC_QUERY_EXPRS_HLL_HASH_FUNCTION_H

#include "exprs/anyval_util.h"
#include "udf/udf.h"
#include "util/hash_util.hpp"

namespace starrocks {

class Expr;
class TupleRow;

// todo(kks): for backward compatibility, we should remove this class
//            when starrocks 0.12 release
class HllHashFunctions {
public:
    static void init();
    static StringVal hll_hash(FunctionContext* ctx, const StringVal& dest_base);
    static BigIntVal hll_cardinality(FunctionContext* ctx, const HllVal& dest_base);
};
} // namespace starrocks

#endif
