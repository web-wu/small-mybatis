package com.tabwu.mybatis.session;

/**
 * @PROJECT_NAME: small-mybatis
 * @USER: tabwu
 * @DATE: 2022/9/2 13:16
 * @DESCRIPTION: 分页记录
 */
public class RowBounds {

    public static final int NO_ROW_OFFSET = 0;
    public static final int NO_ROW_LIMIT = Integer.MAX_VALUE;
    public static final RowBounds DEFAULT = new RowBounds();

    // offset,limit就等于一般分页的start,limit
    private int offset;
    private int limit;

    public RowBounds() {
        this.offset = NO_ROW_OFFSET;
        this.limit = NO_ROW_LIMIT;
    }

    public RowBounds(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }
}
