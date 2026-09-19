package sg.edu.ntu.taskflow_api.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import sg.edu.ntu.taskflow_api.model.Task;
import sg.edu.ntu.taskflow_api.service.TaskService;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController 
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;
    private final ChatClient chatClient;

    public TaskController(TaskService taskService, ChatClient.Builder chatClientBuilder) {
        this.taskService = taskService;
        this.chatClient = chatClientBuilder.build();
    }
    
    @GetMapping
    public ResponseEntity<Map<Long, Task>> getAllTasks() {
        Map<Long, Task> tasks = taskService.findAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.findTaskById(id);
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updatedTask = taskService.updateTask(id, task);
        if (updatedTask != null) {
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Task> markTaskAsComplete(@PathVariable Long id) {
        boolean completedTask = taskService.markTaskAsComplete(id);
        if (completedTask) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<String> getTasksSummary() {
        Map<Long, Task> tasks = taskService.findAllTasks();

        String taskLines = tasks.values().stream()
                .map(task -> String.format("- id=%d, title=%s, status=%s",
                        task.getId(),
                        task.getTitle() == null ? "(untitled)" : task.getTitle(),
                        task.getStatus() == null ? "pending" : task.getStatus()))
                .collect(Collectors.joining("\n"));

        if (taskLines.isBlank()) {
            taskLines = "- no tasks";
        }

        String prompt = "Summarize the tasks in plain English in 2-3 short sentences. "
                + "Mention what is pending and what is done.\n\nTasks:\n" + taskLines;

        String summary = chatClient.prompt(prompt).call().content();
        return new ResponseEntity<>(summary, HttpStatus.OK);
    }

}
