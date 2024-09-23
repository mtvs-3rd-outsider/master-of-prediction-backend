package com.outsider.masterofpredictionbackend.categoryChannelComment.command.application;

import com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.dto.CategoryChannelCommentAddRequestDTO;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.dto.CategoryChannelCommentDeleteRequestDTO;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.dto.CategoryChannelCommentUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.application.service.CategoryChannelCommentService;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.CategoryChannelComment;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.embedded.Content;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.embedded.WriterInfo;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.repository.CategoryChannelCommentRepository;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.service.dto.LoginUserInfo;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.exception.CategoryChannelCommentNotFoundException;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.exception.CategoryChannelCommentPasswordMisMatchException;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.exception.CategoryChannelCommentPasswordNotFoundException;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.infrastructure.service.CategoryChannelCommentPolicyImpl;
import com.outsider.masterofpredictionbackend.common.exception.MisMatchUserException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryChannelCommentServiceTest {

    @Mock
    private CategoryChannelCommentRepository repository;

    private CategoryChannelCommentPolicyImpl policy;

    @InjectMocks
    private CategoryChannelCommentService service;

    @BeforeEach
    void setUp() {
        // 정책 의존성 주입
        CategoryChannelCommentPolicyImpl realPolicy = new CategoryChannelCommentPolicyImpl(repository);

        // 정책은 spy로 설정
        this.policy = spy(realPolicy);

        // 서비스를 주입함
        this.service = new CategoryChannelCommentService(policy, repository);
    }


    /*
     * addComment 테스트 항목:
     * - 정상 생성
     *   - 로그인시 정상 생성
     *   - 비 로그인시 정상 생성
     * - 생성 실패
     *   - 비 로그인:
     *       - 비밀번호가 없을때
     * */
    @Nested
    @DisplayName("댓글 생성 테스트")
    class AddCommentTest {
        @Nested
        @DisplayName("생성 성공")
        class SuccessTest{
            @Test
            @DisplayName("로그인 시 정상 생성")
            void addComment_loggedIn_success() {
                // Given
                CategoryChannelCommentAddRequestDTO requestDTO = new CategoryChannelCommentAddRequestDTO();
                requestDTO.setChannelId(1L);
                requestDTO.setComment("댓글 내용");
                requestDTO.setImageUrl("image.jpg");
                requestDTO.setPassword(null);

                LoginUserInfo loginUserInfo = new LoginUserInfo();
                loginUserInfo.setUserNo(1L);
                loginUserInfo.setUserName("사용자");
                loginUserInfo.setIsAdmin(false);

                CategoryChannelComment comment = new CategoryChannelComment(
                        new WriterInfo(1L, "사용자", true),
                        new Content("댓글 내용", "image.jpg", null)
                );

                when(policy.isLogin()).thenReturn(true);
                when(policy.getLoginUserInfo()).thenReturn(loginUserInfo);
                when(repository.save(any(CategoryChannelComment.class))).thenReturn(comment);

                // When
                service.addComment(requestDTO);

                // Then
                verify(repository).save(any(CategoryChannelComment.class));
            }

            @Test
            @DisplayName("비 로그인 시 정상 생성")
            void addComment_notLoggedIn_success() {
                // Given
                CategoryChannelCommentAddRequestDTO requestDTO = new CategoryChannelCommentAddRequestDTO();
                requestDTO.setChannelId(1L);
                requestDTO.setComment("댓글 내용");
                requestDTO.setImageUrl("image.jpg");
                requestDTO.setPassword("password");

                WriterInfo anonymousUser = new WriterInfo(
                        null,
                        "익명 유저",
                        false
                );

                CategoryChannelComment comment = new CategoryChannelComment(
                        anonymousUser,
                        new Content("댓글 내용", "image.jpg", "password")
                );

                when(policy.isLogin()).thenReturn(false);
                when(repository.save(any(CategoryChannelComment.class))).thenReturn(comment);

                // When
                service.addComment(requestDTO);

                // Then
                verify(repository).save(any(CategoryChannelComment.class));
            }
        }

        @Nested
        @DisplayName("생성 실패")
        class FailTest{

            @Nested
            @DisplayName("비 로그인시")
            class NotLoggedIn{

                private static Stream<Arguments> nonLoginFailCase_PasswordBlank(){
                    CategoryChannelCommentAddRequestDTO passwordIsNull = new CategoryChannelCommentAddRequestDTO();
                    passwordIsNull.setComment("comment");
                    passwordIsNull.setPassword(null);
                    passwordIsNull.setChannelId(1L);
                    passwordIsNull.setImageUrl("imageUrl");

                    CategoryChannelCommentAddRequestDTO passwordIsBlank = new CategoryChannelCommentAddRequestDTO();
                    passwordIsNull.setComment("comment");
                    passwordIsNull.setPassword("");
                    passwordIsNull.setChannelId(1L);
                    passwordIsNull.setImageUrl("imageUrl");

                    CategoryChannelCommentAddRequestDTO passwordIsSpace = new CategoryChannelCommentAddRequestDTO();
                    passwordIsNull.setComment("comment");
                    passwordIsNull.setPassword(" ");
                    passwordIsNull.setChannelId(1L);
                    passwordIsNull.setImageUrl("imageUrl");

                    return Stream.of(
                            Arguments.of(passwordIsNull, "비밀번호가 null일때"),
                            Arguments.of(passwordIsBlank, "비밀번호가 공백일때"),
                            Arguments.of(passwordIsSpace, "비밀번호가 스페이스바(빈 문자열) 일때")
                    );
                }

                @DisplayName("생성 실패 케이스")
                @ParameterizedTest(name = "{1}")
                @MethodSource("nonLoginFailCase_PasswordBlank")
                void addComment_비로그인시_비번_없을때(CategoryChannelCommentAddRequestDTO requestDTO, String scenario) {

                    when(policy.isLogin()).thenReturn(false);

                    // When, Then
                    Assertions.assertThrows(CategoryChannelCommentPasswordNotFoundException.class, () -> service.addComment(requestDTO));
                }

            }

        }
    }

    /*
     * update comment 테스트 항목:
     * - 정상 수정
     *   - 로그인시 정상 수정
     *   - 비 로그인시 정상 수정
     *   - 어드민이 수정(로그인, 비 로그인)
     * - 수정 실패
     *   - 로그인:
     *       - 작성자와 로그인 한 사람이 다를때
     *       - 없는 댓글을 수정하려 할때
     *   - 비 로그인:
     *       - 비번이 없을때
     *       - 비번이 틀렸을때
     *       - 없는 댓글을 수정하려 할때
     * */
    @Nested
    @DisplayName("댓글 수정 테스트")
    class UpdateComment {

        @Nested
        @DisplayName("성공 케이스")
        class SuccessTest{

            @Test
            @DisplayName("로그인시 정상 수정")
            void 로그인시_수정(){
                CategoryChannelCommentUpdateRequestDTO update = new CategoryChannelCommentUpdateRequestDTO();
                update.setCommentId(1L);
                update.setUpdatedComment("댓글 업데이트");
                update.setUpdatedImageUrl("update.jpg");

                LoginUserInfo loginUserInfo = new LoginUserInfo();
                loginUserInfo.setUserNo(1L);
                loginUserInfo.setUserName("사용자");
                loginUserInfo.setIsAdmin(false);

                CategoryChannelComment comment = new CategoryChannelComment(
                        new WriterInfo(1L, "사용자", true),
                        new Content("업데이트 이전", "image.jpg", null)
                );

                when(repository.findById(1L)).thenReturn(Optional.of(comment));
                when(policy.isLogin()).thenReturn(true);
                when(policy.getLoginUserInfo()).thenReturn(loginUserInfo);
                when(repository.save(any(CategoryChannelComment.class))).thenReturn(comment);

                service.updateComment(update);

                verify(policy, times(1)).getCommentById(any(Long.class));
                verify(repository).save(any(CategoryChannelComment.class));
            }

            @Test
            @DisplayName("비 로그인시 정상 수정")
            void 비_로그인_수정(){

                CategoryChannelCommentUpdateRequestDTO update = new CategoryChannelCommentUpdateRequestDTO();
                update.setCommentId(1L);
                update.setUpdatedComment("댓글 업데이트");
                update.setUpdatedImageUrl("update.jpg");
                update.setPassword("123");


                CategoryChannelComment comment = new CategoryChannelComment(
                        policy.generateAnonymousUser(),
                        new Content("업데이트 이전", "image.jpg", "123")
                );

                when(repository.findById(1L)).thenReturn(Optional.of(comment));
                when(policy.isLogin()).thenReturn(false);
                when(repository.save(any(CategoryChannelComment.class))).thenReturn(comment);

                service.updateComment(update);

                verify(policy).getCommentById(any(Long.class));
                verify(repository).save(any(CategoryChannelComment.class));
            }

            private static Stream<Arguments> adminUpdateSuccessCase(){
                CategoryChannelCommentUpdateRequestDTO nonLoginComment = new CategoryChannelCommentUpdateRequestDTO();
                nonLoginComment.setCommentId(1L);
                nonLoginComment.setPassword("password");
                nonLoginComment.setUpdatedComment("익명 댓글");
                nonLoginComment.setUpdatedImageUrl("imageUrl");

                CategoryChannelCommentUpdateRequestDTO myComment = new CategoryChannelCommentUpdateRequestDTO();
                myComment.setCommentId(1L);
                myComment.setPassword("password");
                myComment.setUpdatedComment("관리자가 쓴 댓글");
                myComment.setUpdatedImageUrl("imageUrl");

                CategoryChannelCommentUpdateRequestDTO userComment = new CategoryChannelCommentUpdateRequestDTO();
                userComment.setCommentId(1L);
                userComment.setPassword(null);
                userComment.setUpdatedComment("일반 사용자 댓글");
                userComment.setUpdatedImageUrl("imageUrl");

                return Stream.of(
                        Arguments.of(nonLoginComment, "익명댓글 수정"),
                        Arguments.of(myComment, "관리자가 직접 쓴 댓글 수정"),
                        Arguments.of(userComment, "로그인 한 사용자 댓글 수정")
                );
            }

            @DisplayName("관리자 수정")
            @ParameterizedTest(name = "{1}")
            @MethodSource("adminUpdateSuccessCase")
            void 관리자_수정(CategoryChannelCommentUpdateRequestDTO update, String cause){

                LoginUserInfo loginUserInfo = new LoginUserInfo();
                loginUserInfo.setUserNo(2L);
                loginUserInfo.setUserName("관리자");
                loginUserInfo.setIsAdmin(true);

                CategoryChannelComment comment = new CategoryChannelComment(
                        new WriterInfo(1L, "사용자", true),
                        new Content("업데이트 이전", "image.jpg", null)
                );

                when(repository.findById(1L)).thenReturn(Optional.of(comment));
                when(policy.isLogin()).thenReturn(true);
                when(policy.getLoginUserInfo()).thenReturn(loginUserInfo);
                when(repository.save(any(CategoryChannelComment.class))).thenReturn(comment);

                service.updateComment(update);

                verify(policy, times(1)).getCommentById(any(Long.class));
                verify(repository).save(any(CategoryChannelComment.class));
            }
        }

        @Nested
        @DisplayName("실패 케이스")
        class FailTest{

            private static Stream<Arguments> LoginFailCase(){
                CategoryChannelCommentUpdateRequestDTO passwordIsNull = new CategoryChannelCommentUpdateRequestDTO();
                passwordIsNull.setUpdatedComment("update comment");
                passwordIsNull.setPassword(null);
                passwordIsNull.setCommentId(1L);
                passwordIsNull.setUpdatedImageUrl("update image");

                CategoryChannelCommentUpdateRequestDTO passwordIsBlank = new CategoryChannelCommentUpdateRequestDTO();
                passwordIsBlank.setUpdatedComment("update comment");
                passwordIsBlank.setPassword("");
                passwordIsBlank.setCommentId(1L);
                passwordIsBlank.setUpdatedImageUrl("update image");

                CategoryChannelCommentUpdateRequestDTO passwordIsSpace = new CategoryChannelCommentUpdateRequestDTO();
                passwordIsSpace.setUpdatedComment("update comment");
                passwordIsSpace.setPassword("  ");
                passwordIsSpace.setCommentId(1L);
                passwordIsSpace.setUpdatedImageUrl("update image");

                CategoryChannelCommentUpdateRequestDTO wrongPassword = new CategoryChannelCommentUpdateRequestDTO();
                wrongPassword.setUpdatedComment("update comment");
                wrongPassword.setPassword("wrong password");
                wrongPassword.setCommentId(1L);
                wrongPassword.setUpdatedImageUrl("update image");

                return Stream.of(
                        Arguments.of(passwordIsNull, "비밀번호가 null일때"),
                        Arguments.of(passwordIsBlank, "비밀번호가 공백일때"),
                        Arguments.of(passwordIsSpace, "비밀번호가 스페이스바(빈 문자열) 일때"),
                        Arguments.of(wrongPassword, "비밀번호가 틀렸을때")
                );
            }

            @Nested
            @DisplayName("로그인 한 경우")
            class LoginCase{
                @DisplayName("작성자와 수정하는 사용자가 다를때")
                @Test
                void 로그인_사용자_다름(){
                    CategoryChannelCommentUpdateRequestDTO dto = new CategoryChannelCommentUpdateRequestDTO();
                    dto.setUpdatedComment("update");
                    dto.setCommentId(1L);
                    dto.setUpdatedImageUrl("update.jpg");
                    dto.setPassword(null);


                    LoginUserInfo loginUserInfo = new LoginUserInfo();
                    loginUserInfo.setUserNo(2L);
                    loginUserInfo.setUserName("권한 없는 사용자");
                    loginUserInfo.setIsAdmin(false);

                    CategoryChannelComment comment = new CategoryChannelComment(
                            new WriterInfo(1L, "사용자", true),
                            new Content("업데이트 이전", "image.jpg", null)
                    );

                    when(repository.findById(1L)).thenReturn(Optional.of(comment));
                    when(policy.isLogin()).thenReturn(true);
                    when(policy.getLoginUserInfo()).thenReturn(loginUserInfo);

                    Assertions.assertThrows(MisMatchUserException.class, () -> service.updateComment(dto));

                    verify(policy, times(1)).getCommentById(any(Long.class));
                }

                @DisplayName("없는 댓글을 수정하려 할때")
                @Test
                void 없는_댓글_수정(){
                    CategoryChannelCommentUpdateRequestDTO dto = new CategoryChannelCommentUpdateRequestDTO();
                    dto.setUpdatedComment("update");
                    dto.setCommentId(999L);
                    dto.setUpdatedImageUrl("update.jpg");
                    dto.setPassword(null);


                    LoginUserInfo loginUserInfo = new LoginUserInfo();
                    loginUserInfo.setUserNo(2L);
                    loginUserInfo.setUserName("사용자");
                    loginUserInfo.setIsAdmin(false);

                    CategoryChannelComment comment = new CategoryChannelComment(
                            new WriterInfo(999L, "사용자", true),
                            new Content("삭제된 댓글", "image.jpg", null)
                    );

                    comment.setDeletedAt(LocalDateTime.now());

                    when(repository.findById(999L)).thenReturn(Optional.of(comment));
                    when(policy.isLogin()).thenReturn(true);
                    when(policy.getLoginUserInfo()).thenReturn(loginUserInfo);

                    Assertions.assertThrows(CategoryChannelCommentNotFoundException.class, () -> service.updateComment(dto));

                    verify(policy, times(1)).getCommentById(any(Long.class));
                }
            }


            /*
            * - 비번이 없을때
            * - 비번이 틀렸을때
            * */
            @DisplayName("비 로그인시")
            @ParameterizedTest(name = "{1}")
            @MethodSource("LoginFailCase")
            void 비로그인_실패(CategoryChannelCommentUpdateRequestDTO dto, String cause){

                CategoryChannelComment comment = new CategoryChannelComment(
                        policy.generateAnonymousUser(),
                        new Content("업데이트 이전", "image.jpg", "password")
                );

                when(repository.findById(1L)).thenReturn(Optional.of(comment));
                when(policy.isLogin()).thenReturn(false);

                Assertions.assertThrows(CategoryChannelCommentPasswordMisMatchException.class, () -> service.updateComment(dto));

                verify(policy, times(1)).getCommentById(any(Long.class));
            }


            @DisplayName("비 로그인: 없는 댓글을 수정하려 할때")
            @Test
            void 없는_댓글_수정() {
                CategoryChannelCommentUpdateRequestDTO dto = new CategoryChannelCommentUpdateRequestDTO();
                dto.setUpdatedComment("update");
                dto.setCommentId(999L);
                dto.setUpdatedImageUrl("update.jpg");
                dto.setPassword(null);

                CategoryChannelComment comment = new CategoryChannelComment(
                        policy.generateAnonymousUser(),
                        new Content("삭제된 댓글", "image.jpg", null)
                );

                comment.setDeletedAt(LocalDateTime.now());

                when(repository.findById(999L)).thenReturn(Optional.of(comment));
                when(policy.isLogin()).thenReturn(false);

                Assertions.assertThrows(CategoryChannelCommentNotFoundException.class, () -> service.updateComment(dto));

                verify(policy, times(1)).getCommentById(any(Long.class));
            }

        }
    }

    /*
    * deleteComment 테스트 항목
    * 정상삭제
    * - 로그인시
    * - 비 로그인시
    * - 관리자 삭제
    *
    * 비정상 삭제
    * - 이미 삭제됬거나 없는 댓글에 접근
    * - 비 로그인: 비번 틀림
    * - 로그인: 사용자가 다름
    * */
    @Nested
    @DisplayName("댓글 삭제 테스트")
    class DeleteComment{

        @Nested
        @DisplayName("삭제 성공")
        class SuccessCase{

            @Test
            @DisplayName("로그인시 정상 삭제")
            void login(){
                CategoryChannelCommentDeleteRequestDTO dto = new CategoryChannelCommentDeleteRequestDTO();
                dto.setCommentId(1L);


                LoginUserInfo loginUserInfo = new LoginUserInfo();
                loginUserInfo.setUserNo(1L);
                loginUserInfo.setUserName("사용자");
                loginUserInfo.setIsAdmin(false);

                CategoryChannelComment comment = new CategoryChannelComment(
                        new WriterInfo(1L, "사용자", true),
                        new Content("삭제할 댓글", "image.jpg", null)
                );

                when(repository.findById(1L)).thenReturn(Optional.of(comment));
                when(policy.isLogin()).thenReturn(true);
                when(policy.getLoginUserInfo()).thenReturn(loginUserInfo);

                service.deleteComment(dto);

                verify(policy, times(1)).getCommentById(any(Long.class));
                verify(repository, times(1)).save(any(CategoryChannelComment.class));
            }

            @DisplayName("비 로그인시 정상 삭제")
            @Test
            void nonLogin(){
                CategoryChannelCommentDeleteRequestDTO dto = new CategoryChannelCommentDeleteRequestDTO();
                dto.setCommentId(1L);
                dto.setPassword("password");

                CategoryChannelComment comment = new CategoryChannelComment(
                        policy.generateAnonymousUser(),
                        new Content("삭제할 댓글", "image.jpg", "password")
                );

                when(repository.findById(1L)).thenReturn(Optional.of(comment));
                when(policy.isLogin()).thenReturn(false);


                service.deleteComment(dto);

                verify(policy, times(1)).getCommentById(any(Long.class));
                verify(repository, times(1)).save(any(CategoryChannelComment.class));
            }
        }

        @Test
        @DisplayName("관리자 삭제")
        void login(){
            CategoryChannelCommentDeleteRequestDTO dto = new CategoryChannelCommentDeleteRequestDTO();
            dto.setCommentId(1L);


            LoginUserInfo loginUserInfo = new LoginUserInfo();
            loginUserInfo.setUserNo(2L);
            loginUserInfo.setUserName("관리자");
            loginUserInfo.setIsAdmin(true);

            CategoryChannelComment comment = new CategoryChannelComment(
                    new WriterInfo(1L, "사용자", true),
                    new Content("삭제할 댓글", "image.jpg", null)
            );

            when(repository.findById(1L)).thenReturn(Optional.of(comment));
            when(policy.isLogin()).thenReturn(true);
            when(policy.getLoginUserInfo()).thenReturn(loginUserInfo);

            service.deleteComment(dto);

            verify(policy, times(1)).getCommentById(any(Long.class));
            verify(repository, times(1)).save(any(CategoryChannelComment.class));
        }

        @Nested
        @DisplayName("삭제 실패")
        class FailCase{

            @Nested
            @DisplayName("로그인 한 경우")
            class LoginCase{

                @DisplayName("없는 댓글 삭제")
                @Test
                void 없는댓글_삭제(){
                    CategoryChannelCommentDeleteRequestDTO dto = new CategoryChannelCommentDeleteRequestDTO();
                    dto.setCommentId(999L);

                    LoginUserInfo loginUserInfo = new LoginUserInfo();
                    loginUserInfo.setUserNo(1L);
                    loginUserInfo.setUserName("사용자");
                    loginUserInfo.setIsAdmin(false);

                    when(repository.findById(999L)).thenReturn(Optional.empty());
                    when(policy.isLogin()).thenReturn(true);
                    when(policy.getLoginUserInfo()).thenReturn(loginUserInfo);

                    Assertions.assertThrows(CategoryChannelCommentNotFoundException.class, () -> service.deleteComment(dto));

                    verify(policy, times(1)).getCommentById(any(Long.class));
                }

                @DisplayName("작성자와 삭제하는 사람이 다른 경우")
                @Test
                void 사용자_다름(){
                    CategoryChannelCommentDeleteRequestDTO dto = new CategoryChannelCommentDeleteRequestDTO();
                    dto.setCommentId(1L);


                    LoginUserInfo loginUserInfo = new LoginUserInfo();
                    loginUserInfo.setUserNo(999L);
                    loginUserInfo.setUserName("다른 사용자");
                    loginUserInfo.setIsAdmin(false);


                    CategoryChannelComment comment = new CategoryChannelComment(
                            new WriterInfo(1L, "사용자", true),
                            new Content("삭제할 댓글", "image.jpg", null)
                    );

                    when(repository.findById(1L)).thenReturn(Optional.of(comment));
                    when(policy.isLogin()).thenReturn(true);
                    when(policy.getLoginUserInfo()).thenReturn(loginUserInfo);

                    Assertions.assertThrows(MisMatchUserException.class, () -> service.deleteComment(dto));

                    verify(policy, times(1)).getCommentById(any(Long.class));
                }
            }


            @Nested
            @DisplayName("로그인 하지 않은 경우")
            class nonLogin{

                @DisplayName("없는 댓글 삭제")
                @Test
                void 없는댓글_삭제(){
                    CategoryChannelCommentDeleteRequestDTO dto = new CategoryChannelCommentDeleteRequestDTO();
                    dto.setCommentId(999L);

                    when(repository.findById(999L)).thenReturn(Optional.empty());
                    when(policy.isLogin()).thenReturn(false);

                    Assertions.assertThrows(CategoryChannelCommentNotFoundException.class, () -> service.deleteComment(dto));

                    verify(policy, times(1)).getCommentById(any(Long.class));
                }

                @DisplayName("비밀번호가 다른 경우")
                @Test
                void 사용자_다름(){
                    CategoryChannelCommentDeleteRequestDTO dto = new CategoryChannelCommentDeleteRequestDTO();
                    dto.setCommentId(1L);
                    dto.setPassword("wrong password");

                    CategoryChannelComment comment = new CategoryChannelComment(
                            policy.generateAnonymousUser(),
                            new Content("삭제할 댓글", "image.jpg", "password")
                    );

                    when(repository.findById(1L)).thenReturn(Optional.of(comment));
                    when(policy.isLogin()).thenReturn(false);

                    Assertions.assertThrows(CategoryChannelCommentPasswordMisMatchException.class, () -> service.deleteComment(dto));

                    verify(policy, times(1)).getCommentById(any(Long.class));
                }
            }
        }
    }

}