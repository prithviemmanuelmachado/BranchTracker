package com.example.branch_tracker.branch_tracker.services;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.branch_tracker.branch_tracker.models.BranchCollection;
import com.example.branch_tracker.branch_tracker.models.MessageCollection;
import com.example.branch_tracker.branch_tracker.models.PeerCollection;
import com.example.branch_tracker.branch_tracker.models.StageCollection;
import com.example.branch_tracker.branch_tracker.models.TaskCollection;
import com.example.branch_tracker.branch_tracker.repository.BranchRepository;
import com.example.branch_tracker.branch_tracker.repository.MessageRepository;
import com.example.branch_tracker.branch_tracker.repository.PeerRepository;
import com.example.branch_tracker.branch_tracker.repository.StageRepository;
import com.example.branch_tracker.branch_tracker.repository.TaskRepository;

@Service
public class BranchServices {
    private BranchRepository _branch;
    private MessageRepository _message;
    private PeerRepository _peer;
    private StageRepository _stage;
    private TaskRepository _task;
    private Logger logger;

    public BranchServices(
        BranchRepository _branch,
        MessageRepository _message,
        PeerRepository _peer,
        StageRepository _stage,
        TaskRepository _task
    ){
        this._branch = _branch;
        this._message = _message;
        this._peer = _peer;
        this._stage = _stage;
        this._task = _task;
        this.logger = LoggerFactory.getLogger(BranchServices.class);
    }

    public Map<String, Object> validateBranch(Map<String, Object> inputModel, Map<String, String> decodedToken){
        Map<String, Object> response = new HashMap<>();
        try {
            
            List<BranchCollection> existingBranch = _branch.findByBranchTitle(inputModel.get("branchTitle").toString());
            if(existingBranch.size() == 0){
                response.put("status", "create");
                response.put("message", "Branch does not exist");
                response.put("code", "200");
            } else {

                //send request to join existing brnanch
                BranchCollection branch = existingBranch.getFirst();
                PeerCollection owner = _peer.findByBranchIDAndIsOwnerTrue(branch.getBranchID()).getFirst();
                PeerCollection newPeer = new PeerCollection();
                String newPeerID = UUID.randomUUID().toString();
                newPeer.setPeerID(newPeerID);
                newPeer.setOwner(false);
                newPeer.setStatus("PENDING");
                newPeer.setPeerName(decodedToken.get("name").toString());
                newPeer.setPeerUserID(decodedToken.get("userID").toString());
                newPeer.setBranchID(branch.getBranchID());
                newPeer.setAcitonOn(new Date());
                _peer.save(newPeer);

                response.put("status", "exists");
                response.put("message", "Branch already exists. Sent request to join the branch");
                response.put("ownerID", owner.getPeerID());
                response.put("peerID", newPeerID);
                response.put("peer", decodedToken.get("name").toString());
                response.put("status", "PENDING");
                response.put("code", "200");

            }            
            return response;            

        } catch (Exception e) {
            logger.error("error in validate branch service", e);
            response.put("message", "A problem occured when trying to validate this branch. Please try agian later");
            response.put("code", "500");
            return response;
        }
    }

    public Map<String, Object> addBranch(Map<String, Object> inputModel, Map<String, String> decodedToken){
        Map<String, Object> response = new HashMap<>();
        String branchId = UUID.randomUUID().toString();
        try {

            //create branch
            List<String> stages = Arrays.asList();
            BranchCollection newBranch = new BranchCollection();
            newBranch.setBranchID(branchId);
            newBranch.setBranchTitle(inputModel.get("branchTitle").toString());
            newBranch.setClosed(false);
            newBranch.setStages(stages);
            newBranch.setCreatedBy(decodedToken.get("name").toString());
            newBranch.setCreatedByUserID(decodedToken.get("userID").toString());
            newBranch.setCreatedOn(new Date());
            newBranch.setUserStory(inputModel.get("userStory").toString());
            _branch.save(newBranch);

            //create tasks
            List<String> tasks = (List<String>) inputModel.get("tasks");
            tasks.forEach((task) -> {
                TaskCollection newTask = new TaskCollection();
                newTask.setBranchID(branchId);
                newTask.setTask(task);
                newTask.setTaskID(UUID.randomUUID().toString());
                newTask.setCreatedBy(decodedToken.get("name").toString());
                newTask.setCreatedOn(new Date());
                _task.save(newTask);
            });

            //add owner to peer list
            PeerCollection newPeer = new PeerCollection();
            newPeer.setPeerID(UUID.randomUUID().toString());
            newPeer.setOwner(true);
            newPeer.setStatus("APPROVE");
            newPeer.setPeerName(decodedToken.get("name").toString());
            newPeer.setPeerUserID(decodedToken.get("userID").toString());
            newPeer.setBranchID(branchId);
            newPeer.setAcitonOn(new Date());
            _peer.save(newPeer);

            response.put("branchID", branchId);
            response.put("message", "Branch added successfully");
            response.put("code", "200");
            return response;            

        } catch (Exception e) {
            logger.error("error in add branch service", e);
            _branch.deleteByBranchID(branchId);
            _task.deleteByBranchID(branchId);
            _peer.deleteByBranchID(branchId);
            response.put("message", "A problem occured when trying to add this branch. Please try agian later");
            response.put("code", "500");
            return response;
        }
    }

    public Map<String, Object> getAllBranches(Map<String, Object> inputModel, Map<String, String> decodedToken){
        Map<String, Object> response = new HashMap<>();
        try {
            List<PeerCollection> peers = _peer.findBranchIDsByPeerUserIDSelectFields(decodedToken.get("userID").toString());
            List<String> branchIDs = peers
                                        .stream()
                                        .map(PeerCollection::getBranchID)
                                        .collect(Collectors.toList());
            List<BranchCollection> branches = _branch.findByBranchIDInAndIsClosed(
                                                branchIDs, 
                                                inputModel.get("isClosed").toString().toUpperCase() == "TRUE" ? true: false,
                                                Sort.by(Sort.Order.desc("createdOn")));
            response.put("message", "List of branches retrived");
            response.put("code", "200");
            response.put("branches", branches);
            return response;
        } catch (Exception e) {
            logger.error("error in get all branches service", e);
            response.put("message", "A problem occured when trying to get all branches. Please try agian later");
            response.put("code", "500");
            return response;
        }
    }

    public Map<String, Object> addTask(Map<String, Object> inputModel, Map<String, String> decodedToken){
        Map<String, Object> response = new HashMap<>();
        try {
            TaskCollection newTask = new TaskCollection();
            newTask.setBranchID(inputModel.get("branchID").toString());
            newTask.setTaskID(UUID.randomUUID().toString());
            newTask.setTask(inputModel.get("task").toString());
            newTask.setCreatedBy(decodedToken.get("name").toString());
            newTask.setCreatedOn(new Date());
            _task.save(newTask);
            response.put("message", "Task added");
            response.put("code", "200");
            response.put("status", "success");
            response.put("taskID", newTask.getTaskID());
            response.put("task", newTask.getTask());
            return response;
        } catch (Exception e) {
            logger.error("error in add task service", e);
            response.put("message", "A problem occured when trying to add task details. Please try agian later");
            response.put("code", "500");
            return response;
        }
    }

    public Map<String, Object> getBrancheDetails(Map<String, Object> inputModel, Map<String, String> decodedToken){
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> branch = new HashMap<>();

            //add branch details
            BranchCollection foundBranch = _branch.findByBranchID(inputModel.get("branchID").toString()).getFirst();
            branch.put("branchID", foundBranch.getBranchID());
            branch.put("branchTitle", foundBranch.getBranchTitle());
            branch.put("userStory", foundBranch.getUserStory());
            branch.put("branchID", foundBranch.getBranchID());
            branch.put("stages", foundBranch.getStages());
            
            //add list of peers
            branch.put(
                "listOfPeers", 
                _peer.findByBranchIDAndIsOwnerFalseSelectFields(foundBranch.getBranchID())
            );

            //add list of messges
            branch.put(
                "listOfMessages", 
                _message.findByBranchID(
                    foundBranch.getBranchID(), 
                    Sort.by(Sort.Order.desc("postedOn"))
                )
            );

            //add list of tasks
            branch.put("listOfTasks", _task.findByBranchIDSelectFields(foundBranch.getBranchID()));

            response.put("message", "Branch details retrived");
            response.put("code", "200");
            response.put("branches", branch);
            return response;
        } catch (Exception e) {
            logger.error("error in get all branch service", e);
            response.put("message", "A problem occured when trying to get al branches. Please try agian later");
            response.put("code", "500");
            return response;
        }
    }

    public Map<String, Object> getUsersForBranch(Map<String, Object> inputModel, Map<String, String> decodedToken){
        Map<String, Object> response = new HashMap<>();
        try {
            String branchID = inputModel.get("branchID").toString();
            response.put("status", "success");
            response.put("users",
            _peer
            .findByBranchIDAndNotUserIDSelectFields(branchID, decodedToken.get("userID").toString())
            .stream()
            .map(PeerCollection::getPeerUserID)
            .collect(Collectors.toList()));
            return response;
        } catch (Exception e) {
            logger.error("error in get users for branch service", e);
            response.put("message", "A problem occured when trying to get users for branch. Please try agian later");
            response.put("code", "500");
            return response;
        }
    }

    public Map<String, Object> addMessage(Map<String, Object> inputModel, Map<String, String> decodedToken){
        Map<String, Object> response = new HashMap<>();
        try {
            MessageCollection newMessage = new MessageCollection();
            newMessage.setBranchID(inputModel.get("branchID").toString());
            newMessage.setMessage(inputModel.get("message").toString());
            newMessage.setMessageID(UUID.randomUUID().toString());
            newMessage.setPostedBy(decodedToken.get("userID").toString());
            newMessage.setPostedOn(new Date());
            _message.save(newMessage);

            response.put("status", "success");
            response.put("messageID", newMessage.getMessageID());
            response.put("message", newMessage.getMessage());
            response.put("postedBy", newMessage.getPostedBy());
            response.put("postedOn", newMessage.getPostedOn());
            return response;
        } catch (Exception e) {
            logger.error("error in add message service", e);
            response.put("message", "A problem occured when trying to add message. Please try agian later");
            response.put("code", "500");
            return response;
        }
    }

    public void moveBranch(Map<String, Object> inputModel, Map<String, String> decodedToken){
        try {
            BranchCollection branch = _branch.findById(inputModel.get("branchID").toString()).orElse(null);
            String action = inputModel.get("action").toString().toUpperCase();
            List<StageCollection> stages = _stage.findAll(Sort.by("Order"));
            if(branch != null){
                if(action == "DOWN" && !branch.getStages().isEmpty()){
                    branch.getStages().remove(branch.getStages().size() - 1);
                    _branch.save(branch);
                }
                if(
                    action == "UP" && 
                    !branch.getStages().contains(stages.getLast().getStage())
                ){
                    String latestStage = branch.getStages().get(branch.getStages().size() - 1);
                    StageCollection stage = stages
                                            .stream()
                                            .filter(s -> s.getStage().equalsIgnoreCase(latestStage))
                                            .findFirst()
                                            .orElse(null);
                    if(stage != null){
                        StageCollection nextStage = stages
                                                    .stream()
                                                    .filter(s -> s.getOrder() == (stage.getOrder() + 1))
                                                    .findFirst()
                                                    .orElse(null);
                        if(nextStage != null){
                            List<String> lStages = branch.getStages();
                            lStages.add(nextStage.getStage());
                            branch.setStages(lStages);
                            _branch.save(branch);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("error in move branch service", e);
        }
    }

    public void removeTask(Map<String, Object> inputModel, Map<String, String> decodedToken){
        try {
            _task.deleteById(inputModel.get("taskID").toString());
        } catch (Exception e) {
            logger.error("error in remove task service", e);
        }
    }

    public void peerAction(Map<String, Object> inputModel, Map<String, String> decodedToken){
        try {
            String peerID = inputModel.get("peerID").toString();
            String status = inputModel.get("status").toString().toUpperCase();
            PeerCollection peer = _peer.findById(peerID).orElse(null);
            if (peer != null) {
                peer.setStatus(status);
                _peer.save(peer);
            }
        } catch (Exception e) {
            logger.error("error in get users for branch service", e);
        }
    }

}
