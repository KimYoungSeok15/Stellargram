package com.instargram101.member.api;

import com.instargram101.global.common.response.CommonApiResponse;
import com.instargram101.member.service.FavoriteService;
import com.instargram101.member.service.FavoriteServiceImpl;
import com.instargram101.member.service.MemberService;
import com.instargram101.member.service.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("favorite/")
@Controller
@EnableAspectJAutoProxy
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final MemberService memberService;

    @PostMapping("star/{starId}/like")
    public ResponseEntity<CommonApiResponse> likesStarApi(@RequestHeader("myId") Long myId, @PathVariable Long starId) {
        var member = memberService.getMemberById(myId);
        var favorite = favoriteService.likesStar(member, starId);
        return ResponseEntity.ok(
                CommonApiResponse.OK("favorite well created", favorite)
        );
    }

    @GetMapping("all")
    public ResponseEntity<CommonApiResponse> findAllLikesStarApi(@RequestHeader("myId") Long myId){
        var member = memberService.getMemberById(myId);
        var favorites = favoriteService.findAllLikesStar(member);
        return ResponseEntity.ok(
                CommonApiResponse.OK(favorites)
        );
    }

    @GetMapping("count")
    public ResponseEntity<CommonApiResponse> getStarCountsOfMemberLikeApi(@RequestHeader("myId") Long myId){
        var member = memberService.getMemberById(myId);
        var count = favoriteService.getStarCountsOfMemberLike(member);
        return ResponseEntity.ok(
                CommonApiResponse.OK(count)
        );
    }

    @GetMapping("count/star/{starId}")
    public ResponseEntity<CommonApiResponse> getLikeCountsOfStarApi(@PathVariable Long starId) {
        return ResponseEntity.ok(
                CommonApiResponse.OK(
                        favoriteService.getLikeCountsOfLikeStar(starId)
                )
        );
    }

    @DeleteMapping("star/{starId}/dislike")
    public ResponseEntity<CommonApiResponse> dislikeStarApi(@RequestHeader("myId") Long myId, @PathVariable Long starId){
        var member = memberService.getMemberById(myId);
        favoriteService.dislikesStar(member, starId);
        return ResponseEntity.ok(
                CommonApiResponse.OK(
                        "OK", true
                )
        );
    }
}
