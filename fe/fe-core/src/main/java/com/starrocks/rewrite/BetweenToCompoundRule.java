// This file is made available under Elastic License 2.0.
// This file is based on code available under the Apache license here:
//   https://github.com/apache/incubator-doris/blob/master/fe/fe-core/src/main/java/org/apache/doris/rewrite/BetweenToCompoundRule.java

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

package com.starrocks.rewrite;

import com.starrocks.analysis.Analyzer;
import com.starrocks.analysis.BetweenPredicate;
import com.starrocks.analysis.BinaryPredicate;
import com.starrocks.analysis.CompoundPredicate;
import com.starrocks.analysis.Expr;
import com.starrocks.analysis.Predicate;
import com.starrocks.common.AnalysisException;

/**
 * Rewrites BetweenPredicates into an equivalent conjunctive/disjunctive
 * CompoundPredicate.
 * It can be applied to pre-analysis expr trees and therefore does not reanalyze
 * the transformation output itself.
 * Examples:
 * A BETWEEN X AND Y ==> A >= X AND A <= Y
 * A NOT BETWEEN X AND Y ==> A < X OR A > Y
 */
public final class BetweenToCompoundRule implements ExprRewriteRule {
    public static ExprRewriteRule INSTANCE = new BetweenToCompoundRule();

    @Override
    public Expr apply(Expr expr, Analyzer analyzer) throws AnalysisException {
        if (!(expr instanceof BetweenPredicate)) {
            return expr;
        }
        BetweenPredicate bp = (BetweenPredicate) expr;
        Expr result = null;
        if (bp.isNotBetween()) {
            // Rewrite into disjunction.
            Predicate lower = new BinaryPredicate(BinaryPredicate.Operator.LT,
                    bp.getChild(0), bp.getChild(1));
            Predicate upper = new BinaryPredicate(BinaryPredicate.Operator.GT,
                    bp.getChild(0), bp.getChild(2));
            result = new CompoundPredicate(CompoundPredicate.Operator.OR, lower, upper);
        } else {
            // Rewrite into conjunction.
            Predicate lower = new BinaryPredicate(BinaryPredicate.Operator.GE,
                    bp.getChild(0), bp.getChild(1));
            Predicate upper = new BinaryPredicate(BinaryPredicate.Operator.LE,
                    bp.getChild(0), bp.getChild(2));
            result = new CompoundPredicate(CompoundPredicate.Operator.AND, lower, upper);
        }
        return result;
    }

    private BetweenToCompoundRule() {
    }
}
