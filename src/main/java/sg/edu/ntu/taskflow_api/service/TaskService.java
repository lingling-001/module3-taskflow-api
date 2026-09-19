package sg.edu.ntu.taskflow_api.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import sg.edu.ntu.taskflow_api.repository.TaskRepository;
import sg.edu.ntu.taskflow_api.model.Task;

@Service 
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    public Task createTask(Task task) {
        return taskRepository.createTask(task);
    }

    
    public Map<Long, Task> findAllTasks() {
        return taskRepository.findAllTasks();
    }

    
    public Task findTaskById(Long id) {
        return taskRepository.findTaskById(id);
    }
    

    public Task updateTask(Long id, Task task) {
        return taskRepository.updateTask(id, task);
    }

    
    public void deleteTask(Long id) {
        taskRepository.deleteTask(id);
    }

    
    public boolean markTaskAsComplete(Long id) {
        return taskRepository.markTaskAsComplete(id);
    }
}
