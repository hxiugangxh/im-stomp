package com.ylz.imstomp.controller;

import com.ylz.imstomp.bean.ChatMessage;
import com.ylz.imstomp.bean.ImUser;
import com.ylz.imstomp.bean.MultipartFileParam;
import com.ylz.imstomp.bean.OnlineInfoBean;
import com.ylz.imstomp.constant.Constants;
import com.ylz.imstomp.service.ImChatLogService;
import com.ylz.imstomp.service.ImService;
import com.ylz.imstomp.service.StorageService;
import com.ylz.imstomp.vo.ResultStatus;
import com.ylz.imstomp.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.Principal;
import java.util.*;

@Slf4j
@Controller
@RequestMapping("/im")
public class ImController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private SimpUserRegistry simpUserRegistry;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ImService imService;

    @Autowired
    private ImChatLogService imChatLogService;

    @Value("${breakpoint.upload.dir}")
    private String finalDirPath;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StorageService storageService;

    @RequestMapping("/chatRoom")
    public String chatRoom(Map<String, Object> map, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String userName = userDetails.getUsername();
        ImUser imUser = new ImUser();
        imUser.setUserName(userName);
        imUser.setNick(imService.getNick(userName));

        map.put("imUser", imUser);

        return "chat_room";
    }

    @RequestMapping("/listChatMessage")
    @ResponseBody
    public Page<ChatMessage> listChatMessage(
            @RequestParam("type") Integer type,
            @RequestParam(value = "fromUserName", required = false) String fromUserName,
            @RequestParam(value = "toUserName", required = false) String toUserName,
            @RequestParam(value = "pn") Integer pn,
            @RequestParam(value = "pageSize") Integer pageSize) {

        if (type == 0) {
            imService.readChatMessage(fromUserName, toUserName);
            // 再次广播我已经阅读了信息
            this.brokerOnline();
            simpMessagingTemplate.convertAndSendToUser(toUserName,
                    Constants.SINGLE_CHAT_READ_DES, "read");
        }

        return imService.listChatMessage(type, fromUserName, toUserName, pn, pageSize);
    }

    @RequestMapping("/readChatMessage")
    @ResponseBody
    public Map<String, Object> readChatMessage(
            @RequestParam(value = "fromUserName") String fromUserName,
            @RequestParam(value = "toUserName") String toUserName) {

        imService.readChatMessage(toUserName, fromUserName);
        log.info("toUserName = {}", toUserName);

        // 发送我已经阅读了你的信息
        simpMessagingTemplate.convertAndSendToUser(fromUserName,
                Constants.SINGLE_CHAT_READ_DES, "read");

        // 已经阅读则都为0
        ImUser imUser = new ImUser();
        imUser.setUserName(fromUserName);
        simpMessagingTemplate.convertAndSendToUser(toUserName,
                Constants.SINGLE_CHAT_READ_ROOM_DES, imUser);

        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("flag", true);

        return jsonMap;
    }

    @RequestMapping("/brokerOnline")
    @ResponseBody
    public Map<String, Object> brokerOnline() {
        Map<String, Object> jsonMap = new HashMap<>();
        boolean flag = true;
        try {
            List<String> onlineUserList = new ArrayList<>();
            for (SimpUser simpUser : simpUserRegistry.getUsers()) {
                onlineUserList.add(simpUser.getName());
            }

            OnlineInfoBean onlineInfoBean = imService.listOnlineUser(onlineUserList);

            log.info("brokerOnline--广播在线信息: {}", onlineInfoBean);
            simpMessagingTemplate.convertAndSend(Constants.USER_ONLINE_INFO, onlineInfoBean);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }

        jsonMap.put("flag", flag);

        return jsonMap;
    }

    @RequestMapping("/groupChat")
    @ResponseBody
    public Map<String, Object> groupChat(ChatMessage chatMessage) {
        Map<String, Object> jsonMap = new HashMap<>();
        // 默认群聊为已读
        chatMessage.setReadFlag(1);

        boolean flag = true;
        try {
            log.info("groupChat--保存聊天记录: {}", chatMessage);
            imChatLogService.saveChatLog(chatMessage);

            log.info("groupChat--发送聊天信息: {}", chatMessage);
            simpMessagingTemplate.convertAndSend(Constants.GROUP_CHAT_DES, chatMessage);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        jsonMap.put("flag", flag);

        return jsonMap;
    }

    @RequestMapping("/chatSingleRoomContent")
    public String chatSingleRoomContent(
            @RequestParam(value = "toUserName") String toUserName,
            Map<String, Object> map,
            Principal principal) {

        ImUser imUser = new ImUser();
        imUser.setUserName(principal.getName());
        imUser.setNick(imService.getNick(principal.getName()));

        ImUser toImUser = new ImUser();
        toImUser.setUserName(toUserName);
        toImUser.setNick(imService.getNick(toUserName));

        map.put("imUser", imUser);
        map.put("toImUser", toImUser);

        return "chat_single_room_content";
    }

    @RequestMapping("/chatSingleRoom")
    public String chatSingleRoom(
            @RequestParam(value = "toUserName") String toUserName,
            Map<String, Object> map,
            Principal principal) {

        ImUser imUser = new ImUser();
        imUser.setUserName(principal.getName());
        imUser.setNick(imService.getNick(principal.getName()));

        ImUser toImUser = new ImUser();
        toImUser.setUserName(toUserName);
        toImUser.setNick(imService.getNick(toUserName));

        map.put("imUser", imUser);
        map.put("toImUser", toImUser);

        return "chat_single_room";
    }

    @RequestMapping("/singleChat")
    @ResponseBody
    public Map<String, Object> singleChat(ChatMessage chatMessage, Principal principal) {
        chatMessage.setFromUserName(principal.getName());
        chatMessage.setFromNick(imService.getNick(principal.getName()));

        Map<String, Object> jsonMap = new HashMap<>();
        boolean flag = true;
        try {
            log.info("singleChat--保存聊天记录: {}", chatMessage);
            imChatLogService.saveChatLog(chatMessage);

            simpMessagingTemplate.convertAndSendToUser(chatMessage.getToUserName(),
                    Constants.SINGLE_CHAT_DES, chatMessage);

            simpMessagingTemplate.convertAndSendToUser(chatMessage.getFromUserName(),
                    Constants.SINGLE_CHAT_DES, chatMessage);

            ImUser imUser = imService.getImUserCount(chatMessage.getFromUserName(), chatMessage.getToUserName());
            simpMessagingTemplate.convertAndSendToUser(chatMessage.getToUserName(),
                    Constants.SINGLE_CHAT_READ_ROOM_DES, imUser);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        jsonMap.put("flag", flag);

        return jsonMap;
    }

    /**
     * 秒传判断，断点判断
     *
     * @return
     */
    @RequestMapping(value = "/checkFileMd5", method = RequestMethod.POST)
    @ResponseBody
    public Object checkFileMd5(String md5) throws IOException {
        Object processingObj = stringRedisTemplate.opsForHash().get(Constants.FILE_UPLOAD_STATUS, md5);
        if (processingObj == null) {
            return new ResultVo(ResultStatus.NO_HAVE);
        }
        String processingStr = processingObj.toString();
        boolean processing = Boolean.parseBoolean(processingStr);
        String value = stringRedisTemplate.opsForValue().get(Constants.FILE_MD5_KEY + md5);
        if (processing) {
            return new ResultVo(ResultStatus.IS_HAVE, value);
        } else {
            File confFile = new File(value);
            byte[] completeList = FileUtils.readFileToByteArray(confFile);
            List<String> missChunkList = new LinkedList<>();
            for (int i = 0; i < completeList.length; i++) {
                if (completeList[i] != Byte.MAX_VALUE) {
                    missChunkList.add(i + "");
                }
            }
            return new ResultVo<>(ResultStatus.ING_HAVE, missChunkList);
        }
    }

    /**
     * 上传文件
     *
     * @param param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity fileUpload(MultipartFileParam param, HttpServletRequest request) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            log.info("上传文件start。");
            try {
                // 方法1
                //storageService.uploadFileRandomAccessFile(param);
                // 方法2 这个更快点
                storageService.uploadFileByMappedByteBuffer(param);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("文件上传失败。{}", param.toString());
            }
            log.info("上传文件end。");
        }
        return ResponseEntity.ok().body("上传成功。");
    }

    @RequestMapping(value = "/fileExist")
    @ResponseBody
    public Map<String, Object> fileExist(
            @RequestParam("md5") String md5,
            @RequestParam("name") String name,
            HttpServletResponse response
    ) {

        Map<String, Object> jsonMap = new HashMap<>();
        String filePath = finalDirPath + md5  + "/" + name;
        File file = new File(filePath);
        jsonMap.put("flag", file.exists());

        return jsonMap;
    }

    @RequestMapping(value = "/download")
    public void download(
            @RequestParam("md5") String md5,
            @RequestParam("name") String name,
            HttpServletResponse response
    ) throws IOException {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename="
                + new String(name.getBytes("GB2312"),"ISO-8859-1"));

        String filePath = finalDirPath + md5  + "/" + name;
        InputStream inputStream = new FileInputStream(filePath);

        OutputStream outputStream = response.getOutputStream();

        try {
            IOUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
            IOUtils.closeQuietly(outputStream);
        }
    }

}
