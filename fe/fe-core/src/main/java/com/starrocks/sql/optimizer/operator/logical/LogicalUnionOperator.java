// This file is licensed under the Elastic License 2.0. Copyright 2021 StarRocks Limited.
package com.starrocks.sql.optimizer.operator.logical;

import com.starrocks.sql.optimizer.OptExpression;
import com.starrocks.sql.optimizer.OptExpressionVisitor;
import com.starrocks.sql.optimizer.operator.OperatorType;
import com.starrocks.sql.optimizer.operator.OperatorVisitor;
import com.starrocks.sql.optimizer.operator.scalar.ColumnRefOperator;

import java.util.List;

public class LogicalUnionOperator extends LogicalSetOperator {
    private final boolean isUnionAll;

    public LogicalUnionOperator(List<ColumnRefOperator> result, List<List<ColumnRefOperator>> childOutputColumns,
                                boolean isUnionAll) {
        super(OperatorType.LOGICAL_UNION, result, childOutputColumns);
        this.isUnionAll = isUnionAll;
    }

    public boolean isUnionAll() {
        return isUnionAll;
    }

    @Override
    public <R, C> R accept(OperatorVisitor<R, C> visitor, C context) {
        return visitor.visitLogicalUnion(this, context);
    }

    @Override
    public <R, C> R accept(OptExpressionVisitor<R, C> visitor, OptExpression optExpression, C context) {
        return visitor.visitLogicalUnion(optExpression, context);
    }
}
