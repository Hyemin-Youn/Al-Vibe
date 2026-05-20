package com.alvibe.qna.service;

import com.alvibe.qna.dto.ChangePasswordRequestDto;
import com.alvibe.qna.dto.MyPageProfileDto;
import com.alvibe.qna.dto.UpdateMyInfoRequestDto;
import com.alvibe.qna.entity.Member;
import com.alvibe.qna.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true) // 읽기 전용
public class MyPageService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyPageService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MyPageProfileDto lookUpMemberByEmail(String email){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다"));

        MyPageProfileDto myPageProfileDto = new MyPageProfileDto();
        myPageProfileDto.setId(member.getId());
        myPageProfileDto.setNickname(member.getNickname());
        myPageProfileDto.setEmail(member.getEmail());
        myPageProfileDto.setCreateAt(member.getCreateAt());

        return myPageProfileDto;
    }

    @Transactional
    public void updateMyInfo(String currentEmail, UpdateMyInfoRequestDto dto){
        Member member = memberRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다"));

        if(!member.getNickname().equals(dto.getNickname())){
            if(memberRepository.existsByNickname(dto.getNickname())){
                throw new IllegalStateException("이미 사용중인 닉네임입니다.");
            }
        }

        if(!member.getEmail().equals(dto.getEmail())) {
            if(memberRepository.existsByEmail(dto.getEmail())){
                throw new IllegalStateException("이미 사용중인 이메일입니다.");
            }
        }

        member.updateProfile(dto.getNickname(), dto.getEmail());
    }

    @Transactional
    public void changePassword(String email, ChangePasswordRequestDto dto){
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("사용자를 찾을 수 없습니다"));

        if(!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
            throw new IllegalStateException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());

        member.changePassword(encodedPassword);
    }
}
