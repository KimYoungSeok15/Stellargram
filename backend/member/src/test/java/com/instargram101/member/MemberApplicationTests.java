package com.instargram101.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.instargram101.member.entity.Member;
import com.instargram101.member.repoository.MemberRepository;
import com.instargram101.member.service.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import org.junit.jupiter.api.DisplayName;
//import org.springframework.test.web.servlet.ResultActions;
//import java.util.Optional;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class MemberApplicationTests {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	MemberServiceImpl memberService;

	@Autowired
	MemberRepository memberRepository;

	@BeforeEach
	public void setMockMvc() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
				.build();
		memberRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}


//	@Test
//	@DisplayName("checkMember : 회원 여부 확인")
//	void checkByIdTest() throws Exception {
//		final String url = "/member/check";
//		final Long memberId = Long.valueOf('1');
//		final boolean activated = true;
//		final String nickname = "닉네임";
//		final String profileImageUrl = "프로필이미지url";
//		final Long followCount = Long.valueOf('0');
//		final Long followingCount = Long.valueOf('0');
//		final Long cardCount = Long.valueOf('0');
//
//		Member savedMember = memberRepository.save(Member.builder()
//				.memberId(memberId)
//				.activated(activated)
//				.nickname(nickname)
//				.profileImageUrl(profileImageUrl)
//				.followCount(followCount)
//				.followingCount(followingCount)
//				.cardCount(cardCount)
//				.build());
//		final ResultActions resultActions = mockMvc.perform(get(url, savedMember.getMemberId()));
//
//		resultActions
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.isSuccess").value(true));
//	}


}
