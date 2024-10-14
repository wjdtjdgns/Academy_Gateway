package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.MemberDto;
import com.nhnacademy.miniDooray.common.Status;
import com.nhnacademy.miniDooray.dto.MemberInfoDto;
import com.nhnacademy.miniDooray.dto.MemberRequest;
import com.nhnacademy.miniDooray.dto.MilestoneDto;
import com.nhnacademy.miniDooray.util.RestApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class MemberService {

    @Autowired
    private RestApiClient restApiClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerMember(MemberDto memberDto) {
        memberDto.setStatus(Status.REGISTERED);
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));

        String url = "http://localhost:8081/members/register";
        ResponseEntity<MemberDto> response = restApiClient.sendPostRequest(url, memberDto, MemberDto.class);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            // Registration successful
        } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
            throw new RuntimeException("Member ID already exists");
        } else {
            throw new RuntimeException("Failed to register member");
        }
    }

    public boolean isExistMembers(String userId, List<String> memberIds) {
        try {
            String url = "http://localhost:8081/members/lookup";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);

            MemberRequest memberRequest = new MemberRequest();
            memberRequest.setMemberIds(memberIds);

            ResponseEntity<List<MemberInfoDto>> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.POST,
                    memberRequest,
                    headers,
                    (Class<List<MemberInfoDto>>) (Class<?>) List.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return true;
            }

            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
