package com.richzjc.download.task;

import java.util.List;

public interface IParentTask {

    List<ChildTask> getRealChildTasks();
    List<ChildTask> getChildTasks();
}
