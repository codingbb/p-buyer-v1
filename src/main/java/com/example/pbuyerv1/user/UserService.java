package com.example.pbuyerv1.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepo;

    //로그인
    public User login(UserRequest.LoginDTO requestDTO) {
        User user = userRepo.findByUsernameAndPassword(requestDTO);
        return user;
    }


    //회원가입
    @Transactional
    public void save(UserRequest.JoinDTO requestDTO) {
        userRepo.save(requestDTO);
    }

}
