package com.yzy.community.controller;

import com.yzy.community.common.BaseResponse;
import com.yzy.community.common.ResultUtils;
import com.yzy.community.contant.CommonConstant;
import com.yzy.community.mapper.MessageMapper;
import com.yzy.community.model.dto.message.MessageAddRequest;
import com.yzy.community.model.entity.Message;
import com.yzy.community.model.entity.User;
import com.yzy.community.model.vo.LetterVO;
import com.yzy.community.model.vo.MessageVO;
import com.yzy.community.model.vo.NoticeVO;
import com.yzy.community.service.MessageService;
import com.yzy.community.service.UserService;
import com.yzy.community.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: yzy
 **/
@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController implements CommonConstant {

    @Resource
    private MessageService messageService;

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private UserService userService;

    /**
     * 获取私信列表
     *
     * @return
     */
    @GetMapping("/list")
    public BaseResponse<List<MessageVO>> list() {
        Integer useId = UserHolder.getUser().getId();

        List<Message> conversationList = messageService.getConversationList(useId);
        List<MessageVO> messageVOS = conversationList.stream().map(message -> {
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(message, messageVO);
            int targetId = useId == message.getFromId() ? message.getToId() : message.getFromId();

            //对方的头像
            User one = userService.getById(targetId);
            String avatar = one.getHeaderUrl();
            messageVO.setFromUserAvatar(avatar);

            //未读消息
            messageVO.setUnreadCount(messageService.getLettersUnreadCount(useId, message.getConversationId()));
            //消息总数
            messageVO.setLetterCount(messageService.getLettersCount(message.getConversationId()));

            return messageVO;
        }).collect(Collectors.toList());

        return ResultUtils.success(messageVOS, "获取私信列表成功");
    }

    /**
     * 查看私信详情
     *
     * @param conversationId
     * @return
     */
    @GetMapping("/detail/{conversationId}")
    public BaseResponse<List<LetterVO>> detail(@PathVariable("conversationId") String conversationId) {
        List<Message> messages = messageService.getLetter(conversationId);

        //私信消息列表
        List<LetterVO> letterVOList = messages.stream().map(message -> {
            LetterVO letterVO = new LetterVO();
            BeanUtils.copyProperties(message, letterVO);

            //消息发送者的信息
            Integer fromId = message.getFromId();
            User fromUser = userService.getById(fromId);
            letterVO.setFromUserName(fromUser.getUsername());
            letterVO.setFromUserAvatar(fromUser.getHeaderUrl());

            return letterVO;
        }).collect(Collectors.toList());


        //查看私信时，将接收到的未读私信消息设置为已读
        List<Integer> unreadMessage = getUnreadMessage(messages);
        messageService.updateStatus(unreadMessage, 1);
        return ResultUtils.success(letterVOList, "获取私信详情成功");
    }

    private List<Integer> getUnreadMessage(List<Message> messages) {
        //未读消息ids
        List<Integer> unreadMessages = messages
                .stream()
                .filter(message -> message.getStatus() == 0 && message.getToId().equals(UserHolder.getUser().getId()))
                .map(message -> message.getId())
                .collect(Collectors.toList());

        return unreadMessages;
    }

    /**
     * 发送私信
     *
     * @param messageAddRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> add(@RequestBody MessageAddRequest messageAddRequest) {
        //TODO 可以改成从UserHolder中获取，这里为了测试给自己发消息，先这样写
        Integer fromId = messageAddRequest.getFromId();
        Integer toId = messageAddRequest.getToId();
        String content = messageAddRequest.getContent();
        String conversationId = fromId < toId ? fromId + "_" + toId : toId + "_" + fromId;
        boolean success = messageService.addMessage(fromId, toId, content, conversationId);
        return ResultUtils.success(success, "发送私信成功");
    }

    /**
     * 获取当前用户的通知
     *
     * @return
     */
    @GetMapping("/notice")
    public BaseResponse<NoticeVO> getNoticeList() {
        Integer userId = UserHolder.getUser().getId();
        //获取评论通知
        List<Message> commentNotice = messageService.getNoticeList(userId, TOPIC_COMMENT);
        //获取点赞通知
        List<Message> likeNotice = messageService.getNoticeList(userId, TOPIC_LIKE);
        //获取关注通知
        List<Message> followNotice = messageService.getNoticeList(userId, TOPIC_FOLLOW);

        //未读消息
        int commentUnreadCount = messageService.getNoticeUnreadCount(userId, TOPIC_COMMENT);
        int likeUnreadCount = messageService.getNoticeUnreadCount(userId, TOPIC_LIKE);
        int followUnreadCount = messageService.getNoticeUnreadCount(userId, TOPIC_FOLLOW);

        //未读消息总数
        int unreadCount = commentUnreadCount + likeUnreadCount + followUnreadCount;
        NoticeVO noticeVO = new NoticeVO().setCommentNotice(commentNotice)
                .setLikeNotice(likeNotice)
                .setFollowNotice(followNotice)
                .setUnreadCount(unreadCount);

        return ResultUtils.success(noticeVO, "获取通知列表成功");
    }

    /**
     * 查看通知列表
     *
     * @param topic
     * @return
     */
    @GetMapping("/notice/detail/{topic}")
    public BaseResponse<List<Message>> getNoticeDetail(@PathVariable("topic") String topic) {
        Integer userId = UserHolder.getUser().getId();
        List<Message> noticeList = messageService.getNoticeList(userId, topic);
        List<Integer> ids = noticeList.stream().map(message -> {
            return message.getId();
        }).collect(Collectors.toList());
        messageService.updateStatus(ids, 1);
        return ResultUtils.success(noticeList, "获取通知详情成功");
    }

}
