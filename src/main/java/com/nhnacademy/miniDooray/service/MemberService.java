package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.MemberDto;
import com.nhnacademy.miniDooray.common.Status;
import com.nhnacademy.miniDooray.util.RestApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private RestApiClient restApiClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerMember(MemberDto memberDto) {
        memberDto.setStatus(Status.REGISTERED);
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        String url = "http://localhost:8081/member/register";
        ResponseEntity<MemberDto> response = restApiClient.sendPostRequest(url, memberDto, MemberDto.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            // Registration successful
        } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
            throw new RuntimeException("Member ID already exists");
        } else {
            throw new RuntimeException("Failed to register member");
        }
    }
}
