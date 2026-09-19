package sg.edu.ntu.taskflow_api.repository;

import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import sg.edu.ntu.taskflow_api.model.Task;

@Repository 
public class TaskRepository {
    private final Map<Long, Task> task = new HashMap<>();

    private Long nextId = 0L;

    public Task createTask(Task task) {
        task.setId(nextId++);
        this.task.put(task.getId(), task);
        return task;
    }

    public Map<Long, Task> findAllTasks() {
        return new HashMap<>(task);
    }

    public Task findTaskById(Long id) {
        return task.get(id);
    }

    public Task updateTask(Long id, Task task) {
        if (this.task.containsKey(id)) {
            task.setId(id);
            this.task.put(id, task);
            return task;
        }
        return null;
    }

    public Task deleteTask(Long id) {
        if (this.task.containsKey(id)) {
            return this.task.remove(id);
        }
        return null;
    }

    public boolean markTaskAsComplete(Long id) {
        if (this.task.containsKey(id)) {
            Task task = this.task.get(id);
            task.setStatus("completed");
            return true;
        }
        return false;
        }

}
