package com.alvibe.qna.service;

import com.alvibe.qna.dto.MemberSignupDto;
import com.alvibe.qna.entity.Member;
import com.alvibe.qna.entity.MemberRole;
import com.alvibe.qna.entity.MemberStatus;
import com.alvibe.qna.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional  // DB 적용
    public Member signup(MemberSignupDto memberSignupDto){
        Member member = Member.builder()
                .email(memberSignupDto.getEmail())
                .password((passwordEncoder.encode(memberSignupDto.getPassword())))
                .nickname(memberSignupDto.getNickname())
                .role(MemberRole.USER)
                .status(MemberStatus.ACTIVE)
                .build();

        return memberRepository.save(member);
    }

    public boolean existsByEmail(String email){
       return memberRepository.existsByEmail(email);
    }

    public boolean existsByNickname(String nickname){
        return memberRepository.existsByNickname(nickname);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();
    }
}
