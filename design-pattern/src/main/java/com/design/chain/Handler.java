package com.design.chain;

public interface Handler {

    /**
     * <p>
     *   true:成功 false：拒绝 null:交给下一个处理
     * </p>
     *
     * @author Zhi.Wang
     * @version 1.0
     * @date 2020-10-16
     */
    Boolean process(Request request);
}
