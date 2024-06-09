package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.Status;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl extends AbstractMapService<ProjectDTO, String> implements ProjectService {

    private final TaskService taskService;

    public ProjectServiceImpl(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO save(ProjectDTO project) {
        if(project.getProjectStatus()==null) {
            project.setProjectStatus(Status.OPEN);
        }
        return super.save(project.getProjectCode(), project);
    }

    @Override
    public ProjectDTO findById(String projectCode) {
        return super.findById(projectCode);
    }

    @Override
    public List<ProjectDTO> findAll() {
        return super.findAll();
    }

    @Override
    public void deleteById(String projectCode) {
        super.deleteById(projectCode);
    }

    @Override
    public void update(ProjectDTO project) {
        if(project.getProjectStatus()== null){
            project.setProjectStatus(findById(project.getProjectCode()).getProjectStatus());
        }
        super.update(project.getProjectCode(), project);
    }

    @Override
    public void complete(ProjectDTO project) {
        project.setProjectStatus(Status.COMPLETE);

    }

    @Override
    public List<ProjectDTO> getCountedListOfProjectDTO(UserDTO manager) {

        List<ProjectDTO>  projectList=
                findAll()
                        .stream()
                        .filter(project ->project.getAssignedManager().equals(manager))
                        .map(project -> { //used to do sth to each project

                            List<TaskDTO>  taskList= taskService.findTaskByManager(manager);

                            int unfinishedTaskCount = (int) taskList.stream().filter(t-> t.getProject().equals(project)
                                    && t.getTaskStatus()!=Status.COMPLETE).count();
                            int completeTaskCount= (int) taskList.stream().filter(t->t.getProject().equals(project)
                                    && t.getTaskStatus()==Status.COMPLETE).count();

                            project.setCompleteTaskCounts(completeTaskCount);
                            project.setUnfinishedTaskCounts(unfinishedTaskCount);

                            return project;
                        })
                        .collect(Collectors.toList());
        return projectList;
    }
}
