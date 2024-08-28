package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @GetMapping("/api/v2/members")
    public MembersResult<List<MemberDto>> membersV2() {
        List<Member> members = memberService.findMembers();
        List<MemberDto> collect = members.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new MembersResult<>(collect.size(), collect);

    }

    @Data
    @AllArgsConstructor
    public static class MembersResult<T> {
        private Integer count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    public static class MemberDto {
        private String name;
    }

    @Data
    public static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    public static class CreateMemberRequest {
        @NotEmpty
        private String name;
    }

    @Data
    @AllArgsConstructor
    public static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    @Data
    public static class UpdateMemberRequest {
        @NotEmpty
        private String name;
    }
}
