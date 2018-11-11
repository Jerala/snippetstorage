package me.studying.snippethub.controller;

import me.studying.snippethub.service.LikeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class LikesController {

    private LikeServiceImpl likeService;

    @Autowired
    public LikesController(LikeServiceImpl likeService) {
        this.likeService = likeService;
    }

    @RequestMapping(value = "/likes/{userId}-{snippetName}", method = RequestMethod.GET)
    public String isLiked(@PathVariable(name = "userId") Long userId,
                           @PathVariable(name = "snippetName") String snippetName,
                           Principal principal) {
        if(principal == null)
            return "unknown";
        return likeService.isLiked(userId, snippetName, principal.getName());
    }

    @RequestMapping(value = "/likes/changeState/{userId}-{snippetName}",
    method = RequestMethod.GET)
    public String changeState(@PathVariable(name = "userId") Long userId,
                              @PathVariable(name = "snippetName") String snippetName,
                              Principal principal) {
        if(principal == null)
            return "failed";
        return likeService.changeState(userId, snippetName, principal.getName());
    }

}
