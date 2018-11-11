package me.studying.snippethub.service;

import me.studying.snippethub.dao.SnippetsDAO;
import me.studying.snippethub.entity.Snippets;
import me.studying.snippethub.entity.Users;
import me.studying.snippethub.repositories.SnippetsRepository;
import me.studying.snippethub.repositories.UsersRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl {

    private UsersRepository usersRepository;
    private SnippetsRepository snippetsRepository;
    private SnippetsServiceImpl snippetsService;

    @Autowired
    public AdminServiceImpl(UsersRepository usersRepository,
                            SnippetsRepository snippetsRepository,
                            SnippetsServiceImpl snippetsService) {
        this.usersRepository = usersRepository;
        this.snippetsRepository = snippetsRepository;
        this.snippetsService = snippetsService;
    }

    public List<Users> getUsers() {
        List<Users> list = new ArrayList<>();
        this.usersRepository.findAll().forEach(list::add);
        return list;
    }

    public boolean deleteUserById(Long id) {
        try {
            this.usersRepository.deleteById(id);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    public String approveSnippet(Long userId, String snippet_name) {
        Long snippet_id = snippetsService.getSnippetId(userId, snippet_name);
        Snippets snippet = this.snippetsRepository.findById(snippet_id).orElse(null);
        if(snippet == null)
            return "failed";
        snippet.setApproved(1);
        try {
            this.snippetsRepository.save(snippet);
        } catch (Exception e) {
            System.out.println(e);
            return "failed";
        }
        return "success";
    }

    public String updateSnippet(Snippets snippet) {
        Long id = snippet.getSnippetId();
        Snippets existingSnippet = this.snippetsRepository
                .findById(id).orElse(null);
        if(existingSnippet == null)
            return "failed";
        try {
            this.snippetsRepository.save(snippet);
        } catch (Exception e) {
            System.out.println(e);
            return "failed";
        }
        return "success";
    }

    public String deleteSnippet(Long userId, String snippet_name) {
        Long snippet_id = snippetsService.getSnippetId(userId, snippet_name);
        this.snippetsRepository.deleteById(snippet_id);
        return this.snippetsRepository.findById(snippet_id) == null ? "success" : "failed";
    }

//    public String forbidUserById(Long user_id) {
//        Users user = this.usersRepository.findById(user_id).orElse(null);
//        if(user == null)
//            return "failed";
//        user.setEnabled();
//    }
}
