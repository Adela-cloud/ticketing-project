package com.cydeo.service.impl;

import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl extends AbstractMapService<TaskDTO, Long> implements TaskService {
    @Override
    public TaskDTO save(TaskDTO task) {
        if(task.getTaskStatus()==null){
            task.setTaskStatus(Status.OPEN);
        }
        if(task.getAssignedDate()==null){
            task.setAssignedDate(LocalDate.now());
        }
        if (task.getId()==null){//there is no id field in the form, so we need to create manually
            task.setId(UUID.randomUUID().getMostSignificantBits());
        }

        return super.save(task.getId(), task);
    }

    @Override
    public TaskDTO findById(Long taskId) {
        return super.findById(taskId);
    }

    @Override
    public List<TaskDTO> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(Long taskId) {
        super.deleteById(taskId);
    }

    @Override
    public void update(TaskDTO task) {
        TaskDTO foundTask= findById(task.getId());

        task.setTaskStatus(foundTask.getTaskStatus());
        task.setAssignedDate(foundTask.getAssignedDate());

        super.update(task.getId(), task);
    }

    @Override
    public List<TaskDTO> findTaskByManager(UserDTO manager) {
         return  findAll()
                .stream()
                .filter(task-> task.getProject().getAssignedManager().equals(manager))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> findAllTasksByStatusIsNot(Status status) {
        return findAll()
                .stream()
                .filter(task -> !task.getTaskStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> findAllTasksByStatus(Status status) {
        return findAll()
                .stream()
                .filter(task-> task.getTaskStatus().equals(status))
                .collect(Collectors.toList());
    }

    @Override
    public void updateStatus(TaskDTO task) {
        findById(task.getId()).setTaskStatus(task.getTaskStatus()); //first, status is updated
        update(task);        // second, task is updated with new status information
    }


}
