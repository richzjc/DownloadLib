package com.richzjc.download.task;

import java.util.List;

public interface IParentTask {

    public List<ChildTask> getChildTasks();
}
