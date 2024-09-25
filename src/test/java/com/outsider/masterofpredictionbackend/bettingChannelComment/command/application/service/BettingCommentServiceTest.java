//package com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.service;
//
//import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto.BettingCommentAddRequestDTO;
//import com.outsider.masterofpredictionbackend.bettingChannelComment.command.application.dto.BettingCommentUpdateRequestDTO;
//import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.BettingChannelComment;
//import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded.Content;
//import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded.WriterInfo;
//import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.repository.BettingChannelCommentRepository;
//import com.outsider.masterofpredictionbackend.bettingChannelComment.command.exception.BettingCommentNotFoundException;
//import com.outsider.masterofpredictionbackend.common.exception.MisMatchUserException;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.provider.Arguments;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class BettingCommentServiceTest {
//
//    @Autowired
//    private BettingCommentService service;
//
//    @Autowired
//    private BettingChannelCommentRepository repository;
//
//
//    private static Stream<Arguments> updateFailInput(){
//        return Stream.of(
//                Arguments.of(1L, "댓글 내용1", "url1", 999L, 1L, MisMatchUserException.class),
//                Arguments.of(1L, "댓글 내용1", "url1", 1L, 1L, BettingCommentNotFoundException.class)
//        );
//    }
//
//
//    @Test
//    @DisplayName("댓글 추가")
//    void addComment() {
//        // given
//        long bettingId=1L;
//        String comment="댓글";
//        String url="url";
//        long userId = 1L;
//        BettingCommentAddRequestDTO expect = new BettingCommentAddRequestDTO();
//        expect.setBettingId(bettingId);
//        expect.setComment(comment);
//        expect.setUserNo(userId);
//        expect.setImageUrl(url);
//
//        // when
//        Long created = service.addComment(expect);
//        // then
//
//        BettingChannelComment findComment =  repository.findById(created).get();
//        BettingCommentAddRequestDTO actual = new BettingCommentAddRequestDTO();
//        actual.setBettingId(findComment.getBettingChannelId());
//        actual.setComment(findComment.getContent().getComment());
//        actual.setUserNo(findComment.getWriter().getWriterNo());
//        actual.setImageUrl(findComment.getContent().getImageUrl());
//
//        assertEquals(expect, actual);
//    }
//
//    @Test
//    @DisplayName("댓글 수정 - 성공")
//    void updateComment_성공() {
//        // given
//
//        /*수정할 댓글*/
//        long bettingId=1L;
//        String comment="댓글";
//        String url="url";
//        long userId = 1L;
//        BettingChannelComment target = new BettingChannelComment(
//                bettingId,
//                new Content("123", "123"),
//                new WriterInfo(userId)
//        );
//        BettingChannelComment saved = repository.save(target);
//
//        // when
//
//        /*수정 할 내용 작성*/
//        BettingCommentUpdateRequestDTO expect = new BettingCommentUpdateRequestDTO();
//        expect.setCommentId(saved.getId());
//        expect.setUpdatedComment(comment);
//        expect.setUpdatedImageUrl(url);
//
//        /*댓글 수정*/
//        Boolean isUpdate = service.updateComment(expect);
//
//        /*db에서 수정된 댓글 객체 가져옴*/
//        BettingChannelComment updatedComment = repository.findById(expect.getCommentId()).get();
//
//        /*dto로 변환*/
//        BettingCommentUpdateRequestDTO actual = new BettingCommentUpdateRequestDTO();
//        actual.setCommentId(updatedComment.getId());
//        actual.setUpdatedComment(updatedComment.getContent().getComment());
//        actual.setUpdatedImageUrl(updatedComment.getContent().getImageUrl());
//
//
//        // then
//        assertTrue(isUpdate);
//        assertEquals(expect, actual);
//
//    }
//
//    @DisplayName("댓글 수정 - 사용자 다름")
//    @Test
//    void updateComment_사용자_다를때() {
//        // given
//        /*수정할 댓글*/
//        long bettingId=1L;
//        long userId = 999L;
//        BettingChannelComment target = new BettingChannelComment(
//                bettingId,
//                new Content("123", "123"),
//                new WriterInfo(userId)
//        );
//        BettingChannelComment saved = repository.save(target);
//
//        // when
//
//        /*수정 할 내용 작성*/
//        BettingCommentUpdateRequestDTO updated = new BettingCommentUpdateRequestDTO();
//        updated.setCommentId(saved.getId());
//        updated.setUpdatedComment("업데이트");
//        updated.setUpdatedImageUrl("업데이트");
//
//        // then
//        assertThrows(MisMatchUserException.class, () ->
//            service.updateComment(updated) );
//    }
//
//    @DisplayName("댓글 수정 - 없는 댓글 수정할때")
//    @Test
//    void updateComment_없는댓글_수정() {
//        // given
//        /*수정할 댓글*/
//        long bettingId=1L;
//        long userId = 1L;
//        BettingChannelComment target = new BettingChannelComment(
//                bettingId,
//                new Content("123", "123"),
//                new WriterInfo(userId)
//        );
//        BettingChannelComment saved = repository.save(target);
//
//        // when
//
//        /*수정 할 내용 작성*/
//        BettingCommentUpdateRequestDTO updated = new BettingCommentUpdateRequestDTO();
//        updated.setCommentId(saved.getId()+999L);
//        updated.setUpdatedComment("업데이트");
//        updated.setUpdatedImageUrl("업데이트");
//
//        // then
//        assertThrows(BettingCommentNotFoundException.class, () ->
//                service.updateComment(updated)
//        );
//    }
//
//    @DisplayName("댓글 삭제 - 정상 삭제")
//    @Test
//    void deleteComment() {
//        // given
//        long bettingId=1L;
//        long userId = 1L;
//        BettingChannelComment target = new BettingChannelComment(
//                bettingId,
//                new Content("123", "123"),
//                new WriterInfo(userId)
//        );
//        BettingChannelComment saved = repository.save(target);
//
//
//        // when
//        service.deleteComment(saved.getId());
//
//        // then
//        assertNotNull(
//                repository.findById(saved.getId())
//                .get()
//                .getDeletedAt()
//        );
//    }
//
//    @DisplayName("댓글 삭제 - 삭제할 사람과 댓글 작성자가 다를때")
//    @Test
//    void deleteComment_사용자_다름() {
//        // given
//        /*삭제 댓글*/
//        long bettingId=1L;
//        long userId = 999L;
//        BettingChannelComment target = new BettingChannelComment(
//                bettingId,
//                new Content("123", "123"),
//                new WriterInfo(userId)
//        );
//        BettingChannelComment saved = repository.save(target);
//
//        // when
//        // then
//        assertThrows(MisMatchUserException.class, () ->
//                service.deleteComment(saved.getId())
//        );
//    }
//
//    @DisplayName("댓글 삭제 - 정상 삭제")
//    @Test
//    void deleteComment_없는댓글_삭제() {
//        // given
//        long bettingId=1L;
//        long userId = 1L;
//        BettingChannelComment target = new BettingChannelComment(
//                bettingId,
//                new Content("123", "123"),
//                new WriterInfo(userId)
//        );
//        BettingChannelComment saved = repository.save(target);
//
//        // when
//        // then
//        assertThrows(BettingCommentNotFoundException.class, () ->
//                service.deleteComment(saved.getId()+999L) );
//    }
//}