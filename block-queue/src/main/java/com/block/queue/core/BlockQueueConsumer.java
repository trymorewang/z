package com.block.queue.core;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BlockQueueConsumer {
    String topic() default "deadTopic";
}
